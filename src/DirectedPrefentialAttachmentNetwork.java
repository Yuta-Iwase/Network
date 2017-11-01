import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DirectedPrefentialAttachmentNetwork extends Network{
	int M0;
	double influence;
	int[] inDegree;
	int[] inDegreeDist = null;

	@SuppressWarnings("unchecked")
	public DirectedPrefentialAttachmentNetwork(int input_N, int input_M0, double input_influence){
		N = input_N;
		M0 = input_M0;
		influence = input_influence;

		M = (N-M0)*M0;
		degree = new int[N];
		inDegree = new int[N];
		inDegreeDist = new int[N];
		inDegreeDist[0] = -1;
		list = new int[M][2];

		Sfmt rnd = new Sfmt((int)(System.currentTimeMillis()%Integer.MAX_VALUE));
		ArrayList<Integer> fillingNodeList = new ArrayList<>();
		ArrayList<Integer> currentNodeList = null;
		int currentLine = 0;

		for(int i=0;i<N;i++) {
			degree[i] = 0;
			inDegree[i] = 0;
		}

		for(int i=0;i<M0;i++) fillingNodeList.add(i);
		for(int i=M0;i<N;i++) {
			currentNodeList = (ArrayList<Integer>)fillingNodeList.clone();
			for(int j=0;j<M0;j++) {
				if(rnd.NextUnif() < influence) {
					int sumInDegree = 0;
					for(int k=0;k<currentNodeList.size();k++) {
						sumInDegree = inDegree[currentNodeList.get(k)];
					}

					int targetLink = rnd.NextInt(sumInDegree);
					int threshold = 0;
					int targetIndex = 0;
					int targetNode;
					while(targetLink < threshold) {
						threshold += inDegree[currentNodeList.get(targetIndex)];
						targetIndex++;
					}
					targetNode = currentNodeList.get(targetIndex);
					list[currentLine][0] = i;
					list[currentLine][1] = targetNode;
					inDegree[targetNode]++;
					currentNodeList.remove(targetIndex);
				}else {
					int targetIndex = rnd.NextInt(currentNodeList.size());
					int targetNode = currentNodeList.get(targetIndex);
					list[currentLine][0] = i;
					list[currentLine][1] = targetNode;
					inDegree[targetNode]++;
					currentNodeList.remove(targetIndex);
				}
				currentLine++;
			}
			fillingNodeList.add(i);
		}
	}

	public void printInDegreeDistribution(String path) throws Exception{
		if(inDegreeDist[0]<0) {
			inDegreeDist[0] = 0;
			for(int i=0;i<N;i++) {
				inDegreeDist[inDegree[i]]++;
			}
		}

		if(path.length()>0) {
			PrintWriter pw = new PrintWriter(new File(path));
			for(int i=0;i<N;i++) {
				if(inDegreeDist[i]>0) {
					pw.println(i + "\t" + inDegreeDist[i]);
				}
			}
			pw.close();
		}else {
			for(int i=0;i<N;i++) {
				if(inDegreeDist[i]>0) {
					System.out.println(i + "\t" + inDegreeDist[i]);
				}
			}
		}
	}

	public void printInDegreeDistribution() throws Exception{
		printInDegreeDistribution("");
	}




}
