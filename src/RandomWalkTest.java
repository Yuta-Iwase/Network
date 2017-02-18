import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class RandomWalkTest extends Applet implements Runnable{
	private static final long serialVersionUID = 1L;
	
	// ユーザーが変更する部分
	int peapleN = 100;
	float colorBias = 7.5F;
	
	int t = 0;
	Thread th;
	Node[][] node = new Node[10][10];
	Peaple[] peaple = new Peaple[peapleN];
	ArrayList<Node> crossNode = new ArrayList<Node>();

	public void init() {
		// 各頂点初期化
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if(!((i<=3&&j<=3)||(i>=6&&j<=3)||(i<=3&&j>=6)||(i>=6&&j>=6))){
					node[i][j] = new Node(i,j);
					crossNode.add(node[i][j]);
				}
			}
		}
		
		// peaple初期化
		for(int i=0;i<peapleN;i++){
			peaple[i] = new Peaple();
		}
		
		// 十字左
		node[0][4].addList(node[0][5]);
		node[0][4].addList(node[1][4]);
		node[0][5].addList(node[0][4]);
		node[0][5].addList(node[1][5]);
		for(int x=0;x<3;x++){
			node[1+x][4].addList(node[0+x][4]);
			node[1+x][4].addList(node[1+x][5]);
			node[1+x][4].addList(node[2+x][4]);
			node[1+x][5].addList(node[0+x][5]);
			node[1+x][5].addList(node[1+x][4]);
			node[1+x][5].addList(node[2+x][5]);
		}
		// 十字上
		node[4][0].addList(node[4][1]);
		node[4][0].addList(node[5][0]);
		node[5][0].addList(node[4][0]);
		node[5][0].addList(node[5][1]);
		for(int y=0;y<3;y++){
			node[4][1+y].addList(node[4][0+y]);
			node[4][1+y].addList(node[4][2+y]);
			node[4][1+y].addList(node[5][1+y]);
			node[5][1+y].addList(node[5][0+y]);
			node[5][1+y].addList(node[5][2+y]);
			node[5][1+y].addList(node[4][1+y]);
		}
		//十字中央
		for(int x=0;x<2;x++){
			for(int y=0;y<2;y++){
				node[4+x][4+y].addList(node[3+x][4+y]);
				node[4+x][4+y].addList(node[4+x][3+y]);
				node[4+x][4+y].addList(node[4+x][5+y]);
				node[4+x][4+y].addList(node[5+x][4+y]);
			}
		}
		// 十字下
		for(int y=0;y<3;y++){
			node[4][6+y].addList(node[4][5+y]);
			node[4][6+y].addList(node[4][7+y]);
			node[4][6+y].addList(node[5][6+y]);
			node[5][6+y].addList(node[5][5+y]);
			node[5][6+y].addList(node[5][7+y]);
			node[5][6+y].addList(node[4][6+y]);
		}
		node[4][9].addList(node[4][8]);
		node[4][9].addList(node[5][9]);
		node[5][9].addList(node[4][9]);
		node[5][9].addList(node[5][8]);
		// 十字右
		for(int x=0;x<3;x++){
			node[6+x][4].addList(node[5+x][4]);
			node[6+x][4].addList(node[6+x][5]);
			node[6+x][4].addList(node[7+x][4]);
			node[6+x][5].addList(node[5+x][5]);
			node[6+x][5].addList(node[6+x][4]);
			node[6+x][5].addList(node[7+x][5]);
		}
		node[9][4].addList(node[9][5]);
		node[9][4].addList(node[8][4]);
		node[9][5].addList(node[8][5]);
		node[9][5].addList(node[9][4]);
		
		setBackground(Color.gray);
		super.init();
	}
	
		public void start(){	
			if ( th == null ){
				th = new Thread(this);
				th.start();
			}
		}
		
		public void run(){
			while(true){
				
				for(int i=0;i<peaple.length;i++){
					peaple[i].RandomWalk();
				}
				
				for(int i=0;i<crossNode.size();i++){
					crossNode.get(i).peaples.clear();
				}
				
				for(int i=0;i<peaple.length;i++){
					peaple[i].RegisterNextNode();
				}
				
				repaint();
				t++;
				
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){}
				
			}
		}
			
		public void paint(Graphics g){
			int x,y;
			float p,subColor;
			Color drawColor;
			String printStr;
			
			printStr = "t=" + t;
			
			g.setColor(Color.white);
			g.drawString(printStr,40,40);
			
			for(int i=0;i<crossNode.size();i++){
				x = crossNode.get(i).x;
				y = crossNode.get(i).y;
				p = crossNode.get(i).peaples.size();
				subColor = 1.0F-(p/peapleN)*colorBias;
				if(subColor<0) subColor=0;
				drawColor = new Color(1.0F,subColor,subColor);
				g.setColor(drawColor);
				g.fillOval( 20+70*x, 20+70*y, 50, 50 );	
			}
			
		}
	
	protected class Node{
		int x,y;
		protected int r;
		ArrayList<Node> list = new ArrayList<Node>();
		ArrayList<Peaple> peaples = new ArrayList<Peaple>();
		
		// コンストラクタ
		Node(int inputX, int inputY) {
			x = inputX;
			y = inputY;
		}
		
		void addList(Node node){
			list.add(node);
		}
		
		Node randomGet(){
			r = (int)(list.size()*Math.random());
			if(list.size()==0){
				System.out.println("errorID:x=" + x + " y=" + y);
			}
			return list.get(r);
		}
		
	}
	
	protected class Peaple{
		int x,y;
		protected int r;
		Node myNode,nextNode;
		
		// コンストラクタ
		Peaple(){
			r = (int)(crossNode.size()*Math.random());
			myNode = crossNode.get(r);
			x=myNode.x; y=myNode.y;
			myNode.peaples.add(this);
		}
		
		void RandomWalk(){
			nextNode = myNode.randomGet();
		}
		
		void RegisterNextNode(){
			myNode = nextNode;
			x = myNode.x;
			y = myNode.y;
			myNode.peaples.add(this);
		}
		
	}
	
}