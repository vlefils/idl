package iagl.opl.rebucket.eclipse.model;

import iagl.opl.rebucket.clustering.Donnee;

import java.util.List;

public class Trace extends Bug implements Donnee,Comparable<Trace>{
	public String exceptionType;
	public String message;
	public List<Element> elements;
	
	int cluster=-1; // numero du cluster
	int indice = -1;
	int groupId;
	public Integer bugId;	

	// public Integer commentIndex;
	public Trace causedBy;
	
	
	public int numCluster() {
		return cluster;
	}
	public void setCluster(int numCluster) {
		cluster=numCluster;
		
	}
	public String toComplexString() {
		return "trace "+bugId+" : "+message+"("+exceptionType+")";
	}

	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	@Override
	public int getIndice() {
		return indice;
	}
	@Override
	public void setIndice(int indice) {
		this.indice=indice;
		
	}
	
	public Integer getBugId() {
		return bugId;
	}
	
	
	@Override
	public int compareTo(Trace arg0) {
		if(this!=arg0)return 0;
		else return 1;
	}


}
