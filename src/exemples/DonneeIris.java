package exemples ;
public class DonneeIris extends clustering.Donnee{

    private String nom ;

    public DonneeIris(double[] t, String nom){
        super(t) ;
        this.nom = nom ;
    }

    public String nom(){
        return this.nom ;
    }
}