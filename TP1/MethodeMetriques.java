import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodeMetriques extends Metriques {
    private String methodName;
    private String chemin;
    private String className;
    private double cc;

    public MethodeMetriques(String chemin, String className, String methodInString) {

        this.chemin = chemin;
        this.className = className;

        String lines[] = methodInString.split("\\r?\\n");

        Pattern p = Pattern.compile("(\\w*)(\\s)?(\\u0028)");
        Matcher m = p.matcher(lines[1]); //line[0] est une string vide
        m.find();
        this.methodName = m.group().replace("(", "");

        analyseLines(lines);
    }

    private void analyseLines(String[] lines) {

        for (int i = 1; i < lines.length; i++) { //on commence à i=1 puisque i=0 est une string vide
            String line = lines[i];
            String isComment = this.isComment(line);

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
            }
        }
        System.out.println("Ligne de code : " + loc + " Ligne de commentaire :" + cloc + " dans la methode \"" + this.methodName + "\" à l'intérieur de la classe \"" + this.className + "\"");
    }



}