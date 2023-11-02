package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;

/*
 * N*N의 배열
 * 각각의 값은 디저트의 종류 의미
 * 각각의 칸에서 대각선으로 움직일 수 있음(대각4방)
 * 한 지역에서 출발하면 사각형을 그리며 다시 돌아와야함
 * 
 * ▶제한사항
 *  1. 같은 디저트를 먹지 않음
 *  2. (사각형)지역을 벗어날 수 없음
 *  3. 하나의 카페만 방문 불가
 *  4. 다시 되돌아가는 것 불가
 *  5. 디저트 방문 불가 시 -1 출력
 *  
 * ▶목표 : 임의의 한 카페에서 출발하여 다시 되돌아올 때 가장 많이 먹을 수 있는 경로와 방문 디저트 수 출력
 *  
 * ▶입력 
 *   - N : 정방형 배열의 크기 (4이상 20이하)
 *   - ... : 배열의 값(각 1~100)
 *   
 * ▶문제해결
 *  1. DFS 사용 
 *  2. 방문처리 ? 
 *   - N*N의 int타입 0으로 이뤄진 visited 배열
 *   - 각 값은 방문 시 먹은 디저트 수로 갱신(최댓값 유지, 최댓값 아닐 시 해당 루트는 중단) 
 *  3. !! 방향값을 파라미터로 가져와 해당값과 같거나 큰(다음 방향)으로만 이동할 수 있도록 !!(놓친 부분)
 *  
 *  시간복잡도
 *    모든 출발지점에 대해 구하기 (20*20 == 400)
 *    각 지점별 4가지를 약 20번이라고 가정하면 4^20 *  
 *     
 *  
 */
public class Solution_2105_디저트카페 {

	static int N;
	static int [][] map;
	static boolean [][] visited;
	static boolean [] visited_Dissert;
	static int result;
	
	//하우, 하좌, 상좌, 상우
	static int [] dy = {1, 1,  -1, -1};
	static int [] dx = {1, -1, -1, 1};
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new  InputStreamReader(System.in));
		StringTokenizer str = null;
		
		int test_case = Integer.parseInt(br.readLine());
		for(int T = 1; T<=test_case; T++) {
			N = Integer.parseInt(br.readLine());
			
			map = new int[N][N];
			for(int i=0; i<N; i++) {
				str = new StringTokenizer(br.readLine());
				for(int j=0; j<N; j++) {
					map[i][j] = Integer.parseInt(str.nextToken());
				}
			}
			
			result = -1;
			for(int s=0; s<N*N; s++) {
				visited 			= new boolean[N][N];
				visited_Dissert		= new boolean[100+1];
				visited[s/N][s%N]	= true;
				visited_Dissert[map[s/N][s%N]]	= true;
				DFS(s, s, 1, 0);				
			}
			System.out.println("#"+T+" "+result);
		}
	}

	/*
	 * @param int start : 시작 index(직렬화)
	 * @param int END	: 종료값(최초값)
	 * @param int cnt	: 디저트 cnt
	 * @param int prevD	: 방향값(같거나 클때만 이동)
	 */
	private static void DFS(int start ,int END, int cnt, int prevD) {
		//시작위치(y,x)
		int s_y = start/N;
		int s_x = start%N;

		//4방 분기
		int nextY, nextX, next;
		int temp;
		for(int i=prevD; i<4; i++) {
			nextY = s_y + dy[i];
			nextX = s_x + dx[i];
			//범위 확인
			if(!isRange(nextY, nextX))continue;

			next = (nextY*N)+nextX;
			//종료 조건
			//현재 위치==최초 출발지 and 최소 3번이상 방문 시  cnt 갱신
			if(next == END && cnt>2) {
				result = cnt > result ? cnt:result;
				return;
			}

			
			//중복 방문 확인
			if(visited_Dissert[map[nextY][nextX]]) continue;
			
			//다음 위치 방문 확인
			if(!visited[nextY][nextX]) {
				visited[nextY][nextX] = true;
				visited_Dissert[map[nextY][nextX]] = true;
				DFS(next, END, cnt+1, i);
				
				//값 되돌리기
				visited[nextY][nextX] = false;	
				visited_Dissert[map[nextY][nextX]] = false;
			}
			
		}
		
	}
	
	//범위 확인
	private static boolean isRange(int nextY, int nextX) {
		if( nextY>=N || nextX>=N || nextY<0 || nextX<0 ) return false;
		return true;
	}
}
