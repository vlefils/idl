package iagl.opl.rebucket.clustering;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Utilitaire de clustering avec precalcul des distances (+ rapide mais + gourmand en memoire)
 *
 */
public class ClusteringMemoire {

	private Donnee[] lesDonnees;
	private List<Cluster> lesClusters;
	private List<Donnee> lesCentres;
	private Distance distance;
	double[][] distances;
	double threshold;

	/**
	 * Constructeur
	 * 
	 * @param data
	 *            toutes les donnees que l'on va repartir dans k clusters
	 * @param threshold
	 *            Seuil minimum pour la distance necessaire pour rapprocher deux traces
	 * @param d
	 *            la distance utilisee
	 */
	public ClusteringMemoire(List<Donnee> data, double threshold, Distance d) {

		this.threshold = threshold;

		lesDonnees = new Donnee[data.size()];

		int i = 0;
		for (Donnee d1 : data) {
			d1.setIndice(i);
			lesDonnees[i] = d1;
			i++;
		}

		distances = new double[data.size()][data.size()];
		lesClusters = new ArrayList<Cluster>();
		lesCentres = new ArrayList<Donnee>();
		this.distance = d;

		init();

	}

	private void init() {

		for (int i = 0; i < lesDonnees.length; ++i) {
			for (int j = 0; j < lesDonnees.length; ++j) {
				if (j > i) {
					distances[i][j] = distance.valeur(lesDonnees[i],
							lesDonnees[j]);
				} else if (i == j) {
					distances[i][j] = -1;
				} else {
					distances[i][j] = distances[j][i];
				}
			}
		}
	}

	// on change les centres en creant une trace fictive representant la
	// fusion
	// des traces du cluster
	private void nouveauxCentres() {
		lesCentres.clear();
		for (Cluster c : lesClusters) {
			if (c.size() > 1) {
				int min = -1; // minimum sum of distance with all other elements
				Donnee dmin = c.get(0); // element which have the min sum
				for (Donnee d1 : c) {
					int dist = 0;
					for (Donnee d2 : c) {
						if (d1 != d2)
							dist += distances[d1.getIndice()][d2.getIndice()];
					}
					if (min != -1 && dist < min) {
						min = dist;
						dmin = d1;
					}
				}
				lesCentres.add(dmin);
			} else {
				lesCentres.add(c.get(0));
			}
		}
	}

	// une etape : on calcule la distance de chaque donnee par rapport aux
	// centres des clusters
	// et on place chaque donnee dans le cluster dont le centre est le plus
	// proche
	private boolean etapeCentres() {
		boolean change = false;
		for (int i = 0; i < lesDonnees.length; ++i) {
			double prox = 0; // proximite avec le centre le plus proche (0 =
								// eloigne, 1 = identique)
			int oldcluster = lesDonnees[i].numCluster();// cluster actuel
			for (Donnee centre : lesCentres) {
				if (centre.numCluster() == oldcluster)
					prox = distances[i][centre.getIndice()];
			}
			for (Donnee centre : lesCentres) {
				int j = centre.getIndice();
				double dist = distances[i][j];
				if (dist > threshold && dist > prox) {
					prox = dist;
					int newcluster = centre.numCluster();
					lesClusters.get(oldcluster).remove(lesDonnees[i]);
					if (lesClusters.get(oldcluster).size() == 0) {
						lesClusters.remove(oldcluster);
					}
					lesDonnees[i].setCluster(newcluster);
					lesClusters.get(newcluster).add(lesDonnees[i]);

					change = true;
				}
			}
		}
		return change; // renvoie true ssi au moins une donnee a change de
						// cluster
	}

	// une etape : on calcule la distance de chaque donnee par rapport aux
	// centres des clusters
	// et on place chaque donnee dans le cluster dont le centre est le plus
	// proche
	private void etapeVoisins() {
		double prox = 0; // proximite avec la donnee la plus proche (0 =
							// eloigne, 1
							// = identique)

		for (int i = 0; i < lesDonnees.length; ++i) {
			Donnee d1 = lesDonnees[i];
			Donnee voisin = null;
			prox = 0;
			if (d1.numCluster() > -1) {
				continue;
			}
			for (int j = 0; j < lesDonnees.length; ++j) {
				Donnee d2 = lesDonnees[j];
				if (i != j) {
					double dist = distances[i][j];
					if (dist < 0 || dist > 1)
						System.err.println(dist);
					if (dist > threshold && dist > prox) {
						prox = dist;
						voisin = d2;
					}
				}
			}
			if (voisin != null && voisin.numCluster() == -1) {
				Cluster c = new Cluster();
				lesClusters.add(c);
				d1.setCluster(lesClusters.indexOf(c));
				voisin.setCluster(lesClusters.indexOf(c));

				c.add(d1);
				c.add(voisin);
			} else if (voisin != null) {
				d1.setCluster(voisin.numCluster());
				lesClusters.get(d1.numCluster()).add(d1);
			} else {
				Cluster c = new Cluster();
				lesClusters.add(c);
				d1.setCluster(lesClusters.indexOf(c));

				c.add(d1);
			}
		}
	}

	/**
	 * l'algorithme de clustering sur les donnees que l'on a passees au
	 * constructeur. On applique l'algo des k-means
	 * 
	 * @param trace
	 *            boolean qui permet de demander (ou pas) d'avoir une trace des
	 *            etapes de l'algorithme. A eviter s'il y a beaucoup de
	 *            donnees !
	 * @return le tableau des Clusters resultat de l'application de
	 *         l'algorithme.
	 */
	public List<Cluster> algoVoisins(boolean trace) {
		if (trace) {
			System.out.println("donnees avant le clustering : ");
			this.affichage();
			System.out.println("Application du clustering : ");
		}
		etapeVoisins();

		return lesClusters;
	}

	/**
	 * l'algorithme de clustering sur les donnees que l'on a passees au
	 * constructeur. On applique l'algo des k-means
	 * 
	 * @param trace
	 *            boolean qui permet de demander (ou pas) d'avoir une trace des
	 *            etapes de l'algorithme. A eviter s'il y a beaucoup de
	 *            donnees !
	 * @param limit
	 * 			  nombre d'itération maximum si les clusters ne se stabilisent pas
	 * @return le tableau des k Clusters resultat de l'application de
	 *         l'algorithme.
	 * @throws ClusterException
	 */
	public List<Cluster> algoCentres(boolean trace, int limit) {
		boolean change = true;
		if (trace) {
			System.out.println("donnees avant le clustering : ");
			this.affichage();
			System.out.println("Application du clustering : ");
		}

		// initialisation
		etapeVoisins();
		nouveauxCentres();

		while (change && limit-- > 0) {
			change = etapeCentres();
			if (change) {
				nouveauxCentres();
			}
		}
		return lesClusters;
	}

	// affiche toutes les donnees avec leur numero de cluster et la distance
	// par
	// rapport au centre
	// donne aussi un resume des mesures de qualite : WC et BC
	private void affichage() {
		System.out.println("--------------------");
		for (Donnee d : this.lesDonnees) {
			System.out.println(d.toComplexString());
		}
		System.out.println("--------------------");
	}


}
