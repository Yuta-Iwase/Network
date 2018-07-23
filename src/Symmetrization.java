import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Symmetrization {

	public static void main(String[] args) {
		Symmetrization.symmetrization("C:\\users\\owner\\desktop\\f.csv");

	}

	public static void symmetrization(String networkFilePath) {
		try {
			File networkFile = new File(networkFilePath);
			Scanner scan = new Scanner(networkFile);
			scan.useDelimiter(",| |\t|\n|\r\n");

			ArrayList<Integer> left = new ArrayList<>();
			ArrayList<Integer> right = new ArrayList<>();
			ArrayList<Double> weight = new ArrayList<>();

			while(scan.hasNext()) {
				int a = scan.nextInt();
				int b = scan.nextInt();
				double w = scan.nextDouble();

				int min = Math.min(a, b);
				int max = Math.max(a, b);

				boolean multi = false;
				for(int i=0;i<left.size();i++) {
					if(left.get(i)==min && right.get(i)==max) {
						double before_w = weight.get(i);
						weight.set(i, (before_w+w)*0.5);
						multi = true;
						break;
					}
				}

				if(!multi) {
					left.add(min);
					right.add(max);
					weight.add(w);
				}

				if(!(left.size()==weight.size() && right.size()==weight.size())) {
					System.out.println("error");
				}
			}


			int slashPos = 0;
			while(true) {
				int temp_pos = networkFilePath.indexOf("\\",slashPos+1);
				if(temp_pos<0) {
					break;
				}else {
					slashPos = temp_pos;
				}
			}
			String path = networkFile.getAbsolutePath().substring(0,slashPos+1);
			String fileName = networkFilePath.substring(path.length(),networkFilePath.length());
			System.out.println(path + "[Symmetrization]" + fileName);
			PrintWriter pw = new PrintWriter(new File(path + "[Symmetrization]" + fileName));
			for(int i=0;i<left.size();i++) {
				System.out.println(left.get(i) + "\t" + right.get(i) + "\t" + weight.get((i)));
				pw.println(left.get(i) + "," + right.get(i) + "," + weight.get(i));
			}

			pw.close();
			scan.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
