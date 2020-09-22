/*
    Programme écrit par :
        Alex Defoy
        Matricule : 20131653
        Félix-Antoine Lesieur
        Matricule : 1054023
    Ce programme est un jeu.
    Pour jouer, il suffit de faire un clic gauche avec la souris.
    Une balle sera envoyée et il faut toucher les poissons qui
    apparaissent dans l'écran.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;


//Contient le timer et l'effet de touches
public final class FishHunt extends Application {

    public static final int WIDTH = 640, HEIGHT = 480;


    public static void main(String[] args) {
        launch(args);

    }

    //Timer et effet des touches
    @Override
    public void start(Stage primaryStage) throws Exception {

        Controleur controleur = new Controleur();

        // Créer la scène contenant l'écran d'accueil
        Pane rootHome = new Pane();
        Scene homeScene = new Scene (rootHome, WIDTH, HEIGHT);
        Canvas canvasHome = new Canvas(WIDTH, HEIGHT);
        rootHome.getChildren().add(canvasHome);
        //Couleur bleu
        GraphicsContext contextHome = canvasHome.getGraphicsContext2D();
        contextHome.setFill(Color.rgb(0,0,135));
        contextHome.fillRect(0,0,WIDTH,HEIGHT);
        //Image du logo
        Image img = new Image("/images/logo.png");
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        //Boutons de la scène home
        Button buttonGame = new Button("Nouvelle Partie!");
        Button buttonScores = new Button("Meilleurs scores");
        //Ajouter les boutons et l'image à la scène home
        VBox boxHome = new VBox(2,imageView, buttonGame, buttonScores);
        boxHome.setAlignment(Pos.CENTER);
        boxHome.setPrefSize(WIDTH, HEIGHT);
        rootHome.getChildren().add(boxHome);


        //Créer la scene contenant le jeu principal
        Pane rootGame = new Pane();
        Scene gameScene = new Scene (rootGame, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        rootGame.getChildren().add(canvas);
        GraphicsContext contextGame = canvas.getGraphicsContext2D();


        //Créer la scene contenant le tableau de scores
        BorderPane rootScore = new BorderPane();
        Scene scoreScene = new Scene(rootScore, WIDTH, HEIGHT);
        rootScore.setPadding(new Insets(50,20,30,20));

        //Texte en haut du BorderPane
        Text meilleursScores = new Text("Meilleurs scores");
        meilleursScores.setFont(Font.font("serif", 30));
        rootScore.setAlignment(meilleursScores, Pos.CENTER);
        rootScore.setTop(meilleursScores);

        //Boutons en bas du tableau de score
        Button buttonMenu = new Button("Menu");
        HBox bottomScoreMenu = new HBox();
        bottomScoreMenu.setAlignment(Pos.CENTER);
        bottomScoreMenu.setPadding(new Insets(30));
        bottomScoreMenu.getChildren().add(buttonMenu);
        //Bas du tableau lorsque l'on entre un nouveau meilleur score
        Button buttonAjouter = new Button("Ajouter!");
        TextField entrerScore = new TextField();
        Text votreNom = new Text("Votre nom : ");




        // Effet des boutons
        EventHandler<ActionEvent> eventGame = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                primaryStage.setScene(gameScene);
            }
        };

        //Pour commencer une partie
        buttonGame.setOnAction(eventGame);

        //Pour afficher les meilleurs scores
        buttonScores.setOnAction((event)-> {
            rootScore.setCenter(controleur.getHighScores());
            rootScore.setBottom(bottomScoreMenu);
            primaryStage.setScene(scoreScene);
        });
        //Pour retourner à la scène d'accueil à partir des scores
        buttonMenu.setOnAction((event)-> {
            primaryStage.setScene(homeScene);
        });

        //Pour ajouter un nouveau meilleur score
        buttonAjouter.setOnAction((event)->{
            String nouveauMeilleur = entrerScore.getText();
            if (nouveauMeilleur.equals("")){
                nouveauMeilleur = "Talentueux_inconnu";
            }
            controleur.setHighScores(nouveauMeilleur, controleur.lastScore);
            entrerScore.clear();
            primaryStage.setScene(homeScene);
        });



        //Tous les effets lorsque les touches sont activées
        //Utilise le controleur
        gameScene.setOnKeyPressed((e) -> {
            switch (e.getCode()) {
                case H:
                    controleur.niveauPlus();
                    break;
                case J:
                    controleur.scorePlus();
                    break;
                case K:
                    controleur.viePlus();
                    break;
                case L:
                    controleur.setGameOver();
                    break;
            }
        });
        //Evenements de souris
        gameScene.setOnMouseClicked((event) ->{
            controleur.mouseClick(event.getX(),event.getY());
        });

        gameScene.setOnMouseMoved((event) -> {
            controleur.setX(event.getX());
            controleur.setY(event.getY());
        });


        //Appelé à chaque coup d'horloge
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                //Aller à la scène des meilleurs scores si la partie est perdue
                if (controleur.gameOver){
                    //Liste des meilleurs scores
                    rootScore.setCenter(controleur.getHighScores());
                    //Si le dernier score est un meilleur score
                    if (controleur.isHighScores()){
                        //Ajoute le textfield et le bouton pour ajouter un nouveau score
                        String texteScore = " a fait "+controleur.lastScore+" points! ";
                        Text points = new Text(texteScore);
                        HBox bottomNewScore = new HBox();
                        bottomNewScore.setAlignment(Pos.CENTER);
                        bottomNewScore.getChildren().add(votreNom);
                        bottomNewScore.getChildren().add(entrerScore);
                        bottomNewScore.getChildren().add(points);
                        bottomNewScore.getChildren().add(buttonAjouter);
                        VBox containerBottom = new VBox(5, bottomNewScore, bottomScoreMenu);
                        rootScore.setBottom(containerBottom);
                    } else{
                        //Sinon on montre les scores avec seulement le bouton pour aller au menu
                        rootScore.setBottom(bottomScoreMenu);
                    }
                    primaryStage.setScene(scoreScene);
                    controleur.boolGameOver(false);
                }
                //Pour rendre les vitesses constantes et non dépendantes de la vitesse de l'ordinateur
                double deltaTime = (now - lastTime) * 1e-9;
                contextGame.clearRect(0, 0, WIDTH, HEIGHT);
                if(primaryStage.getScene().equals(gameScene)) {
                    controleur.update(deltaTime);
                    controleur.draw(contextGame);
                }

                lastTime = now;
            }
        };


        timer.start();


        //Paramètres du stage
        primaryStage.setScene(homeScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Fish Hunt");

        primaryStage.show();

    }



}