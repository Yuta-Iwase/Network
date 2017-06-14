
public class test0614 {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		MakePowerLaw make = new MakePowerLaw(1000, 3.0);
		MakePowerLaw make2 = new MakePowerLaw(1000, 3.0, 1, 1000);
		ConfigrationNetwork net = new ConfigrationNetwork(make2.degree, 1000);
//		net.printList();
		
		int[][] newList = new int[net.M*2][2];
		for(int i=0;i<net.M;i++){
			newList[i][0] = net.list[i][0];
			newList[i][1] = net.list[i][1];
		}
		for(int i=net.M;i<net.M*2;i++){
			newList[i][0] = net.list[i-net.M][1];
			newList[i][1] = net.list[i-net.M][0];
		}
		net.list = newList;
		
		net.sort();
		
		Comprator2dim comp = new Comprator2dim();
		comp.sort(net.list, 0, true);
		
		net.printList();


	}

}
