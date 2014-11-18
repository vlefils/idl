package iagl.opl.rebucket.oops.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class ModInfo {

	// TODO : Enum
	@XmlElement(name = "tainted")
	public String tainted;

	@XmlElements(value = { @XmlElement(name = "modname", type = String.class) })
	public List<String> modNames;
}
