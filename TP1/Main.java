import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    /**
     * Fait un tableau avec tous les fichiers .java à partir du chemin
     *     d'un répertoire
     * @param path
     * @param extension
     * @return une liste de arrays contenant chacun des fichiers analysés sous la forme d'objets "files"
     */
    public static ArrayList<File> getListFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        ArrayList<File> allJavaFiles = new ArrayList<File>(); //Fait un ArrayList de tous les fichiers dans le dossier

        if (!directory.isDirectory()) {  //si c'est pas un dossier et plutôt un fichier
            if (directory.getName().endsWith(extension)) {
                allJavaFiles.add(directory);
                return allJavaFiles;
            }
            else return null;
        }
        //Pour tout les objets File, ajoute à la liste si c'est un .java ou
        //Fais un appel récursif si c'est un dossier, sinon rien.
        for (File file : files) {
            if (file.getName().endsWith(extension)) {
                allJavaFiles.add(file);
            } else if (file.isDirectory()) {
                ArrayList<File> newJavaFiles = getListFiles(path + file.getName() + "/",extension);
                if (newJavaFiles != null) {
                    newJavaFiles.addAll(allJavaFiles);
                    allJavaFiles = newJavaFiles;
                }
            }
        }
        return allJavaFiles;
    }

    /**
     * Analyse chacun des fichiers qui lui sont donnés en arguments pour trouver des déclarations de classes java
     *
     * @param files
     * @return array list contenant les classes découvertes dans le code des fichiers donnés
     */
    public static ArrayList<ClasseMetriques> readFiles(ArrayList<File> files) {
        ArrayList<ClasseMetriques> classeMetriques = new ArrayList<ClasseMetriques>();
        int importLineCounter = 0;

        try {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);

                try {
                    Scanner reader = new Scanner(file);

                    while (reader.hasNextLine()) {
                        String line = reader.nextLine();

                        if (isImportLines(line)) { //si c'est une déclaration de "import ..."
                            importLineCounter++;
                        }

                        if (isClassOrJavadoc(line)) {
                            line = classToString(reader, line);
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
        } catch (NullPointerException e){
            System.out.println("No java file found");
        }

        return classeMetriques;

    }

    /**
     * Prend en entrée la ligne actuellement lu par le reader,
     * détecte le début et la fin des déclarations de classes et
     * transforme le contenu des classes en une longue string.
     *
     * @param reader
     * @param classToString
     * @return totalString qui contient tout le contenu d'une classe sour la forme d'une longue string
     *         séparant chaque ligne de code par un "\n"
     */
    public static String classToString(Scanner reader, String classToString) {
        int numberOfBrackets = 0;
        boolean firstBracketFound = false;
        String currentLine = classToString;
        String totalString = "";


        Pattern patternOpeningBracket = Pattern.compile("(\\u007b)"); //on regarde pour une braquette ouvrante
        Pattern patternClosingBracket = Pattern.compile("(\\u007d)"); //on regarde pour une braquette fermante

        Pattern patternClass = Pattern.compile("((^public\\s+)((final|abstract)\\s+)?(enum|interface|class)(\\s))|(^class\\s)");

        while (!patternClass.matcher(currentLine).find() && reader.hasNextLine()) {
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
                return totalString + currentLine;
            } else {
                totalString += currentLine + "\n";
            }
            currentLine = reader.nextLine();
        }
        return totalString += currentLine;
    }

    /**
     * Vérifie si la ligne de code contient le début d'une classe ou de javadoc
     * @param line
     * @return boolean où true := la ligne contient le début d'une déclaraction de classe ou de javadoc
     */
    public static boolean isClassOrJavadoc(String line) {
        //On vérifie si ça on trouve un pattern signifiant une classe
        Pattern pattern = Pattern.compile("((^public\\s)((final|abstract)\\s)?(enum|interface|class)(\\s))|(^class\\s)"); //notre pattern recherché est "public" situé en début de ligne + les différents types de classes possibles
        Pattern javadoc = Pattern.compile("/\\*\\*"); //pour "**"

        return (pattern.matcher(line).find() || javadoc.matcher(line).find()); //s'il trouve notre pattern à l'intérieur de la ligne de code
    }

    /**
     * Initialise des fichiers CSV qui vont contenir nos métriques.
     * Un fichier pour les classes et un pour les méthodes.
     *
     * @param arrayMetriques
     * @param folder
     */
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

    /**
     * analyse la ligne donnée en entrée et regarde s'il s'agit d'une déclaration de import
     * @param line
     * @return boolean où true := ligne débute avec "import"
     */
    public static boolean isImportLines(String line) {
        Pattern importLinePattern = Pattern.compile("(^import)");
        Matcher importLine = importLinePattern.matcher(line);

        return importLine.find();
    }

    /**
     * Demande à l'usager de donner un chemin vers le dossier ou le fichier à analyser.
     * Un fichier CSV pour les classes et un autre fichier CSV pour les méthodes
     * seront produits avec les résultats de l'analyse.
     *
     * @param args
     */
    public static void main(String[] args) {

        var scan = new Scanner(System.in);
        System.out.println("Veuillez entrer le chemin du dossier ou fichier à analyser.");
        String folder = scan.nextLine();

        ArrayList<File> listFiles = getListFiles(folder,".java");

        ArrayList<ClasseMetriques> classeMetriques = readFiles(listFiles);

        writeCSV(classeMetriques, "./");
    }
}