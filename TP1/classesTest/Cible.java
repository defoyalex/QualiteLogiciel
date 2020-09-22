
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Cible extends Entite{

    //Constructeur de l'image indiquant l'emplacement de la souris
    public Cible(){
        image = new Image("/images/cible.png");
        largeur = 50;
        hauteur = 50;
    }
    //On determine l'image CENTRÉE sur le centre de la cible
    public void setX(double x){
        this.x = x - (largeur/2);
    }
    public void setY(double y){
        this.y = y - (hauteur/2);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }


}