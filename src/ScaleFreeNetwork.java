
public class ScaleFreeNetwork extends ConfigrationNetwork{

	/**
	 * scale-free networkを生成する。<br>
	 * 与えられた条件で生成できるまでネットワーク生成を繰り返す。<br>
	 * @param N 頂点数
	 * @param gamma べき指数
	 * @param minDegree 最小次数
	 * @param maxDegree 最大次数
	 * @param loopLimit 生成時のループ数の許容値
	 */
	protected ScaleFreeNetwork(int N, double gamma, int minDegree, int maxDegree, int loopLimit) {
		super(N, gamma, minDegree, maxDegree, loopLimit);
	}

}
