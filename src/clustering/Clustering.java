package clustering;

import java.util.Random;

/**
 * L'algorithme de clustering, méthode des k-means. On applique l'algorithme sur
 * des données placées initialement dans 1 cluster, et que l'on va répartir dans
 * k clusters. Il est possible de partir de k centres donnés, ou bien on va les
 * choisir aléatoirement. Il est aussi possible de choisir la distance, par
 * défaut c'est la distance euclidienne sans normalisation.
 * 
 * @author Anne-Cécile Caron
 */
public class Clustering {
	private int k; // nb de clusters
	private Cluster lesDonnees; // les données sur lesquelles on applique le
								// clustering
	private Cluster[] lesClusters; // tableau de k Clusters
	private Donnee[] lesCentres; // tableau des k centres des clusters
	private Random hasard = new Random(); // va servir à initialiser les centres
	private Distance distance; // permet de choisir la façon de calculer la
								// distance entre 2 données

	// initialisation de l'ensemble des données, et des k clusters (ils sont
	// vides au départ).
	private void init(int k, Cluster data) {
		this.k = k;
		this.lesDonnees = data;
		// on crée un tableau de taille k pour les clusters
		this.lesClusters = new Cluster[k];
		for (int i = 0; i < k; i++) { // on crée k Clusters
			this.lesClusters[i] = new Cluster(data.nbDimensions());
		}
	}


	/**
	 * Constructeur
	 * 
	 * @param k
	 *            le nombre de clusters
	 * @param data
	 *            toutes les données que l'on va répartir dans k clusters
	 * @param d
	 *            la distance utilisée, si on veut autre chose que la distance
	 *            euclidienne non normalisée
	 * @param random	
	 * 			  choix des centres (completement random (true) ou parmis les points existants (false))
	 */
	public Clustering(int k, Cluster data, Distance d,boolean random) {
		this.init(k, data);
		this.distance = d;
		// on crée un tableau de taille k pour les centres des clusters
		this.lesCentres = new Donnee[k];
		this.lesCentres = new Donnee[k];
		if (random)	this.choisirCentresRandom(data.min(), data.max());
		else this.choisirCentresSelect();
	}

	/**
	 * Constructeur
	 * 
	 * @param k
	 *            le nombre de clusters
	 * @param data
	 *            toutes les données que l'on va répartir dans k clusters
	 * @param centres
	 *            un tableau avec les k centres initiaux des k clusters.
	 */
	public Clustering(int k, Cluster data, Donnee[] centres,boolean random)
			throws ClusterException {
		this.init(k, data);
		// par defaut, la distance est euclidienne
		this.distance = new DistanceEuclidienne();
		// on initialise les centres des clusters
		this.lesCentres = centres;
	}

	/**
	 * Constructeur
	 * 
	 * @param k
	 *            le nombre de clusters
	 * @param data
	 *            toutes les données que l'on va répartir dans k clusters
	 * @param centres
	 *            un tableau avec les k centres initiaux des k clusters.
	 * @param d
	 *            la distance utilisée, si on veut autre chose que la distance
	 *            euclidienne non normalisée
	 */
	public Clustering(int k, Cluster data, Donnee[] centres, Distance d,boolean random)
			throws ClusterException {
		this.init(k, data);
		this.distance = d;
		// on initialise les centres des clusters
		this.lesCentres = centres;
	}

	/**
	 * Constructeur
	 * 
	 * @param k
	 *            le nombre de clusters
	 * @param data
	 *            toutes les données que l'on va répartir dans k clusters
	 * @param centres
	 *            un tableau avec les k centres initiaux des k clusters.
	 * @param random	
	 * 			  choix des centres (completement random (true) ou parmis les points existants (false))
	 */
	public Clustering(int k, Cluster data,boolean random)
			throws ClusterException {
		this.init(k, data);
		// par defaut, la distance est euclidienne
		this.distance = new DistanceEuclidienne();
		// on initialise les centres des clusters
		this.lesCentres = new Donnee[k];
		if (random)	this.choisirCentresRandom(data.min(), data.max());
		else this.choisirCentresSelect();
	}

	// on choisit (pseudo-)aléatoirement k centres créés pour commencer l'algo.
	private void choisirCentresRandom(Donnee min, Donnee max) {
		// la dimension est choisie en fonction du premier élément, deal with
		// it...
		int dim = lesDonnees.get(0).nbDimensions();
		for (int i = 0; i < k; ++i) {
			double[] coord = new double[dim];
			for (int j = 0; j < dim; ++j) {
				double factor = hasard.nextDouble();
				coord[j] = ( factor * (max.valeurDim(j)-min.valeurDim(j)) ) + (min.valeurDim(j));
				//System.out.println("centre[" + i + "," + j + "] :" + coord[j]);
			}
			Donnee centre = new Donnee(coord);
			centre.setClusterCentre(i);
			lesCentres[i] = centre;
		}
	}

	// on choisit (pseudo-)aléatoirement k centres parmis les données pour
	// commencer l'algo.
	private void choisirCentresSelect() {
		// la dimension est choisie en fonction du premier élément, deal with
		// it...
		int dim = lesDonnees.get(0).nbDimensions();
		for (int i = 0; i < k; ++i) {
			double[] coord = new double[dim];
			// choisis une donnée au hasard comme centre
			int rand = (int) (hasard.nextDouble() * lesDonnees.size());
			for (int j = 0; j < dim; j++) {
				coord[j] = lesDonnees.get(rand).valeurDim(j);
			}
			Donnee centre = new Donnee(coord);
			centre.setClusterCentre(i);
			lesCentres[i] = centre;
		}
	}

	// on change les centres en prenant les barycentres des clusters.
	// à faire après chaque étape
	private void nouveauxCentres() {
		lesCentres = new Donnee[k];
		int i = 0;
		for (Cluster c : lesClusters) {
			Donnee d = c.moyenne();
			//System.out.println("nouveau centre : " + d);
			d.setClusterCentre(i);
			lesCentres[i++] = d;
		}
	}

	// une étape : on calcule la distance de chaque donnée par rapport aux
	// centres des clusters
	// et on place chaque donnée dans le cluster dont le centre est le plus
	// proche
	private boolean etape() {
		boolean change = false;
		for (Donnee d : lesDonnees) {
			for (Donnee centre : lesCentres) {
				d.evalueChangementCluster(centre, centre.numCluster(),
						lesClusters, distance);
				if (d.aChangeDeCluster())
					change = true;
			}
		}
		return change; // renvoie true ssi au moins une donnee a change de
						// cluster
	}

	/**
	 * renvoie la compacité des clusters, c'est à dire la somme des compacités
	 * de tous les clusters (WC vient de "within clusters"). La compacité d'un
	 * cluster est la somme des distances des données du cluster par rapport à
	 * son centre.
	 * 
	 * @return la compacité des k clusters
	 * @throws ClusterException
	 */
	public double wc() {
		double som = 0.0;
		for (Cluster c : lesClusters) {
			for (Donnee d : c) {
				try {
					som += d.distanceCentre();
				} catch (ClusterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return som;
	}

	/**
	 * renvoie la séparation, c'est à dire la somme des distances entre les
	 * centres des clusters (BC vient de "between clusters").
	 * 
	 * @return la séparation des clusters
	 */
	public double bc() {
		double som = 0.0;
		for (Donnee d1 : lesCentres) {
			for (Donnee d2 : lesCentres) {
				if (d1 != d2) {
					som += distance.valeur(d1, d2);
				}
			}
		}
		return som;
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
	public Cluster[] algo(boolean trace) {
		boolean change = true;
		if (trace) {
			System.out.println("données avant le clustering : ");
			this.affichage();
			System.out.println("Application du clustering : ");
		}
		while (change) {
			change = etape();
			if (change) {
				nouveauxCentres();
			}
		}
		return this.lesClusters;
	}

	// affiche toutes les données avec leur numéro de cluster et la distance par
	// rapport au centre
	// donne aussi un résumé des mesures de qualité : WC et BC
	private void affichage() {
		System.out.println("--------------------");
		for (Donnee d : this.lesDonnees) {
			System.out.println(d.toComplexString());
		}
		double wc = this.wc();
		double bc = this.bc();
		double rapport;
		try {
			rapport = bc / wc;
		} catch (java.lang.ArithmeticException e) {
			rapport = 0.0;
		}
		System.out.println("WC = " + wc + " BC = " + bc + " Rapport BC/WC = "
				+ rapport);
		System.out.println("--------------------");
	}

}
