import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class ConfigurationModel2 {
	int N,M;
	int[][] list;
	boolean success;
	public ConfigurationModel2(int[] degree,int loopLimit){
		N = degree.length;

		ArrayList<Integer> array = new ArrayList<Integer>();
		int sumDegree=0;
		for(int i=0;i<N;i++){
			for(int j=0;j<degree[i];j++){
				array.add(i);
				sumDegree++;
			}
		}
		M = sumDegree/2;

		Random rnd = new Random();
		int disconnectedN=sumDegree;
		int targetEdgeA,targetEdgeB;
		int nowLine=0;
		int nowLoopLimit;
		boolean selfLoop,multiple;
		success = true;
		list = new int[M][2];
		generateLoop: do{
			nowLoopLimit=loopLimit;
			do{
				targetEdgeA=rnd.nextInt(disconnectedN);
				targetEdgeB=rnd.nextInt(disconnectedN);

				if(nowLoopLimit<=0){
					System.out.println("生成に失敗しました。");
					success = false;
					break generateLoop;
				}
				nowLoopLimit--;

				selfLoop= array.get(targetEdgeA)==array.get(targetEdgeB);
				multiple=false;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					if((array.get(targetEdgeA)==list[i][0]&&array.get(targetEdgeB)==list[i][1])||
					   (array.get(targetEdgeA)==list[i][1]&&array.get(targetEdgeB)==list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}
			}while(selfLoop || multiple);

			if(targetEdgeA >= targetEdgeB){
				list[nowLine][1]=array.get(targetEdgeA);
				array.remove(targetEdgeA);
				list[nowLine][0]=array.get(targetEdgeB);
				array.remove(targetEdgeB);
			}else{
				list[nowLine][1]=array.get(targetEdgeB);
				array.remove(targetEdgeB);
				list[nowLine][0]=array.get(targetEdgeA);
				array.remove(targetEdgeA);
			}

			disconnectedN -= 2;
			nowLine++;
		}while(disconnectedN>0);
	}

	public void printList(){
		if(success)
			for(int i=0;i<M;i++){
				System.out.println(list[i][0] + "\t" + list[i][1]);
			}
		else
			System.out.println("生成に失敗しているため表示できません。");
	}

	PrintWriter pw;
	public void printList(String fileName){
		if(success){
			try{
				pw = new PrintWriter(new File(fileName));
				for(int i=0;i<M;i++){
					pw.println(list[i][0] + "\t" + list[i][1]);
				}
				pw.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			System.out.println("生成に失敗しているため表示できません。");
		}
	}
	
	private int[][] quickSortLevel1(int[][] list,int low,int high){
		int space1,space2;
		if(low<high){
			int mid = (low + high)/2;
			int x = list[mid][0];
			int i=low;
			int j=high;
			while(i<=j){
				while(list[i][0] < x) i++;
				while(list[j][0] > x) j--;
				if(i<=j){
					space1=list[i][0]; space2=list[i][1];
					list[i][0]=list[j][0]; list[i][1]=list[j][1];
					list[j][0]=space1; list[j][1]=space2;
					i++; j--;
				}
			}
			quickSortLevel1(list,low,j);
			quickSortLevel1(list,i,high);
		}
		return list;
	}
	
	private int[][] quickSortLevel2(int[][] list,int low,int high){
		int space;
		if(low<high){
			int mid = (low + high)/2;
			int x = list[mid][1];
			int i=low;
			int j=high;
			while(i<=j){
				while(list[i][1] < x) i++;
				while(list[j][1] > x) j--;
				if(i<=j){
					space=list[i][1];
					list[i][1]=list[j][1];
					list[j][1]=space;
					i++; j--;
				}
			}
			quickSortLevel2(list,low,j);
			quickSortLevel2(list,i,high);
		}
		return list;
	}
	
	public void sort(){
		if(success){
			quickSortLevel1(list,0,M-1);
			int low=0;
			int high=0;
			sortLevel2 : for(int n=0 ; n<N ; n++){
				while(list[high][0] == n){
					high++;
					if(high == M)break sortLevel2;
				}
				if( (high-low) > 1){
					quickSortLevel2(list,low,high-1);
				}
				low = high;
			}
		}else{
			System.out.println("生成に失敗しているためソートできません。");
		}
	}
}
