package iagl.opl.rebucket.oops;

import iagl.opl.rebucket.oops.Main.Distance;
import iagl.opl.rebucket.oops.model.Oops;
import iagl.opl.rebucket.oops.session.QueryFile;

import java.util.List;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class QueryFileTest {

	@Test
	public void getRepo() throws Exception {
		Assertions.assertThat(QueryFile.INSTANCE).isNotNull();
		Assertions.assertThat(QueryFile.INSTANCE.getRepository()).isNotNull();
		Assertions.assertThat(QueryFile.INSTANCE.getRepository().oopss)
				.isNotEmpty();
		System.out.println(QueryFile.INSTANCE.getRepository().oopss.size());

		List<Oops> oopss = QueryFile.INSTANCE.getRepository().oopss;
		double max = 0.0;
		String id = "";
		for (int i = 2; i < oopss.size(); i++) {
			try {
				double val = new Distance(oopss.get(1), oopss.get(i)).eval();
				if (max < val) {
					id = oopss.get(i).id;
					max = val;
				}
			} catch (Exception ex) {
				System.out.println("Err : " + ex.getMessage());
			}
		}
		System.out.println(id + " - " + max);
	}
}
