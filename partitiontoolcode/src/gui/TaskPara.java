package gui;

public class TaskPara {
	double softcost;
	int softtime;
	double hardcost;
	int hardtime;
	int hardarea;
	
	TaskPara(double softcost,int softtime,double hardcost,int hardtime,int hardarea){
		this.softcost=softcost;
		this.softtime=softtime;
		this.hardcost=hardcost;
		this.hardtime=hardtime;
		this.hardarea=hardarea;
	}
	
	public String toString() {
		String str="";
		str=str+softcost+" "+softtime+" "+hardcost+" "+hardtime+" "+hardarea;
		return str;
	}

}
