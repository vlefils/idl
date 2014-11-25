package iagl.opl.rebucket.eclipse.model;

public class Element {
	public String method;
	public String source;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Element) {
			Element other = (Element) obj;
			return this.method.equals(other.method);
		}
		else return false;
	}

}
