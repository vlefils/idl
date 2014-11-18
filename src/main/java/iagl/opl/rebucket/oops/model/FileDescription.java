package iagl.opl.rebucket.oops.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "file")
public class FileDescription {

	@XmlElement(name = "filename")
	public String filename;

	@XmlElement(name = "linenum", type = Integer.class)
	public Integer linenum;
}
