import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    ArrayList<File> listFiles;
    ArrayList<ClasseMetriques> listClasses;

    @Test
    public void getListFiles() {

        String path = "./TP1/tests";
        ArrayList<File> listFiles = Main.getListFiles(path,".txt");
        assertEquals(5,listFiles.size());
        this.listFiles = listFiles;
    }

    @Test
    public void readFiles() {
        //Chartcolor loc = (24+22), cloc = 22    Tester ligne de code
        //DefaultWIndDataset  plusieurs classes dans un fichier, déclaration de classe sur plusieurs lignes
        //DefaultXYDataset : déclaration de méthode et de classe complexe
        //Chartfactory : Méthodes complexes
        //ChartMouseListener = Interface + méthode Override


        assertEquals(1,1);
    }

    @Test
    public void findEndClass(){
        File file = new File("./TP1/tests/DefaultWindDataset.txt");
        assertEquals(1,1);

        //Test pour un fichier avec deux classes. On s'assure qu'on trouve deux classes.
        try {
            Scanner reader = new Scanner(file);
            String classInString1 = Main.findEndClass(reader, "");
            String[] numberLine = classInString1.split("\\r?\\n");
            assertEquals(90,numberLine.length,"FindEndClass for 1st class not good");


            String classInString2 = Main.findEndClass(reader, "");
            String[] numberLine2 = classInString2.split("\\r?\\n");
            assertEquals(31,numberLine2.length,"FindEndClass for 2nd class not good");

        } catch (FileNotFoundException e){
            System.out.println("No java file found");
        }

    }

}