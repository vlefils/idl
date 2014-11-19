package iagl.opl.rebucket.eclipse.model;

import iagl.opl.rebucket.clustering.Donnee;

import java.util.List;

public class Trace extends Bug implements Donnee{
	public String exceptionType;
	public String message;
	public List<Element> elements;
	
	int cluster=-1;
	
	// public Integer number;
	// public Integer commentIndex;
	// public Trace causedBy;
	
	
	public int numCluster() {
		return cluster;
	}
	public void setCluster(int numCluster) {
		cluster=numCluster;
		
	}
	public String toComplexString() {
		return "trace "+bugId+" : "+message+"("+exceptionType+")";
	}

}
