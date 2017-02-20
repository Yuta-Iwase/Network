import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class IsingModel extends JFrame{
	// Eclipseの警告対策、本質的な部分ではない
	private static final long serialVersionUID = 1L;
	
	// 全クラスを通して管理する変数はここに書く
	static int sqrtN = 16;// 一辺の頂点数[テキストボックスで入力可]
	static double T = 3.0;// 温度(絶対温度)[テキストボックスで入力可]
	static long speed = 500; //描画速度(ms)[テキストボックスで入力可]
	static int length; //頂点の画面上での長さ
	static byte[][] spin; //頂点のスピン情報
	static int[][] posX,posY; //頂点(長方形)の左上の座標
	static int time; //経過時間
	static boolean stop; //STOPボタンが押されているか
	static Color bgColor;

	public IsingModel() {
		// ウィンドウ大きさ、閉じた時の挙動を設定
		this.setBounds(100, 100, 1000, 800);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // 背景色の設定
	    bgColor = new Color(238, 238, 238);
		this.setBackground(bgColor);
	    // パネル読み込み
	    MainPanel p = new MainPanel();
	    this.add(p);
	    // 頂点情報を初期化
	    resetNode();
	    // 変数情報の初期化
	    stop = true;
	    time=0;
	    // スレッド開始
	    new Thread(p).start();
	    // このフレームの可視化
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		// プログラム全体の起動
		new IsingModel();
	}
	
	public static void resetNode(){
		// 頂点情報初期化
		IsingModel.length =  720/IsingModel.sqrtN;
		IsingModel.spin = new byte[IsingModel.sqrtN][IsingModel.sqrtN];
		IsingModel.posX = new int[IsingModel.sqrtN][IsingModel.sqrtN];
		IsingModel.posY = new int[IsingModel.sqrtN][IsingModel.sqrtN];
		for(int x=0;x<IsingModel.sqrtN;x++){
			for(int y=0;y<IsingModel.sqrtN;y++){
				// スピンをランダムに割り振る
				if(Math.random()<0.5) IsingModel.spin[x][y] = 1; 
				else IsingModel.spin[x][y] = -1; 
				// 座標情報の再定義
				IsingModel.posX[x][y] = (int)((720/IsingModel.sqrtN)*x +20);
				IsingModel.posY[x][y] = (int)((720/IsingModel.sqrtN)*y +20);
			}
		}
	}
	
}


@SuppressWarnings("serial") //Eclipseの警告対策、本質的な部分ではない
class MainPanel extends JPanel implements Runnable,ActionListener{
	// 埋め込むコンポーネントを定義
	JLabel label_sqrtN,label_T,label_speed,label_time;
	JTextField text_sqrtN,text_T,text_speed;
	JButton stop_start_btn,reset_btn;
	
	public MainPanel() {
		// 配置方法を定義
		this.setLayout(null);
		
		//// ラベル配置
		// 一辺の頂点数
		label_sqrtN = new JLabel("一辺の頂点数:");
		this.add(label_sqrtN);
		label_sqrtN.setBounds(800, 80, 200, 20);
		// 絶対温度
		label_T = new JLabel("絶対温度:");
		this.add(label_T);
		label_T.setBounds(800, 180, 200, 20);
		// 描画速度
		label_speed = new JLabel("描画速度(ms):");
		this.add(label_speed);
		label_speed.setBounds(800, 280, 200, 20);
		// 経過時間
		label_time = new JLabel("経過時間(フレーム数):"+String.valueOf(IsingModel.time));
		label_time.setOpaque(true); //背景を不透明に
		label_time.setBackground(IsingModel.bgColor); //背景色をフレームと統一
		this.add(label_time);
		label_time.setBounds(800, 450, 200, 20);
		
		//// テキストボックス配置
		// 一辺の頂点数
		text_sqrtN = new JTextField();
		this.add(text_sqrtN);
		text_sqrtN.setText("16");
		text_sqrtN.setBounds(800, 100, 100, 30);
		// 絶対温度
		text_T = new JTextField();
		this.add(text_T);
		text_T.setText("3.0");
		text_T.setBounds(800, 200, 100, 30);
		// 描画速度
		text_speed = new JTextField();
		this.add(text_speed);
		text_speed.setText("500");
		text_speed.setBounds(800, 300, 100, 30);

		//// ボタン配置
		// STOP/STARTボタン
		stop_start_btn = new JButton("START");
		this.add(stop_start_btn);
		stop_start_btn.addActionListener(this);
		stop_start_btn.setBounds(790, 700, 90, 50);
		// RESETボタン
		reset_btn = new JButton("RESET");
		this.add(reset_btn);
		reset_btn.addActionListener(this);
		reset_btn.setBounds(890, 700, 90, 50);
	}
	
	public void paintComponent(Graphics g){
		// 頂点の色塗り
		for(int x=0;x<IsingModel.sqrtN;x++){
			for(int y=0;y<IsingModel.sqrtN;y++){
				if(IsingModel.spin[x][y] <= -1)g.setColor(Color.black);
				else g.setColor(Color.white);
				g.fillRect(IsingModel.posX[x][y], IsingModel.posY[x][y], IsingModel.length, IsingModel.length);				
			}
		}
	}
	
	// スレッド処理部分
	@Override
	public void run() {
		// 巡回する順序を決めるためのリスト
		ArrayList<Integer> order = new ArrayList<Integer>();
		int index; //リストの番号
		int cur,curX,curY; //現在の頂点情報
		double p; //スピンが変わる確率
		int sumSpin; //隣接頂点のスピンの和
		int sqrtN; //毎度「IsingModel.sqrtN」を呼び出すのが面倒なので
		while(true){
			try{
				// テキストボックスの情報を読み込む
				IsingModel.T = Double.parseDouble(text_T.getText());
				IsingModel.speed = Long.parseLong(text_speed.getText());
				
				if(!IsingModel.stop){
					//// ストップしていない時、以下の動作を行う
					//sqrtNを定義
					sqrtN = IsingModel.sqrtN;
					// 頂点番号を格納
					for(int i=0;i<sqrtN*sqrtN;i++){
						order.add(i);
					}
					for(int i=0;i<sqrtN*sqrtN;i++){
						// リストからランダムに頂点を取り出す
						index = (int)(Math.random()*order.size());
						cur = order.get(index);
						order.remove(index);
						// x方向の頂点情報を計算
						curX = cur % sqrtN;
						// y方向の頂点情報を計算
						curY = (int)(cur / sqrtN);
						/// 取得した頂点のスピンの決定
						// 隣接頂点のスピンの和を求める
						sumSpin = 0;
						// [上方向の隣]
						sumSpin += IsingModel.spin[curX][((curY-1)+IsingModel.sqrtN)%IsingModel.sqrtN];
						// [下方向の隣]
						sumSpin += IsingModel.spin[curX][((curY+1)+IsingModel.sqrtN)%IsingModel.sqrtN];
						// [左方向の隣]
						sumSpin += IsingModel.spin[((curX-1)+IsingModel.sqrtN)%IsingModel.sqrtN][curY];
						// [右方向の隣]
						sumSpin += IsingModel.spin[((curX+1)+IsingModel.sqrtN)%IsingModel.sqrtN][curY];
						// スピンが変わる確率pを求める
						if(IsingModel.T == 0) IsingModel.T = 0.00001; // 零除算回避
						p = Math.exp(-2*(1.0/IsingModel.T)*IsingModel.spin[curX][curY]*sumSpin);
						// pもとにスピンが変わるか判定
						if(Math.random()<p) IsingModel.spin[curX][curY] *= -1;
					}
					// 時間経過
					IsingModel.time++;
					label_time.setText("経過時間(フレーム数):"+String.valueOf(IsingModel.time));
					// 再描画
					repaint();
				}
				// 時間待ち
				Thread.sleep(IsingModel.speed);
			}catch(Exception e){System.out.println(e);}
			
		}
	}
	
	// ボタンが押された時の動作設定
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == stop_start_btn){
			//// STOP/STARTボタンが押された時の動作
			// ボタンの文字をSTOP⇔STARTに
			if(IsingModel.stop){
				IsingModel.stop = false;
				stop_start_btn.setText("STOP");
			}else{
				IsingModel.stop = true;
				stop_start_btn.setText("START");
			}
		}else if(e.getSource() == reset_btn){
			//// RESETボタンが押された時の動作
			// 入力情報読み込み
			try{
				IsingModel.sqrtN = Integer.parseInt(text_sqrtN.getText());
			}catch(Exception exc){
				/// エラー時の操作
				// エラーメッセージ
				System.out.println("テキストボックスに誤りがあります。");
				// デフォルトの値に戻す
				IsingModel.sqrtN = 16;
				IsingModel.T = 10;
				IsingModel.speed = 500;
			}
			// 頂点情報リセット
			IsingModel.resetNode();
			// 時間リセット
			IsingModel.time = 0;
			label_time.setText("経過時間(フレーム数):"+String.valueOf(IsingModel.time));
		}
		// 再描画
		repaint();
	}
	
}
