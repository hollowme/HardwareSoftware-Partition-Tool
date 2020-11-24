package gui;

public class ResultofModularize extends ResultFather{

	ResultofModularize(int tasknumber, int modulenumber) {
		super(tasknumber, modulenumber);
		// TODO Auto-generated constructor stub
	}
	void modular(int tasknumber,int modulenumber,int[][] communicatematrix) {
		moduleMental thisTask = new moduleMental();
		thisTask.n = tasknumber;

		int max = (int)Math.ceil((double)thisTask.n/(double)modulenumber);//模块最大任务数

		//run:
		int[] haveLocate = new int[thisTask.n];//是否已加入模块的“已用表”，0为未加入，否则为加入模块的序号
		//获取排好序的路径表
		Road[] roadTable = new Road[thisTask.n*thisTask.n];//一维路径表
		for(int i = 0;i < thisTask.n;i++){//初始化一维路径表
			for(int v = 0;v < thisTask.n;v++){
				roadTable[i*thisTask.n+v] = new Road();
				roadTable[i*thisTask.n+v].cost = communicatematrix[i][v];
				roadTable[i*thisTask.n+v].start = i+1;
				roadTable[i*thisTask.n+v].end = v+1;
			}
		}
		for(int i = 0;i < thisTask.n*thisTask.n-1;i++){//排序一维路径表
			for(int v = 0;v < thisTask.n*thisTask.n-1;v++){
				if(roadTable[v].cost < roadTable[v+1].cost){
					Road x = roadTable[v];
					roadTable[v] = roadTable[v+1];
					roadTable[v+1] = x;
				}
			}
		}
		//初始化模块及其任务
		Module[] the_modules = new Module[modulenumber];
		initialModule(the_modules,max);
		//按循环执行反复加入
		int tablePoint = 0;
		do {
			//获取当前指向的路径的两个任务
			int tiNum = roadTable[tablePoint].start,tjNum = roadTable[tablePoint].end;
			if(tiNum==tjNum){
				tablePoint++;
				continue;
			}
			//两个任务是否都未分配模块
			if((haveLocate[tiNum-1]==0)&&(haveLocate[tjNum-1]==0)){
				//是否存在模块任务数+2<=max
				//若存在，将这两个任务分配到该模块中
				existCanAddTwoTasksModule(the_modules,tiNum,tjNum,haveLocate);
			}else{
				//其中一个任务所在模块任务数+1<max
				//若存在，将另一个任务分配到该模块中
				addTogether(the_modules,tiNum,tjNum,haveLocate);
			}
			tablePoint++;//接下来对后续的进行操作
		}while(tablePoint < thisTask.n*thisTask.n);
		//未分配的任务依次分配到任务较少的模块中
		for(int i = 0;i < haveLocate.length;i++){
			if(haveLocate[i]==0){
				int targetModuleNum = 0;//得出任务最少的模块
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
		//导入划分
		for(int i = 0;i < the_modules.length;i++){
			for(int t = 0;t < the_modules[i].preNum;t++){
				modules.get(i).add(the_modules[i].tasks[t].num-1);
			}
		}
		//计算代价
		for(int i = 0;i < thisTask.n;i++){
			for(int t = i;t < thisTask.n;t++){
				if(haveLocate[i]!=haveLocate[t]){
					commcost += communicatematrix[i][t];
				}
			}
		}
	}
	//n个任务，m个模块，每个模块最多max个任务
	private static class moduleMental{
		private int n;
		private int m;
		private int max;

	}
	//路径，具有三个属性：代价，首尾任务
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
	//初始化modulesClass
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
				//是否存在模块任务数<=max
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
		//其中一个任务所在模块任务数+1<max
		int orderI = haveLocate[tiNum-1]-1,orderJ = haveLocate[tjNum-1]-1;
		//如果第一个任务已分配且其模块可以再容纳
		if((haveLocate[tiNum-1]!=0)&&(modules[orderI].preNum + 1 <=modules[orderI].tasks.length)){
			for(int v = 0; v < modules[orderI].tasks.length; v++){
				if(modules[orderI].tasks[v].num==0){
					modules[orderI].preNum += 1;
					//如果另一个任务已经分配
					if(haveLocate[tjNum-1]!=0){
						//两者模块相同，无事发生
						if(orderI==orderJ){modules[orderI].preNum -= 1;}
						else{
							modules[orderJ].preNum -= 1;//目标模块任务数减一
							for(int t = 0;t < modules[orderJ].tasks.length;t++){//从目标模块删除任务，重新排列
								if(modules[orderJ].tasks[t].num==tjNum)modules[orderJ].tasks[t].num = 0;
								if((t < modules[orderJ].tasks.length -1)&&(modules[orderJ].tasks[t].num == 0)){
									modules[orderJ].tasks[t].num = modules[orderJ].tasks[t+1].num;
									modules[orderJ].tasks[t+1].num = 0;
								}
							}
							modules[orderI].tasks[v].num = tjNum;
						}
					}else{//另一个任务未分配
						modules[orderI].tasks[v].num = tjNum;
					}
					haveLocate[tjNum-1] = orderI+1;
					return;
				}
			}
		}
		//如果第二个任务已分配且其模块可以再容纳
		else if((haveLocate[tjNum-1]!=0)&&(modules[orderJ].preNum + 1 <=modules[orderJ].tasks.length)){
			for(int v = 0; v < modules[orderJ].tasks.length; v++){
				if(modules[orderJ].tasks[v].num==0){
					modules[orderJ].preNum += 1;
					//如果另一个任务已经分配
					if(haveLocate[tiNum-1]!=0){
						//两者模块相同，无事发生
						if(orderI==orderJ){modules[orderJ].preNum -= 1;}
						else{
							modules[orderI].preNum -= 1;//目标模块任务数减一
							for(int t = 0;t < modules[orderI].tasks.length;t++){//从目标模块删除任务，重新排列
								if(modules[orderI].tasks[t].num==tiNum)modules[orderI].tasks[t].num = 0;
								if((t < modules[orderI].tasks.length -1)&&(modules[orderI].tasks[t].num == 0)){
									modules[orderI].tasks[t].num = modules[orderI].tasks[t+1].num;
									modules[orderI].tasks[t+1].num = 0;
								}
							}
							modules[orderJ].tasks[v].num = tiNum;
						}
					}else{//另一个任务未分配
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
