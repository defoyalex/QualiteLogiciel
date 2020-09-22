import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Poisson extends Entite{

    protected String type;
    protected double orientation; //Soit 1 ou -1, direction du poisson
    protected double rouge;
    protected double vert;
    protected double bleu;

    //Constructeur, dépendamment, différent types de poisson ont différents comportements
    public Poisson(String type){
        this.largeur = Math.floor(Math.random() * 40) + 80;
        this.hauteur = Math.floor(Math.random() * 40) + 80;

        //La creation de l'instance dépend du type de poisson
        if (type.equals("normal")){
            this.ay = 100;
            this.vy = Math.random() * -100 - 100;

            this.y = (Math.random()*3+1)*480/5;

            //L'image du poisson Normal est déterminée au hasard
            int intImage = (int) Math.floor(Math.random() * 8)+1;
            switch (intImage) {
                case 1:
                    this.image = new Image("/images/fish/00.png");
                    break;
                case 2:
                    this.image = new Image("/images/fish/01.png");
                    break;
                case 3:
                    this.image = new Image("/images/fish/02.png");
                    break;
                case 4:
                    this.image = new Image("/images/fish/03.png");
                    break;
                case 5:
                    this.image = new Image("/images/fish/04.png");
                    break;
                case 6:
                    this.image = new Image("/images/fish/05.png");
                    break;
                case 7:
                    this.image = new Image("/images/fish/06.png");
                    break;
                case 8:
                    this.image = new Image("/images/fish/07.png");
                    break;
            }

        } else if (type.equals("crabe")){ //creation du crabe
            this.image = new Image("/images/crabe.png");
            this.ay = 0;
            this.y = 480 - hauteur; //le crabe est toujours en bas de l'écran

        } else { //creation de l'étoile
            this.image = new Image("/images/star.png");
            this.ay = 0;
            this.y = (Math.random()*3+1)*480/5;
        }

        //Éléments de couleurs sont choisis au hasard
        this.rouge =  Math.random();
        this.vert = Math.random();
        this.bleu =  Math.random();
        this.color = Color.color(rouge,vert,bleu);
        this.image = ImageHelpers.colorize(image, this.color);



        //Orientation = 1 si les poissons vont VERS LA DROITE ou -1 si VERS LA GAUCHE
        double intOrientation = Math.random();
        if(intOrientation < 0.5) {
            this.orientation = 1;
            this.x = -this.largeur;
        } else {
            this.orientation = -1;
            this.image = ImageHelpers.flop(this.image);
            this.x = 640;
        }

    }

    //Permet de déterminer la vitesse en fonction du niveau
    public void setVx(int numeroNiveau){
        this.vx = (Math.pow(numeroNiveau, (1.0/3.0))*100.0+200.0)*orientation;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getLargeur(){
        return largeur;
    }

    public double getHauteur(){
        return hauteur;
    }

    public void update(double deltaTime){
        //Physique du personnage
        super.update(deltaTime);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }

}