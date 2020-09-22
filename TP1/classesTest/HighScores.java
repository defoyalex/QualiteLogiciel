import javafx.scene.control.ListView;

import java.io.*;
import java.util.ArrayList;

public class HighScores {
    //Pour gérer le fichier contenant les meilleurs scores
    private ArrayList<String> listeNoms;
    private ArrayList<Integer> listeScores;
    private String file;

    //Constructeur, va chercher les scores dans un fichier
    //Sur chaque ligne, le premier mot est le nom et le deuxième le score
    //Ils doivent être séparé d'un espace
    public HighScores(String file){
        this.file = file;
        this.listeNoms = new ArrayList<>();
        this.listeScores = new ArrayList<>();
        //Écrire le contenu dans les deux ArrayList
        try{
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String scores;
            while ((scores = reader.readLine()) != null) {
                String[] separate = scores.split(" ");
                listeNoms.add(separate[0]);
                listeScores.add(Integer.parseInt(separate[1]));
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("Erreur à l’ouverture du fichier");
        }
    }

    //Retourne une liste des hauts scores
    public ListView<String> getHighScores(){
        ArrayList<String> list = new ArrayList<String>();
        String scores;
        for (int i=0; i<this.listeNoms.size();i++){
            scores = "";
            scores += "#" + (i+1);
            scores += " - " + listeNoms.get(i);
            scores += " - " + listeScores.get(i);
            list.add(scores);
        }
        ListView<String> listview = new ListView<>();
        listview.getItems().setAll(list);
        return listview;
    }

    // Modifier les deux Arraylist et écrire les meilleurs scores dans le fichier
    public void setHighScores(String nom, int score) {

        //Ajouter aux ArrayList à la bonne position
        int index = 0;
        while( (index<listeScores.size()) && (score<listeScores.get(index)) ){
            index+=1;
        }
        listeScores.add(index,score);
        listeNoms.add(index,nom);
        if (listeScores.size()>10) {
            listeScores.remove(10);
            listeNoms.remove(10);
        }

        //Écrire dans le fichier des scores
        try {
            FileWriter fw = new FileWriter(this.file);
            BufferedWriter writer = new BufferedWriter(fw);

            for (int i = 0; i < this.listeNoms.size(); i++) {
                writer.write(this.listeNoms.get(i)+" "+this.listeScores.get(i));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Erreur à l’écriture");
        }
    }

    //Pour évaluer si la quantité de poissons touchés est un nouveau highscore
    public boolean isHighScores(int newScore){
        int size = this.listeScores.size();
        if (size<10||newScore> this.listeScores.get(size - 1)){
            return true;
        }
        else {
            return false;
        }
    }

}
