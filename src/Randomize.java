import java.util.Random;


public class Randomize {
	
	public int[][] randomize(int[][] list,int times,int loopLimit){
		boolean selfLoop,multiple;
		int x,y,nowLoopLimit,space;
		Random rnd = new Random();
		
		randomize:for(int i=0;i<times;i++){
			nowLoopLimit = loopLimit;
			do{
				if(nowLoopLimit <= 0){
					System.out.println("ランダマイズに失敗しました。");
					break randomize;
				}
				
				selfLoop = false;
				multiple = false;
				
				x = rnd.nextInt(list.length);
				y = rnd.nextInt(list.length);
				
				if(list[x][0]==list[y][1] || list[x][1]==list[y][0])
					selfLoop=true;
				
				checkMultiple:for(int j=0;j<list.length;j++){
					if(j!=x && j!=y){
						if((list[j][0]==list[x][0] && list[j][1]==list[y][1]) ||
							(list[j][1]==list[x][0] && list[j][0]==list[y][1]) ||
							(list[j][0]==list[y][0] && list[j][1]==list[x][1]) ||
							(list[j][1]==list[y][0] && list[j][0]==list[x][1])){
							multiple=true;
							break checkMultiple;
						}
					}
				}
				
				nowLoopLimit--;
			}while(selfLoop || multiple);
			
			space = list[x][1];
			list[x][1] = list[y][1];
			list[y][1] = space;
			
		}
		
		return list;
	}
}
