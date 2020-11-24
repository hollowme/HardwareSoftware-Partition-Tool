package gui;

public class ResultofModularize extends ResultFather{

	ResultofModularize(int tasknumber, int modulenumber) {
		super(tasknumber, modulenumber);
		// TODO Auto-generated constructor stub
	}
	void modular(int tasknumber,int modulenumber,int[][] communicatematrix) {
		moduleMental thisTask = new moduleMental();
		thisTask.n = tasknumber;

		int max = (int)Math.ceil((double)thisTask.n/(double)modulenumber);//ģ�����������

		//run:
		int[] haveLocate = new int[thisTask.n];//�Ƿ��Ѽ���ģ��ġ����ñ���0Ϊδ���룬����Ϊ����ģ������
		//��ȡ�ź����·����
		Road[] roadTable = new Road[thisTask.n*thisTask.n];//һά·����
		for(int i = 0;i < thisTask.n;i++){//��ʼ��һά·����
			for(int v = 0;v < thisTask.n;v++){
				roadTable[i*thisTask.n+v] = new Road();
				roadTable[i*thisTask.n+v].cost = communicatematrix[i][v];
				roadTable[i*thisTask.n+v].start = i+1;
				roadTable[i*thisTask.n+v].end = v+1;
			}
		}
		for(int i = 0;i < thisTask.n*thisTask.n-1;i++){//����һά·����
			for(int v = 0;v < thisTask.n*thisTask.n-1;v++){
				if(roadTable[v].cost < roadTable[v+1].cost){
					Road x = roadTable[v];
					roadTable[v] = roadTable[v+1];
					roadTable[v+1] = x;
				}
			}
		}
		//��ʼ��ģ�鼰������
		Module[] the_modules = new Module[modulenumber];
		initialModule(the_modules,max);
		//��ѭ��ִ�з�������
		int tablePoint = 0;
		do {
			//��ȡ��ǰָ���·������������
			int tiNum = roadTable[tablePoint].start,tjNum = roadTable[tablePoint].end;
			if(tiNum==tjNum){
				tablePoint++;
				continue;
			}
			//���������Ƿ�δ����ģ��
			if((haveLocate[tiNum-1]==0)&&(haveLocate[tjNum-1]==0)){
				//�Ƿ����ģ��������+2<=max
				//�����ڣ���������������䵽��ģ����
				existCanAddTwoTasksModule(the_modules,tiNum,tjNum,haveLocate);
			}else{
				//����һ����������ģ��������+1<max
				//�����ڣ�����һ��������䵽��ģ����
				addTogether(the_modules,tiNum,tjNum,haveLocate);
			}
			tablePoint++;//�������Ժ����Ľ��в���
		}while(tablePoint < thisTask.n*thisTask.n);
		//δ������������η��䵽������ٵ�ģ����
		for(int i = 0;i < haveLocate.length;i++){
			if(haveLocate[i]==0){
				int targetModuleNum = 0;//�ó��������ٵ�ģ��
				for(int t = 0,min = 999;t < the_modules.length;t++){
					if(min > the_modules[t].preNum){targetModuleNum = t+1;min = the_modules[t].preNum;}
				}
				haveLocate[i] = targetModuleNum;
				the_modules[targetModuleNum-1].preNum++;
				for(int t = 0;t < the_modules[targetModuleNum-1].tasks.length;t++){
					if(the_modules[targetModuleNum-1].tasks[t].num==0){
						the_modules[targetModuleNum-1].tasks[t].num = i+1;
						break;
					}
				}
			}
		}
		//���뻮��
		for(int i = 0;i < the_modules.length;i++){
			for(int t = 0;t < the_modules[i].preNum;t++){
				modules.get(i).add(the_modules[i].tasks[t].num-1);
			}
		}
		//�������
		for(int i = 0;i < thisTask.n;i++){
			for(int t = i;t < thisTask.n;t++){
				if(haveLocate[i]!=haveLocate[t]){
					commcost += communicatematrix[i][t];
				}
			}
		}
	}
	//n������m��ģ�飬ÿ��ģ�����max������
	private static class moduleMental{
		private int n;
		private int m;
		private int max;

	}
	//·���������������ԣ����ۣ���β����
	private static class Road{
		private int cost,start,end;
	}
	private static class Module{
		int preNum;
		Task[] tasks;
	}
	private static class Task{
		private int num;
	}
	//��ʼ��modulesClass
	private static void initialModule(Module[] modules,int max){
		for(int i = 0; i < modules.length; i++){
			modules[i] = new Module();
			modules[i].tasks = new Task[max];
			for(int v = 0; v < modules[i].tasks.length; v++){
				modules[i].tasks[v] = new Task();
			}
		}
	}
	private  static  void existCanAddTwoTasksModule(Module[] modules, int tiNum,int tjNum,int[] haveLocate){
		for(int i = 0; i < modules.length; i++){
			for(int v = 0; v < modules[i].tasks.length-1; v++){
				//�Ƿ����ģ��������<=max
				if(modules[i].tasks[v].num==0){
					modules[i].preNum += 2;
					modules[i].tasks[v].num = tiNum;
					modules[i].tasks[v+1].num = tjNum;
					haveLocate[tiNum-1] = haveLocate[tjNum-1] = i+1;
					return;
				}
			}
		}
	}
	private  static  void addTogether(Module[] modules, int tiNum,int tjNum,int[] haveLocate){
		//����һ����������ģ��������+1<max
		int orderI = haveLocate[tiNum-1]-1,orderJ = haveLocate[tjNum-1]-1;
		//�����һ�������ѷ�������ģ�����������
		if((haveLocate[tiNum-1]!=0)&&(modules[orderI].preNum + 1 <=modules[orderI].tasks.length)){
			for(int v = 0; v < modules[orderI].tasks.length; v++){
				if(modules[orderI].tasks[v].num==0){
					modules[orderI].preNum += 1;
					//�����һ�������Ѿ�����
					if(haveLocate[tjNum-1]!=0){
						//����ģ����ͬ�����·���
						if(orderI==orderJ){modules[orderI].preNum -= 1;}
						else{
							modules[orderJ].preNum -= 1;//Ŀ��ģ����������һ
							for(int t = 0;t < modules[orderJ].tasks.length;t++){//��Ŀ��ģ��ɾ��������������
								if(modules[orderJ].tasks[t].num==tjNum)modules[orderJ].tasks[t].num = 0;
								if((t < modules[orderJ].tasks.length -1)&&(modules[orderJ].tasks[t].num == 0)){
									modules[orderJ].tasks[t].num = modules[orderJ].tasks[t+1].num;
									modules[orderJ].tasks[t+1].num = 0;
								}
							}
							modules[orderI].tasks[v].num = tjNum;
						}
					}else{//��һ������δ����
						modules[orderI].tasks[v].num = tjNum;
					}
					haveLocate[tjNum-1] = orderI+1;
					return;
				}
			}
		}
		//����ڶ��������ѷ�������ģ�����������
		else if((haveLocate[tjNum-1]!=0)&&(modules[orderJ].preNum + 1 <=modules[orderJ].tasks.length)){
			for(int v = 0; v < modules[orderJ].tasks.length; v++){
				if(modules[orderJ].tasks[v].num==0){
					modules[orderJ].preNum += 1;
					//�����һ�������Ѿ�����
					if(haveLocate[tiNum-1]!=0){
						//����ģ����ͬ�����·���
						if(orderI==orderJ){modules[orderJ].preNum -= 1;}
						else{
							modules[orderI].preNum -= 1;//Ŀ��ģ����������һ
							for(int t = 0;t < modules[orderI].tasks.length;t++){//��Ŀ��ģ��ɾ��������������
								if(modules[orderI].tasks[t].num==tiNum)modules[orderI].tasks[t].num = 0;
								if((t < modules[orderI].tasks.length -1)&&(modules[orderI].tasks[t].num == 0)){
									modules[orderI].tasks[t].num = modules[orderI].tasks[t+1].num;
									modules[orderI].tasks[t+1].num = 0;
								}
							}
							modules[orderJ].tasks[v].num = tiNum;
						}
					}else{//��һ������δ����
						modules[orderJ].tasks[v].num = tiNum;
						haveLocate[tiNum-1] = orderI+1;
					}
					haveLocate[tiNum-1] = orderJ+1;
					return;
				}
			}
		}
	}
}
