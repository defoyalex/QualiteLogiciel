import javafx.scene.canvas.GraphicsContext;

public final class GroupeBulles {
    private Bulle[] bulles = new Bulle[5];

    public GroupeBulles(double baseX){
        this.initGroupeBulles(baseX);
    }

    //Crée un nouveau groupe de 5 bulles à partir d'une coordonnée en x

    //Constructeur
    public void initGroupeBulles(double baseX) {
        for (int i = 0; i < this.bulles.length; i++) {
            //Crée les bulles à une distance de -20px à +20px
            double x = baseX + Math.random()*40-20;
            this.bulles[i] = new Bulle(x);
        }
    }

    public void update (double dt){
        for (int i=0; i<this.bulles.length; i++) {
            this.bulles[i].update(dt);
        }
    }

    public void draw(GraphicsContext context) {
        for (int i=0; i<this.bulles.length; i++) {
            this.bulles[i].draw(context);
        }
    }

}
