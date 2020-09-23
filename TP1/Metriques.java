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

	public boolean isComment(String ligne){
		Pattern singleLine = Pattern.compile("//");
		Pattern multipleLine = Pattern.compile("/*");
		Pattern javadoc = Pattern.compile("/u002A/u002A");
		
		//if pattern match do.....
		
		return false; //TODO
	}
	
}