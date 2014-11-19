package iagl.opl.rebucket.eclipse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Bug {
	public Integer bugId;
	public Integer duplicateId;
	// public String date;
	// public String product;
	// public String component;
	// public String severity;
}
