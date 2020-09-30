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
        //TODO
        this.chemin = chemin;
        this.className = className;
        analyseLines(methodInString);
    }

    private void analyseLines(String methodInString) {
        String lines[] = methodInString.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
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
    }



}