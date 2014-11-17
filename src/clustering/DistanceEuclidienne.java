package clustering;

public class DistanceEuclidienne implements Distance {

	@Override
	public double valeur(Donnee d1, Donnee d2) {
		int result=0;
		if (d1.nbDimensions()==d2.nbDimensions()){
			for(int i=0;i<d1.nbDimensions();++i){
				result+=Math.abs(d1.valeurDim(i)-d2.valeurDim(i));
			}
			return result;
		}
			
		return -1;
	}

}
