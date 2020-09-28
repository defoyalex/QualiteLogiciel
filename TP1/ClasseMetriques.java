import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClasseMetriques extends Metriques{
	private MethodeMetriques[] methodesMetriques;
	private double wmc;
	
	public ClasseMetriques(String path, String classInString){
		String lines[] = classInString.split("\\r?\\n");
		System.out.println(lines.length);
		for(int i=0; i<lines.length; i++){
			String line = lines[i];
			
		}
	}

}
