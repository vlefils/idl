package iagl.opl.rebucket.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * L'algorithme de clustering, méthode des k-means. On applique l'algorithme sur
 * des données placées initialement dans 1 cluster, et que l'on va répartir dans
 * k clusters. Il est possible de partir de k centres donnés, ou bien on va les
 * choisir aléatoirement. Il est aussi possible de choisir la distance, par
 * défaut c'est la distance euclidienne sans normalisation.
 * 
 * @author Anne-Cécile Caron
 */
public class ClusteringSimple {

	private Donnee[] lesDonnees; // les données sur lesquelles on applique le
									// clustering

	private List<Cluster> lesClusters;

	private List<Donnee> lesCentres; // les contenant les indices des centres

	private Distance distance; // permet de choisir la façon de calculer la
								// distance entre 2 données

	//double[][] distances;

	double threshold;

	/**
	 * Constructeur
	 * 
	 * @param data
	 *            toutes les données que l'on va répartir dans k clusters
	 * @param centres
	 *            liste des centres des clusters
	 * @param d
	 *            la distance utilisée, si on veut autre chose que la distance
	 *            euclidienne non normalisée
	 */
	public ClusteringSimple(List<Donnee> data, double threshold, Distance d) {

		this.threshold = threshold;

		lesDonnees = new Donnee[data.size()];

		int i = 0;
		for (Donnee d1 : data) {
			d1.setIndice(i);
			lesDonnees[i] = d1;
			i++;
		}

		//distances = new double[data.size()][data.size()];

		lesClusters = new ArrayList<Cluster>();

		lesCentres = new ArrayList<Donnee>();

		this.distance = d;

		init();

	}

	private void init() {

		/*for (int i = 0; i < lesDonnees.length; ++i) {
			for (int j = 0; j < lesDonnees.length; ++j) {
				if (j > i) {
					distances[i][j] = distance.valeur(lesDonnees[i], lesDonnees[j]);
				} else if (i == j) {
					distances[i][j] = -1;
				} else {
					distances[i][j] = distances[j][i];
				}
			}
		}*/
	}

	// on change les centres en créant une trace fictive représentant la fusion
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
							dist += distance.valeur(d1, d2);
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

	// une étape : on calcule la distance de chaque donnée par rapport aux
	// centres des clusters
	// et on place chaque donnée dans le cluster dont le centre est le plus
	// proche
	private boolean etapeCentres() {
		boolean change = false;
		for (int i = 0; i < lesDonnees.length; ++i) {
			double prox = 0; // proximité avec le centre le plus proche (0 =
								// éloigné, 1 = identique)
			int cluster = lesDonnees[i].numCluster();// cluster actuel
			for (Donnee centre : lesCentres) {
				double dist = distance.valeur(lesDonnees[i],centre);// distance.valeur(lesDonnees[i],centre);
				if (dist > threshold && dist > prox) {
					prox = dist;
					lesDonnees[i].setCluster(lesDonnees[centre.getIndice()].numCluster());
					if (centre.numCluster()!=cluster)change = true;
				}
			}
		}
		return change; // renvoie true ssi au moins une donnee a change de
						// cluster
	}

	// une étape : on calcule la distance de chaque donnée par rapport aux
	// centres des clusters
	// et on place chaque donnée dans le cluster dont le centre est le plus
	// proche
	private void etapeVoisins() {
		double prox = 0; // proximité avec la donnée la plus proche (0 =
							// éloigné, 1
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
					double dist = distance.valeur(lesDonnees[i],lesDonnees[j]);
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
	 * l'algorithme de clustering sur les données que l'on a passées au
	 * constructeur. On applique l'algo des k-means
	 * 
	 * @param trace
	 *            boolean qui permet de demander (ou pas) d'avoir une trace des
	 *            étapes de l'algorithme. A eviter s'il y a beaucoup de données
	 *            !
	 * @return le tableau des k Clusters résultat de l'application de
	 *         l'algorithme.
	 * @throws ClusterException
	 */
	public List<Cluster> algoVoisins(boolean trace) {
		if (trace) {
			System.out.println("données avant le clustering : ");
			this.affichage();
			System.out.println("Application du clustering : ");
		}
		etapeVoisins();

		return lesClusters;
	}

	/**
	 * l'algorithme de clustering sur les données que l'on a passées au
	 * constructeur. On applique l'algo des k-means
	 * 
	 * @param trace
	 *            boolean qui permet de demander (ou pas) d'avoir une trace des
	 *            étapes de l'algorithme. A eviter s'il y a beaucoup de données
	 *            !
	 * @return le tableau des k Clusters résultat de l'application de
	 *         l'algorithme.
	 * @throws ClusterException
	 */
	public List<Cluster> algoCentres(boolean trace,int limit) {
		boolean change = true;
		if (trace) {
			System.out.println("données avant le clustering : ");
			this.affichage();
			System.out.println("Application du clustering : ");
		}

		// initialisation
		etapeVoisins();
		nouveauxCentres();

		while (change && limit-->0) {
			change = etapeCentres();
			if (change) {
				System.out.println("nouveaux centres");
				nouveauxCentres();
			}
		}
		return lesClusters;
	}

	// affiche toutes les données avec leur numéro de cluster et la distance par
	// rapport au centre
	// donne aussi un résumé des mesures de qualité : WC et BC
	private void affichage() {
		System.out.println("--------------------");
		for (Donnee d : this.lesDonnees) {
			System.out.println(d.toComplexString());
		}
		System.out.println("--------------------");
	}

	public List<Cluster> getLesClusters() {
		return lesClusters;
	}

	public void setLesClusters(List<Cluster> lesClusters) {
		this.lesClusters = lesClusters;
	}

}
