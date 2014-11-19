package iagl.opl.rebucket.eclipse;

import iagl.opl.rebucket.eclipse.model.Element;
import iagl.opl.rebucket.eclipse.model.Trace;

import java.util.List;

public class Distance {
	public static final double C = 0;
	public static final double O = 0;

	Double[][] matrix;
	public List<Element> elements1;
	private List<Element> elements2;

	public Distance(Trace trace1, Trace trace2) throws IllegalAccessException {
		if (trace1.elements == null || trace1.elements.isEmpty())
			throw new IllegalAccessException("No CT for "
					+ trace1.exceptionType + " " + trace1.message);
		if (trace2.elements == null || trace2.elements.isEmpty())
			throw new IllegalAccessException("No CT for "
					+ trace2.exceptionType + " " + trace2.message);
		this.elements1 = trace1.elements;
		this.elements2 = trace2.elements;
		matrix = new Double[trace1.elements.size()][trace2.elements.size()];
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
		if (elements1.get(i).equals(elements2.get(j))) {
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
		for (int i = 0; i < Math.min(elements1.size(), elements2.size()); i++) {
			sum += Math.exp((-1) * C * i);
		}
		return sum;
	}
}
