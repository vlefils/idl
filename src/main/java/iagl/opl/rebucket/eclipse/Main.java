package iagl.opl.rebucket.eclipse;

import iagl.opl.rebucket.clustering.Cluster;
import iagl.opl.rebucket.clustering.Clustering;
import iagl.opl.rebucket.clustering.Donnee;
import iagl.opl.rebucket.eclipse.model.ReportBug;
import iagl.opl.rebucket.eclipse.model.Trace;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		int max = 750;
		for(File f : jsons){
			if (max-- == 0) break;
			ReportBug report = mapper.readValue(f, ReportBug.class);
//			traces.addAll(report.traces);
			traces.add(report.traces.get(report.traces.size()-1));
			if (report.traces.isEmpty())System.err.println(report.bugId);
		}
		System.out.println("traces ajoutées : "+traces.size());
		
		Clustering clustering = new Clustering(traces, 0.9, new DistanceTraceEclipse());
		
		System.out.println("début de l'algo");
		long timeInit = System.currentTimeMillis();
		clustering.algoVoisins(false);
		long timeEnd = System.currentTimeMillis();
		
		int cptNonUniq=0;
		int cpt=0;
		for(Cluster c : clustering.getLesClusters()){
			if(c.size()>1){
						
				cptNonUniq++;
				System.out.println("\nbucket n°"+cpt);
				cpt++;
								
				for(Donnee d : c){
					System.out.println(d.toComplexString());
				}
				
			}
		}
		
		System.out.println("nb clusters : "+clustering.getLesClusters().size());
		System.out.println("nb clusters non uniques : "+cptNonUniq);
		
		System.out.println("fin de l'algo (durée : "+(timeEnd-timeInit)+")");
		
		System.out.println("calcul des f-score");
		
		for(Cluster c : clustering.getLesClusters()){

		}
		

		
		
	}
	
}
