
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class question513 {
	public static void main(String[] args) {
		Scanner scan = null;
		String text;		
		try {
			// テキスト読み込み
			File file = new File("ALICE'S ADVENTURES IN WONDERLAND.txt");
			scan = new Scanner(file);
			text = scan.nextLine();
			text = text.toLowerCase();
			// 検索をかけた単語の両端を定める数
			int old=-1, now=-1;
			// オブジェクトwordCounterを11000個の配列で定義
			wordCounter wc[] = new wordCounter[11000];
			// 次に登録する単語のID
			int wordID=0;
			// この先の行で定義する条件4の初期化
			boolean bool4 = true;
			
			// ***** 集計開始 *****
			do{
				// 直近のドット、カンマ、スペースを検索
				int dot, comma, space;
				dot = text.indexOf(".",now+1);
				comma = text.indexOf(",",now+1);
				space = text.indexOf(" ",now+1);
				// 1～3行下の文は4行下の文のエラーを防ぐ文
				if(dot==-1) dot=Integer.MAX_VALUE;
				if(comma==-1) comma=Integer.MAX_VALUE;
				if(space==-1) space=Integer.MAX_VALUE;
				now = Math.min(Math.min(dot, comma), space);
				// 現ループでの単語をnowWordとする
				String nowWord = text.substring(old+1, now);
				
				// 条件1 単語の文字数が1以上
				boolean bool1 = now-old>1;
				// 条件2 新出単語である
				boolean bool2 = true;
				int registeredWordID=0;
				for(int i=0 ; i<wordID ; i++){
					if(nowWord.equals(wc[i].getWord())){
						bool2 = false;
						registeredWordID=i;
					}
				}
				// 条件3 現単語が文末である
				boolean bool3 = (Math.min(dot, comma) < space);
				
				// 条件1を満たす単語に処理を行う
				if(bool1){
					// 条件2を満たす場合は登録とカウント処理
					if(bool2){
						wc[wordID] = new wordCounter(text.substring(old+1, now));
						if(bool3 || bool4){
							wc[wordID].countUp(1);
						}else{
							wc[wordID].countUp(2);
						}
						wordID++;	
					}
					// 条件2を満たさない場合はカウント処理のみ
					if(!bool2){
						if(bool3 || bool4){
							wc[registeredWordID].countUp(1);
						}else{
							wc[registeredWordID].countUp(2);
						}
					}
				}
				old=now;
				// 条件4 次ループの単語が文頭である
				bool4 = bool3;
			}while(text.indexOf(".",now+1)!=text.length()-1);
			// ***** 集計終了 *****
			
			// 集計結果表示
			for(int i=0 ; i<wordID ; i++){
				System.out.println(wc[i].getCount() + "\t" + wc[i].getWord());
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} finally{
			if(scan != null){
				scan.close();
			}
		}
	}
}
class wordCounter{
	// 単語の内容
	String word;
	// 単語の共起数
	int count=0;
	
	// 初期化
	public wordCounter(String s) {
		word = s;
	}
	
	// iの分カウント増加
	public void countUp(int i){
		count += i;
	}
	// ゲッター
	public String getWord(){
		return word;
	}
	public int getCount(){
		return count;
	}
}
