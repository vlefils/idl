package iagl.opl.rebucket.clustering ;

public class Cluster implements java.lang.Iterable<Donnee>{

    private java.util.List<Donnee> data = new java.util.LinkedList<Donnee>() ; // les donnees du cluster


    /**
     * comme son nom l'indique, permet d'obtenir un iterateur sur les donnees.
     * @return un iterateur sur les donnees du cluster.
     */
    public java.util.Iterator<Donnee> iterator(){
        return this.data.iterator() ;
    }

  
    /**
     * permet d'ajouter une donnee au cluster.
     * @param d la donnee a  ajouter
     * @exception ClusterException si on ajoute une donnee qui n'a pas le nombre de dimensions prevu a  la creation du cluster.
     */
    public void add(Donnee d){
        this.data.add(d) ;
    }

    /**
     * permet de recuperer la ieme donnee (ici, on voit le cluster comme une liste).
     * Attention, l'implementation du cluster est basee sur une liste chainee, donc cette methode n'est pas efficace.
     * Pour un parcours de toutes les donnees, utiliser un iterateur.
     * @param i la position ou se trouve la donnee
     * @return la donnee en position i, sachant que la premiere position est 0.
     */
    public Donnee get(int i){
        return this.data.get(i) ;
    }

    /**
     * renvoie la taille du cluster.
     * @return le nombre de donnees du cluster
     */
    public int size(){return this.data.size() ;}
  
    /**
     * permet d'enlever une donnee du cluster.
     * @param d la donnee a  retirer
     */
    public void remove(Donnee d){
        this.data.remove(d) ;
    }

}
