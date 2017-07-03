/*
 * 「ネットワーク演習」プログラム課題　課題D
 * 15S1034Y 水上 大輝
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

/**
 * 入力ファイル: ワーキングディレクトリ内の"Beauty and the Beast.txt" (またはコマンドラインの第1引数で指定されたパス)
 * 出力先: 標準出力
 * 出力形式: 1行目: クラスター係数と平均距離. 2行目以降: 次数分布（gnuplotのplotコマンドで読み込める形式）
 *
 * 注意書き記載の置換に関しては、簡単なものについてのみ実施している。
 */
public class WordCooccurrenceNetwork {
	/**
	 * 単語の出現回数と隣接点（単語）を記録するクラス。
	 */
	static class Word {
		int cardinality; // 出現回数
		List<Integer> neighbors; // 隣接点のリスト。重複（多重辺）を許す

		Word() {
			this.cardinality = 0;
			this.neighbors = new ArrayList<>();
		}
	}

	/**
	 * 単語の文字列に対して、注意書き記載の置換（の一部）を行う。
	 */
	static String normalize(String word) {
		return word
			.replaceFirst("'.*", "") // アポストロフィ以降を削除
			.toLowerCase(); // 小文字に統一
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		{
			String inPath = args.length > 0 ? args[0] : "Beauty and the Beast.txt";
			in = new Scanner(new File(inPath));
		}
		in.useDelimiter("(?:[.,?;:])+"); // 文の区切り

		List<Word> words = new ArrayList<>(); // 頂点（単語）のリスト

		{
			// 単語の文字列と、その`words`におけるインデックス（頂点番号）の対応
			Map<String, Integer> indexMap = new HashMap<>();

			// `words`リストにおける、単語`word`のインデックスを返す関数。`word`がリストに存在しない時は新たに登録する
			Function<String, Integer> indexOf = word -> indexMap.computeIfAbsent(normalize(word), _w -> {
				words.add(new Word());
				return indexMap.size();
			});

			// 各文章に対する処理
			while (in.hasNext()) {
				Scanner sc = new Scanner(in.next());
				sc.useDelimiter("(?:[\\s()\"—])+"); // 単語の区切り

				// ピリオドの間に1単語も含まれていない場合
				if (! sc.hasNext()) continue;

				int previous = indexOf.apply(sc.next()); // 1つ前の単語の頂点番号
				words.get(previous).cardinality++;

				// 各単語に対する処理
				while (sc.hasNext()) {
					int i = indexOf.apply(sc.next()); // 単語の頂点番号
					Word w =  words.get(i);
					w.cardinality++;

					// 隣接点（単語）を記録
					w.neighbors.add(previous);
					words.get(previous).neighbors.add(i);

					previous = i;
				}
			}
		}

		int maxDegree = 0; // 最大次数
		int maxCardinality = 0; // 単語の最大出現回数
		for (Word w: words) {
			int k = w.neighbors.size();
			if (k > maxDegree) maxDegree = k;

			int c = w.cardinality;
			if (c > maxCardinality) maxCardinality = c;
		}

		// 分布は誤差を最小化するためにintで計算する（出力時に実数値に直す）
		int[] frequencyDistribution = new int[maxCardinality + 1]; // 出現頻度分布
		int[] degreeDistribution = new int[maxDegree + 1]; // 次数分布
		double cSum = 0; // 局所クラスター係数の総和
		int cDenominator = 0; // 次数が2以上の頂点数。クラスター係数の分母
		int lSum = 0; // 頂点間距離の総和
		int lDenominator = 0; // 各連結成分内の頂点対の数の総和。平均頂点間距離の分母

		for (int i=0; i < words.size(); i++) {
			Word w = words.get(i);
			int degree = w.neighbors.size();

			degreeDistribution[degree]++;
			frequencyDistribution[w.cardinality]++;

			// クラスター係数の計算
			if (degree > 1) {
				int nTriangles = 0;
				for (int j = 0; j < degree; j++) {
					for (int k = j + 1; k < degree; k++) {
						if (words.get(w.neighbors.get(j)).neighbors.contains(w.neighbors.get(k))) {
							nTriangles++;
						}
					}
				}
				cSum += (double) nTriangles / (degree * (degree - 1) / 2);
				cDenominator++;
			}

			// 距離の計算
			List<Integer> searchList = new ArrayList<>(1);
			boolean[] visited = new boolean[words.size()];
			searchList.add(i);
			visited[i] = true;
			for (int d=1; ! searchList.isEmpty(); d++) {
				List<Integer> nextList = new ArrayList<>();
				for (int j: searchList) {
					for (int k: words.get(j).neighbors) {
						if (! visited[k]) {
							if (k > i) {
								lSum += d; // d(i,k)
								lDenominator++;
							}
							nextList.add(k);
							visited[k] = true;
						}
					}
				}
				searchList = nextList;
			}
		}

		double c = cSum / cDenominator; // クラスター係数
		double l = (double)lSum / lDenominator; // 平均頂点間距離

		// 出力
		System.out.println("# C = " + c + ", L = " + l);
		for (int k=0; k < degreeDistribution.length; k++) {
			double p = (double)degreeDistribution[k] / words.size();
			System.out.println(k + "\t" + p);
		}

		in.close();
	}
}
