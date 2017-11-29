
public class WattsStrogatzGridNetwork extends Network{

	int N;
	int M;
	int width;
	int height;

	public WattsStrogatzGridNetwork(int input_width, int input_height, double rewiringProbability) {
		width = input_width;
		height = input_height;
		N=width*height;
		M=2*width*height-width-height; //計算すると辺の本数はこうなる
		list = new int[M][2]; //隣接リスト
		degree = new int[N]; //次数リスト

		// 格子グラフ構築
		int currentNode=0;
		int currentEdge=0;
		for(int y=0;y<height-1;y++) {
			for(int x=0;x<width-1;x++) {
				list[currentEdge][0] = currentNode;
				list[currentEdge][1] = currentNode+1;
				degree[currentNode]++;
				degree[currentNode+1]++;
				currentEdge++;
				list[currentEdge][0] = currentNode;
				list[currentEdge][1] = currentNode+width;
				degree[currentNode]++;
				degree[currentNode+width]++;
				currentEdge++;


				currentNode++;
			}
			list[currentEdge][0] = currentNode;
			list[currentEdge][1] = currentNode+width;
			degree[currentNode]++;
			degree[currentNode+width]++;
			currentEdge++;

			currentNode++;
		}
		for(int x=0;x<width-1;x++) {
			list[currentEdge][0] = currentNode;
			list[currentEdge][1] = currentNode+1;
			degree[currentNode]++;
			degree[currentNode+1]++;
			currentEdge++;

			currentNode++;
		}

		// リワイヤリング
		int[] neigthborList = new int[M*2];
		int[] addressList = new int[N];
		int[] cursor = new int[N];
		for(int i=0;i<N;i++) cursor[i]=0;
		addressList[0]=0;
		for(int i=1;i<N;i++) {
			addressList[i] = addressList[i-1]+degree[i-1];
		}
		boolean rewiringFlag;
		for(int i=0;i<M;i++) {
			int left = list[i][0];
			int right = list[i][1];

			rewiringFlag=true;
			double r = Math.random();
			if(r < rewiringProbability) {
				int targetNode = (int)(N*Math.random());
				if(Math.random() < 0.5) {
					for(int j=0;j<cursor[left];j++) {
						if(neigthborList[addressList[left]+j]==targetNode) {
							rewiringFlag = false;
							break;
						}
					}
					if(left==targetNode) rewiringFlag=false;
					if(rewiringFlag) {
						list[i][1] = targetNode;
						degree[list[i][0]]--;
						degree[targetNode]++;
					}
				}else {
					for(int j=0;j<cursor[right];j++) {
						if(neigthborList[addressList[right]+j]==targetNode) {
							rewiringFlag = false;
							break;
						}
					}
					if(right==targetNode) rewiringFlag=false;
					if(rewiringFlag) {
						list[i][0] = targetNode;
						degree[list[i][1]]--;
						degree[targetNode]++;
					}
				}
			}

			cursor[left]++;
			cursor[right]++;
		}
	}

	public static void main(String[] args) {
		WattsStrogatzGridNetwork net = new WattsStrogatzGridNetwork(20, 20,0.20);
		net.printList();
		net.printList("GridNetwork.csv");
	}

}
