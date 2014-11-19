package iagl.opl.rebucket.eclipse;

import iagl.opl.rebucket.eclipse.model.ReportBug;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class Main {

	public static void main(String[] args) throws JSONException, IOException,
			IllegalAccessException {

		URL urlDirJson = Main.class.getClassLoader().getResource("jsonfiles");

		File[] jsons = new File(urlDirJson.getFile())
				.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".json");
					}
				});

		List<File> jsonList = Arrays.asList(jsons);
		Collections.sort(jsonList, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				String name1 = o1.getName();
				String name2 = o2.getName();
				Pattern pattern = Pattern.compile("^([0-9]+)\\.json");
				Matcher m1 = pattern.matcher(name1);
				Matcher m2 = pattern.matcher(name2);
				if (m1.matches() && m2.matches()) {
					int id1 = Integer.parseInt(m1.group(1));
					int id2 = Integer.parseInt(m2.group(1));
					return new Integer(id1).compareTo(new Integer(id2));
				} else {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			}
		});

		List<String> jsonMin0 = new LinkedList<String>();
		ObjectMapper mapper = new ObjectMapper();
		long start = System.currentTimeMillis();
		for (File json : jsonList) {
			ReportBug rb;
			try {
				rb = mapper.readValue(json, ReportBug.class);
			} catch (UnrecognizedPropertyException upe) {
				System.err.println(json.getName() + " : "
						+ upe.getPathReference());
				continue;
			}
			double min = 1.0;
			for (int i = 0; i < rb.traces.size(); i++) {
				for (int j = 0; j < rb.traces.size(); j++) {
					if (i != j) {
						Double val = new Distance(rb.traces.get(i),
								rb.traces.get(j)).eval();
						min = Math.min(min, val);

					}
				}
			}
			if (min == 0.0) {
				// System.out.println(json.getName() + " ====> " + min);
				jsonMin0.add(json.getName());
			}
		}
		System.out.println(System.currentTimeMillis() - start + " ms");
		System.out
				.println(String
						.format("Il y a %d fichiers où deux CT sont différentes soit %3.2f%% des fichiers",
								jsonMin0.size(), (new Double(jsonMin0.size()))
										/ (new Double(jsonList.size())) * 100.0));

	}
}
