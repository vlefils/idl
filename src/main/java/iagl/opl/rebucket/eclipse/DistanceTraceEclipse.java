package iagl.opl.rebucket.eclipse;

import iagl.opl.rebucket.clustering.Distance;
import iagl.opl.rebucket.clustering.Donnee;
import iagl.opl.rebucket.eclipse.model.Trace;

public class DistanceTraceEclipse implements Distance {
	public static final double C = 0.5;
	public static final double O = 5;

	// 85% de la boucle init
	public double valeur(Donnee d1, Donnee d2) {

		Trace trace1 = (Trace) d1;
		Trace trace2 = (Trace) d2;

		Double[][] matrix = new Double[trace1.elements.size()][trace2.elements
				.size()];

		for (int i = 0; i < matrix.length; i++) {
			matrix[i][0] = cost(trace1, trace2, i, 0);
		}
		for (int j = 1; j < matrix[0].length; j++) {
			matrix[0][j] = cost(trace1, trace2, 0, j);
		}
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				matrix[i][j] = max(matrix[i - 1][j], matrix[i][j - 1],
						matrix[i - 1][j - 1] + cost(trace1, trace2, i, j));
			}
		}

		double sum = 0;
		for (int i = 0; i <= Math.min(trace1.elements.size(),
				trace2.elements.size()); i++) {
			sum += Math.exp((-1) * C * i);
		}
		return (matrix[matrix.length - 1][matrix[0].length - 1]) / sum;
	}

	// 35% temps calcul de la boucle init
	private Double cost(Trace trace1, Trace trace2, int i, int j) {
		if (trace1.elements.get(i).equals(trace2.elements.get(j))) {
			double coef = C * Math.min(i, j) + O * Math.abs(i - j);
			return Math.exp((-1) * coef);
		} else {
			return 0.0;
		}
	}

	// 8% temps calcul de la boucle init
	public double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}

}
