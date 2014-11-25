package iagl.opl.rebucket.clustering;

public interface Donnee {

	// return cluster num, -1 if no cluster
	public int numCluster();

	public void setCluster(int numCluster);
	
	public int getIndice();// retourne la position dans le dataset

	public void setIndice(int indice);


	public String toComplexString();

}
