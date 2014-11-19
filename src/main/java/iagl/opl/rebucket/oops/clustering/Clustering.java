package iagl.opl.rebucket.oops.clustering;

import java.util.ArrayList;
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
public class Clustering {

	private List<Donnee> lesDonnees; // les données sur lesquelles on applique le
								// clustering

	private List<Cluster> lesClusters; // tableau de k Clusters

	private List<Donnee> lesCentres; // tableau des k centres des clusters

	private Distance distance; // permet de choisir la façon de calculer la
								// distance entre 2 données

	double threshold;

	private void init(List<Donnee> data, double threshold) {
		this.threshold = threshold;

		lesDonnees = data;

		lesClusters = new ArrayList<Cluster>();
	}

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
	public Clustering(List<Donnee> data, double threshold, Distance d){
		init(data, threshold);
		this.distance = d;
		// on initialise les centres des clusters
		this.lesCentres = null;
	}

	// on change les centres en créant une trace fictive représentant la fusion
	// des traces du cluster
	private void nouveauxCentres() {
		// TODO
	}

	// une étape : on calcule la distance de chaque donnée par rapport aux
	// centres des clusters
	// et on place chaque donnée dans le cluster dont le centre est le plus
	// proche
	private boolean etapeCentres() {
		boolean change = false;
		double prox = 0; // proximité avec le centre le plus proche (0 =
							// éloigné, 1
							// = identique)
		for (Donnee d : lesDonnees) {
			for (Donnee d2 : lesCentres) {
				double dist = distance.valeur(d, d2);
				if (dist > threshold && dist > prox) {
					prox = dist;
					d.setCluster(d2.numCluster());
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
	private void etapeVoisins(){
		double prox = 0; // proximité avec la donnée la plus proche (0 = éloigné, 1
						// = identique)
		
		for (Donnee d : lesDonnees) {
			Donnee voisin=null;
			prox=0;
			if(d.numCluster()>-1){
				continue;
			}
			for (Donnee d2 : lesDonnees) {
				if(d!=d2){
					double dist = distance.valeur(d,d2);
					if (dist < 0 || dist > 1) System.err.println(dist);
					if (dist > threshold && dist > prox) {
						prox = dist;
						voisin=d2;
					}
				}
			}
			if(voisin!=null && voisin.numCluster()==-1){
				Cluster c = new Cluster();
				lesClusters.add(c);
				d.setCluster(lesClusters.indexOf(c));
				voisin.setCluster(lesClusters.indexOf(c));
				
				c.add(d);
				c.add(voisin);
			}
			else if(voisin!=null){
				d.setCluster(voisin.numCluster());
				lesClusters.get(d.numCluster()).add(d);
			}
			else{
				Cluster c = new Cluster();
				lesClusters.add(c);
				d.setCluster(lesClusters.indexOf(c));
				
				c.add(d);
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
	public List<Cluster> algoCentres(boolean trace) {
		boolean change = true;
		if (trace) {
			System.out.println("données avant le clustering : ");
			this.affichage();
			System.out.println("Application du clustering : ");
		}
		while (change) {
			change = etapeCentres();
			if (change) {
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
