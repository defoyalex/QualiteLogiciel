
public class Etoile extends Poisson {
    private double timer=0;
    private int inversion = -1; // Alternance entre montée/descente de l'étoile

    //Constructeur pour l'étoile
    public Etoile() {
        super("etoile");
    }


    //Met à jour la position de l'étoile
    @Override
    public void update(double deltaTime){
        timer += deltaTime;

        //On a choisi 75pixels comme amplitude
        vy = 75 * inversion;

        //Alternance de vitesse aux 0.5 secondes
        if(timer >=0.5){
            inversion *= -1;
            timer = 0;
        }

        super.update(deltaTime);

    }
}