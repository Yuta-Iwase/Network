import java.util.Arrays;
import java.util.Comparator;

// 二次元配列のソート用クラス

public class Comprator2dim{
    private int key = 0;
    private short multiplier;

    /**
     * 二次元配列のソート用メソッド<br>
     * <br>
     * ・arrayはソートしたいリスト<br>
     * ・keyはソートの基準とする列<br>
     * ・ascending_orderはtrueで小さい順,falseで大きい順<br>
     */
    public int[][] sort(int[][] array, int key, boolean ascending_order) {
    	this.key = key;
        
        if(ascending_order){
        	multiplier = 1;
        }else{
        	multiplier = -1;
        }

    	Arrays.sort(array, new innerComparator_int());
    	
    	return array;
    }
    
    /**
     * 二次元配列のソート用メソッド<br>
     * <br>
     * ・arrayはソートしたいリスト<br>
     * ・keyはソートの基準とする列<br>
     * ・ascending_orderはtrueで小さい順,falseで大きい順<br>
     */
    public double[][] sort(double[][] array, int key, boolean ascending_order) {
    	this.key = key;
        
        if(ascending_order){
        	multiplier = 1;
        }else{
        	multiplier = -1;
        }

    	Arrays.sort(array, new innerComparator_double());
    	
    	return array;
    }
    
    /**
     * 二次元配列のソート用メソッド<br>
     * <br>
     * ・arrayはソートしたいリスト<br>
     * ・keyはソートの基準とする列<br>
     * ・ascending_orderはtrueで小さい順,falseで大きい順<br>
     */
    public int[][] sort(int[] array, int key, boolean ascending_order) {
    	int[][] take_array = new int[array.length][2];
    	for(int i=0;i<array.length;i++){
    		take_array[i][0] = i;
    		take_array[i][1] = array[i];
    	}
    	
    	this.key = key;
        
        if(ascending_order){
        	multiplier = 1;
        }else{
        	multiplier = -1;
        }

    	Arrays.sort(take_array, new innerComparator_int());
    	
    	return take_array;
    }
    
    /**
     * 二次元配列のソート用メソッド<br>
     * <br>
     * ・arrayはソートしたいリスト<br>
     * ・keyはソートの基準とする列<br>
     * ・ascending_orderはtrueで小さい順,falseで大きい順<br>
     */
    public double[][] sort(double[] array, int key, boolean ascending_order) {
    	double[][] take_array = new double[array.length][2];
    	for(int i=0;i<array.length;i++){
    		take_array[i][0] = i;
    		take_array[i][1] = array[i];
    	}
    	
    	this.key = key;
        
        if(ascending_order){
        	multiplier = 1;
        }else{
        	multiplier = -1;
        }

    	Arrays.sort(take_array, new innerComparator_double());
    	
    	return take_array;
    }
    
    private class innerComparator_int implements Comparator<int[]>{    	
        @Override
        public int compare(int[] o1, int[] o2) {
            return (o1[key] - o2[key])*multiplier;
        }
    }
    private class innerComparator_double implements Comparator<double[]>{
        @Override
        public int compare(double[] o1, double[] o2) {
            return (int)((o1[key] - o2[key])*multiplier*10);
        }
    }
    
}