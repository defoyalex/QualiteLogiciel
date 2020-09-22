
public class Crabe extends Poisson {
    private double timer;
    private boolean avance; // avance ou recule
    private int inversion; //le crabe change de direction, on alterne entre 1  et -1

    //Constructeur pour le crabe qui avance et recule
    public Crabe() {
        super("crabe");
        timer = 0;
        avance = true;
        inversion = 1;
    }

    //Met à jour la position du crabe
    @Override
    public void update(double deltaTime){
        timer += deltaTime;

        if (avance && timer >= 0.5){
            avance = false;
            timer = 0;
            inversion = -1; //changer de direction

        } else if (!avance && timer >=0.25){
            avance = true;
            timer = 0;
            inversion = 1; // changer de direction
        }
        //Physique du crabe
        x += deltaTime * vx * inversion;

    }
}
