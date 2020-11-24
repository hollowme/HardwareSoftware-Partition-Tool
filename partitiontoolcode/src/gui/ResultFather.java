package gui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

public class ResultFather {
	int taskmax;// 模块内任务上限，为tasknumber/modulenumber的向上取整
	ArrayList<ArrayList<Integer>> modules = new ArrayList<ArrayList<Integer>>();// 模块化结果
	int commcost;// 通信代价
	int[] hworsw;// 每个任务的软硬件实现，0为软件实现，1为硬件实现
	double[] modulecost;// 每个模块的开销
	int[] modulearea;// 每个模块的硬件面积
	double[] moduletime;// 每个模块的运行时间
	double runtime;// 系统运行时长
	double syscost;// 系统开销总和，取各模块开销相加
	double sysarea;// 系统面积总和，取各模块面积相加

	ResultFather(int tasknumber, int modulenumber) {
		// 初始化模块结果
		for (int i = 0; i < modulenumber; i++) {
			modules.add(new ArrayList<Integer>());
		}

		// 初始化软硬件划分结果
		hworsw = new int[tasknumber];
		modulecost = new double[modules.size()];
		modulearea = new int[modules.size()];
		moduletime = new double[modules.size()];

		// 计算得到模块内任务上限
		if ((tasknumber / modulenumber) * modulenumber == tasknumber) {
			taskmax = tasknumber / modulenumber;
		} else {
			taskmax = tasknumber / modulenumber + 1;
		}
	}

	// 模块化，需要被每个不同的算法重写
	void modular(int tasknumber, int modulenumber, int[][] communicatematrix) {
	}

	class result
//储存结果局部最小时间res,和取得这个局部最小时间需要硬件化的信息
	{
		public double res = 10;
		public boolean[] hwo;

		public result(double resconstr, boolean[] hwoconstr) {
			this.res = resconstr;
			this.hwo = hwoconstr;
		}

		public result() {
			this.res = 0;
		};
	}

	class defin
//储存限制cost，area，index
	{
		public double flimit;
		public double slimit;
		public int index;

		public defin(double fconstr, double sconstr, int indexconstr) {
			this.flimit = fconstr;
			this.slimit = sconstr;
			this.index = indexconstr;
		}

		public defin() {
			this.flimit = 0;
			this.slimit = 0;
			this.index = 0;
		}

		public int hashCode() {
			return Objects.hash(flimit, slimit, index);
		}

		public boolean equals(Object o)
		// 比较函数
		{
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			defin test = (defin) o;
			return flimit == test.flimit && index == test.index && Objects.equals(slimit, test.slimit);
		}
	}

	private boolean[] mintime1(TaskPara[] tp, double cost, double area, int index, Map<defin, result> times, int[] vert,
			int maxTsktime)
//使用递归计算最小时间
	{
		double res = 0;// 储存局部最小时间
		defin defS = new defin(); // 当前任务软件实现，后续任务的限制
		defS.flimit = cost;
		defS.slimit = area;
		defS.index = index - 1;
		if (times.containsKey(defS))
			return times.get(defS).hwo;
		defin defH = new defin(0, 0, 0); // 当前任务硬件化，后续任务的限制
		if (index >= 0) {
			defS.flimit = cost - tp[vert[index]].softcost;
			defH.flimit = cost - tp[vert[index]].hardcost;
			defH.slimit = area - tp[vert[index]].hardarea;
			defH.index = index - 1;
		}
		boolean temphwo = false; // 当前任务是否要硬件化
		boolean localhwos[] = new boolean[vert.length]; // 后续任务是否要硬件化的信息
		for (int i = 0; i < vert.length; i++)
			localhwos[i] = false;
		if (defH.flimit <= 0 || defH.slimit <= 0 || index == 0)
			// 限制不够硬件化，或者为最后一个任务
			if (index == 0)
			// 最后一个任务
			{
				if (defH.flimit < 0 || defH.slimit < 0) {
					temphwo = false;
					if (defS.flimit < 0)
						res = maxTsktime;
					else
						res = tp[vert[index]].softtime;
				}

				else {
					temphwo = true;
					res = tp[vert[index]].hardtime;
				}
			} else
			// 限制使得该任务不能硬件实现
			{
				if (defH.flimit < 0 || defH.slimit < 0) {
					temphwo = false;
					localhwos = mintime1(tp, defS.flimit, defS.slimit, index - 1, times, vert, maxTsktime);
					if (defS.flimit < 0)
						res = maxTsktime + times.get(defS).res;
					else {
						res = tp[vert[index]].softtime + times.get(defS).res;
					}
				} else {
					temphwo = true;
					localhwos = mintime1(tp, defH.flimit, defH.slimit, index - 1, times, vert, maxTsktime);
					res = tp[vert[index]].hardtime + times.get(defH).res;
				}
			}

		else {
			localhwos = mintime1(tp, defH.flimit, defH.slimit, index - 1, times, vert, maxTsktime);
			boolean temphwos[] = new boolean[vert.length];
			temphwos = mintime1(tp, defS.flimit, defS.slimit, index - 1, times, vert, maxTsktime);
			double temp1 = tp[vert[index]].hardtime + times.get(defH).res; // 选取当前任务为硬实现，那么最小时间为

			double temp2 = tp[vert[index]].softtime + times.get(defS).res; // 当前任务不为硬实现

			if (temp1 <= temp2 && temp1 < maxTsktime) {
				temphwo = true;
				res = temp1;
			} else if (temp1 > temp2 && temp1 >= maxTsktime && temp2 >= maxTsktime) {
				temphwo = false;
				res = temp1;
				localhwos = temphwos;
			} else {
				temphwo = false;
				res = temp2;
				localhwos = temphwos;
			}
		}
		/* 更新map times的信息 */
		//System.out.println("res = " + res);
		localhwos[index] = temphwo;
		result tempres = new result(res, localhwos);
		defS.index++;
		defS.flimit = cost;
		times.put(defS, tempres);
		/*******************/
		return localhwos;
	}

	private boolean[] mintime2(TaskPara[] tp, double time, double area, int index, Map<defin, result> times, int[] vert)
	// 使用递归计算最小时间
	{
		double res = 0;// 储存局部最小时间
		defin defS = new defin(); // 当前任务软件实现，后续任务的限制
		defS.flimit = time;
		defS.slimit = area;
		defS.index = index - 1;
		defin defH = new defin(0, 0, 0);
		defin defSN = new defin(0, 0, 0);// 当前任务硬件化，后续任务的限制
		if (index >= 0) {
			defSN.flimit = time - tp[vert[index]].softtime;
			defSN.slimit = area;
			defSN.index = index - 1;
			defH.flimit = time - tp[vert[index]].hardtime;
			defH.slimit = area - tp[vert[index]].hardarea;
			defH.index = index - 1;
		}
		if (times.containsKey(defS))
			return times.get(defS).hwo;
		boolean temphwo = false; // 当前任务是否要硬件化
		boolean localhwos[] = new boolean[vert.length]; // 后续任务是否要硬件化的信息
		for (int i = 0; i < vert.length; i++)
			localhwos[i] = false;
		if (defH.flimit <= 0 || defH.slimit <= 0 || index == 0)
			// 限制不够硬件化，或者为最后一个任务
			if (index == 0)
			// 最后一个任务
			{
				if (defSN.flimit >= 0) {
					temphwo = false;
					res = tp[vert[index]].softcost;
				}

				else {
					temphwo = true;
					if (defH.flimit < 0)
						res = 100 * tp[vert[index]].hardcost;
					else
						res = tp[vert[index]].hardcost;
				}
			} else
			// 限制使得该任务不能硬件实现
			{
				if (defSN.flimit >= 0) {
					temphwo = false;
					localhwos = mintime2(tp, time - tp[vert[index]].softtime, area, index - 1, times, vert);
					res = tp[vert[index]].softcost + times.get(defSN).res;
				} else {
					temphwo = true;
					localhwos = mintime2(tp, defH.flimit, defH.slimit, index - 1, times, vert);
					res = tp[vert[index]].hardcost + times.get(defH).res;
				}
			}

		else {
			localhwos = mintime2(tp, defH.flimit, defH.slimit, index - 1, times, vert);
			boolean temphwos[] = new boolean[vert.length];
			temphwos = mintime2(tp, time - tp[vert[index]].softtime, area, index - 1, times, vert);
			double temp1 = tp[vert[index]].hardcost + times.get(defH).res; // 选取当前任务为硬实现，那么最小时间为

			double temp2 = tp[vert[index]].softcost + times.get(defSN).res; // 当前任务不为硬实现

			if (temp1 <= temp2) {
				temphwo = true;
				res = temp1;
			} else {
				temphwo = false;
				res = temp2;
				localhwos = temphwos;
			}
		}
		/* 更新map times的信息 */
		localhwos[index] = temphwo;
		result tempres = new result(res, localhwos);
		defS.index++;
		times.put(defS, tempres);
		/*******************/
		return localhwos;
	}

	private boolean[] mintime3(TaskPara[] tp, double time, int index, Map<defin, result> times, int[] vert)
	// 使用递归计算最小时间
	{
		double res = 0;// 储存局部最小时间
		defin defS = new defin(); // 当前任务软件实现，后续任务的限制
		defS.flimit = time;
		defS.slimit = 0;
		defS.index = index - 1;
		defin defH = new defin(0, 0, 0);
		defin defSN = new defin(0, 0, 0);// 当前任务硬件化，后续任务的限制
		if (index >= 0) {
			defSN.flimit = time - tp[vert[index]].softtime;
			defSN.slimit = 0;
			defSN.index = index - 1;
			defH.flimit = time - tp[vert[index]].hardtime;
			defH.slimit = 0;
			defH.index = index - 1;
		}
		if (times.containsKey(defS))
			return times.get(defS).hwo;
		boolean temphwo = false; // 当前任务是否要硬件化
		boolean localhwos[] = new boolean[vert.length]; // 后续任务是否要硬件化的信息
		for (int i = 0; i < vert.length; i++)
			localhwos[i] = false;
		if (defH.flimit <= 0 || index == 0)
			// 限制不够硬件化，或者为最后一个任务
			if (index == 0)
			// 最后一个任务
			{
				if (defSN.flimit >= 0) {
					temphwo = false;
					res = 0;
				}

				else {
					temphwo = true;
					if (defH.flimit < 0)
						res = 100 * tp[vert[index]].hardarea;
					else
						res = tp[vert[index]].hardarea;
				}
			} else
			// 限制使得该任务不能硬件实现
			{
				if (defSN.flimit >= 0) {
					temphwo = false;
					localhwos = mintime3(tp, time - tp[vert[index]].softtime, index - 1, times, vert);
					res = 0 + times.get(defSN).res;
				} else {
					temphwo = true;
					localhwos = mintime3(tp, defH.flimit, index - 1, times, vert);
					res = tp[vert[index]].hardarea + times.get(defH).res;
				}
			}

		else {
			localhwos = mintime3(tp, defH.flimit, index - 1, times, vert);
			boolean temphwos[] = new boolean[vert.length];
			temphwos = mintime3(tp, time - tp[vert[index]].softtime, index - 1, times, vert);
			double temp1 = tp[vert[index]].hardarea + times.get(defH).res; // 选取当前任务为硬实现，那么最小时间为

			double temp2 = 0 + times.get(defSN).res; // 当前任务不为硬实现

			if (temp1 <= temp2) {
				temphwo = true;
				res = temp1;
			} else {
				temphwo = false;
				res = temp2;
				localhwos = temphwos;
			}
		}
		/* 更新map times的信息 */
		localhwos[index] = temphwo;
		result tempres = new result(res, localhwos);
		defS.index++;
		times.put(defS, tempres);
		/*******************/
		return localhwos;
	}

	// 软硬件划分，输入除了括号里的三个参数外，还需要模块化结果。输出为hworsw数组，运行时长runtime，modulecost,modulearea
	void hwswPartition1(TaskPara[] tp, double costconstr, int areaconstr) {

		double maxTime = 0;
		double totalcost = 0;
		int totalarea = 0;
		for (int i = 0; i < modules.size(); i++) {
			totalcost = 0;
			totalarea = 0;
			int maxTsktime = 0;
			int vert[] = new int[modules.get(i).size()];
			for (int j = 0; j < vert.length; j++) {
				vert[j] = modules.get(i).get(j);
				maxTsktime = maxTsktime < tp[vert[j]].softtime ? tp[vert[j]].softtime : maxTsktime;
			}
			maxTsktime += 1;
			maxTsktime *= vert.length;
			Map<defin, result> m = new HashMap<defin, result>();
			boolean localhwo[] = new boolean[vert.length];
			localhwo = mintime1(tp, costconstr, areaconstr, vert.length - 1, m, vert, maxTsktime);
			//System.out.println("maxTsktime = " + maxTsktime);
			for (int j = 0; j < localhwo.length; j++) // 储存硬实现
			{
				if (localhwo[j]) {
					hworsw[vert[j]] = 1;
					totalcost += tp[vert[j]].hardcost;
					totalarea += tp[vert[j]].hardarea;
				} else {
					totalcost += tp[vert[j]].softcost;
					totalarea += 0;
				}
			}
			defin tempDef = new defin(costconstr, areaconstr, vert.length - 1);
			double localtime = m.get(tempDef).res;
			maxTime = Math.max(maxTime, localtime);
			modulecost[i] = totalcost;
			modulearea[i] = totalarea;
		}
		runtime = maxTime;
	}

	// 软硬件划分，输入除了括号里的三个参数外，还需要模块化结果。输出为hworsw数组，系统开销总和syscost，moduletime,modulearea
	void hwswPartition2(TaskPara[] tp, int timeconstr, int areaconstr) {
		double totaltime = 0;
		int totalarea = 0;
		syscost = 0;
		for (int i = 0; i < modules.size(); i++) {
			totaltime = 0;
			totalarea = 0;
			int vert[] = new int[modules.get(i).size()];
			for (int j = 0; j < vert.length; j++)
				vert[j] = modules.get(i).get(j);
			Map<defin, result> m = new HashMap<defin, result>();
			boolean localhwo[] = new boolean[vert.length];
			localhwo = mintime2(tp, timeconstr, areaconstr, vert.length - 1, m, vert);
			for (int j = 0; j < localhwo.length; j++) // 储存硬实现
			{
				if (localhwo[j]) {
					hworsw[vert[j]] = 1;
					totaltime += tp[vert[j]].hardtime;
					totalarea += tp[vert[j]].hardarea;
				} else {
					totaltime += tp[vert[j]].softtime;
					totalarea += 0;
				}
			}
			defin tempDef = new defin(timeconstr, areaconstr, vert.length - 1);
			double localcost = m.get(tempDef).res;
			syscost += localcost;
			moduletime[i] = totaltime;
			modulearea[i] = totalarea;
		}

	}

	// 软硬件划分，输入除了括号里的三个参数外，还需要模块化结果。输出为hworsw数组，系统面积总和sysarea，moduletime
	void hwswPartition3(TaskPara[] tp, int timeconstr) {
		sysarea = 0;
		double totaltime = 0;

		for (int i = 0; i < modules.size(); i++) {
			totaltime = 0;
			int vert[] = new int[modules.get(i).size()];
			for (int j = 0; j < vert.length; j++)
				vert[j] = modules.get(i).get(j);
			Map<defin, result> m = new HashMap<defin, result>();
			boolean localhwo[] = new boolean[vert.length];
			localhwo = mintime3(tp, timeconstr, vert.length - 1, m, vert);
			for (int j = 0; j < localhwo.length; j++) // 储存硬实现
			{
				if (localhwo[j]) {
					hworsw[vert[j]] = 1;
					totaltime += tp[vert[j]].hardtime;
				} else {
					totaltime += tp[vert[j]].softtime;
				}
			}
			defin tempDef = new defin(timeconstr, 0, vert.length - 1);
			double localarea = m.get(tempDef).res;
			sysarea += localarea;
			moduletime[i] = totaltime;
		}
	}

	// 求解目标一打印信息。
	String print1() {
//		String str = "通信代价：";
//		str = str + commcost + "\n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "模块" + i + ": ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//		}
//
//		// 测试时加的输出
//		str = str + "\n" + "软硬件划分: \n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "模块" + i + "硬件实现: ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				if (hworsw[modules.get(i).get(j)] == 1)
//					str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//			str += "模块开销: " + String.format("%.2f", modulecost[i]) + "\n";
//			str += "模块面积: " + String.format("%d", modulearea[i]) + "\n";
//		}
//		str += "系统运行时间: " + runtime + "\n";
		
		String str="";
		for(int i=0;i<modules.size();i++) {
			str=str+"模块"+i+"包含任务：";
			ArrayList<Integer> swqueue=new ArrayList<Integer>();
			ArrayList<Integer> hwqueue=new ArrayList<Integer>();
			for(int j=0;j<modules.get(i).size();j++) {
				str=str+modules.get(i).get(j)+" ";
				if(hworsw[modules.get(i).get(j)]==0)
					swqueue.add(modules.get(i).get(j));
				else
					hwqueue.add(modules.get(i).get(j));
			}
			str=str+"其中软件实现：";
			for(int j=0;j<swqueue.size();j++)
				str=str+swqueue.get(j)+" ";
			str=str+"硬件实现：";
			for(int j=0;j<hwqueue.size();j++)
				str=str+hwqueue.get(j)+" ";
			str=str+"\n";
			str=str+"模块开销："+String.format("%.2f", modulecost[i])+" ";
			str=str+"模块面积："+String.format("%d", modulearea[i])+"\n";
		}
		
		return str;
	}

	// 求解目标二打印信息。
	String print2() {
//		String str = "通信代价：";
//		str = str + commcost + "\n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "模块" + i + ": ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//		}
//
//		// 测试时加的输出
//		str = str + "\n" + "软硬件划分: \n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "模块" + i + "硬件实现: ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				if (hworsw[modules.get(i).get(j)] == 1)
//					str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//			str += "模块时间: " + String.format("%.2f", moduletime[i]) + "\n";
//			str += "模块面积: " + String.format("%d", modulearea[i]) + "\n";
//		}
//		str += "系统开销: " + syscost + "\n";
		
		String str="";
		for(int i=0;i<modules.size();i++) {
			str=str+"模块"+i+"包含任务：";
			ArrayList<Integer> swqueue=new ArrayList<Integer>();
			ArrayList<Integer> hwqueue=new ArrayList<Integer>();
			for(int j=0;j<modules.get(i).size();j++) {
				str=str+modules.get(i).get(j)+" ";
				if(hworsw[modules.get(i).get(j)]==0)
					swqueue.add(modules.get(i).get(j));
				else
					hwqueue.add(modules.get(i).get(j));
			}
			str=str+"其中软件实现：";
			for(int j=0;j<swqueue.size();j++)
				str=str+swqueue.get(j)+" ";
			str=str+"硬件实现：";
			for(int j=0;j<hwqueue.size();j++)
				str=str+hwqueue.get(j)+" ";
			str=str+"\n";
			str=str+"模块时间："+String.format("%.2f", moduletime[i])+" ";
			str=str+"模块面积："+String.format("%d", modulearea[i])+"\n";
		}

		return str;
	}

	// 求解目标三打印信息。
	String print3() {
//		String str = "通信代价：";
//		str = str + commcost + "\n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "模块" + i + ": ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//		}
//
//		// 测试时加的输出
//		str = str + "\n" + "软硬件划分: \n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "模块" + i + "硬件实现: ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				if (hworsw[modules.get(i).get(j)] == 1)
//					str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//			str += "模块时间: " + String.format("%.2f", moduletime[i]) + "\n";
//		}
//		str += "系统面积: " + sysarea + "\n";
		
		String str="";
		for(int i=0;i<modules.size();i++) {
			str=str+"模块"+i+"包含任务：";
			ArrayList<Integer> swqueue=new ArrayList<Integer>();
			ArrayList<Integer> hwqueue=new ArrayList<Integer>();
			for(int j=0;j<modules.get(i).size();j++) {
				str=str+modules.get(i).get(j)+" ";
				if(hworsw[modules.get(i).get(j)]==0)
					swqueue.add(modules.get(i).get(j));
				else
					hwqueue.add(modules.get(i).get(j));
			}
			str=str+"其中软件实现：";
			for(int j=0;j<swqueue.size();j++)
				str=str+swqueue.get(j)+" ";
			str=str+"硬件实现：";
			for(int j=0;j<hwqueue.size();j++)
				str=str+hwqueue.get(j)+" ";
			str=str+"\n";
			str=str+"模块时间："+String.format("%.2f", moduletime[i])+"\n";
		}

		return str;
	}

}
