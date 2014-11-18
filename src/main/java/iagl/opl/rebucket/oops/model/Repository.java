package iagl.opl.rebucket.oops.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Repository {

	@XmlElements(value = { @XmlElement(name = "oops", type = Oops.class) })
	public List<Oops> oopss;
}
