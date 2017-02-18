
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AirportTest {
	static ArrayList<Node> nodes = new ArrayList<Node>();
	
	public static void main(String[] args) {
		read("S10b-14_BetAport.csv");
		
		int visitN;
		int currentIndex;
		for(int start=0;start<nodes.size();start++){
			visitN = 0;
			for(int n=0;n<nodes.size();n++) nodes.get(n).visited=false;
			
			currentIndex = start;
			nodes.get(currentIndex).visited = true;
			visitN++;
			nodes.get(currentIndex).rank += visitN;
			while(visitN<nodes.size()){
				currentIndex = nodes.get(currentIndex).randomGet().index;
				if(!nodes.get(currentIndex).visited){
					nodes.get(currentIndex).visited = true;
					visitN++;
					nodes.get(currentIndex).rank += visitN;
				}
			}
		}
		
		for(int n=0;n<nodes.size();n++){
			System.out.println(nodes.get(n).label + "\t" + (nodes.get(n).rank/nodes.size()) );
		}
		
	}
	
	public static void read(String filePath){
		Scanner scan;
		String currentLine;
		String[] label = new String[2];
		int[] index = new int[2];
		boolean[] registered = new boolean[2];
		
		try {
			scan = new Scanner(new File(filePath));
			
			// 謎のバグ(恐らく文字コードのせい)により
			// 1行目の"東京"と2行目の"東京"だけ別の文字列と
			// 判定されてしまう
			// なので、csvを改行して1行目をダミーとして
			// 2行目から読み込んでいく
			if(scan.hasNextLine())scan.nextLine();
			
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
		double rank;
		String label;
		ArrayList<Node> list = new ArrayList<Node>();
		boolean visited;
		
		Node(String inputLabel){
			rank = 0;
			label = inputLabel;
		}
		
		Node randomGet(){
			int r = (int)(list.size()*Math.random());
			return list.get(r);
		}
		
	}
}
