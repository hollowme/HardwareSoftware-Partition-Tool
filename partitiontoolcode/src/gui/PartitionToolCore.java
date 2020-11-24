package gui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class PartitionToolCore {
	String filename;//������ļ���
	int tasknumber;//�ļ��ж�ȡ����������
	int[][] communicatematrix;//�ļ��ж�ȡ��ͨ�ž���
	TaskPara[] tp;//�����5������
	int modulenumber;//�����ģ������
	double costconstr;//����Ŀ���Լ��
	int areaconstr;//��������Լ��
	int timeconstr;//�����ʱ��Լ��
	
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
	
	//��ȡ�ļ�
	void getContent() {
		try {
			Scanner sc=new Scanner(Path.of(filename),StandardCharsets.UTF_8);
			//����������
			tasknumber=sc.nextInt();
			//��ͨ�ž���
			communicatematrix=new int[tasknumber][tasknumber];
			for(int i=0;i<tasknumber;i++) {
				for(int j=0;j<tasknumber;j++) {
					communicatematrix[i][j]=sc.nextInt();
				}
			}
			//���������
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
	
	//����������ļ���Ϣ
	String printContent() {
		String content = "";
		content=content+"����������"+tasknumber+"\n";
		content=content+"ͨ�ž��������"+tasknumber+"*"+tasknumber+"\n";
		content=content+"���������\n";
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
	
	//��ȡģ������������Լ�������Լ��
	void getConstr(int modulenumber,double costconstr,int areaconstr,int timeconstr) {
		this.modulenumber=modulenumber;
		this.costconstr=costconstr;
		this.areaconstr=areaconstr;
		this.timeconstr=timeconstr;
	}
	
	//ͨ���Ƚ������㷨�õ����ս����
	String run(int target,int[] commresult,double[] targetresult) {//target����1��2��3�ֱ��Ӧ�������Ŀ��
		String str="";
		
		//���������жϷ�֧������ֻ����Ӳ���������Ŀ�겻ͬʱ�����ַ�����ѡ����
		if (target==1) {//��ϵͳ������Ӳ�����Լ���£�ϵͳ����ʱ�����
			//mmm1�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm1 = new ResultofMMM1(tasknumber, modulenumber);
			mmm1.modular(tasknumber, modulenumber, communicatematrix);
			mmm1.hwswPartition1(tp, costconstr, areaconstr);
			commresult[0]=mmm1.commcost;
			targetresult[0]=mmm1.runtime;
			//str = str + mmm1.print1();
			//str = str + "\n";
			//mmm2�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm2 = new ResultofMMM2(tasknumber, modulenumber);
			mmm2.modular(tasknumber, modulenumber, communicatematrix);
			mmm2.hwswPartition1(tp, costconstr, areaconstr);
			commresult[1]=mmm2.commcost;
			targetresult[1]=mmm2.runtime;
			//str = str + mmm2.print1();
			//str = str + "\n";
			//mmm3�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm3 = new ResultofMMM3(tasknumber, modulenumber);
			mmm3.modular(tasknumber, modulenumber, communicatematrix);
			mmm3.hwswPartition1(tp, costconstr, areaconstr);
			commresult[2]=mmm3.commcost;
			targetresult[2]=mmm3.runtime;
			//str = str + mmm3.print1();
			//str = str + "\n";
			//kl�㷨��ģ�黯����ͻ��ֽ��
			ResultFather kl = new ResultofKL(tasknumber, modulenumber);
			kl.modular(tasknumber, modulenumber, communicatematrix);
			kl.hwswPartition1(tp, costconstr, areaconstr);
			commresult[3]=kl.commcost;
			targetresult[3]=kl.runtime;
			//str = str + kl.print1();
			//str = str + "\n";
			//modularize�㷨��ģ�黯����ͻ��ֽ��
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
			str=str+"���Ž��Ϊ";
			switch(algno) {
			case 0:
				str=str+"������MMM�㷨�Ľ��\n";
				break;
			case 1:
				str=str+"ȫ����MMM�㷨�Ľ��\n";
				break;
			case 2:
				str=str+"������MMM�㷨�Ľ��\n";
				break;
			case 3:
				str=str+"KL�㷨�Ľ��\n";
				break;
			case 4:
				str=str+"Modularize�㷨�Ľ��\n";
				break;
			}
			str=str+"ͨ�Ŵ��ۣ�"+commresult[algno]+"\n";
			str=str+"����ʱ�䣺"+targetresult[algno]+"\n";
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
		else if(target==2) {//��ϵͳ����ʱ���Ӳ�����Լ���£�ϵͳ������С
			//mmm1�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm1 = new ResultofMMM1(tasknumber, modulenumber);
			mmm1.modular(tasknumber, modulenumber, communicatematrix);
			mmm1.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[0]=mmm1.commcost;
			targetresult[0]=mmm1.syscost;
			//str = str + mmm1.print2();
			//str = str + "\n";
			//mmm2�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm2 = new ResultofMMM2(tasknumber, modulenumber);
			mmm2.modular(tasknumber, modulenumber, communicatematrix);
			mmm2.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[1]=mmm2.commcost;
			targetresult[1]=mmm2.syscost;
			//str = str + mmm2.print2();
			//str = str + "\n";
			//mmm3�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm3 = new ResultofMMM3(tasknumber, modulenumber);
			mmm3.modular(tasknumber, modulenumber, communicatematrix);
			mmm3.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[2]=mmm3.commcost;
			targetresult[2]=mmm3.syscost;
			//str = str + mmm3.print2();
			//str = str + "\n";
			//kl�㷨��ģ�黯����ͻ��ֽ��
			ResultFather kl = new ResultofKL(tasknumber, modulenumber);
			kl.modular(tasknumber, modulenumber, communicatematrix);
			kl.hwswPartition2(tp, timeconstr, areaconstr);
			commresult[3]=kl.commcost;
			targetresult[3]=kl.syscost;
			//str = str + kl.print2();
			//str = str + "\n";
			//modularize�㷨��ģ�黯����ͻ��ֽ��
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
			str=str+"���Ž��Ϊ";
			switch(algno) {
			case 0:
				str=str+"������MMM�㷨�Ľ��\n";
				break;
			case 1:
				str=str+"ȫ����MMM�㷨�Ľ��\n";
				break;
			case 2:
				str=str+"������MMM�㷨�Ľ��\n";
				break;
			case 3:
				str=str+"KL�㷨�Ľ��\n";
				break;
			case 4:
				str=str+"Modularize�㷨�Ľ��\n";
				break;
			}
			str=str+"ͨ�Ŵ��ۣ�"+commresult[algno]+"\n";
			str=str+"ϵͳ������"+String.format("%.2f", targetresult[algno])+"\n";
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
		else {//target==3����ϵͳ����ʱ��Լ���£�Ӳ�������С
			//mmm1�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm1 = new ResultofMMM1(tasknumber, modulenumber);
			mmm1.modular(tasknumber, modulenumber, communicatematrix);
			mmm1.hwswPartition3(tp, timeconstr);
			commresult[0]=mmm1.commcost;
			targetresult[0]=mmm1.sysarea;
			//str = str + mmm1.print3();
			//str = str + "\n";
			//mmm2�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm2 = new ResultofMMM2(tasknumber, modulenumber);
			mmm2.modular(tasknumber, modulenumber, communicatematrix);
			mmm2.hwswPartition3(tp, timeconstr);
			commresult[1]=mmm2.commcost;
			targetresult[1]=mmm2.sysarea;
			//str = str + mmm2.print3();
			//str = str + "\n";
			//mmm3�㷨��ģ�黯����ͻ��ֽ��
			ResultFather mmm3 = new ResultofMMM3(tasknumber, modulenumber);
			mmm3.modular(tasknumber, modulenumber, communicatematrix);
			mmm3.hwswPartition3(tp, timeconstr);
			commresult[2]=mmm3.commcost;
			targetresult[2]=mmm3.sysarea;
			//str = str + mmm3.print3();
			//str = str + "\n";
			//kl�㷨��ģ�黯����ͻ��ֽ��
			ResultFather kl = new ResultofKL(tasknumber, modulenumber);
			kl.modular(tasknumber, modulenumber, communicatematrix);
			kl.hwswPartition3(tp, timeconstr);
			commresult[3]=kl.commcost;
			targetresult[3]=kl.sysarea;
			//str = str + kl.print3();
			//str = str + "\n";
			//modularize�㷨��ģ�黯����ͻ��ֽ��
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
			str=str+"���Ž��Ϊ";
			switch(algno) {
			case 0:
				str=str+"������MMM�㷨�Ľ��\n";
				break;
			case 1:
				str=str+"ȫ����MMM�㷨�Ľ��\n";
				break;
			case 2:
				str=str+"������MMM�㷨�Ľ��\n";
				break;
			case 3:
				str=str+"KL�㷨�Ľ��\n";
				break;
			case 4:
				str=str+"Modularize�㷨�Ľ��\n";
				break;
			}
			str=str+"ͨ�Ŵ��ۣ�"+commresult[algno]+"\n";
			str=str+"Ӳ�������"+String.format("%.2f", targetresult[algno])+"\n";
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
