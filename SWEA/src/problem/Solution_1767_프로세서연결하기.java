package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/*
 * 프로세서 엑시녹스 : N * N cell로 구성
 * 
 * 1개의 cell에는 1개 core 혹은 1개 전선 위치 가능
 * 배열 가장자리에는 전류가 흐름
 * core에 전원을 연결하는 전선은 직선으로만 설치 가능
 * 전선 교차 불가능
 * 가장 자리에 위치한 core는 전류 연결됨(전선 불필요)
 * 최대한 많은 core 전원 연결 시 전선 길이의 합 구하기(최소)
 * 전원 연결이 불가능한 core 존재 가능
 * 
 * 입력
 *  - N : 7 ~ 12
 *  - core 수 : 1~12
 *  
 * 문제 솔루션
 *  1. 중복순열 생성 (경우의 수 : 4^12 == 16_777_216)
 *  2. 생성된 순열을 바탕으로 전선깔기
 *  3. 가장 많은 코어가 연결된 경우 전선count 값 갱신
 *  4. 
 *  
 */
public class Solution_1767_프로세서연결하기 {
	
	static class Core {
		int y;
		int x;
		
		public Core(int y, int x) {
			this.y = y;
			this.x = x;
		}
	}
	
	static int N;
	static int [][] axinox;
	static int maxCore;
	static int minCable;
	static int [] dy = { -1, 1,  0, 0};
	static int [] dx = {  0, 0, -1, 1};
	static List<Core> core;
	
		
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer str = null;
		
		int T = Integer.parseInt(br.readLine());
		for(int test_case=1; test_case<=T; test_case++) {
			N = Integer.parseInt(br.readLine());
			
			axinox = new int[N][N];
			core = new ArrayList<>();
			for(int i=0; i<N; i++) {
				str = new StringTokenizer(br.readLine().replace("", " "));
				for(int j=0; j<N; j++) {
					axinox[i][j] = Integer.parseInt(str.nextToken());

					//코어 식별 시
					if(axinox[i][j]==1) {
						//코어가 테두리에 위차한다면 저장 x
						if(i==0 || j==0 ||i==(N-1)||j==(N-1)) continue;
						//코어 저장
						core.add(new Core(i, j));
					}
				}
			}
			//System.out.println(core.size());
			//System.out.println(Arrays.deepToString(axinox));
			minCable = Integer.MAX_VALUE;
			maxCore = Integer.MIN_VALUE;
			
			dfs(0, 0, 0);
			System.out.println("#"+test_case+" "+minCable);
		}
	}




	private static void dfs(int cur, int coreCnt, int cableCnt) {
		
		if(cur==core.size()) {
			//더 많은 코어 연결 시 (연결 코어 수, 케이블 수 갱신)
			if(maxCore < coreCnt) {
				maxCore = coreCnt;
				minCable = cableCnt;
				System.out.println("cable: " + minCable );
			}
			//동일 코어 연결 시 (더 작은 케이블 수 갱신)
			else if(maxCore == coreCnt) minCable = minCable<cableCnt? minCable:cableCnt;
			
			return;
		}

		int y = core.get(cur).y;
		int x = core.get(cur).x;
		
		//상하좌우
		for(int i=0; i<4; i++) {
			int my = y;
			int mx = x;
			int count = 0;
			
			while(true) {
				my += dy[i];
				mx += dx[i];

				//범위 벗어나면 정지
				if(!isInRange(my,mx)) break;
				
				//core혹은 전선이라면 정지 및 방향 전환
				if(axinox[my][mx] == 1) {
					count = 0;
					break;
				}
				count++;	
			}
			
			// 전선 채우기
			if(count>0)fillCable(i, y, x, count, 1);
			
			// 다음 코어로
			if(count==0) dfs(cur+1, coreCnt, cableCnt);
			// 연결 코어 수, 전선 수 갱신 후 다음 코어로
			else {
				dfs(cur+1, coreCnt+1, cableCnt+count);
				
				// 채운 전선 되돌리기
				fillCable(i, y, x, count, 0);
			}
			
		}
		
	}




	private static void fillCable(int dir, int y, int x, int cnt, int target) {
		int fill_y = y;
		int fill_x = x;
		
		//전선 채우기 || 전선 제거
		for(int i=0; i<cnt; i++) {
			fill_y += dy[dir];
			fill_x += dx[dir];
			//if(!isInRange(fill_y, fill_x)) continue;
			axinox[fill_y][fill_x] = target;
		}
		
		
	}




	private static boolean isInRange(int my, int mx) {
		if(my<0 || mx<0 || my>=N || mx>=N) return false;
		return true;
	}
}
