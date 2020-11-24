package gui;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
public class ResultofKL extends ResultFather{

	ResultofKL(int tasknumber, int modulenumber) {
		super(tasknumber, modulenumber);
		// TODO Auto-generated constructor stub
	}
	
	ArrayList<ArrayList<Integer>> Divides=new ArrayList<ArrayList<Integer>>();
	private static void calculateDG(double[][]G,double[][]matrix,ArrayList<Integer>A,ArrayList<Integer>B) {
		ArrayList<Integer>Da=new ArrayList<Integer>();
		ArrayList<Integer>Db=new ArrayList<Integer>();
		for(int i=0;i<A.size();i++)
		{
			int Ea=0;
			int Ia=0;
			int Eb=0;
			int Ib=0;
		  for(int j=0;j<B.size();j++)
		  {
			  Ea+=matrix[A.get(i)][B.get(j)];
			  Eb+=matrix[B.get(i)][A.get(j)];
			  if(i!=j)
			  {
				  Ia+=matrix[A.get(i)][A.get(j)];
				  Ib+=matrix[B.get(i)][B.get(j)];
			  }
		  }
		  Da.add(Ea-Ia);
		  Db.add(Eb-Ib);
		}
		for(int i=0;i<A.size();i++)
			for(int j=0;j<B.size();j++)
			{
				double cost=matrix[A.get(i)][B.get(j)];
				G[i][j]=Da.get(i)+Db.get(j)-2*cost;
			}
	}
	
	private void KLrecursion(double[][]matrix,ArrayList<Integer>array)
	{
		if(array.size()<=taskmax)
		{
			modules.add(array);
			return;
		}
		TreeSet<Integer>SA=new TreeSet<Integer>();
		int num=0;
		int len=array.size();
		Random r=new Random();
		for(int i=0;i<len/2;i++)
		{
				num=r.nextInt(array.size());
				SA.add(array.get(num));
				array.remove(num);
		}
		ArrayList<Integer>sA=new ArrayList<Integer>(SA);
		ArrayList<Integer>sB=new ArrayList<Integer>(array);
		KL(matrix,sA,sB);
		KLrecursion(matrix, sA);
		KLrecursion(matrix, sB);
	}
	private void KL(double[][]matrix,ArrayList<Integer>A,ArrayList<Integer>B)
	{
		int n=A.size();
		int swaps=0;
		ArrayList<Integer>change_A=new ArrayList<Integer>();
		ArrayList<Integer>change_B=new ArrayList<Integer>();
		ArrayList<Double>max_D=new ArrayList<Double>();
	    //2划分KL
		while(swaps!=n) {
		double[][] G=new double[n][n];
		calculateDG(G, matrix, A, B);
		double max_cost=G[0][0];
		int max_i=0;
		int max_j=0;
		for(int i=0;i<A.size();i++)
			for (int j = 0;j < A.size();j++){  	   
			 if (max_cost < G[i][j]) {
				 max_cost = G[i][j];
				 max_i = i;
				 max_j = j;
			 } 
			}
		change_A.add(A.get(max_i));
		change_B.add(B.get(max_j));
		max_D.add(G[max_i][max_j]);
		A.remove(max_i);
		B.remove(max_j);
		swaps++;
	  }	
		ArrayList<Double>g=new ArrayList<Double>();
		int count=0;
		for(int i=0;i<max_D.size();i++) {
			for(int j=count;j<max_D.size();j++)
			{
				if (i == 0)
					g.add(max_D.get(i));
				else
				{
	              g.set(j,g.get(i-1)+max_D.get(i));
				}
			}
			count++;
		}
		double sum=0;
		int flag1 = 1;
		int change_position = 0;
		for (int i = 0;i < g.size();i++) {
			if (flag1 == 1)
			{
				sum = g.get(i);
				change_position = i;
				flag1 = 0;
			}
			else
			{
				if (g.get(i) > sum)
				{
					sum = g.get(i);
					change_position = i;
				}
			}
		}
		if(sum>0)
		{
			for(int i=0;i<=change_position;i++)
			{
				A.add(change_B.get(i));
				B.add(change_A.get(i));
			}
			for(int i=change_position+1;i<n;i++)
			{
				A.add(change_A.get(i));
				B.add(change_B.get(i));
			}
		}
		else {
			{
				for(int i=0;i<n;i++)
				{
					A.add(change_A.get(i));
					B.add(change_B.get(i));
				}
			}
		}
	}
	
	void modular(int tasknumber,int modulenumber,int[][] communicatematrix) {
		int n = tasknumber;
		int pn = modulenumber;
		int m =taskmax;
		//添加零元素
		int k=2;
		int L=1;
		while(k<pn)
		{
			k<<=1;
			L++;
		}
		int AddZero=k*m;
		ArrayList<Integer>S=new ArrayList<Integer>();
		for(int i=0;i<AddZero;i++)
		{
			S.add(i);
		}
		double[][] matrix=new double[AddZero][AddZero];//通信代价矩阵
		//读入代价矩阵
		for(int i=0;i<AddZero;i++) {
			for(int j=0;j<AddZero;j++) {
				if(i<n&&j<n) {
				matrix[i][j]=communicatematrix[i][j];
				}
				else {
					matrix[i][j]=0;
				}
			}
		}
		//递归调用KL算法，进行K划分
		KLrecursion(matrix, S);
		//去除零元素
		for(int i=0;i<modules.size();i++)
		{
			for(int j=0;j<modules.get(i).size();j++)
			{
				int id=modules.get(i).get(j);
				if(id>tasknumber-1) {
					modules.get(i).remove(j);
				    j--;
				}
			}
			if(modules.get(i).size()==0)
			{
				modules.remove(i);
				i--;
			}
		}
		//合并簇来使得簇的数目达到要求
		while(modules.size()>modulenumber)
		{
			int min_index1=-1;
			int min_index2=-1;
			for(int i=0;i<modules.size();i++)
			{
				if(min_index1<0)
					min_index1=i;
				else if(min_index2<0)
					min_index2=i;
				else {
				int p=modules.get(i).size();
				int r=modules.get(min_index1).size();
				int s=modules.get(min_index2).size();
				if(p<r)
					min_index1=i;
				else if(p<s)
					min_index2=i;
				}
			}
			if(min_index1!=-1&&min_index2!=-1)
			{
				int low,high;
				if(min_index1<min_index2)
				{
					low=min_index1;
					high=min_index2;
				}
				else {
					{
						low=min_index2;
						high=min_index1;
					}
				}
			ArrayList<Integer>unitTwo=new ArrayList<Integer>(modules.get(low));
			unitTwo.addAll(modules.get(high));
			//合并超过大小，只合并部分
			if(unitTwo.size()>taskmax)
			{
				//转移low中的元素给high
				int e=taskmax-modules.get(high).size();
				for(int i=0;i<e;i++)
				{
					modules.get(high).add(modules.get(low).get(0));
					modules.get(low).remove(0);
				}
			}
			//没有超过大小，直接合并
			else {
				modules.remove(high);
				modules.remove(low);
				modules.add(unitTwo);
			}
		  }
		}
		int f=0;
		int n1=n;
		int fleng=modules.size();
		while(modules.size()<modulenumber)
		{
			while(modules.get(f).size()%2!=0)
			{
				if(modules.get(f).size()==1)
					f++;
				else{
				modules.get(f).add(n1++);
				if(n1++>=AddZero)
					n1=n;
				}
			}
			if(modules.get(f).size()==taskmax&&taskmax%2==0)
			{
	           ArrayList<Integer>tA=new ArrayList<Integer>(); 
	           ArrayList<Integer>tB=new ArrayList<Integer>();
	           if(taskmax==2)
	           {
	        	   tA.add(modules.get(f).get(0));
	        	   tB.add(modules.get(f).get(1));
	           }
	           else {
			     for(int i=0;i<modules.get(f).size()/2;i++)
			    	 tA.add(modules.get(f).get(i));
			     for(int i=modules.get(f).size()/2;i<modules.get(f).size();i++)
			    	 tB.add(modules.get(f).get(i));
			     KL(matrix,tA,tB);
	           }
			     modules.remove(f);
			     modules.add(tA);
			     modules.add(tB);
			 
			}
			else {
			KLrecursion(matrix, modules.get(f));
			modules.remove(f);
			}
		}
		//去零
		for(int i=0;i<modules.size();i++)
		{
			for(int j=0;j<modules.get(i).size();j++)
			{
				int id=modules.get(i).get(j);
				if(id>tasknumber-1) {
					modules.get(i).remove(j);
				    j--;
				}
			}
			if(modules.get(i).size()==0)
			{
				modules.remove(i);
				i--;
			}
		}
		   //计算通信开销
		   int lengt=modules.size();
		   for(int i=0;i<lengt;i++) {
			   ArrayList<Integer>first=new ArrayList<Integer>(modules.get(i));
			   for(int j=i+1;j<lengt;j++)
			   {
				   ArrayList<Integer>second=new ArrayList<Integer>(modules.get(j));
				   for(int p=0;p<first.size();p++) {
					   for(int q=0;q<second.size();q++)
					   {
						   commcost+=matrix[first.get(p)][second.get(q)];
					}
				}
			   }
		   }
		   /*for(int i=0;i<modules.size();i++)
			   for(int j=0;j<modules.get(i).size();j++)
			   {
				   modules.get(i).set(j,modules.get(i).get(j)+1);
			   }*/
	}
}
