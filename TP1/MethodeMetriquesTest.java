import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodeMetriquesTest {



    @Test
    public void MethodeMetriques(){

        //Noms de méthode complexes
        //Haute complexité cyclomatique
        //Commentaire avec ligne de code
        ClasseMetriques classe;
        ArrayList<MethodeMetriques> methodes;
        MethodeMetriques methode;

        String path = "./TP1/tests/ChartMouseListener";
        File file = new File(path+".txt");
        String classInString;

        try {
            Scanner reader = new Scanner(file);
            classInString = Main.classToString(reader, "");
            classe = new ClasseMetriques(path+".java",classInString,0);
            methodes = classe.getMethodeMetriquesList();
            assertEquals(1, methodes.size(), "MethodeMetrique not equal to one");
            methode = methodes.get(0);

            assertEquals(2,methode.getCLOC());
            assertEquals(71,methode.getLOC());
            double dc = (double)methode.getCLOC()/(double)methode.getLOC();
            assertEquals(dc,methode.getDC());
            assertEquals(11, methode.getComplexity());
            String name = "createPieChart_String_PieDataset_PieDataset_" +
                          "int_boolean_boolean_boolean_Locale_boolean_boolean";
            assertEquals(name, methode.getMethodName());

        } catch (FileNotFoundException e){
            System.out.println("No file found");
        }

        //ChartColor. 2 méthodes.
        //Commentaire de plusieurs lignes et JavaDoc
        path = "./TP1/tests/ChartColor";
        file = new File(path+".txt");

        try {
            Scanner reader = new Scanner(file);
            classInString = Main.classToString(reader, "");
            classe = new ClasseMetriques(path+".java",classInString,0);
            methodes = classe.getMethodeMetriquesList();
            assertEquals(2, methodes.size(), "MethodeMetrique not equal to one");

            //Première méthode
            methode = methodes.get(0);
            assertEquals(8,methode.getCLOC(),"Wrong CLOC, method 1");
            assertEquals(11,methode.getLOC(), "Wrong LOC, method 1");
            double dc = (double)methode.getCLOC()/(double)methode.getLOC();
            assertEquals(dc,methode.getDC(), "Wrong DC, method 1");
            assertEquals(1, methode.getComplexity(), "Wrong complexity, method 1");
            String name = "ChartColor_int_int_int";
            assertEquals(name, methode.getMethodName(), "Wrong name, method 1");

            //Deuxième méthode
            methode = methodes.get(1);
            assertEquals(10,methode.getCLOC(), "Wrong CLOC, method 2");
            assertEquals(27,methode.getLOC(), "Wrong LOC, method 2");
            dc = (double)methode.getCLOC()/(double)methode.getLOC();
            assertEquals(dc,methode.getDC(), "Wrong DC, method 2.");
            assertEquals(1, methode.getComplexity(), "Wrong complexity, method 2");
            name = "createDefaultPaintArray";
            assertEquals(name, methode.getMethodName(), "Wrong name method 2");

        } catch (FileNotFoundException e){
            System.out.println("No file found");
        }

    }



}