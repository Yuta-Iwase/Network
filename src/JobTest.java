import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JobTest extends Job{

	public static void main(String[] args) {
		JobTest job = new JobTest();
		job.run("param.ini");
	}

	@Override
	public void job(ArrayList<Object> controlParameterList){
		try{
			int[] param = new int[2];

			param[0] = Integer.parseInt(controlParameterList.get(0).toString());
			param[1] = Integer.parseInt(controlParameterList.get(1).toString());

			String folderName = "JobTest_param=" + param[0] + "" + param[1];
			String folderPath = folderName + "/";
			new File(folderName).mkdirs();

			PrintWriter pw = new PrintWriter(new File(folderPath + "result.txt"));

			pw.println(param[0]);
			pw.println(param[1]);

			pw.close();

			String commandFilePath = folderPath + "command.gplot";
			PrintWriter command = new PrintWriter(new File(commandFilePath));
			command.println(gnuplot_cd_Relative(folderName));
			command.println(gnuplot_plot("result.txt", "lp"));
			command.println(gnuplot_terminalPostscript());
			command.println(gnuplot_outputMathod("result.eps"));
			command.println(gnuplot_terminalPNG());
			command.println(gnuplot_outputMathod("result.png"));
			command.close();

			System.out.println(workDirectory());

			Runtime.getRuntime().exec(gnuplotPath + " " + commandFilePath);
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
