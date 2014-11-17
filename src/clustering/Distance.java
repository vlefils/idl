package clustering;

/**
 * Distance entre deux données.
 *
 * @author  Anne-Cécile Caron
 */
public interface Distance {
    /**
     * renvoie la distance entre les deux données ayant le même nombre de dimensions.
     * @param d1 première donnée
     * @param d2 deuxième donnée 
     * @return la distance entre d1 et d2
     */
       double valeur(Donnee d1, Donnee d2) ;
}

// A FAIRE : ecrire plusieurs classes implémentant cette interface (distance euclidienne, ...)