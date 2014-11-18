package iagl.opl.rebucket.oops.Main;

import iagl.opl.rebucket.oops.model.Oops;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class Unmarshaller {

	public static Oops convert(String oopsXml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Oops.class);
		javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
		Oops oops = (Oops) unmarshaller.unmarshal(new StringReader(oopsXml));
		return oops;
	}

}
