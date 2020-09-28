import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    /* Fait un tableau avec tous les fichiers javas à partir du chemin
    d'un répertoire */
    public static File[] getListJavaFiles(String directory) {
        File files = new File(directory);

        if (!files.isDirectory()) {  //si c'est pas un dossier et plutôt un fichier
            return null;
        }

        System.out.println("files is directory? " + files.isDirectory());

        FileFilter filter = new FileFilter() { //sert à ne conserver que les fichiers java
            public boolean accept(File files) {
                return files.getName().endsWith("java");
            }
        };

        File[] listFiles = files.listFiles(filter);
        return listFiles;
    }

    // Lis une liste de fichiers 0
    public static ArrayList<ClasseMetriques> readFiles(File[] files) {
		ArrayList<ClasseMetriques> classeMetriques = new ArrayList<ClasseMetriques>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String classeMetriquesInString = "";

            try {
                Scanner reader = new Scanner(file);

                while (reader.hasNextLine()) {
                    String line = reader.nextLine();

                    if (isClass(line)) {
                        //TODO si la ligne est le début d'une classe
                        while (true) {
                            String nextLine = reader.nextLine();
                            Pattern p = Pattern.compile("(^})"); //on regarde pour une braquette en début de ligne signifiant la fin de la classe
                            Matcher m = p.matcher(nextLine);
                            if (m.find()) { //lorsqu'on trouve la fin de la classe, on concatène une dernière ligne puis on break du loop
                                line = line + nextLine;
                                break;
                            }
                            line = line + nextLine; //tant qu'on trouve pas la fin de la classe, on concatène chaque ligne avec la précédente
                        }
//                        System.out.println("================= BEGINNING OF CLASS =================");
//                        System.out.println("" + line);
//                        System.out.println("======================================================");

                        classeMetriquesInString = line;
                        ClasseMetriques nouvelleClasse = new ClasseMetriques(classeMetriquesInString);
                        classeMetriques.add(nouvelleClasse);
                    }
                }
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
        }
		return classeMetriques;

    }

    // Pour vérifier si la ligne est le début d'une classe
    public static boolean isClass(String line) {
        //On vérifie si ça match le mot class entourer de whitespace
        Pattern pattern = Pattern.compile("(^public)(\\s)(class)(\\s)"); //notre pattern recherché est "public" situé en début de ligne suivit d'un espace puis "classe" suivit d'une autre espace
        boolean ifItFinds = pattern.matcher(line).find(); //s'il trouve notre pattern à l'intérieur de la ligne de code
        return ifItFinds;
    }

    public static void main(String[] args) {
        //String folder = "./classesTest/jfree/chart";
        String folder = "E:/Documents/GitHub/QualiteLogiciel/TP1/classesTest/jfree/chart";
        File[] listFiles = getListJavaFiles(folder);

        System.out.println("taille de listFiles = " + listFiles.length);
		ArrayList<ClasseMetriques> classeMetriques = readFiles(listFiles);
        System.out.println("taille de classeMetriques = " + classeMetriques.size());
    }
}	