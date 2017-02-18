import java.util.ArrayList;

public class AirportTest14_Randomize extends Network{
	public AirportTest14_Randomize() {
		int loopLimit = 10000;
		boolean success = true;

		NetworkForCSVFile net = new NetworkForCSVFile("S10b-14_BetAport_LabelRemoved_and_Weighted.csv", false, true, true, true);
		net.setNode();
		new AirportNetworkTransformer().makeUndirectedEdge(net);
		int[][] list = new int[net.M][2];

		this.N = net.N;
		this.M = net.M;
		this.list = net.list;
		this.weight = new double[net.M];
		this.nodeList = net.nodeList;

		int[] stubN = new int[net.N];
		int maxStubN;
		int maxStubNode;
		int maxStubIndex;

		ArrayList<Integer> array = new ArrayList<Integer>();
		for(int i=0;i<net.N;i++){
			stubN[i] = net.degree[i];
			for(int j=0;j<net.degree[i];j++){
				array.add(i);
			}
		}

		int targetStubA,targetStubB;

		int nowLine=0;
		int nowLoopTimes;
		boolean selfLoop,multiple;

		loop: do{
			nowLoopTimes=0;
			do{
				maxStubN = 0;
				maxStubNode = -1;
				maxStubIndex = -1;
				for(int i=0;i<net.N;i++){
					if(maxStubN < stubN[i]){
						maxStubN = stubN[i];
						maxStubNode=i;
					}
				}
				for(int i=0;i<array.size();i++){
					if(array.get(i) == maxStubNode){
						maxStubIndex = i;
						break;
					}
				}
				targetStubA = maxStubIndex;
				targetStubB = (int)(array.size()*Math.random());

				if(nowLoopTimes>=loopLimit){
					System.out.println("生成に失敗しました。");
					success = false;
					break loop;
				}
				nowLoopTimes++;

				selfLoop= (array.get(targetStubA)==array.get(targetStubB));
				multiple=false;
				cheakMultiple:for(int i=0;i<nowLine;i++){
					if((array.get(targetStubA)==list[i][0]&&array.get(targetStubB)==list[i][1])||
					   (array.get(targetStubA)==list[i][1]&&array.get(targetStubB)==list[i][0])){
						multiple=true;
						break cheakMultiple;
					}
				}

			}while(selfLoop || multiple);

			if(targetStubA >= targetStubB){
				stubN[array.get(targetStubA)]--;
				list[nowLine][1]=array.get(targetStubA);
				array.remove(targetStubA);
				stubN[array.get(targetStubB)]--;
				list[nowLine][0]=array.get(targetStubB);
				array.remove(targetStubB);
			}else{
				stubN[array.get(targetStubB)]--;
				list[nowLine][1]=array.get(targetStubB);
				array.remove(targetStubB);
				stubN[array.get(targetStubA)]
						--;
				list[nowLine][0]=array.get(targetStubA);
				array.remove(targetStubA);
			}

			nowLine++;
		}while(array.size()>0);

		if(success){
			for(int i=0;i<list.length;i++){
//				System.out.println(list[i][0] + "\t" + list[i][1]);
			}

			ArrayList<Double> weightCopyList = new ArrayList<Double>();
			for(int i=0;i<net.M;i++) weightCopyList.add(net.weight[i]);
			int targetNode;
			for(int i=0;i<net.M;i++){
				targetNode = (int)(weightCopyList.size()*Math.random());
				this.weight[i] = weightCopyList.get(targetNode);
				weightCopyList.remove(targetNode);
			}
		}
	}

	public static void main(String[] args) {
		new AirportTest14_Randomize();
	}

}
