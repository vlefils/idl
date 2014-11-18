package iagl.opl.rebucket.oops.Main;

import iagl.opl.rebucket.oops.model.CallTrace;
import iagl.opl.rebucket.oops.model.Oops;

public class Distance {

	public static final double C = 1;
	public static final double O = 1;

	Double[][] matrix;
	public CallTrace trace1;
	private CallTrace trace2;

	public Distance(Oops oops, Oops oops2) throws IllegalAccessException {
		if (oops.callTrace == null)
			throw new IllegalAccessException("No CT for " + oops.id);
		if (oops2.callTrace == null)
			throw new IllegalAccessException("No CT for " + oops2.id);
		this.trace1 = oops.callTrace;
		this.trace2 = oops2.callTrace;
		matrix = new Double[trace1.traces.size()][trace2.traces.size()];
	}

	public Double eval() {
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][0] = cost(i, 0);
		}
		for (int j = 1; j < matrix[0].length; j++) {
			matrix[0][j] = cost(0, j);
		}
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				matrix[i][j] = max(matrix[i - 1][j], matrix[i][j - 1],
						matrix[i - 1][j - 1] + cost(i, j));
			}
		}
		return (matrix[matrix.length - 1][matrix[0].length - 1]) / sum();
	}

	private Double cost(int i, int j) {
		if (trace1.traces.get(i).equals(trace2.traces.get(j))) {
			double coef = C * Math.min(i, j) + O * Math.abs(i - j);
			return Math.exp((-1) * coef);
		} else {
			return 0.0;
		}
	}

	public double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}

	private Double sum() {
		double sum = 0;
		for (int i = 0; i < Math
				.min(trace1.traces.size(), trace2.traces.size()); i++) {
			sum += Math.exp((-1) * C * i);
		}
		return sum;
	}
}
