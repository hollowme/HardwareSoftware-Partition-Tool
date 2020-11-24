package gui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class PartitionToolCore {
	String filename;//输入的文件名
	int tasknumber;//文件中读取的任务数量
	int[][] communicatematrix;//文件中读取的通信矩阵
	TaskPara[] tp;//任务的5个参数
	int modulenumber;//输入的模块数量
	double costconstr;//输入的开销约束
	int areaconstr;//输入的面积约束
	int timeconstr;//输入的时间约束
	
	PartitionToolCore(){
		filename=null;
		tasknumber=0;
		communicatematrix=null;
		tp=null;
		modulenumber=0;
		costconstr=0;
		areaconstr=0;
		timeconstr=0;
	}
	
	//读取文件
	void getContent() {
		try {
			Scanner sc=new Scanner(Path.of(filename),StandardCharsets.UTF_8);
			//读任务数量
			tasknumber=sc.nextInt();
			//读通信矩阵
			communicatematrix=new int[tasknumber][tasknumber];
			for(int i=0;i<tasknumber;i++) {
				for(int j=0;j<tasknumber;j++) {
					communicatematrix[i][j]=sc.nextInt();
				}
			}
			//读任务参数
			tp=new TaskPara[tasknumber];
			for(int i=0;i<tasknumber;i++) {
				double temp1=sc.nextDouble();
				int temp2=sc.nextInt();
				double temp3=sc.nextDouble();
				int temp4=sc.nextInt();
				int temp5=sc.nextInt();
				tp[i]=new TaskPara(temp1,temp2,temp3,temp4,temp5);
			}
			
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//输出读到的文件信息
	String printContent() {
		String content = "";
		content=content+"任务数量："+tasknumber+"\n";
		content=content+"通信矩阵阶数："+tasknumber+"*"+tasknumber+"\n";
		content=content+"任务参数：\n";
		for(int i=0;i<tasknumber;i++) {
			content=content+"task"+i+": "+tp[i].toString()+"\n";
		}
		
		return content;
	}
	
	String printCommMatrix() {
		String str="";
		for(int i=0;i<tasknumber;i++) {
			for(int j=0;j<tasknumber;j++) {
				str=str+communicatematrix[i][j]+" ";
			}
			str=str+"\n";
		}
		
		return str;
	}
	
	//读取模块数量，开销约束，面积约束
	void getConstr(int modulenumber,double costconstr,int areaconstr,int timeconstr) {
		this.modulenumber=modulenumber;
		this.costconstr=costconstr;
		this.areaconstr=areaconstr;
		this.timeconstr=timeconstr;
	}
	
	//通过比较所有算法得到最终结果。
	String run(int target,int[] commresult,double[] targetresult) {//target等于1，2，3分别对应三个求解目标
		String str="";
		
		//以下三个判断分支的区别只在软硬件划分求解目标不同时，划分方法的选择上
		if (target==1) {//在系统开销和硬件面积约束下，系统运行时间最短
			//mmm1算法的模块化结果和划分结果
			ResultFather mmm1 = new ResultofMMM1(tasknumber, modulenumber);
			mmm1.modular(tasknumber, modulenumber, communicatematrix);
			mmm1.hwswPartition1(tp, costconstr, areaconstr);
			commresult[0]=mmm1.commcost;
			targetresult[0]=mmm1.runtime;
			//str = str + mmm1.print1();
			//str = str + "\n";
			//mmm2算法的模块化结果和划分结果
			ResultFather mmm2 = new ResultofMMM2(tasknumber, modulenumber);
			mmm2.modular(tasknumber, modulenumber, communicatematrix);
			mmm2.hwswPartition1(tp, costconstr, areaconstr);
			commresult[1]=mmm2.commcost;
			targetresult[1]=mmm2.runtime;
			//str = str + mmm2.print1();
			//str = str + "\n";
			//mmm3算法的模块化结果和划分结果
			ResultFather mmm3 = new ResultofMMM3(tasknumber, modulenumber);
			mmm3.modular(tasknumber, modulenumber, communicatematrix);
			mmm3.hwswPartition1(tp, costconstr, areaconstr);
			commresult[2]=mmm3.commcost;
			targetresult[2]=mmm3.runtime;
			//str = str + mmm3.print1();
			//str = str + "\n";
			//kl算法的模块化结果和划分结果
			ResultFather kl = new ResultofKL(tasknumber, modulenumber);
			kl.modular(tasknumber, modulenumber, communicatematrix);
			kl.hwswPartition1(tp, costconstr, areaconstr);
			commresult[3]=kl.commcost;
			targetresult[3]=kl.runtime;
			//str = str + kl.print1();
			//str = str + "\n";
			//modularize算法的模块化结果和划分结果
			ResultFather mod = new ResultofModularize(tasknumber, modulenumber);
			mod.modular(tasknumber, modulenumber, communicatematrix);
			mod.hwswPartition1(tp, costconstr, areaconstr);
			commresult[4]=mod.commcost;
			targetresult[4]=mod.runtime;
			//str = str + mod.print1();
			
			int algno=0;
			for(int i=0;i<5;i++) {
				if(targetresult[i]<targetresult[algno]) {
					algno=i;
				}
			}
			str=str+"最优结果为";
			switch(algno) {
			case 0:
				str=str+"单链接MMM算法的结果\n";
				break;
			case 1:
				str=str+"全链接MMM算法的结果\n";
				break;
			case 2:
				str=str+"均链接MMM算法的结果\n";
				break;
			case 3:
				str=str+"KL算法的结果\n";
				break;
			case 4:
				str=str+"Modularize算法的结果\n";
				break;
			}
			str=str+"通信代价："+commresult[algno]+"\n";
			str=str+"运行时间："+targetresult[algno]+"\n";
			switch(algno) {
			case 0:
				str=str+mmm1.print1();
				break;
			case 1:
				str=str+mmm2.print1();
				break;
			case 2:
				str=str+mmm3.print1();
				break;
			case 3:
				str=str+kl.print1();
				break;
			case 4:
				str=str+mod.print1();
				break;
			}
		}
		else if(target==2) {//在系统运行时间和硬件面积约束下，系统开销最小
			//mmm1算法的模块化结果和划分结果
			ResultFather mmm1 = new ResultofMMM1(tasknumber, modulenumber);
			mmm1.modular(tasknumber, modulenumber, communicatematrix);
			mmm1.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[0]=mmm1.commcost;
			targetresult[0]=mmm1.syscost;
			//str = str + mmm1.print2();
			//str = str + "\n";
			//mmm2算法的模块化结果和划分结果
			ResultFather mmm2 = new ResultofMMM2(tasknumber, modulenumber);
			mmm2.modular(tasknumber, modulenumber, communicatematrix);
			mmm2.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[1]=mmm2.commcost;
			targetresult[1]=mmm2.syscost;
			//str = str + mmm2.print2();
			//str = str + "\n";
			//mmm3算法的模块化结果和划分结果
			ResultFather mmm3 = new ResultofMMM3(tasknumber, modulenumber);
			mmm3.modular(tasknumber, modulenumber, communicatematrix);
			mmm3.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[2]=mmm3.commcost;
			targetresult[2]=mmm3.syscost;
			//str = str + mmm3.print2();
			//str = str + "\n";
			//kl算法的模块化结果和划分结果
			ResultFather kl = new ResultofKL(tasknumber, modulenumber);
			kl.modular(tasknumber, modulenumber, communicatematrix);
			kl.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[3]=kl.commcost;
			targetresult[3]=kl.syscost;
			//str = str + kl.print2();
			//str = str + "\n";
			//modularize算法的模块化结果和划分结果
			ResultFather mod = new ResultofModularize(tasknumber, modulenumber);
			mod.modular(tasknumber, modulenumber, communicatematrix);
			mod.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[4]=mod.commcost;
			targetresult[4]=mod.syscost;
			//str = str + mod.print2();
			
			int algno=0;
			for(int i=0;i<5;i++) {
				if(targetresult[i]<targetresult[algno]) {
					algno=i;
				}
			}
			str=str+"最优结果为";
			switch(algno) {
			case 0:
				str=str+"单链接MMM算法的结果\n";
				break;
			case 1:
				str=str+"全链接MMM算法的结果\n";
				break;
			case 2:
				str=str+"均链接MMM算法的结果\n";
				break;
			case 3:
				str=str+"KL算法的结果\n";
				break;
			case 4:
				str=str+"Modularize算法的结果\n";
				break;
			}
			str=str+"通信代价："+commresult[algno]+"\n";
			str=str+"系统开销："+String.format("%.2f", targetresult[algno])+"\n";
			switch(algno) {
			case 0:
				str=str+mmm1.print2();
				break;
			case 1:
				str=str+mmm2.print2();
				break;
			case 2:
				str=str+mmm3.print2();
				break;
			case 3:
				str=str+kl.print2();
				break;
			case 4:
				str=str+mod.print2();
				break;
			}
		}
		else {//target==3，在系统运行时间约束下，硬件面积最小
			//mmm1算法的模块化结果和划分结果
			ResultFather mmm1 = new ResultofMMM1(tasknumber, modulenumber);
			mmm1.modular(tasknumber, modulenumber, communicatematrix);
			mmm1.hwswPartition3(tp, timeconstr);
			commresult[0]=mmm1.commcost;
			targetresult[0]=mmm1.sysarea;
			//str = str + mmm1.print3();
			//str = str + "\n";
			//mmm2算法的模块化结果和划分结果
			ResultFather mmm2 = new ResultofMMM2(tasknumber, modulenumber);
			mmm2.modular(tasknumber, modulenumber, communicatematrix);
			mmm2.hwswPartition3(tp, timeconstr);
			commresult[1]=mmm2.commcost;
			targetresult[1]=mmm2.sysarea;
			//str = str + mmm2.print3();
			//str = str + "\n";
			//mmm3算法的模块化结果和划分结果
			ResultFather mmm3 = new ResultofMMM3(tasknumber, modulenumber);
			mmm3.modular(tasknumber, modulenumber, communicatematrix);
			mmm3.hwswPartition3(tp, timeconstr);
			commresult[2]=mmm3.commcost;
			targetresult[2]=mmm3.sysarea;
			//str = str + mmm3.print3();
			//str = str + "\n";
			//kl算法的模块化结果和划分结果
			ResultFather kl = new ResultofKL(tasknumber, modulenumber);
			kl.modular(tasknumber, modulenumber, communicatematrix);
			kl.hwswPartition3(tp, timeconstr);
			commresult[3]=kl.commcost;
			targetresult[3]=kl.sysarea;
			//str = str + kl.print3();
			//str = str + "\n";
			//modularize算法的模块化结果和划分结果
			ResultFather mod = new ResultofModularize(tasknumber, modulenumber);
			mod.modular(tasknumber, modulenumber, communicatematrix);
			mod.hwswPartition3(tp, timeconstr);
			commresult[4]=mod.commcost;
			targetresult[4]=mod.sysarea;
			//str = str + mod.print3();
			
			int algno=0;
			for(int i=0;i<5;i++) {
				if(targetresult[i]<targetresult[algno]) {
					algno=i;
				}
			}
			str=str+"最优结果为";
			switch(algno) {
			case 0:
				str=str+"单链接MMM算法的结果\n";
				break;
			case 1:
				str=str+"全链接MMM算法的结果\n";
				break;
			case 2:
				str=str+"均链接MMM算法的结果\n";
				break;
			case 3:
				str=str+"KL算法的结果\n";
				break;
			case 4:
				str=str+"Modularize算法的结果\n";
				break;
			}
			str=str+"通信代价："+commresult[algno]+"\n";
			str=str+"硬件面积："+String.format("%.2f", targetresult[algno])+"\n";
			switch(algno) {
			case 0:
				str=str+mmm1.print3();
				break;
			case 1:
				str=str+mmm2.print3();
				break;
			case 2:
				str=str+mmm3.print3();
				break;
			case 3:
				str=str+kl.print3();
				break;
			case 4:
				str=str+mod.print3();
				break;
			}
		}
		return str;
	}
	
}//PartitionToolCore
