import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodeMetriques extends Metriques {
    private String methodName;
    private String chemin;
    private String className;
    private int cc;

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
		//Pour calculer la complexité cyclomatique
		//Égal au nombre de (for, if, while, etc.) + 1
		boolean atLeastOneCase = false;
		int countPredicate = 0;

        for (int i = 1; i < lines.length; i++) { //on commence à i=1 puisque i=0 est une string vide
            String line = lines[i];
            String isComment = this.isComment(line);

            switch (isComment) {
                case "Single line":
                    if (isCodeAndComment(line)) {
						//Sépare le commentaire de la ligne de code
						String[] code = line.split("//");
						//On ajoute +1 au compte s'il y a un prédicat ou un case
						if(containsCase(code[0])){
							atLeastOneCase = true;
							countPredicate++;
						}
						if(containsPredicate(code[0])){
							countPredicate++;
						}
                        this.loc++;
                    }
                    cloc++;
                    break;
                case "Multiple line":
                    if (isCodeAndComment(line)) {
                        String[] code = line.split("//");
						if(containsCase(code[0])){
							atLeastOneCase = true;
							countPredicate++;
						}
						if(containsPredicate(code[0])){
							countPredicate++;
						}
                        this.loc++;
                    }
                    i = countLineComment(lines, i);
                    break;
                case "No comment":            
					//Si la ligne est vide, on fait rien
                    if(!isEmptyLine(line)){
						if(containsCase(line)){
							atLeastOneCase = true;
							countPredicate++;
						}
						if(containsPredicate(line)){
							countPredicate++;
						}
						this.loc++;
					}
                    break;
            }
        }
		
		//S'il y a au moins un case, on ajoute pas +1 au compte de prédicat
		if(!atLeastOneCase){
			countPredicate++;
		}
		this.cc = countPredicate;
        System.out.println("Ligne de code : " + loc + " Ligne de commentaire :" + cloc + " dans la methode \"" + this.methodName + "\" à l'intérieur de la classe \"" + this.className + "\"");
		System.out.println("Complexité cyclomatique :" + this.cc);
    }
	
	//Vérifie si la ligne contient un case ou un default (prédicat switch)
	public boolean containsCase(String line){
		Pattern casePattern = Pattern.compile("\\s(case|default)[:\\s]");
		Matcher caseMatcher = casePattern.matcher(line);

		if (caseMatcher.find()){
			return true;
		}
		return false;
	}
	
	//Vérifie si la ligne contient un prédicat if, while, for, etc.
	public boolean containsPredicate(String line){
		//u0028 = "(", u007b = "{"
		Pattern predicate = Pattern.compile("\\s(if|for|while)[\\u0028\\s\\u007b]"); 
		Matcher predicateMatcher = predicate.matcher(line);

		if (predicateMatcher.find()){
			return true;
		}
		return false;
	}



}