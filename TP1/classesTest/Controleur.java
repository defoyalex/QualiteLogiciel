import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;



public final class Controleur {
    //Fait le lien entre FishHunt et le modele
    private Modele modele;
    private HighScores highScores;
    protected boolean gameOver; //Indique si la partie est perdue
    protected int lastScore; //Sauvegarde le dernier score pour les HighScores

    //Constructeur, crée un instance de Modele et HighScores
    public Controleur() {
        modele = new Modele(this);
        highScores = new HighScores("scores.txt");
        gameOver = false;
    }

    public ListView<String> getHighScores() {
        return highScores.getHighScores();
    }

    public void setHighScores(String nom, int score) {
        highScores.setHighScores(nom, score);
    }

    public boolean isHighScores(){
        return highScores.isHighScores(lastScore);
    }


    public void niveauPlus() {
        modele.niveauPlus();
    }

    public void scorePlus() {
        modele.scorePlus();

    }

    public void viePlus() {
        modele.viePlus();
    }

    public void partiePerdu(int score) {
        this.gameOver = true;
        this.lastScore = score;
    }

    public void setGameOver(){
        this.modele.setGameOver();
    }

    public void boolGameOver(boolean bool){
        this.gameOver = bool;
    }

    public void setX(double x) {
        modele.setX(x);
    }

    public void setY(double y) {
        modele.setY(y);
    }

    public void mouseClick(double x, double y) {
        modele.mouseClick(x,y);
    }

    void update(double deltaTime){
        modele.update(deltaTime);
    }

    void draw(GraphicsContext context) {modele.draw(context);}

}