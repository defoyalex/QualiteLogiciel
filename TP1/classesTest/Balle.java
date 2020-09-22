import javafx.scene.canvas.GraphicsContext;

public class Balle extends Entite{

    private double rayon;
    private double vitRayon; //Vitesse à laquelle le rayon diminue
    //Indique si une balle est lancée, faux lorsqu'elle atteint zéro
    private boolean active;

    //Constructeur
    public Balle(double x, double y, int rayon) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.vitRayon = 0;
        this.active = false; //balle active peut entrer en collision avec un poisson
    }

    public void setActive(){
        this.vitRayon = -300;
        this.active = true;
    }

    public void setPosition (double x, double y){
        this.x = x;
        this.y = y;
    }

    //Determine si la balle a atteint sa destination (rayon = 0)
    public boolean ballZero () {
        if (rayon <= 0) {
            rayon = 50;
            this.vitRayon = 0;
            this.active = false;
            return true;
        } else{
            return false;
        }
    }

    //Mettre à jour son rayon
    public void update(double deltaTime){
        rayon += vitRayon * deltaTime;
    }

    //Dessine la balle
    @Override
    public void draw(GraphicsContext context) {
        if (this.active) {
            context.setFill(color.BLACK);
            context.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
        }
    }
}
