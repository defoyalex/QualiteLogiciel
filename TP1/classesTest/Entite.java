import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public abstract class Entite {

    //Toutes les entités du jeu (poissons, poissons spéciaux, cible)

    protected double largeur, hauteur; //Taille des entités
    protected Image image;

    //Position, vitesse et accélération
    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;

    //Couleur de l'entité
    protected Color color;

    //Met à jour la position de l'entité
    public void update(double dt) {
        vy += dt * ay;
        y += dt * vy;

        vx += dt * ax;
        x += dt * vx;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    //Affiche l'entité
    public abstract void draw(GraphicsContext context);
}