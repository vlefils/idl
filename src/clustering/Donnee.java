package clustering;

/**
 * Une donnée est un n-uplets de nombres réels (type double), n étant le nombre de dimensions de la donnée. 
 * Pendant l'algorithme de clustering, les données vont être réparties dans des clusters.
 * On peut connaître le numéro du cluster (entre 0 et k-1) dont fait partie la donnée, il vaut -1 si la donnée n'est pas encore affectée à un cluster.
 *
 * @author  Anne-Cécile Caron
 */
public class Donnee{
  
    private int dim ;
    private double[] valeurs ; // on a dim = valeurs.length
    private int cluster = -1; // le numéro du cluster où on a rangé la donné
    private boolean change =false ; // permet de savoir si la donnée a changé de cluster
    private double distanceCentre ; // la distance entre la donnée et le centre du cluster
  
    /**
     * Constructeur
     * @param t un tableau de n doubles qui va servir à construire le n-uplet.
     */
    public Donnee(double[] t){
        // pour éviter des problèmes de partage, on recopie le tableau
        this.valeurs = t.clone() ;
        this.dim = t.length ;
    }
  
    /**
     * Une donnée est affectée à un cluster, sauf avant le démarrage de l'algorithme.
     * @return le numéro du cluster (entre 0 et k-1) dont fait partie la donnée, -1 si la donnée n'est pas encore affectée à un cluster.
     */
    public int numCluster() {
        return this.cluster;
    }
  
    // met à jour le numéro du cluster
    private void setCluster(int i) {
        if (i == this.cluster) change = false ;
        else { change=true ; this.cluster = i;}
    }
  
    /**
     * permet de savoir si après une étape de l'algorithme, la donnée a changé de cluster
     * @return true ssi la donnée a changé de cluster
     */
    public boolean aChangeDeCluster() { return this.change ;}
  
    /**
     * La donnée étant un n-uplet (n dimensions), renvoie la valeur de la ième dimension, i compris entre 0 et n-1.
     * @param i renvoie la valeur de la donnée pour la ième dimension
     */
    public double valeurDim(int i) {
        return this.valeurs[i] ;
    }
  
    /**
     * La donnée étant un n-uplet (n dimensions), renvoie ce nombre n de dimensions
     * @return le nombre de dimensions de la donnée
     */
    public int nbDimensions() { return this.dim ; }

    /**
     * renvoie la distance de la donnée par rapport au centre de son cluster
     * @return la distance entre la donnée et le centre de son cluster
     * @exception ClusterException si la donnée n'est pas encore affectée à un cluster
     */
    public double distanceCentre() throws ClusterException{
        if (this.cluster == -1) throw new ClusterException("pas de cluster affecté") ;
        else return this.distanceCentre ;
    }
  
    /**
     * evalue la distance entre la donnée et le centre du cluster lesClusters[newCluster].
     * si cette distance est inférieure à la distance entre la donnée et son actuel cluster, alors on déplace 
     * la donnée vers sont nouveau cluster.
     * @param centre donnée qui représente le centre du cluster (à part au début de l'algo où les clusters sont vides, centre vaut lesClusters[newCluster].moyenne())
     * @param newCluster l'indice du cluster à étudier
     * @param lesClusters le tableau qui contient tous les clusters
     * @param d la méthode de calcul de la distance entre 2 données
     */
    public void evalueChangementCluster(Donnee centre, int newCluster, Cluster[] lesClusters, Distance d) {
        double dist = d.valeur(this,centre) ; // distance par rapport au centre du cluster newCluster
        if ((this.cluster == -1)||(this.cluster >=0 && dist < this.distanceCentre)) {
            // on affecte la donnee au cluster newCluster
            if (this.cluster != -1) //la donnee etait deja dans un cluster : on l'enleve
                lesClusters[this.cluster].remove(this) ;
            this.setCluster(newCluster) ; //met à jour this.k et this.change
            try{
                lesClusters[this.cluster].add(this) ;
            }catch(ClusterException e){System.err.println("des donnees n'ont pas la même dimension ... ça ne devrait pas se produire");}
        }else this.change=false ;
        if (this.cluster == newCluster) this.distanceCentre = dist ;
    }
  
    /**
     * renvoie une chaîne de caractères donnant l'état de la donnée. 
     * @return une chaine de caractère avec la donnée (V1,...,Vn)
     */
    @Override
	public String toString(){
        String s="(";
        for (int i=0; i<this.dim-1; i++) s += this.valeurs[i]+",";
        s+=this.valeurs[this.dim-1]+")";
        return s;
    }
    
    /**
     * set le cluster associé à un centre
     * !!!!!!! A utiliser uniquement sur un centre !!!!!
     * @param cluster
     */
    public void setClusterCentre(int cluster){
    	this.cluster=cluster;
    }

    /**
     * renvoie une chaîne de caractères donnant l'état de la donnée. 
     * @return une chaine de caractère avec la donnée (V1,...,Vn), son numéro de cluster et sa distance au centre
     */
    public String toComplexString(){
        String s="("+this.toString()+") k="+this.cluster+ " dist="+this.distanceCentre;
        return s;
    }
  
}
