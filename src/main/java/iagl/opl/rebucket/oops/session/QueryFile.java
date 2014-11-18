package iagl.opl.rebucket.oops.session;

import iagl.opl.rebucket.oops.model.Repository;

import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import org.apache.commons.io.IOUtils;

public class QueryFile {

	public static final QueryFile INSTANCE = new QueryFile();

	private static final URL PATH = QueryFile.class.getClassLoader()
			.getResource("xmlfiles/rep_apr01_apr03_1268.xml");
	private Repository repository;

	public Repository getRepository() throws Exception {
		if (repository == null) {
			JAXBContext context = JAXBContext.newInstance(Repository.class);
			javax.xml.bind.Unmarshaller unmarshaller = context
					.createUnmarshaller();
			repository = (Repository) unmarshaller.unmarshal(new StringReader(
					IOUtils.toString(PATH)));
		}
		return repository;
	}
}
