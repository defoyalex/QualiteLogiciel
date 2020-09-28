import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClasseMetriques extends Metriques{
	private MethodeMetriques[] methodesMetriques;
	private double wmc;
	
	public ClasseMetriques(String path, String classInString){

		Pattern p = Pattern.compile("(\\w*).java");
        Matcher m = p.matcher(path);
		m.find();
        this.className = m.group().replace(".java","");
		this.chemin = path.replace(m.group(),"");
		this.countLines(classInString);

	}
	
	public void countLines(String classInString){
		String lines[] = classInString.split("\\r?\\n");
		
	
		for(int i=0; i<lines.length; i++){
			String line = lines[i];
			String isComment = this.isComment(line);
			
			//Si la ligne n'est pas vide on continue, sinon on fait rien
			if(!line.isEmpty()){
				if(this.isMethod(line)){
					//TODO line is the start of a method
				}else{		
					
					switch(isComment) {
						case "Single line":
							this.singleLineComment(line);
							break;
						case "Multiple line":
							this.multipleLineComment(line);
							break;
						case "No comment":
							this.loc++;
							break;
						default:
							// code block
					}
				}
			}
		}
	}
	
	public boolean isMethod(String line){
		return false; //TODO
		
	}
	
	public boolean singleLineComment(String line){
		return false; //TODO
		
	}
	
	public boolean multipleLineComment(String line){
		return false; //TODO
		
	}

}
