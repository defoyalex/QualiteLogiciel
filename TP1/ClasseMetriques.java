import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClasseMetriques extends Metriques {
    private MethodeMetriques[] methodesMetriques;
    private double wmc;

    public ClasseMetriques(String path, String classInString) {

        Pattern p = Pattern.compile("(\\w*).java");
        Matcher m = p.matcher(path);
        m.find();
        this.className = m.group().replace(".java", "");
        this.chemin = path.replace(m.group(), "");
        this.countLines(classInString);

    }

    public void countLines(String classInString) {
        String lines[] = classInString.split("\\r?\\n");

        ArrayList<MethodeMetriques> methodeMetriques = new ArrayList<MethodeMetriques>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String isComment = this.isComment(line);

            //Si la ligne n'est pas vide on continue, sinon on fait rien
            if (!line.isEmpty()) {
                if (this.isMethod(line)) {

                    int numberOfBrackets = 0;
                    String methodToString = "";

                    while (true) {
                        String currentLine = lines[i];

                        Pattern patternOpeningBracket = Pattern.compile("(\\u007b)"); //on regarde pour une braquette ouvrante
                        Pattern patternClosingBracket = Pattern.compile("(\\u007d)"); //on regarde pour une braquette fermante
                        Matcher OpeningBracket = patternOpeningBracket.matcher(currentLine);
                        Matcher ClosingBracket = patternClosingBracket.matcher(currentLine);

                        if (ClosingBracket.find()) { //on décrémente par le nombre de braquette fermante sur la ligne
                            numberOfBrackets--;
                        }
                        if (OpeningBracket.find()) { //on incrémente par le nombre de braquette fermante sur la ligne
                            numberOfBrackets++;
                        }
                        if (numberOfBrackets == 0) { //on sait qu'on a la dernière ligne de la méthode lorsque on arrive à zéro
                            //TODO juste etre sur que ca marche
                            MethodeMetriques newMethod = new MethodeMetriques(this.chemin, this.className,methodToString);
                            methodeMetriques.add(newMethod);
                            break;
                        } else {
                            i++;
                            methodToString = methodToString + "\n" + currentLine;
                        }
                    }
                } else {
                    //Switch selon le type de commentaire ou s'il n'y a pas de commentaire
                    switch (isComment) {
                        case "Single line":
                            if (isCodeAndComment(line)) {
                                this.loc++;
                            }
                            cloc++;
                            break;
                        case "Multiple line":
                            if (isCodeAndComment(line)) {
                                this.loc++;
                            }

                            i = countLineComment(lines, i);
                            break;
                        case "No comment":
                            this.loc++;
                            break;
                        default:
                            // code block
                    }
                }
            }
        }
        System.out.println("Ligne de code : " + loc + " Ligne de commentaire :" + cloc + " dans la classe " + this.className);
    }

    public boolean isMethod(String line) {
        Pattern pattern = Pattern.compile("(public|private|protected)(\\s)+.*(\\u0028)(.*)(\\u0029)(\\s)*(\\u007b)");
        //u0028 = "(", u0029 = ")" et u007b = "{"

        boolean ifItFinds = pattern.matcher(line).find(); //s'il trouve notre pattern à l'intérieur de la ligne de code
        return ifItFinds;
    }

    /*Compte la quantité de lignes de commentaires et renvoie la nouvelle position
    dans le tableau de lignes de code après les commentaires.
    */
    public int countLineComment(String[] lines, int i) {

        while (!isEndMultipleLineComment(lines[i])) {
            this.cloc++;

            //Si on atteint la fin tu tableau sans trouver la fin du commentaire
            if (i == lines.length) {

                return i;
            }
            i++;
        }
        this.cloc++;
        return i;
    }

}
