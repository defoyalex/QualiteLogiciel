import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClasseMetriques extends Metriques{
	private MethodeMetriques[] methodesMetriques;
	private double wmc;
	
	public ClasseMetriques(String path, String classInString){
		String lines[] = classInString.split("\\r?\\n");
		Pattern p = Pattern.compile("(\\w*).java");
        Matcher m = p.matcher(path);
		m.find();
        this.className = m.group().replace(".java","");
		this.chemin = path.replace(m.group(),"");
		System.out.println(this.chemin);
		System.out.println(this.className);
		System.out.println(lines.length);
		for(int i=0; i<lines.length; i++){
			String line = lines[i];
			
		}
	}

}
