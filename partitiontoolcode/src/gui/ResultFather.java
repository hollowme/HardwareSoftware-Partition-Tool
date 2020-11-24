package gui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

public class ResultFather {
	int taskmax;// ģ�����������ޣ�Ϊtasknumber/modulenumber������ȡ��
	ArrayList<ArrayList<Integer>> modules = new ArrayList<ArrayList<Integer>>();// ģ�黯���
	int commcost;// ͨ�Ŵ���
	int[] hworsw;// ÿ���������Ӳ��ʵ�֣�0Ϊ���ʵ�֣�1ΪӲ��ʵ��
	double[] modulecost;// ÿ��ģ��Ŀ���
	int[] modulearea;// ÿ��ģ���Ӳ�����
	double[] moduletime;// ÿ��ģ�������ʱ��
	double runtime;// ϵͳ����ʱ��
	double syscost;// ϵͳ�����ܺͣ�ȡ��ģ�鿪�����
	double sysarea;// ϵͳ����ܺͣ�ȡ��ģ��������

	ResultFather(int tasknumber, int modulenumber) {
		// ��ʼ��ģ����
		for (int i = 0; i < modulenumber; i++) {
			modules.add(new ArrayList<Integer>());
		}

		// ��ʼ����Ӳ�����ֽ��
		hworsw = new int[tasknumber];
		modulecost = new double[modules.size()];
		modulearea = new int[modules.size()];
		moduletime = new double[modules.size()];

		// ����õ�ģ������������
		if ((tasknumber / modulenumber) * modulenumber == tasknumber) {
			taskmax = tasknumber / modulenumber;
		} else {
			taskmax = tasknumber / modulenumber + 1;
		}
	}

	// ģ�黯����Ҫ��ÿ����ͬ���㷨��д
	void modular(int tasknumber, int modulenumber, int[][] communicatematrix) {
	}

	class result
//�������ֲ���Сʱ��res,��ȡ������ֲ���Сʱ����ҪӲ��������Ϣ
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
//��������cost��area��index
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
		// �ȽϺ���
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
//ʹ�õݹ������Сʱ��
	{
		double res = 0;// ����ֲ���Сʱ��
		defin defS = new defin(); // ��ǰ�������ʵ�֣��������������
		defS.flimit = cost;
		defS.slimit = area;
		defS.index = index - 1;
		if (times.containsKey(defS))
			return times.get(defS).hwo;
		defin defH = new defin(0, 0, 0); // ��ǰ����Ӳ�������������������
		if (index >= 0) {
			defS.flimit = cost - tp[vert[index]].softcost;
			defH.flimit = cost - tp[vert[index]].hardcost;
			defH.slimit = area - tp[vert[index]].hardarea;
			defH.index = index - 1;
		}
		boolean temphwo = false; // ��ǰ�����Ƿ�ҪӲ����
		boolean localhwos[] = new boolean[vert.length]; // ���������Ƿ�ҪӲ��������Ϣ
		for (int i = 0; i < vert.length; i++)
			localhwos[i] = false;
		if (defH.flimit <= 0 || defH.slimit <= 0 || index == 0)
			// ���Ʋ���Ӳ����������Ϊ���һ������
			if (index == 0)
			// ���һ������
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
			// ����ʹ�ø�������Ӳ��ʵ��
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
			double temp1 = tp[vert[index]].hardtime + times.get(defH).res; // ѡȡ��ǰ����ΪӲʵ�֣���ô��Сʱ��Ϊ

			double temp2 = tp[vert[index]].softtime + times.get(defS).res; // ��ǰ����ΪӲʵ��

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
		/* ����map times����Ϣ */
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
	// ʹ�õݹ������Сʱ��
	{
		double res = 0;// ����ֲ���Сʱ��
		defin defS = new defin(); // ��ǰ�������ʵ�֣��������������
		defS.flimit = time;
		defS.slimit = area;
		defS.index = index - 1;
		defin defH = new defin(0, 0, 0);
		defin defSN = new defin(0, 0, 0);// ��ǰ����Ӳ�������������������
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
		boolean temphwo = false; // ��ǰ�����Ƿ�ҪӲ����
		boolean localhwos[] = new boolean[vert.length]; // ���������Ƿ�ҪӲ��������Ϣ
		for (int i = 0; i < vert.length; i++)
			localhwos[i] = false;
		if (defH.flimit <= 0 || defH.slimit <= 0 || index == 0)
			// ���Ʋ���Ӳ����������Ϊ���һ������
			if (index == 0)
			// ���һ������
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
			// ����ʹ�ø�������Ӳ��ʵ��
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
			double temp1 = tp[vert[index]].hardcost + times.get(defH).res; // ѡȡ��ǰ����ΪӲʵ�֣���ô��Сʱ��Ϊ

			double temp2 = tp[vert[index]].softcost + times.get(defSN).res; // ��ǰ����ΪӲʵ��

			if (temp1 <= temp2) {
				temphwo = true;
				res = temp1;
			} else {
				temphwo = false;
				res = temp2;
				localhwos = temphwos;
			}
		}
		/* ����map times����Ϣ */
		localhwos[index] = temphwo;
		result tempres = new result(res, localhwos);
		defS.index++;
		times.put(defS, tempres);
		/*******************/
		return localhwos;
	}

	private boolean[] mintime3(TaskPara[] tp, double time, int index, Map<defin, result> times, int[] vert)
	// ʹ�õݹ������Сʱ��
	{
		double res = 0;// ����ֲ���Сʱ��
		defin defS = new defin(); // ��ǰ�������ʵ�֣��������������
		defS.flimit = time;
		defS.slimit = 0;
		defS.index = index - 1;
		defin defH = new defin(0, 0, 0);
		defin defSN = new defin(0, 0, 0);// ��ǰ����Ӳ�������������������
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
		boolean temphwo = false; // ��ǰ�����Ƿ�ҪӲ����
		boolean localhwos[] = new boolean[vert.length]; // ���������Ƿ�ҪӲ��������Ϣ
		for (int i = 0; i < vert.length; i++)
			localhwos[i] = false;
		if (defH.flimit <= 0 || index == 0)
			// ���Ʋ���Ӳ����������Ϊ���һ������
			if (index == 0)
			// ���һ������
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
			// ����ʹ�ø�������Ӳ��ʵ��
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
			double temp1 = tp[vert[index]].hardarea + times.get(defH).res; // ѡȡ��ǰ����ΪӲʵ�֣���ô��Сʱ��Ϊ

			double temp2 = 0 + times.get(defSN).res; // ��ǰ����ΪӲʵ��

			if (temp1 <= temp2) {
				temphwo = true;
				res = temp1;
			} else {
				temphwo = false;
				res = temp2;
				localhwos = temphwos;
			}
		}
		/* ����map times����Ϣ */
		localhwos[index] = temphwo;
		result tempres = new result(res, localhwos);
		defS.index++;
		times.put(defS, tempres);
		/*******************/
		return localhwos;
	}

	// ��Ӳ�����֣������������������������⣬����Ҫģ�黯��������Ϊhworsw���飬����ʱ��runtime��modulecost,modulearea
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
			for (int j = 0; j < localhwo.length; j++) // ����Ӳʵ��
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

	// ��Ӳ�����֣������������������������⣬����Ҫģ�黯��������Ϊhworsw���飬ϵͳ�����ܺ�syscost��moduletime,modulearea
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
			for (int j = 0; j < localhwo.length; j++) // ����Ӳʵ��
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

	// ��Ӳ�����֣������������������������⣬����Ҫģ�黯��������Ϊhworsw���飬ϵͳ����ܺ�sysarea��moduletime
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
			for (int j = 0; j < localhwo.length; j++) // ����Ӳʵ��
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

	// ���Ŀ��һ��ӡ��Ϣ��
	String print1() {
//		String str = "ͨ�Ŵ��ۣ�";
//		str = str + commcost + "\n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "ģ��" + i + ": ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//		}
//
//		// ����ʱ�ӵ����
//		str = str + "\n" + "��Ӳ������: \n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "ģ��" + i + "Ӳ��ʵ��: ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				if (hworsw[modules.get(i).get(j)] == 1)
//					str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//			str += "ģ�鿪��: " + String.format("%.2f", modulecost[i]) + "\n";
//			str += "ģ�����: " + String.format("%d", modulearea[i]) + "\n";
//		}
//		str += "ϵͳ����ʱ��: " + runtime + "\n";
		
		String str="";
		for(int i=0;i<modules.size();i++) {
			str=str+"ģ��"+i+"��������";
			ArrayList<Integer> swqueue=new ArrayList<Integer>();
			ArrayList<Integer> hwqueue=new ArrayList<Integer>();
			for(int j=0;j<modules.get(i).size();j++) {
				str=str+modules.get(i).get(j)+" ";
				if(hworsw[modules.get(i).get(j)]==0)
					swqueue.add(modules.get(i).get(j));
				else
					hwqueue.add(modules.get(i).get(j));
			}
			str=str+"�������ʵ�֣�";
			for(int j=0;j<swqueue.size();j++)
				str=str+swqueue.get(j)+" ";
			str=str+"Ӳ��ʵ�֣�";
			for(int j=0;j<hwqueue.size();j++)
				str=str+hwqueue.get(j)+" ";
			str=str+"\n";
			str=str+"ģ�鿪����"+String.format("%.2f", modulecost[i])+" ";
			str=str+"ģ�������"+String.format("%d", modulearea[i])+"\n";
		}
		
		return str;
	}

	// ���Ŀ�����ӡ��Ϣ��
	String print2() {
//		String str = "ͨ�Ŵ��ۣ�";
//		str = str + commcost + "\n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "ģ��" + i + ": ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//		}
//
//		// ����ʱ�ӵ����
//		str = str + "\n" + "��Ӳ������: \n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "ģ��" + i + "Ӳ��ʵ��: ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				if (hworsw[modules.get(i).get(j)] == 1)
//					str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//			str += "ģ��ʱ��: " + String.format("%.2f", moduletime[i]) + "\n";
//			str += "ģ�����: " + String.format("%d", modulearea[i]) + "\n";
//		}
//		str += "ϵͳ����: " + syscost + "\n";
		
		String str="";
		for(int i=0;i<modules.size();i++) {
			str=str+"ģ��"+i+"��������";
			ArrayList<Integer> swqueue=new ArrayList<Integer>();
			ArrayList<Integer> hwqueue=new ArrayList<Integer>();
			for(int j=0;j<modules.get(i).size();j++) {
				str=str+modules.get(i).get(j)+" ";
				if(hworsw[modules.get(i).get(j)]==0)
					swqueue.add(modules.get(i).get(j));
				else
					hwqueue.add(modules.get(i).get(j));
			}
			str=str+"�������ʵ�֣�";
			for(int j=0;j<swqueue.size();j++)
				str=str+swqueue.get(j)+" ";
			str=str+"Ӳ��ʵ�֣�";
			for(int j=0;j<hwqueue.size();j++)
				str=str+hwqueue.get(j)+" ";
			str=str+"\n";
			str=str+"ģ��ʱ�䣺"+String.format("%.2f", moduletime[i])+" ";
			str=str+"ģ�������"+String.format("%d", modulearea[i])+"\n";
		}

		return str;
	}

	// ���Ŀ������ӡ��Ϣ��
	String print3() {
//		String str = "ͨ�Ŵ��ۣ�";
//		str = str + commcost + "\n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "ģ��" + i + ": ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//		}
//
//		// ����ʱ�ӵ����
//		str = str + "\n" + "��Ӳ������: \n";
//		for (int i = 0; i < modules.size(); i++) {
//			str = str + "ģ��" + i + "Ӳ��ʵ��: ";
//			for (int j = 0; j < modules.get(i).size(); j++) {
//				if (hworsw[modules.get(i).get(j)] == 1)
//					str = str + modules.get(i).get(j) + " ";
//			}
//			str = str + "\n";
//			str += "ģ��ʱ��: " + String.format("%.2f", moduletime[i]) + "\n";
//		}
//		str += "ϵͳ���: " + sysarea + "\n";
		
		String str="";
		for(int i=0;i<modules.size();i++) {
			str=str+"ģ��"+i+"��������";
			ArrayList<Integer> swqueue=new ArrayList<Integer>();
			ArrayList<Integer> hwqueue=new ArrayList<Integer>();
			for(int j=0;j<modules.get(i).size();j++) {
				str=str+modules.get(i).get(j)+" ";
				if(hworsw[modules.get(i).get(j)]==0)
					swqueue.add(modules.get(i).get(j));
				else
					hwqueue.add(modules.get(i).get(j));
			}
			str=str+"�������ʵ�֣�";
			for(int j=0;j<swqueue.size();j++)
				str=str+swqueue.get(j)+" ";
			str=str+"Ӳ��ʵ�֣�";
			for(int j=0;j<hwqueue.size();j++)
				str=str+hwqueue.get(j)+" ";
			str=str+"\n";
			str=str+"ģ��ʱ�䣺"+String.format("%.2f", moduletime[i])+"\n";
		}

		return str;
	}

}
