package iagl.opl.rebucket.oops.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "register")
public class Register {

	@XmlElement(name = "regname")
	public String name;

	@XmlElement(name = "value")
	public String value;
}
