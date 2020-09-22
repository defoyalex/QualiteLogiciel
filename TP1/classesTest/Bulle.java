
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class Bulle extends Entite {
    //Crée une bulle décorative d'une et position au hasard

    //Constructeur pour Bulle
    public Bulle(double x) {
        this.largeur = Math.random() * 30 + 10;
        this.hauteur = largeur;

        this.x = x;
        this.y = 480;
        this.color = Color.rgb(0, 0, 255, 0.4);
        this.vy = Math.random() * 100 - 450;
        this.ay = 0;

    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(color);
        context.fillOval(x, y, largeur, hauteur);
    }
}


