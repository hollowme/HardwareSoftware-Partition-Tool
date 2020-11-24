package gui;
//单链接
public class ResultofMMM1 extends ResultFather{
	
	ResultofMMM1(int tasknumber,int modulenumber){
		super(tasknumber, modulenumber);
	}
	
	void modular(int tasknumber,int modulenumber,int[][] communicatematrix) {
		
		int n = tasknumber;
		int pn = modulenumber;
		int m =taskmax;
		double[][] matrix=new double[n][n];//距离矩阵
		
		double maxdist=0;
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				matrix[i][j]=communicatematrix[i][j];
				if(matrix[i][j]>maxdist)
					maxdist=matrix[i][j];
			}
		}
		
		//main part
		int[] setno=new int[n];//记录每个集合元素数量
		int[] K=new int[n];//所属集合
		for(int i=0;i<n;i++) {
			setno[i]=1;//初始化每个元素为1个集合，故元素数量均为1
			K[i]=i;//初始化集合标号，开始时各自为一个集合，故标号为自身
		}
		double thCost=maxdist;
		int absk=n;//初始化每个元素为1个集合，故集合数量为n
		while(absk>pn && thCost>0) {
			
			//检测死锁
			int min1=n;int g=0;
			int min2=n;int h=0;
			for(int x=0;x<n;x++) {
				if(setno[x]<min1&&setno[x]!=0) {
					min1=setno[x];
					g=x;
				}
			}
			for(int x=0;x<n;x++) {
				if(setno[x]<min2&&x!=g&&setno[x]!=0) {
					min2=setno[x];
					h=x;
				}
			}
			if(min1+min2>m) {//如果存在死锁，将h集合中与g最相似的加入g
				double max=0;
				int e=0;
				int f = 0;
				for(int i=0;i<n;i++) {
					if(K[i]==g) {
						for(int j=0;j<n;j++) {
							if(K[j]==h) {
								if(matrix[i][j]>max) {
									max=matrix[i][j];
									e=i;f=j;
								}
							}
						}
					}
				}
				K[f]=g;
				setno[g]++;
				setno[h]--;
			}
			
			for(int i=0;i<n&&absk>pn;i++) {
				if (setno[i]!=0) {
					for (int j = i + 1; j < n&&absk>pn; j++) {
						if (setno[j]!=0) {
							double maxd = 0;//两个集合的相似度
							for (int a = 0; a < n; a++) {
								if (K[a]==i) {
									for (int b = 0; b < n ; b++) {
										if (K[b]==j) {
											if (matrix[a][b] > maxd) {
												maxd = matrix[a][b];//更新相似度
											} 
										}
									} 
								}
							}
							if (maxd >= thCost && setno[i]+setno[j] <= m) {//条件满足，合并集合i,j
								setno[i]=setno[i]+setno[j];
								setno[j] = 0;
								for (int x = 0; x < n; x++) {
									if (K[x] == j) {
										K[x] = i;
									}
								}
								absk = absk - 1;
							}
						}
					}
				}
			}
			thCost=thCost-1;//循环一次减少阈值
		}
		int groupno=0;
		//输出划分和代价
		for(int i=0;i<n;i++) {
			if(setno[i]!=0) {
				for(int j=0;j<n;j++) {
					if(K[j]==i) {
						modules.get(groupno).add(j);
					}
				}
				++groupno;
			}
		}
		double cost=0;
		for(int i=0;i<n;i++) {
			if (setno[i]!=0) {
				for (int j = i + 1; j < n; j++) {
					if (setno[j]!=0) {
						for (int a = 0; a < n; a++) {
							if (K[a]==i) {
								for (int b = 0; b < n ; b++) {
									if(K[b]==j) {
										cost=cost+matrix[a][b];
									}
								}
							}
						}
					}
				}
			}
		}
		commcost=(int) cost;
		
	}

}
