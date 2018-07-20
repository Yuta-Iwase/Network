
public class MyCalc {

	static int max(int[] array) {
		int max = array[0];
		for(int i=0;i<array.length;i++) {
			if(max<array[i]) max = array[i];
		}
		return max;
	}

	static double max(double[] array) {
		double max = array[0];
		for(int i=0;i<array.length;i++) {
			if(max<array[i]) max = array[i];
		}
		return max;
	}

	static int sum(int[] array) {
		int sum = 0;
		for(int i=0;i<array.length;i++) {
			sum += array[i];
		}
		return sum;
	}

	static double sum(double[] array) {
		double sum = 0.0;
		for(int i=0;i<array.length;i++) {
			sum += array[i];
		}
		return sum;
	}

	static double average(int[] array) {
		return sum(array)/((double)array.length);
	}

	static double average(double[] array) {
		return sum(array)/array.length;
	}

	static int square(int a) {
		return a*a;
	}

	static double square(double a) {
		return a*a;
	}



}
