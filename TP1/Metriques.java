import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Metriques{
	protected String chemin;
	protected String className;
	protected int loc;
	protected int cloc;
	protected int dc;
	protected double bc;

	public String isComment(String line){


		if (isSingleLineComment(line)){
			return "Single line";
		}
		
		if(		isStartMultipleLineComment(line) ||
				isEndMultipleLineComment(line) ||
				isJavadocMultipleLineComment(line)) {
			return "Multiple line";
		}
		return "No comment";
	}
	
	//Vérifie si la ligne contient un commentaire d'une ligne
	public boolean isSingleLineComment(String line){
		Pattern singleLine = Pattern.compile("//");
		Matcher singleLineMatcher = singleLine.matcher(line);
		if (singleLineMatcher.find()){
			return true;
		}
		return false;
	}
	
	//Vérifie si la ligne contient le début d'un commentaire de plusieurs lignes
	public boolean isStartMultipleLineComment(String line){
		Pattern multipleLineBeginning = Pattern.compile("/\\*");  //pour "/*"
		Matcher multipleLineBeginningMatcher = multipleLineBeginning.matcher(line);
		if (multipleLineBeginningMatcher.find()){
			return true;
		}
		return false;
	}
	
	//Vérifie si la ligne contient la fin d'un commentaire de plusieurs lignes
	public boolean isEndMultipleLineComment(String line){
		Pattern multipleLineEnding = Pattern.compile("\\*/"); //pour "*/"
		Matcher multipleLineEndingMatcher = multipleLineEnding.matcher(line);
		if (multipleLineEndingMatcher.find()){
			return true;
		}
		return false;
	}
	
	//Vérifie si la ligne contient la suite d'un commentaire JavaDoc
	public boolean isJavadocMultipleLineComment(String line){
		Pattern javadoc = Pattern.compile("\\*\\*"); //pour "**"
		Matcher javadocMatcher = javadoc.matcher(line);
		if (javadocMatcher.find()){
			return true;
		}
		return false;
	}
	
	//Vérifie s'il y a une ligne de code avant un commentaire
	public boolean isCodeAndComment(String line){
		Pattern singleLine = Pattern.compile("\\S+\\s*//");
		Matcher singleLineMatcher = singleLine.matcher(line);
		
		Pattern multipleLine = Pattern.compile("\\S+\\s*/\\*");
		Matcher multipleLineMatcher = singleLine.matcher(line);
		if (singleLineMatcher.find() || multipleLineMatcher.find()){
			return true;
		}
		return false;
	}

	/*Compte la quantité de lignes de commentaires et renvoie la nouvelle position
    dans le tableau de lignes de code après les commentaires.*/
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