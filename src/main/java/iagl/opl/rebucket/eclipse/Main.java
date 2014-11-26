package iagl.opl.rebucket.eclipse;

import iagl.opl.rebucket.clustering.Cluster;
import iagl.opl.rebucket.clustering.ClusteringSimple;
import iagl.opl.rebucket.clustering.Donnee;
import iagl.opl.rebucket.eclipse.model.ReportBug;
import iagl.opl.rebucket.eclipse.model.Trace;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static boolean VERBOSE = false;
	public static int nbFiles = -1;

	public static void main(String[] args) throws JSONException, IOException,
			IllegalAccessException {

		System.out.println("-----------------------\ndebut de l'init\n");
		long timeInit = System.currentTimeMillis();

		URL urlDirJson = Main.class.getClassLoader().getResource("jsonfiles");

		File[] jsons = new File(urlDirJson.getFile())
				.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.endsWith(".json");
					}
				});

		List<Donnee> traces = new LinkedList<Donnee>();

		ObjectMapper mapper = new ObjectMapper();
		for (File f : jsons) {
			if (nbFiles-- == 0)
				break;
			ReportBug report = mapper.readValue(f, ReportBug.class);
			Trace t = report.traces.get(report.traces.size() - 1);
			int id = t.getGroupId();
			int idB = t.bugId;
			while (t.causedBy != null) {
				t = t.causedBy;
			}
			t.setGroupId(id);
			t.bugId = idB;
			t.setGroupId(report.groupId);
			traces.add(t);
			if (report.traces.isEmpty())
				System.err.println(report.bugId);
		}
		System.out.println("traces ajoutees : " + traces.size());

		ClusteringSimple clustering = new ClusteringSimple(traces, 0.95,
				new DistanceTraceEclipse());

		long timeEnd = System.currentTimeMillis();
		System.out.println("\nfin de l'init (duree : " + (timeEnd - timeInit)
				+ ")\n-----------------------");

		System.out.println("-----------------------\ndebut de l'algo\n");
		timeInit = System.currentTimeMillis();
		// clustering.algoCentres(false,100);
		clustering.algoVoisins(false);
		timeEnd = System.currentTimeMillis();

		System.out.println("\nfin de l'algo (duree : " + (timeEnd - timeInit)
				+ ")\n-----------------------");

		// Affichage des buckets
		int cptNonUniq = 0;
		int cpt = 0;
		int validNonUniq =0;
		float oks = 0, kos = 0;
		float kouniqs = 0, okuniqs = 0;

		for (Cluster c : clustering.getLesClusters()) {
			if (c.size() > 1) {
				boolean ok = true;
				// verify cluster
				for (Donnee d1 : c) {
					Trace t1 = (Trace) d1;
					for (Donnee d2 : c) {
						Trace t2 = (Trace) d2;
						if (d1 != d2) {
							if ((t1.getGroupId() != t2.getGroupId())) {
								ok = false;
								kos++;
							} else {
								oks++;
							}
						}
					}
				}
				if (ok)
					validNonUniq++;
				cptNonUniq++;
				System.out.println("bucket n�" + cpt + (ok ? "(VALIDE)" : "(NON VALIDE)"));
				cpt++;

				if (VERBOSE) {
					for (Donnee d : c) {
						System.out.println(d.toComplexString());
					}
				}

			} else {
				Trace t = (Trace) c.get(0);
				if (t.getGroupId() == t.getBugId()) {
					okuniqs++;
				} else {
					kouniqs++;
				}
			}
		}
		
		System.out.println("-----------------------");

		System.out.println("nb clusters uniques: "
				+ (clustering.getLesClusters().size() - cptNonUniq));
		System.out.println("ok : " + okuniqs + " / ko : " + kouniqs
				+ " / precision : " + (okuniqs / (okuniqs + kouniqs) * 100)
				+ "%\n");
		System.out.println("nb clusters non uniques : " + cptNonUniq+" (100% valides : "+validNonUniq+")");		
		System.out.println("ok : " + oks + " / ko : " + kos + " / precision : "
				+ (oks / (oks + kos) * 100) + "%\n");




	}

}

/*
 * 
 * ALGO VOISINS : 5000 elements ----------------------------- fin de l'init
 * (duree : 227025) fin de l'algo (duree : 500) nb clusters uniques: 3377 nb
 * clusters non uniques : 641 buckets de plus d'un element : ok : 808.0 / ko :
 * 2450.0 / precision : 24.800491% buckets uniques : ok : 3005.0 / ko : 372.0 /
 * precision : 88.98431%
 * 
 * 
 * 
 * 
 * ALGO CENTRES : 5000 elements ------------------------------
 */