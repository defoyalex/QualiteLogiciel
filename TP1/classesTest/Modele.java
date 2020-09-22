import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public final class Modele {

    public static final int WIDTH = 640, HEIGHT = 480;
    private int numeroNiveau; //Level modifié à chaque 5 poissons touchés
    private boolean gameOver = false; //Indique une défaite
    private boolean partieCommencee = false;// Indique si la partie est commencée
    private double timer; //Timer depuis la dernière apparition d'un poisson normal
    private double timerSpecial; //Timer depuis le dernier poisson special
    private double timerBulles; //Timer depuis le dernier groupe de bulles
    private boolean presencePoissonNormal;
    private boolean presenceSpecial;
    private Poisson poissonSpecial;
    private Poisson poissonNormal;
    private int poissonsEchappes; // indirectement, nombre de vies
    private int comptePoissons; //Quantité de poissons touchés depuis le début de la partie
    private Cible cible = new Cible();
    private Balle[] balles = new Balle[5]; //Tableau contenant les balles
    private int compteurBalle = 0; //Indique la prochaine balle qui doit être lancée dans le tableau
    private Controleur controleur;
    private GroupeBulles[] groupeBulles = new GroupeBulles[3]; //Trois groupes de bulles en même temps

    //Constructeur de la class modele
    public Modele(Controleur controleur) {
        this.initGroupeBulles();
        this.initPartie();
        this.initBalles();
        this.controleur = controleur;
    }

    //Permet de mettre les bons paramètres pour le début de partie
    public void initPartie() {
        numeroNiveau = 1;
        timer = 0;
        timerBulles = 0;
        poissonsEchappes = 0;
        comptePoissons = 0;
        presencePoissonNormal = false;
        presenceSpecial = false;
        partieCommencee = false;
        gameOver = false;
    }

    public void initGroupeBulles() {
        for (int i = 0; i < this.groupeBulles.length; i++) {
            double baseX = Math.random() * WIDTH;
            this.groupeBulles[i] = new GroupeBulles(baseX);
        }
    }

    public void initBalles() {
        for (int i = 0; i < balles.length; i++) {
            balles[i] = new Balle(0, 0, 50);
        }
    }

    //Gère les paramètres de changement de niveau et la fonction debug "H"
    public void niveauPlus() {
        partieCommencee = false;
        timer = 0;
        timerSpecial = 0;
        numeroNiveau++;

        //On fait disparaître les poissons et les balles présentes
        if(presenceSpecial) {
            poissonSpecial.setX(5000);
        }
        presenceSpecial = false;
        if(presencePoissonNormal){
            poissonNormal.setX(5000);
        }
        presencePoissonNormal = false;

        for(int i = 0; i<balles.length; i++){
            balles[i].setPosition(5000,0);
        }
    }

    //Fonction debug "K"
    public void viePlus() {
        poissonsEchappes -= 1;
    }

    //Fonction debug "J"
    public void scorePlus() {
        comptePoissons++;
    }



    //Determine si le poisson a quitté l'écran
    public boolean isPresence(Poisson poisson) {
        if (poisson.orientation == 1) {
            return (poisson.getX() < WIDTH);
        } else {
            return (poisson.getX() + poisson.largeur > 0);
        }
    }

    //La partie se termine si le nombre de poissons echappés dépasse 3
    public void setGameOver() {
        gameOver = true;
        timer = 0;
        controleur.partiePerdu(this.comptePoissons);
    }

    //Gère la position de la cible
    public void setX(double x) {
        cible.setX(x);
    }
    public void setY(double y) {
        cible.setY(y);
    }

    //Lance une balle lors d'un clic de souris
    public void mouseClick(double x, double y) {
        if (this.partieCommencee) {
            balles[compteurBalle].setPosition(x, y);
            balles[compteurBalle].setActive();
            compteurBalle++;
            if (compteurBalle >= balles.length) {
                compteurBalle = 0;
            }
        }
    }

    //Collision des balles avec les poissons
    public boolean testCollision(Balle balle, Poisson poisson) {
        return ((balle.x < poisson.getX() + poisson.getLargeur()) && (balle.x > poisson.getX()) &&
                (balle.y < poisson.getY() + poisson.getHauteur()) && (balle.y > poisson.getY()));
    }

    //on regarde si les conditions sont respectées pour augmenter de niveau
    public void testNiveau() {
        if(comptePoissons%5==0) {
            niveauPlus();
        }
    }

    public void update(double dt) {
        timer+=dt;
        if (gameOver){
            this.initPartie();
        }

        //Indique quand il faut recréer un poisson et le début de partie
        if (timer>=3){
            partieCommencee = true;
            timer = 0;
            poissonNormal = new Poisson("normal");
            poissonNormal.setVx(numeroNiveau);
            presencePoissonNormal = true;
        }

        //Indique quand il faut creer un poisson special
        if (numeroNiveau >= 2){
            timerSpecial += dt;
            if(timerSpecial >= 5){
                timerSpecial = 0;
                double choisirSpecial = Math.random();
                if(choisirSpecial <=0.5){
                    poissonSpecial = new Crabe();
                    presenceSpecial = true;
                    poissonSpecial.setVx(numeroNiveau);
                } else{
                    poissonSpecial = new Etoile();
                    presenceSpecial = true;
                    poissonSpecial.setVx(numeroNiveau);
                }
            }
        }

        //Crée des nouveaux groupes de bulles
        timerBulles+=dt;
        if (timerBulles>=3){
            this.initGroupeBulles();
            timerBulles = 0;
        }
        for (int i=0; i<groupeBulles.length; i++){
            groupeBulles[i].update(dt);
        }

        // On n'update pas le reste si la partie n'est pas commencée ou entre les levels
        if(!this.partieCommencee){
            return;
        }

        // Update le poisson normal
        if(presencePoissonNormal) {
            poissonNormal.update(dt);
            if(!isPresence(poissonNormal) && presencePoissonNormal){
                poissonsEchappes += 1;
                presencePoissonNormal = false;
            }
        }

        //Update le poisson spécial
        if(presenceSpecial){
            poissonSpecial.update(dt);
            if(!isPresence(poissonSpecial) && presenceSpecial){
                poissonsEchappes += 1;
                presenceSpecial = false;
            }
        }

        //Tests de collision si le rayon de la balle tombe à zéro
        for(int i = 0; i< balles.length; i++) {
            balles[i].update(dt);
            if (balles[i].ballZero()){
                if(testCollision(balles[i], poissonNormal)) {
                    presencePoissonNormal = false;
                    comptePoissons++;
                    poissonNormal.setX(5000);
                    testNiveau();
                }
                if(presenceSpecial) {
                    if (testCollision(balles[i], poissonSpecial)) {
                        presenceSpecial = false;
                        comptePoissons++;
                        poissonSpecial.setX(5000);
                        testNiveau();
                    }
                }
            }
        }

        // determine si la partie est terminée
        if (poissonsEchappes >= 3 && !gameOver) {
            setGameOver();
        }

    }

    //Dessine tous les éléments du modèle
    void draw(GraphicsContext context) {
        context.setFill(Color.rgb(25,0,150));
        context.fillRect(0,0,WIDTH,HEIGHT);

        //Affiche le prochain level lorsqu'on est pas en train de jouer
        if(!this.partieCommencee) {
            String level = "Level " + numeroNiveau;
            context.setFill(Color.WHITE);
            context.setFont(Font.font("Copperplate", 65));
            context.setTextAlign(TextAlignment.CENTER);
            context.fillText(level, 320, 170);
        }

        //Affiche la quantité de poissons touchés
        String qtePoissons = ""+comptePoissons;
        context.setFill(Color.WHITE);
        context.setFont(Font.font("Copperplate",30));
        context.setTextAlign(TextAlignment.CENTER);
        context.fillText(qtePoissons, 320, 40);

        //Affiche le nombre de vies restantes
        Image viesImage = new Image("/images/fish/00.png");
        if(poissonsEchappes<=3 && poissonsEchappes>=0) {
            if (poissonsEchappes <= 2) {
                context.drawImage(viesImage, 255, 70, 26, 26);
            }
            if (poissonsEchappes <= 1) {
                context.drawImage(viesImage, 307, 70, 26, 26);
            }
            if (poissonsEchappes == 0) {
                context.drawImage(viesImage, 359, 70, 26, 26);
            }
        } else if (poissonsEchappes < 0){
            context.drawImage(viesImage, 255, 70, 26, 26);
            String nbVies = "X " + (-1 * poissonsEchappes + 3);
            context.fillText(nbVies, 325, 93);
        }

        //Dessiner chaque groupe de bulles
        for (int i=0; i < groupeBulles.length; i++) {
            this.groupeBulles[i].draw(context);
        }


        //Dessine le poisson
        if(presencePoissonNormal){
            poissonNormal.draw(context);

        }
        if(presenceSpecial){
            poissonSpecial.draw(context);
        }

        cible.draw(context);
        for(int i = 0; i < balles.length; i++){
            balles[i].draw(context);
        }

    }
}