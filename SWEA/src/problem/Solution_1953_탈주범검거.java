package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
 * 탈출한 흉악범, 맨홀 뚜껑을 통해 지하터널로 이동
 *  시간당 1의 거리를 이동
 *   터널 구조물 타입
 *    1 상하좌우
 *    2 상하
 *    3 좌우
 *    4 상우
 *    5 하우
 *    6 하좌
 *    7 상좌
 *    
 */
public class Solution_1953_탈주범검거 {

	static int N,M,R,C,L; 
	static int [][] map;
	static int [][] dy = {  {-1,1},{0,0},{-1,0},{1,0},{1,0},{-1,0} }; //상하, 좌우, 상우, 하우, 하좌, 상좌
	static int [][] dx = {  {0,0},{-1,1},{0,1},{0,1},{0,-1},{0,-1} };
	static int result;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer str =null;
		
		int T = Integer.parseInt(br.readLine());
		for(int t=1; t<=T; t++) {
			str = new StringTokenizer(br.readLine());
			N = Integer.parseInt(str.nextToken()); // 행
			M = Integer.parseInt(str.nextToken()); // 열
			R = Integer.parseInt(str.nextToken()); // 맨홀위치y
			C = Integer.parseInt(str.nextToken()); // 맨홀위치x
			L = Integer.parseInt(str.nextToken()); // 경과시간
			
			result = Integer.MIN_VALUE;
			map = new int[N][M];
			for(int n=0; n<N; n++) {
				str = new StringTokenizer(br.readLine());
				for(int m=0; m<M; m++) {
					map[n][m] = Integer.parseInt(str.nextToken());
				}
			}
			int[][] visited = new int[N][M];
			visited[R][C] = 1;
			start(R, C, 1, visited);
			
			System.out.println("#"+t+" "+result);
		}
	}
	
	/*
	 * 
	 */
	private static void start(int y, int x, int time, int[][] visit) {
		if(time==L) {
//			for(int b=0; b<N; b++) {
//				for(int a : visit[b]) {
//					System.out.print(a+" ");				
//				}
//				System.out.println("");
//			}
//			System.out.println("");

			result = calculation(visit);
			return;
		}
		
		if(map[y][x]==0) return;
		int temp = map[y][x]-2;
		
		int nextX, nextY;
		//4방
		if(temp == -1) {
			for(int i=0; i<2; i++) {
				for(int j=0; j<2; j++) {
					nextY = y + dy[i][j];
					nextX = x + dx[i][j];
					//범위 초과 시 continue
					if(!isInRange(nextY, nextX)) continue;
					//파이프가 없다면 continue
					if(map[nextY][nextX] == 0) continue;
					//파이프가 이어져 있지 않다면
					if(!isConec(map[nextY][nextX]-2, dy[i][j], dx[i][j]) ) continue;
					//다음 방문 위치가 방문하지 않았거나, 방문회차가 현 회차보다 크거나 같은 경우 
					if(visit[nextY][nextX] > visit[y][x]+1 || visit[nextY][nextX]==0 ) {
						visit[nextY][nextX] = visit[y][x]+1;
						start(nextY, nextX, time+1, visit);	
					}
				}
			}
		} 
		//2방
		else {
			for(int j=0; j<2; j++) {
				nextY = y + dy[temp][j];
				nextX = x + dx[temp][j];
				//범위 초과 시 continue
				if(!isInRange(nextY, nextX)) continue;
				//파이프가 없다면 continue
				if(map[nextY][nextX] == 0) continue;
				//파이프가 이어져 있지 않다면 continue
				if(!isConec(map[nextY][nextX]-2, dy[temp][j], dx[temp][j]) ) continue;
				//다음 방문 위치가 방문하지 않았거나, 방문회차가 현 회차보다 크거나 같은 경우 
				if(visit[nextY][nextX] >= visit[y][x]+1 || visit[nextY][nextX]==0) {
					visit[nextY][nextX] = visit[y][x]+1;
					start(nextY, nextX, time+1, visit);	
				}				
			}
		}
		start(y, x, time+1, visit);
	}

	//다음 파이프의 연결여부 확인
	private static boolean isConec(int nextPipe, int dirY, int dirX) {
		if(nextPipe == -1) return true;
		for(int i=0; i<2; i++) {
			if( dy[nextPipe][i] == dirY*-1 && dx[nextPipe][i] == dirX*-1 ) return true;
		}
		return false;
	}

	// 범위 확인
	private static boolean isInRange(int y, int x) {
		if( y<0 || x<0 || y>=N || x>=M ) return false;
		return true;
	}

	// 경우의 수 확인하기
	private static int calculation(int[][] visit) {
		int count = 0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(visit[i][j]>0) count +=1;
			}
		}
		return count;
	}
	
	
}










