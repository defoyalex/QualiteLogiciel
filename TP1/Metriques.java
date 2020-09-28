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

	public boolean isComment(String line){
		Pattern singleLine = Pattern.compile("//");
		Pattern multipleLineBeginning = Pattern.compile("/\\*");  //pour "/*"
		Pattern multipleLineEnding = Pattern.compile("\\*/"); //pour "*/"
		Pattern javadoc = Pattern.compile("\\*\\*"); //pour "**"

		Matcher singleLineMatcher = singleLine.matcher(line);
		Matcher multipleLineBeginningMatcher = multipleLineBeginning.matcher(line);
		Matcher multipleLineEndingMatcher = multipleLineEnding.matcher(line);
		Matcher javadocMatcher = javadoc.matcher(line);

		boolean isItComment = false;

		if (singleLineMatcher.find() ||
				multipleLineBeginningMatcher.find() ||
				multipleLineEndingMatcher.find() ||
				javadocMatcher.find()) {
			isItComment = true;
		}
		return isItComment;
	}
}