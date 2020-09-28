import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Metriques{
	private String chemin;
	private String className;
	private int loc;
	private int cloc;
	private int dc;
	private double bc;

	public boolean isComment(String line){
		Pattern singleLine = Pattern.compile("//");
		Pattern multipleLine = Pattern.compile("/*");
		Pattern javadoc = Pattern.compile("/u002A/u002A");

		Matcher singleLineMatcher = singleLine.matcher(line);
		Matcher multipleLineMatcher = multipleLine.matcher(line);
		Matcher javadocMatcher = javadoc.matcher(line);

		boolean isItComment = false;

		if (singleLineMatcher.find() || multipleLineMatcher.find() || javadocMatcher.find()) {
			isItComment = true;
		}
		return isItComment;
	}
	
}