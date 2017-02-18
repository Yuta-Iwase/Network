import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

// ・実行回数を増やす
// ・歩数によるランキング

public class AirportTest3 {
	// 試行回数
	static int times = 10000;
	
	static ArrayList<Node> nodes = new ArrayList<Node>();
	
	public static void main(String[] args) {
		read("S10b-14_BetAport_Roman_AvoidMultiple.csv");
		
		long clockS = System.currentTimeMillis();
		
		int visitN;
		int currentIndex,currentHop;
		for(int t=0;t<times;t++){
			for(int start=0;start<nodes.size();start++){
				visitN = 0;
				currentHop=0;
				for(int n=0;n<nodes.size();n++) nodes.get(n).visited=false;
				
				currentIndex = start;
				nodes.get(currentIndex).visited = true;
				visitN++;
				currentHop++;
				nodes.get(currentIndex).hop += currentHop;
				while(visitN<nodes.size()){
					currentIndex = nodes.get(currentIndex).randomGet().index;
					if(!nodes.get(currentIndex).visited){
						nodes.get(currentIndex).visited = true;
						visitN++;
						currentHop++;
						nodes.get(currentIndex).hop += currentHop;
					}else{
						currentHop++;
					}
				}
			}
		}
		
		for(int n=0;n<nodes.size();n++){
			System.out.println(nodes.get(n).label + "\t" + (nodes.get(n).hop/(nodes.size()*times*1.0)) );
		}
		System.out.println();
		System.out.println("所要時間: " + (System.currentTimeMillis()-clockS) + "[ms]");
		
	}
	
	public static void read(String filePath){
		Scanner scan;
		String currentLine;
		String[] label = new String[2];
		int[] index = new int[2];
		boolean[] registered = new boolean[2];
		
		try {
			scan = new Scanner(new File(filePath));
			
			while(scan.hasNextLine()){
				registered[0] = false;
				registered[1] = false;
				
				currentLine = scan.nextLine();
				label[0] = currentLine.substring(0,currentLine.indexOf(","));
				label[1] = currentLine.substring(currentLine.indexOf(",")+1);
				
				// 両頂点が登録済みであるか判定
				// 同時にindexも調べる
				for(int i=0;i<2;i++){
					search:for(int j=0;j<nodes.size();j++){
						if(label[i].equals(nodes.get(j).label)){
							registered[i] = true;
							index[i] = j;
							break search;
						}
					}
					if(!registered[i]){
						nodes.add(new Node(label[i]));
						index[i] = nodes.size()-1;
						nodes.get(index[i]).index = index[i];
					}
				}
				
				// 両方の頂点が登録済みである時、多重辺の可能性がある
				// それを判別し多重辺を回避
				if(registered[0] && registered[1]){
					for(int k=0 ; k<nodes.get(index[0]).list.size() ; k++){
						if(label[1].equals(nodes.get(index[0]).list.get(k).label)){
							continue;
						}
					}
				}
				
				nodes.get(index[0]).list.add(nodes.get(index[1]));
			}
			scan.close();
			
		}catch(Exception e){System.out.println(e);}
	}
	
	private static class Node{
		int index;
		int hop;
//		double rank;
		String label;
		ArrayList<Node> list = new ArrayList<Node>();
		boolean visited;
		
		Node(String inputLabel){
//			rank = 0;
			hop = 0;
			label = inputLabel;
		}
		
		Node randomGet(){
			int r = (int)(list.size()*Math.random());
			return list.get(r);
		}
		
	}
}
