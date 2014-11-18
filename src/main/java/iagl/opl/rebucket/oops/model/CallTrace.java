package iagl.opl.rebucket.oops.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "calltrace")
public class CallTrace {

	@XmlElements(value = { @XmlElement(name = "trace") })
	public List<Trace> traces;

	// TODO : Enum
	@XmlElement(name = "delimiter")
	public String delimiter;
}
