package iagl.opl.rebucket.oops.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Oops {

	@XmlAttribute(name = "id")
	public String id;

	// TODO : date format
	@XmlElement(name = "date")
	public String date;

	@XmlElement(name = "hash")
	public String hash;

	// TODO : Enum
	@XmlElement(name = "type")
	public String type;

	@XmlElement(name = "cause")
	public String cause;

	@XmlElement(name = "ver")
	public String version;

	@XmlElement(name = "full_version")
	public String fullVersion;

	// TODO : Enum
	@XmlElement(name = "arch")
	public String architecture;

	// TODO : <smp> ??
	@XmlElement(name = "trace_id")
	public String traceId;

	@XmlElement(name = "errcode")
	public String errCode;

	@XmlElement(name = "fault_addr")
	public String faultAdress;

	// TODO : <instr_ptr> ??

	@XmlElement(name = "pid", type = Integer.class)
	public Integer pid;
	// TODO : <comm> ??

	@XmlElement(name = "diecnt", type = Integer.class)
	public Integer diecnt;

	@XmlElement(name = "calltrace", type = CallTrace.class)
	public CallTrace callTrace;

	@XmlElement(name = "modinfo", type = ModInfo.class)
	public ModInfo modInfo;

	@XmlElements(value = { @XmlElement(name = "register") })
	@XmlElementWrapper(name = "register_set")
	public List<Register> registers;

	@XmlElement(name = "distribution")
	public String distribution;

	@XmlElement(name = "file", type = FileDescription.class)
	public FileDescription file;

	// TODO : <duplicates> 1 value

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else if (hash != null) {
			return hash.hashCode();
		} else {
			return super.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Oops) {
			Oops other = (Oops) obj;
			if (id != null) {
				return id.equals(other.id);
			} else if (hash != null) {
				return hash.equals(other.hash);
			}
		}
		return super.equals(obj);
	}
}
