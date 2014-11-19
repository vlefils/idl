package iagl.opl.rebucket.eclipse;

import iagl.opl.rebucket.eclipse.model.ReportBug;
import iagl.opl.rebucket.oops.clustering.Cluster;
import iagl.opl.rebucket.oops.clustering.Clustering;
import iagl.opl.rebucket.oops.clustering.Donnee;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static void main(String[] args) throws JSONException, IOException,
			IllegalAccessException {

		URL urlDirJson = Main.class.getClassLoader().getResource("jsonfiles");

		File[] jsons = new File(urlDirJson.getFile())
				.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.endsWith(".json");
					}
				});

		List<Donnee> traces = new LinkedList<Donnee>();
		
		ObjectMapper mapper = new ObjectMapper();
		for(File f : jsons){
			ReportBug report = mapper.readValue(f, ReportBug.class);
			traces.addAll(report.traces);
		}

		System.out.println("traces ajoutées : "+traces.size());
		
		Clustering clustering = new Clustering(traces, 0.5, new DistanceTraceEclipse());
		
		System.out.println("début de l'algo");
		long timeInit = System.currentTimeMillis();
		clustering.algoCentres(false);
		long timeEnd = System.currentTimeMillis();
		
		int cptNonUniq=0;
		for(Cluster c : clustering.getLesClusters()){
			if(c.size()>1){
				cptNonUniq++;
			}
		}
		
		System.out.println("nb clusters : "+clustering.getLesClusters().size());
		System.out.println("nb clusters non uniques : "+cptNonUniq);
		
		System.out.println("fin de l'algo (durée : "+(timeEnd-timeInit)+")");
		
		
	}
	
}
