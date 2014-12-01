package iagl.opl.rebucket.clustering;

public interface Distance {
	/*
	 * renvoie la distance entre les deux donnees ayant le meme nombre de
	 * dimensions.
	 */
	double valeur(Donnee d1, Donnee d2);
}