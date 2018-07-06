import java.util.ArrayList;

public class DMSNetwork extends Network{
	int generateCount = 0;

	public DMSNetwork(int N0, int N, int insertEdges, int loopLimit) {
		do {
			// 初期化
			degree = new int[N];

			// 接続頂点決定判定用のリスト
			ArrayList<Integer> stubList = new ArrayList<>();

			// 初期条件としてN0個の頂点を完全グラフで作成
			for(int i=0;i<N0;i++) {
				degree[i] = N0-1;
				for(int j=0;j<N0-1;j++) {
					stubList.add(i);
				}
			}

			// BAモデル的な次数割り振りを行う
			for(int i=N0;i<N;i++) {
				ArrayList<Integer> currentChosedNodes = new ArrayList<>();

				// 選択
				for(int j=0;j<insertEdges;j++) {
					int chosedStub = (int)(Math.random()*stubList.size());
					int chosedNode = stubList.get(chosedStub);
					if(!currentChosedNodes.contains(chosedNode)) {
						currentChosedNodes.add(chosedNode);
					}else {
						j--;
						continue;
					}
				}

				// 選択頂点の情報更新
				for(int j=0;j<currentChosedNodes.size();j++) {
					int currentChosedNode = currentChosedNodes.get(j);
					stubList.add(currentChosedNode);
					degree[currentChosedNode]++;
				}

				// 追加頂点の初期情報設定
				degree[i] = insertEdges;
				for(int j=0;j<insertEdges;j++) {
					stubList.add(i);
				}
			}

			// Configurationの要領でネットワークを構築
			ConfigrationNetwork.generate(this, degree, loopLimit, false);

			// ネットワーク生成回数カウント
			generateCount++;
		}while(!success);

	}


}
