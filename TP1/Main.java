import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    /* Fait un tableau avec tous les fichiers .java à partir du chemin
    d'un répertoire */
    public static ArrayList<File> getListJavaFiles(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        ArrayList<File> allJavaFiles = new ArrayList<File>(); //Fait un ArrayList de tous les fichiers dans le dossier

        if (!directory.isDirectory()) {  //si c'est pas un dossier et plutôt un fichier
            if (directory.getName().endsWith(".java")) {
                allJavaFiles.add(directory);
                return allJavaFiles;
            }
        }
        //Pour tout les objets File, ajoute à la liste si c'est un .java ou
        //Fais un appel récursif si c'est un dossier, sinon rien.
        for (File file : files) {
            if (file.getName().endsWith(".java")) {
                allJavaFiles.add(file);
            } else if (file.isDirectory()) {
                ArrayList<File> newJavaFiles = getListJavaFiles(path + file.getName() + "/");
                if (newJavaFiles != null) {
                    newJavaFiles.addAll(allJavaFiles);
                    allJavaFiles = newJavaFiles;
                }
            }
        }
        return allJavaFiles;
    }

    // Lis une liste de fichiers 0
    public static ArrayList<ClasseMetriques> readFiles(ArrayList<File> files) {
        ArrayList<ClasseMetriques> classeMetriques = new ArrayList<ClasseMetriques>();
        int importLineCounter = 0;
        System.out.println(files);

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            System.out.println(file);

            try {
                Scanner reader = new Scanner(file);

                while (reader.hasNextLine()) {
                    String line = reader.nextLine();


                    if (isImportLines(line)) { //si c'est une déclaration de "import ..."
                        importLineCounter++;
                    }

                    if (isClassOrJavadoc(line)) {
                        line = findEndClass(reader, line);
                        ClasseMetriques nouvelleClasse = new ClasseMetriques(files.get(i).toString(), line, importLineCounter);
                        classeMetriques.add(nouvelleClasse);
                    }
                }
                importLineCounter = 0;
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
        }
        return classeMetriques;

    }

    //Renvoie la classe en String
    public static String findEndClass(Scanner reader, String classToString) {
        int numberOfBrackets = 0;
        boolean firstBracketFound = false;
        String currentLine = classToString;
        String totalString = "";


        Pattern patternOpeningBracket = Pattern.compile("(\\u007b)"); //on regarde pour une braquette ouvrante
        Pattern patternClosingBracket = Pattern.compile("(\\u007d)"); //on regarde pour une braquette fermante

        Pattern patternClass = Pattern.compile("(^public)(\\s)((final(\\s)class)|(abstract(\\s)class)|enum|interface|class)(\\s)");

        while (!patternClass.matcher(currentLine).find() && reader.hasNextLine()) {
            System.out.println(currentLine);
            totalString += currentLine + "\n";
            currentLine = reader.nextLine();
        }


        while (reader.hasNextLine()) {

            Matcher OpeningBracket = patternOpeningBracket.matcher(currentLine);
            Matcher ClosingBracket = patternClosingBracket.matcher(currentLine);

            if (ClosingBracket.find()) { //on décrémente par le nombre de braquette fermante sur la ligne
                numberOfBrackets--;
            }
            if (OpeningBracket.find()) { //on incrémente par le nombre de braquette fermante sur la ligne
                numberOfBrackets++;
                firstBracketFound = true;
            }
            if (numberOfBrackets == 0 && firstBracketFound) { //on sait qu'on a la dernière ligne de la méthode lorsque on arrive à zéro
                System.out.println(totalString + currentLine);
                return totalString + currentLine;
            } else {
                totalString += currentLine + "\n";
            }
            currentLine = reader.nextLine();
        }
        return totalString;
    }

    // Pour vérifier si la ligne est le début d'une classe
    public static boolean isClassOrJavadoc(String line) {
        //On vérifie si ça on trouve un pattern signifiant une classe
        Pattern pattern = Pattern.compile("(^public)(\\s)((final(\\s)class)|(abstract(\\s)class)|enum|interface|class)(\\s)"); //notre pattern recherché est "public" situé en début de ligne + les différents types de classes possibles
        Pattern javadoc = Pattern.compile("/\\*\\*"); //pour "**"

        return (pattern.matcher(line).find() || javadoc.matcher(line).find()); //s'il trouve notre pattern à l'intérieur de la ligne de code

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

    public static boolean isImportLines(String line) {
        Pattern importLinePattern = Pattern.compile("(^import)");
        Matcher importLine = importLinePattern.matcher(line);

        return importLine.find();
    }

    public static void main(String[] args) {
        //String folder = "./classesTest/jfree/chart";
        String folder = "E:/Downloads/jfree/chart/ChartColor.java";
        //String folder = "E:/Documents/GitHub/QualiteLogiciel/TP1/classesTest/jfree/";

//        var scan = new Scanner(System.in);
//        System.out.println("Veuillez entrer le chemin du dossier ou fichier à analyser.");
//        String folder = scan.nextLine();

        ArrayList<File> listFiles = getListJavaFiles(folder);

        ArrayList<ClasseMetriques> classeMetriques = readFiles(listFiles);

        writeCSV(classeMetriques, "./");
    }
}