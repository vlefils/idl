package iagl.opl.rebucket.oops.clustering ;

/**
 *  Cluster, dans un algorithme de clustering de type K-mean. 
 * Un cluster contient des données, et un certain nombre de statistiques sur ces données.
 * Les données sont des n-uplets de nombres réels (double), n estle nombre de dimensions de la donnée
 *
 * @author     Anne-Cécile Caron
 */
public class Cluster implements java.lang.Iterable<Donnee>{

    private java.util.List<Donnee> data = new java.util.LinkedList<Donnee>() ; // les données du cluster


    /**
     * comme son nom l'indique, permet d'obtenir un itérateur sur les données.
     * @return un iterateur sur les données du cluster.
     */
    public java.util.Iterator<Donnee> iterator(){
        return this.data.iterator() ;
    }

  
    /**
     * permet d'ajouter une donnée au cluster.
     * @param d la donnée à ajouter
     * @exception ClusterException si on ajoute une donnée qui n'a pas le nombre de dimensions prévu à la création du cluster.
     */
    public void add(Donnee d){
        this.data.add(d) ;
    }

    /**
     * permet de récupérer la ième donnée (ici, on voit le cluster comme une liste).
     * Attention, l'implémentation du cluster est basée sur une liste chaînée, donc cette méthode n'est pas efficace.
     * Pour un parcours de toutes les données, utiliser un iterateur.
     * @param i la position où se trouve la donnée
     * @return la donnée en position i, sachant que la première position est 0.
     */
    public Donnee get(int i){
        return this.data.get(i) ;
    }

    /**
     * renvoie la taille du cluster.
     * @return le nombre de données du cluster
     */
    public int size(){return this.data.size() ;}
  
    /**
     * permet d'enlever une donnée du cluster.
     * @param d la donnee à retirer
     */
    public void remove(Donnee d){
        this.data.remove(d) ;
    }

}
