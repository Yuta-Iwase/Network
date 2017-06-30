import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class InterWordStrength {
	public static void main(String[] args) throws Exception{
		solution1();
	}
	
	static void solution1() throws Exception{
		// 書き込み用ファイル
		PrintWriter pw = new PrintWriter(new File("BEAUTY AND THE BEAST/InterWordStrength.csv"));
		
		// テキストをすべてstoryへ格納
		Scanner scan = new Scanner(new File("BEAUTY AND THE BEAST/text.txt"));
		String story = scan.nextLine();
		
		// 解析用変数
		int M=0;
		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<Integer> LList = new ArrayList<Integer>();
		ArrayList<Integer> RList = new ArrayList<Integer>();
		ArrayList<Integer> interWordStrength = new ArrayList<Integer>();
		
		// 作業変数定義
		String currentSentence;
		int previousPeriodAddress = -1;
		int currentPeriodAddress;
		int nextPeriodAddress = story.indexOf(".");
		ArrayList<String> currentWordList = new ArrayList<String>();
		ArrayList<Integer> currentWordIndexList = new ArrayList<Integer>();
		int currentPosion,spacePostion;
		String currentWord;
		boolean isNew;
		int smallerIndex,biggerIndex;
		// 作業本体
		while(nextPeriodAddress!=-1){
			// 1文を切り出し
			currentPeriodAddress = nextPeriodAddress;
			currentSentence = story.substring(previousPeriodAddress+1, currentPeriodAddress);
			
			// 単語を読み込み
			currentWordList.clear();
			currentPosion = 0;
			spacePostion = currentSentence.indexOf(" ");
			while(spacePostion!=-1){
				currentWord = currentSentence.substring(currentPosion,spacePostion);
				currentWordList.add(currentWord);
				
				currentPosion = spacePostion+1;
				spacePostion = currentSentence.indexOf(" ",currentPosion);
			}
			currentWordList.add(currentSentence.substring(currentPosion, currentSentence.length()));
			
			// wordList登録とcurrentWordIndexList設定
			currentWordIndexList.clear();
			for(int i=0;i<currentWordList.size();i++){
				currentWord = currentWordList.get(i);
				
				// 新出か判定
				isNew = true;
				for(int j=0;j<wordList.size();j++){
					if(currentWord.equals(wordList.get(j))){
						isNew = false;
						currentWordIndexList.add(j);
						break;
					}
				}
				
				// 新出なら登録
				if(isNew){
					wordList.add(currentWord);
					currentWordIndexList.add(wordList.size()-1);
				}
			}
			
			// interWordStrength設定
			for(int i=0;i<currentWordIndexList.size();i++){
				for(int j=i+1;j<currentWordIndexList.size();j++){
					// 同一文章内で2回以上登場する単語どうしの結合は無視する
					// (つまり、自己ループを避ける)
					if(currentWordIndexList.get(i) == currentWordIndexList.get(j)) continue;
					
					// 単語番号の大小関係を比較
					smallerIndex = Math.min(currentWordIndexList.get(i), currentWordIndexList.get(j));
					biggerIndex  = Math.max(currentWordIndexList.get(i), currentWordIndexList.get(j));
					
					// 新出か判定
					isNew = true;
					for(int m=0;m<M;m++){
						if(LList.get(m)==smallerIndex && RList.get(m)==biggerIndex){
							isNew = false;
							interWordStrength.set(m,interWordStrength.get(m)+1);
							break;
						}
					}
					
					// 新出なら登録
					if(isNew){
						LList.add(smallerIndex);
						RList.add(biggerIndex);
						interWordStrength.add(1);
						M++;
					}
				}
			}
			
			// ピリオド位置更新
			previousPeriodAddress = currentPeriodAddress;
			nextPeriodAddress = story.indexOf(".",currentPeriodAddress+1);
		}
		
		
		// データ書き込み
		for(int m=0;m<M;m++){
			pw.println(wordList.get(LList.get(m)) + "\t" + wordList.get(RList.get(m)) + "\t" + interWordStrength.get(m));
		}
		
		pw.close();
		scan.close();
	}
}
