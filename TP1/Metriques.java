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
		Pattern singleLine = Pattern.compile("//");
		Pattern multipleLineBeginning = Pattern.compile("/\\*");  //pour "/*"
		Pattern multipleLineEnding = Pattern.compile("\\*/"); //pour "*/"
		Pattern javadoc = Pattern.compile("\\*\\*"); //pour "**"

		Matcher singleLineMatcher = singleLine.matcher(line);
		Matcher multipleLineBeginningMatcher = multipleLineBeginning.matcher(line);
		Matcher multipleLineEndingMatcher = multipleLineEnding.matcher(line);
		Matcher javadocMatcher = javadoc.matcher(line);


		if (singleLineMatcher.find()){
			return "Single line";
		}
		
		if(		multipleLineBeginningMatcher.find() ||
				multipleLineEndingMatcher.find() ||
				javadocMatcher.find()) {
			return "Multiple line";
		}
		return "No comment";
	}
}