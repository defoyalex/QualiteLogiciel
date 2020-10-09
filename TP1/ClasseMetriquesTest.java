import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClasseMetriquesTest {

    @Test
    public void ClasseMetriques(){
        //Test pour DefaultXYDataset. Déclaration de méthode et classe complexes.
        ClasseMetriques classe;
        String path = "./TP1/tests/DefaultXYDataset";
        File file = new File(path+".txt");
        String classInString;
        try {
            Scanner reader = new Scanner(file);
            classInString = Main.classToString(reader, "");
            classe = new ClasseMetriques(path+".java",classInString,0);
            assertEquals(28,classe.getCLOC());
            assertEquals(55,classe.getLOC());
            double dc = (double)classe.getCLOC()/(double)classe.getLOC();
            assertEquals(dc,classe.getDC());
        } catch (FileNotFoundException e){
            System.out.println("No file found");
        }

        //Test pour ChartFactory. Méthodes complexes.
        //Lignes vides avec des whitespace.
        path = "./TP1/tests/ChartFactory";
        file = new File(path+".txt");
        try {
            Scanner reader = new Scanner(file);
            classInString = Main.classToString(reader, "");
            classe = new ClasseMetriques(path+".java",classInString,0);
            assertEquals(120,classe.getCLOC());
            assertEquals(278,classe.getLOC());
            double dc = (double)classe.getCLOC()/(double)classe.getLOC();
            assertEquals(dc,classe.getDC());
        } catch (FileNotFoundException e){
            System.out.println("No file found");
        }

    }



}