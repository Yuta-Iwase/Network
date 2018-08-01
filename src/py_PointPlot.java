import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class py_PointPlot {


	public void plot(String str_inputPath, String str_outputName, double dou_plotRangeX_start, double dou_plotRangeX_end, double dou_plotRangeY_start, double dou_plotRangeY_end, boolean bool_withLine, String str_lineColor, boolean bool_dottedLine, boolean bool_withPoint, String str_pointColor, int int_pointSize, int int_accumulationMode, boolean bool_logscaleX, boolean bool_logscaleY, String str_title, String str_xLabel, String str_yLabel, boolean bool_withLegend, String str_legendLabel, String str_legendPosition) {
		String name = "py_" + System.currentTimeMillis() + ".py";
		try {
			core(name, str_inputPath, str_outputName, dou_plotRangeX_start, dou_plotRangeX_end, dou_plotRangeY_start, dou_plotRangeY_end, bool_withLine, str_lineColor, bool_dottedLine, bool_withPoint, str_pointColor, int_pointSize, "o", true, int_accumulationMode, bool_logscaleX, bool_logscaleY, str_title, str_xLabel, str_yLabel, bool_withLegend, str_legendLabel, str_legendPosition, "", "red", false, "");
		}catch(FileNotFoundException e) {
			System.out.println(e);
		};
	}

	/**
	 * ※plotRangeのはじめと終わりを同一にすることで自動スケール。<br>
	 */
	public void plot(String pyFilePath, String str_inputPath, String str_outputName, double dou_plotRangeX_start, double dou_plotRangeX_end, double dou_plotRangeY_start, double dou_plotRangeY_end, boolean bool_withLine, String str_lineColor, boolean bool_dottedLine, boolean bool_withPoint, String str_pointColor, int int_pointSize, int int_accumulationMode, boolean bool_logscaleX, boolean bool_logscaleY, String str_title, String str_xLabel, String str_yLabel, boolean bool_withLegend, String str_legendLabel, String str_legendPosition) {
		try {
			core(pyFilePath, str_inputPath, str_outputName, dou_plotRangeX_start, dou_plotRangeX_end, dou_plotRangeY_start, dou_plotRangeY_end, bool_withLine, str_lineColor, bool_dottedLine, bool_withPoint, str_pointColor, int_pointSize, "o", true, int_accumulationMode, bool_logscaleX, bool_logscaleY, str_title, str_xLabel, str_yLabel, bool_withLegend, str_legendLabel, str_legendPosition, "", "red", false, "");
		}catch(FileNotFoundException e) {
			System.out.println(e);
		};
	}

	public void plot(String pyFilePath, String str_inputPath, String str_outputName, double dou_plotRangeX_start, double dou_plotRangeX_end, double dou_plotRangeY_start, double dou_plotRangeY_end, boolean bool_withLine, String str_lineColor, boolean bool_dottedLine, boolean bool_withPoint, String str_pointColor, int int_pointSize, String str_pointDescription, boolean bool_withAnnotate, int int_accumulationMode, boolean bool_logscaleX, boolean bool_logscaleY, String str_title, String str_xLabel, String str_yLabel, boolean bool_withLegend, String str_legendLabel, String str_legendPosition, String str_function, String str_function_LineColors, boolean bool_function_DottedLine, String str_function_LegendLabel){
		try {
			core(pyFilePath, str_inputPath, str_outputName, dou_plotRangeX_start, dou_plotRangeX_end, dou_plotRangeY_start, dou_plotRangeY_end, bool_withLine, str_lineColor, bool_dottedLine, bool_withPoint, str_pointColor, int_pointSize, str_pointDescription, bool_withAnnotate, int_accumulationMode, bool_logscaleX, bool_logscaleY, str_title, str_xLabel, str_yLabel, bool_withLegend, str_legendLabel, str_legendPosition, str_function, str_function_LineColors, bool_function_DottedLine, str_function_LegendLabel);
		}catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}

	private void core(String pyFilePath, String str_inputPath, String str_outputName, double dou_plotRangeX_start, double dou_plotRangeX_end, double dou_plotRangeY_start, double dou_plotRangeY_end, boolean bool_withLine, String str_lineColor, boolean bool_dottedLine, boolean bool_withPoint, String str_pointColor, int int_pointSize, String str_pointDescription, boolean bool_withAnnotate, int int_accumulationMode, boolean bool_logscaleX, boolean bool_logscaleY, String str_title, String str_xLabel, String str_yLabel, boolean bool_withLegend, String str_legendLabel, String str_legendPosition, String str_function, String str_function_LineColors, boolean bool_function_DottedLine, String str_function_LegendLabel) throws FileNotFoundException{
		ArrayList<String> programPath_List = new ArrayList<>();
		// pythonランチャーの在り処候補を列挙
		programPath_List.add("C:\\ProgramData\\Anaconda2\\pythonw.exe");
		programPath_List.add("D:\\shortcut\\Anaconda2\\pythonw.exe");
		programPath_List.add("/users/takehisalab/anaconda3/bin/python");
		programPath_List.add("/usr/bin/python");
		programPath_List.add("/usr/local/bin/python");

		File pyFile = new File(pyFilePath);
		PrintWriter pw = new PrintWriter(pyFile);

		final String BR = "\r\n";

		String code1 =
//				"#!/usr/bin/env python2" + BR +
				"# -*- coding: utf-8 -*-" + BR +
				"" + BR +
				"### ここに入出力情報を打つ";

		ArrayList<String> argCodeList = new ArrayList<>();
		String temp;

		temp = "inputPath = [\"" + str_inputPath + "\"]";
		argCodeList.add(temp);

		temp = "outputName = \"" + str_outputName + "\"";
		argCodeList.add(temp);

		if(dou_plotRangeX_start==dou_plotRangeX_end)
			temp = "plotRangeX = []";
		else
			temp = "plotRangeX = [" + dou_plotRangeX_start + ", " + dou_plotRangeX_end + "]";
		argCodeList.add(temp);

		if(dou_plotRangeY_start==dou_plotRangeY_end)
			temp = "plotRangeY = []";
		else
			temp = "plotRangeY = [" + dou_plotRangeY_start + ", " + dou_plotRangeY_end + "]";
		argCodeList.add(temp);

		if(bool_withLine) temp="withLines = True";
		else temp="withLines = False";
		argCodeList.add(temp);

		temp = "lineColors = [\"" + str_lineColor + "\"]";
		argCodeList.add(temp);

		if(bool_dottedLine) temp="dottedLine = [True]";
		else temp="dottedLine = [False]";
		argCodeList.add(temp);

		if(bool_withPoint) temp="withPoints = True";
		else temp="withPoints = False";
		argCodeList.add(temp);

		temp = "pointColors = [\"" + str_pointColor + "\"]";
		argCodeList.add(temp);

		temp = "pointSizes = [" + int_pointSize + "]";
		argCodeList.add(temp);

		temp = "pointDescription = [\"" + str_pointDescription + "\"]#マーカーの形、'o'で丸、'^'で三角";
		argCodeList.add(temp);

		if(bool_withAnnotate) temp = "withAnnotate = True";
		else temp = "withAnnotate = False";
		argCodeList.add(temp);

		temp = "accumulationMode = " + int_accumulationMode;
		argCodeList.add(temp);

		if(bool_logscaleX) temp="logscaleX = True";
		else temp="logscaleX = False";
		argCodeList.add(temp);

		if(bool_logscaleY) temp="logscaleY = True";
		else temp="logscaleY = False";
		argCodeList.add(temp);

		temp = "title = r\"" + str_title + "\"";
		argCodeList.add(temp);

		temp = "xLabel = r\"" + str_xLabel + "\"";
		argCodeList.add(temp);

		temp = "yLabel = r\"" + str_yLabel + "\"";
		argCodeList.add(temp);

		if(bool_withLegend) temp="withLegend = True";
		else temp="withLegend = False";
		argCodeList.add(temp);

		if(str_legendLabel.length()>0) temp = "legendLabel = [r\"" + str_legendLabel + "\"]";
		else temp = "legendLabel = [\"\"]";
		argCodeList.add(temp);

		temp = "legendPosition = \"" + str_legendPosition + "\"";
		argCodeList.add(temp);

		argCodeList.add("");

		if(str_function.length()>0)
			temp = "function_List = [\"" + str_function + "\"]";
		else
			temp = "function_List = []";
		argCodeList.add(temp);

		temp = "function_LineColors = [\"" + str_function_LineColors + "\"]";
		argCodeList.add(temp);

		if(bool_function_DottedLine) temp="function_DottedLine = [True]";
		else temp="function_DottedLine = [False]";
		argCodeList.add(temp);

		temp = "function_LegendLabel = [r\"" + str_function_LegendLabel + "\"]";
		argCodeList.add(temp);

		String code2="### ここからプログラム内容\r\n" +
				"\r\n" +
				"import matplotlib.pyplot as plt\r\n" +
				"import numpy as np\r\n" +
				"import pandas as pd\r\n" +
				"import os\r\n" +
				"\r\n" +
				"# データ読み取り\r\n" +
				"xList = []\r\n" +
				"yList = []\r\n" +
				"annotateList = []\r\n" +
				"for i in range(len(inputPath)):\r\n" +
				"    # 読み取りDataFrame形式で返す\r\n" +
				"    readData = pd.read_csv(inputPath[i],header=None)\r\n" +
				"    # (DataFrame形式).value で縦横のラベルを剥がしてndarray形式に変更\r\n" +
				"    adarrayData = readData.values\r\n" +
				"    # x,yのデータへ分離\r\n" +
				"    adarrayDataX = adarrayData[:,0]\r\n" +
				"    adarrayDataY = adarrayData[:,1]\r\n" +
				"    # (ndarray形式).tolist() でndarrayをリスト形式へ変換\r\n" +
				"    x = adarrayDataX.tolist()\r\n" +
				"    xList.append(x)\r\n" +
				"    y = adarrayDataY.tolist()\r\n" +
				"    yList.append(y)\r\n" +
				"    # annotate情報を確認、格納\r\n" +
				"    an = []\r\n" +
				"    if len(adarrayData[0])>2:\r\n" +
				"        adarrayAnnotate = adarrayData[:,2]\r\n" +
				"        an = adarrayAnnotate.tolist()\r\n" +
				"    annotateList.append(an)\r\n" +
				"\r\n" +
				"\r\n" +
				"# 累積分布の処理\r\n" +
				"if accumulationMode==1:\r\n" +
				"    for i in range(len(yList)):\r\n" +
				"        for j in range(len(yList[i])-1):\r\n" +
				"            yList[i][j+1] += yList[i][j]\r\n" +
				"if accumulationMode==2:\r\n" +
				"    for i in range(len(yList)):\r\n" +
				"        length = len(yList[i])\r\n" +
				"        for j in range(len(yList[i])-1):\r\n" +
				"            inv_j = length-1-j\r\n" +
				"            yList[i][inv_j-1] += yList[i][inv_j]\r\n" +
				"\r\n" +
				"# プロット\r\n" +
				"width = 0\r\n" +
				"if len(plotRangeX)>0:\r\n" +
				"    plt.xlim(plotRangeX)\r\n" +
				"if len(plotRangeY)>0:\r\n" +
				"    plt.ylim(plotRangeY)\r\n" +
				"if withLines:\r\n" +
				"    width = 2\r\n" +
				"if logscaleX:\r\n" +
				"    plt.xscale(\"log\")\r\n" +
				"if logscaleY:\r\n" +
				"    plt.yscale(\"log\")\r\n" +
				"plt.title(title)\r\n" +
				"plt.xlabel(xLabel)\r\n" +
				"plt.ylabel(yLabel)\r\n" +
				"\r\n" +
				"for i in range(len(inputPath)):\r\n" +
				"    if len(legendLabel)==len(inputPath):\r\n" +
				"        legend = legendLabel[i]\r\n" +
				"    else:\r\n" +
				"        legend = inputPath[i][:inputPath[i].find(\".\")]\r\n" +
				"    if len(dottedLine)>=len(inputPath) and withLines:\r\n" +
				"        if dottedLine[i]:\r\n" +
				"            style = \"--\"\r\n" +
				"        else:\r\n" +
				"            style = \"-\"\r\n" +
				"    else:\r\n" +
				"        style = \"\"\r\n" +
				"    if withPoints:\r\n" +
				"        m = \"o\"\r\n" +
				"        if len(pointDescription)>0:\r\n" +
				"            m = pointDescription[i]\r\n" +
				"        plt.plot(xList[i],yList[i],linestyle=style,linewidth=width,color=lineColors[i],marker=m,fillstyle=\"none\",markersize=pointSizes[i],markeredgecolor=pointColors[i],label=legend)\r\n" +
				"    else:\r\n" +
				"        plt.plot(xList[i],yList[i],linestyle=style,linewidth=width,color=lineColors[i],label=legend)\r\n" +
				"    if (len(annotateList[i])>0 and withAnnotate):\r\n" +
				"        currentAnnotate = annotateList[i]\r\n" +
				"        currentX = xList[i]\r\n" +
				"        currentY = yList[i]\r\n" +
				"        for p in range(len(currentAnnotate)):\r\n" +
				"            plt.annotate(currentAnnotate[p], (currentX[p], currentY[p]), color=pointColors[i])\r\n" +
				"\r\n" +
				"if len(function_List)>0:\r\n" +
				"    xLim = plt.xlim()\r\n" +
				"    beforeYLim = plt.ylim()\r\n" +
				"    x = np.linspace(xLim[0],xLim[1],100)\r\n" +
				"    for i in range(len(function_List)):\r\n" +
				"        exec(function_List[i]) ##ここでを計算\r\n" +
				"        if len(function_LegendLabel)>=len(function_List):\r\n" +
				"            legend = function_LegendLabel[i]\r\n" +
				"        else:\r\n" +
				"            legend = function_List[i]\r\n" +
				"        if len(function_DottedLine)>=len(function_List):\r\n" +
				"            if function_DottedLine[i]:\r\n" +
				"                style = \"--\"\r\n" +
				"            else:\r\n" +
				"                style = \"-\"\r\n" +
				"        plt.plot(x, y, linestyle=style, linewidth=2, color=function_LineColors[i], markersize=0, label=legend)\r\n" +
				"    plt.ylim(beforeYLim)\r\n" +
				"    del x\r\n" +
				"    del y\r\n" +
				"\r\n" +
				"if withLegend:\r\n" +
				"    if len(legendPosition)>0:\r\n" +
				"        plt.legend(loc = legendPosition)\r\n" +
				"    else:\r\n" +
				"        plt.legend()\r\n" +
				"\r\n" +
				"# 書き込み\r\n" +
				"if len(outputName)<=0:\r\n" +
				"    outputName = inputPath[0][:inputPath[0].find(\".csv\")]\r\n" +
				"if (not os.path.exists(\"output\")):\r\n" +
				"    os.makedirs(\"output\")\r\n" +
				"plt.savefig(\"output/\" + outputName + \".png\")\r\n" +
				"plt.savefig(\"output/\" + outputName + \".eps\", transparent=True)\r\n" +
				"plt.savefig(\"output/\" + outputName + \".pdf\", transparent=True)";

		// pyファイル書き込み
		pw.println(code1);
		for(int i=0;i<argCodeList.size();i++) {
			pw.println(argCodeList.get(i));
		}
		pw.println(code2);

		pw.close();

		// 実行
		String selectedPath = "";
		for(int i=0;i<programPath_List.size();i++) {
			if(new File(programPath_List.get(i)).exists()) {
				selectedPath = programPath_List.get(i);
				break;
			}
		}
		if(selectedPath.length()>0) {
			try {
				Runtime.getRuntime().exec(selectedPath + " " + pyFile.getAbsolutePath(), null, pyFile.getParentFile());
			} catch (Exception e) {}
		}


	}

}
