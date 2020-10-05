import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    /* Fait un tableau avec tous les fichiers .java à partir du chemin
    d'un répertoire */
    public static ArrayList<File> getListJavaFiles(String path) {
        File directory = new File(path);

        if (!directory.isDirectory()) {  //si c'est pas un dossier et plutôt un fichier
            return null;
        }

        //Fais un ArrayList de tous les fichiers dans le dossier
        File[] files = directory.listFiles();
        ArrayList<File> allJavaFiles = new ArrayList<File>();

        //Pour tout les objets File, ajoute à la liste si c'est un .java ou
        //Fais un appel récursif si c'est un dossier, sinon rien.
        for (File file : files) {
            if (file.getName().endsWith(".java")) {
                allJavaFiles.add(file);
            } else if (file.isDirectory()) {
                ArrayList<File> newJavaFiles = getListJavaFiles(path + file.getName() + "/");
                if (newJavaFiles != null) {
                    newJavaFiles.addAll(allJavaFiles);
                    //allJavaFiles = newJavaFiles; //DÉCOMMENTER SI ON VEUT PASSER TOUS LES DOSSIERS RÉCURSIVEMENT
                }
            }
        }

        return allJavaFiles;
    }

    // Lis une liste de fichiers 0
    public static ArrayList<ClasseMetriques> readFiles(ArrayList<File> files) {
        ArrayList<ClasseMetriques> classeMetriques = new ArrayList<ClasseMetriques>();
        int importLineCounter = 0;
        var metriques = new Metriques(); //utilisé pour compter les lignes de javadoc précédant déclaration de classes

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);

            try {
                Scanner reader = new Scanner(file);

                while (reader.hasNextLine()) {
                    String line = reader.nextLine();

                    metriques.javadocLineCounter(line);

                    if (metriques.isImportLines(line)) { //si c'est une déclaration de "import ..."
                        importLineCounter++;
                    }

                    if (isClass(line)) {
                        //TODO si la ligne est le début d'une classe
                        while (true) {
                            String nextLine = reader.nextLine();
                            Pattern p = Pattern.compile("(^})"); //on regarde pour une braquette en début de ligne signifiant la fin de la classe
                            Matcher m = p.matcher(nextLine);
                            if (m.find()) { //lorsqu'on trouve la fin de la classe, on concatène une dernière ligne puis on break du loop
                                line = line + "\n" + nextLine;
                                break;
                            }
                            line = line + "\n" + nextLine; //si on trouve pas la fin de la classe, on concatène chaque ligne avec la précédente
                        }
                        int currentJavadocLinesNumber = metriques.getJavadocLineCounter();
                        metriques.resetJavadocLineCounter(); //on réinitialise le compteur de ligne de javadoc
                        ClasseMetriques nouvelleClasse = new ClasseMetriques(files.get(i).toString(), line, currentJavadocLinesNumber + importLineCounter);
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
        //On vérifie si ça on trouve un pattern signifiant une classe
        Pattern pattern = Pattern.compile("(^public)(\\s)((final(\\s)class)|(abstract(\\s)class)|enum|interface|class)(\\s)"); //notre pattern recherché est "public" situé en début de ligne + les différents types de classes possibles
        boolean ifItFinds = pattern.matcher(line).find(); //s'il trouve notre pattern à l'intérieur de la ligne de code
        return ifItFinds;
    }

    public static void writeCSV(ArrayList<ClasseMetriques> arrayMetriques, String folder) {
        File csvClass = new File(folder + "classes.csv");
        File csvMethod = new File(folder + "methodes.csv");

        try {
            FileWriter fwClass = new FileWriter(csvClass);
            fwClass.write("chemin,class,classe_LOC,classe_CLOC,classe_DC,classe_WMC,classe_BC\n");
            fwClass.close();


            FileWriter fwMethod = new FileWriter(csvMethod);
            fwMethod.write("chemin,class,methode,methode_LOC,methode_CLOC,methode_DC,methode_CC,methode_BC\n");
            fwMethod.close();

            for (ClasseMetriques classeMetriques : arrayMetriques) {
                classeMetriques.writeCSV(folder + "classes.csv", folder + "methodes.csv");
            }


        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //String folder = "./classesTest/jfree/chart";
        String folder = "E:/Documents/GitHub/QualiteLogiciel/TP1/";
        //String folder = "E:/Documents/GitHub/QualiteLogiciel/TP1/classesTest/jfree/";
        //String folder = "E:/Documents/GitHub/QualiteLogiciel/TP1/classesTest/jfree/chart/TESTING";
        ArrayList<File> listFiles = getListJavaFiles(folder);

        ArrayList<ClasseMetriques> classeMetriques = readFiles(listFiles);

        writeCSV(classeMetriques, folder);
    }
}	