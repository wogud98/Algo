package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/*
 *  H * W 배열
 *  구술은 좌, 우로 움직임
 *  벽돌은 맨 위만 꺠트릴 수 있음
 *  벽돌은 1 ~ 9로 표현
 *  구술 명중 벽돌의 숫자-1만큼 좌우로 제거됨 
 *   ex) 3일경우, 자신포함 상하좌우 각 2칸씩
 *  
 *  - 부서진 벽돌에 의해 부서진 다른 벽돌 또한 동일하게 작용
 *  - 빈 공간 발생 시 위 벽돌은 아래로 떨어짐
 *  
 *  목표 : N개 구술을 떨어뜨려 최대한 많은 벽돌 부수기
 *  
 *  N : 구술 수 ( 1~4 )
 *  H : column ( 2~15 )
 *  W : row ( 2 ~ 12 )
 *  
 *  문제해결 솔루션
 *  1. 구슬을 N 번 떨어뜨리는 중복순열을 구한다.
 *  2. 중복순열을 바탕으로 구슬을 떨어뜨림
 *  3. 벽돌의 범위에 따라 벽돌 부수기(bfs, queue) 
 *  4. 벽돌을 부순 후 벽돌 down 시키기
 *  
 *  오류 : 배열의 타입(참조형)
 *   - 배열은 참조형으로 파라미터 값으로 한 배열을 넣고
 *   - 해당 파라미터를 받은 메소드가 그 인자를 수정하게 되면
 *   - 원본 배열의 주솟값이 같아 원본 배열이 수정됨. 
 *  
 */
public class Solution_5656_벽돌깨기3 {
    
	static class Brick {
		int y, x, range;
		
		public Brick(int y, int x, int range) {
			this.y = y;
			this.x = x;
			this.range = range;
		}
	}
	
	static Queue<Brick> que;
	static int [] dy = { -1, 1, 0, 0};
	static int [] dx = {  0, 0, -1, 1};

	
	static int N, H, W;
	static int [][] map;
    static int [] selected;
    static boolean [][] visited;
    static int result;
    
   
    
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer str = null;
        
        int test_case = Integer.parseInt(br.readLine());
        
        for(int T = 1; T <= test_case; T++) {
            str = new StringTokenizer(br.readLine());
            N = Integer.parseInt(str.nextToken());
            W = Integer.parseInt(str.nextToken());
            H = Integer.parseInt(str.nextToken());
            
            map = new int[H][W];
            que = new LinkedList();
            for(int h=0; h<H; h++) {
                str = new StringTokenizer(br.readLine());
                for(int w=0; w<W; w++) {
                    map[h][w] = Integer.parseInt(str.nextToken());
                }
            }
//            System.out.println("N:"+N+" W:"+W+" H:"+H);
            
            selected = new int [N];	//중복순열 저장할 배열
            result = Integer.MAX_VALUE;
            select(0);
                        
            System.out.println("#"+T+" "+result);
        }
    }

    //중복순열 생성기
    private static void select(int cnt) {
    	if(cnt == N) {    		
    		// copy_map 초기화
    		int [][] copy = copy();
    		int brick=0;
    		
//    		System.out.println("선택됨 : " + Arrays.toString(selected));
    		
    		//선택된 네개 구간에 구슬 떨어뜨리기
    		for(int i=0; i<N; i++) {
    			visited = new boolean[H][W];
    			//구슬 떨어뜨리기, 연쇄처리
    			drop(copy, selected[i]);
    			//벽돌 다운 처리
    			compact(copy);
    		}
    		
    		brick = count(copy);
    		result = result > brick ? brick : result;
    		return;
    	}
    	
    	for(int i=0; i<W; i++) {
    		selected[cnt] = i;
    		select(cnt+1);
    	}
	}
    

	//구슬떨어뜨리기
   	private static void drop (int[][] copy, int target) {
   		
   		//최초 벽돌 찾기
   		for(int i=0; i<H; i++) {
   			if(copy[i][target] > 0) {
//   				visited[i][target] = true;
   				que.offer(new Brick(i, target, copy[i][target]));
	            	break;
            }	
        }
           
   		//BFS
        while(!que.isEmpty()) {
        	Brick cur = que.poll();
           	
           	int mY, mX;
           	for(int i=0; i<4; i++) {
           		loop1:for(int j=0; j<cur.range; j++) {
           			mY = cur.y + dy[i] *j;
           			mX = cur.x + dx[i] *j;
           		
           			//범위 확인, 방문 확인
           			if(!isInRange(mY, mX) || visited[mY][mX]) continue loop1;
           			visited[mY][mX] = true;
           			que.offer(new Brick(mY, mX, copy[mY][mX]));
           			copy[mY][mX] = 0;
           			}
           		}
          }
     }
    
   
   // 벽돌 내리기.
   private static void compact(int[][] copy) {
		Queue<Integer> temp;
		
		for(int w=0; w<W; ++w) {
			temp = new LinkedList<>();
			
			for(int h = H-1; h>=0; --h) {
				if(copy[h][w]>0) temp.offer(copy[h][w]);
			}
			
			for(int h=H-1; h>=0; --h) {
				if(!temp.isEmpty()) {
					copy[h][w] = temp.poll();
				} else {
					copy[h][w] = 0;
				}
			}
		}
	}
   
   
   // 남은 벽돌 카운트
   private static int count(int[][] copy) {
		int brick = 0;
		
		for (int h = 0; h < H; ++h) {
			for (int w = 0; w < W; ++w) {
				if(copy[h][w] > 0) brick++;
			}
		}
		return brick;
	}
   
   
   	// 배열 복사
	private static int[][] copy() {
		int [][] arr = new int[H][W];
		
		for(int i=0; i<H; i++) {
			for(int j=0; j<W; j++) {
				arr[i][j] = map[i][j];
			}
		}		
		return arr;
	}


	//범위 확인
    private static boolean isInRange(int mY, int mX) {
    	if( mY<0 || mX<0 || mY>= H || mX>=W) return false;
    	return true;
    }

}