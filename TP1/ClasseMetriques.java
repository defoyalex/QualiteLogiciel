import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClasseMetriques extends Metriques {
	/* Weighted Methods per class 
	   Égale à la somme pondérée de complexité des méthodes*/
    private float wmc; 
	private ArrayList<MethodeMetriques> methodeMetriquesList;
	private boolean isClassJavadoc;

    public ClasseMetriques(String path, String classInString, int numberImportLines) {

		this.loc = numberImportLines;
        Pattern p = Pattern.compile("(\\w*).java");
        Matcher m = p.matcher(path);
        m.find();
        this.className = m.group().replace(".java", "");
        this.chemin = path.replace(m.group(), "");
        countLines(classInString);
		

		this.addMethodLines();
		this.dc = (double)this.cloc/(double)this.loc;
		this.calculateWMC();
		this.bc = this.dc/this.wmc;

    }
	
	//Calcul la métrique Weighted Method per Class
	public void calculateWMC(){
		float sommePondérée = 0;
		for(MethodeMetriques methodeMetrique: this.methodeMetriquesList){
			sommePondérée+=methodeMetrique.getComplexity();
		}
		this.wmc = sommePondérée;
	}
	
	public void addMethodLines(){
		for(MethodeMetriques methodeMetrique: this.methodeMetriquesList){
			this.loc += methodeMetrique.getLOC();
			this.cloc += methodeMetrique.getCLOC();
		}
	}

	//Compte LOC, CLOC et trouve les méthodes
    public void countLines(String classInString) {
        String lines[] = classInString.split("\\r?\\n");
		int javadocLineCount = 0;
		this.isClassJavadoc = lines[0].contains("/**");

        ArrayList<MethodeMetriques> methodeMetriquesList = new ArrayList<MethodeMetriques>();


        for (int i = 0; i < lines.length; i++) {
			
            String line = lines[i];
			
			if(isJavadocMultipleLineComment(line) && !isClassJavadoc){
				while(true){
					javadocLineCount++;
					if(isEndMultipleLineComment(lines[i])){
						i++;
						line = lines[i];
						break;
					}
					i++;
				}
			}
			
			
            String isComment = this.isComment(line);

			if (this.isMethod(lines, i)) {
				boolean firstBracketFound = false;

				int numberOfBrackets = 0;
				String methodToString = "";
				
				Pattern patternOpeningBracket = Pattern.compile("(\\u007b)"); //on regarde pour une braquette ouvrante
				Pattern patternClosingBracket = Pattern.compile("(\\u007d)"); //on regarde pour une braquette fermante

				while (i < lines.length) {
					String currentLine = lines[i];

					
					Matcher OpeningBracket = patternOpeningBracket.matcher(currentLine);
					Matcher ClosingBracket = patternClosingBracket.matcher(currentLine);

					if (ClosingBracket.find()) { //on décrémente par le nombre de braquette fermante sur la ligne
						numberOfBrackets--;
					}
					if (OpeningBracket.find()) { //on incrémente par le nombre de braquette fermante sur la ligne
						numberOfBrackets++;
						firstBracketFound = true;
					}
					if (numberOfBrackets == 0 && firstBracketFound) { //on sait qu'on a la dernière ligne de la méthode lorsque on arrive à zéro
						methodToString = methodToString + "\n" + currentLine;
						MethodeMetriques newMethod = new MethodeMetriques(this.chemin, this.className, methodToString, javadocLineCount);
						methodeMetriquesList.add(newMethod);

						break;
					} else {
						i++;
						methodToString = methodToString + "\n" + currentLine;
					}
				}
				javadocLineCount = 0;
			} else {
				//Si le JavaDoc est pour un attribut, on remet le compte à zéro
				if(line.contains(";")){
					this.cloc += javadocLineCount;
					this.loc += javadocLineCount;
					javadocLineCount = 0;
				}
				
				//Switch selon le type de commentaire ou s'il n'y a pas de commentaire
				switch (isComment) {
					case "Single line":
						this.cloc++;
						this.loc++;
						break;
					case "Multiple line":
						i = countLineComment(lines, i);
						break;
					case "Javadoc":
						if(isClassJavadoc){
							i = countLineComment(lines, i);
							this.isClassJavadoc = false;
						}
						break;
					case "No comment":
						if (!isEmptyLine(line)) {
							this.loc++;
						}
						break;
				}
				
			}
        }
		
		this.methodeMetriquesList = methodeMetriquesList;
    }

    public boolean isMethod(String[] lines, int i) {
        Pattern patternStart = Pattern.compile("(public|private|protected)(\\s)+(\\w*\\s)?(\\S*\\s)?[\\w]*\\s?(\\u0028)(.*)");
		//Pattern patternTotal = Pattern.compile("(public|private|protected)(\\s)+(\\w*\\s)?[\\w]*(\\u0028)(.*)(\\u0029)(\\s)*(\\u007b)");
        //u0028 = "(", u0029 = ")" et u007b = "{"

        return patternStart.matcher(lines[i]).find(); //s'il trouve notre pattern à l'intérieur de la ligne de code
    }
	
	public void writeCSV(String pathClass, String pathMethod){

		String csv = this.chemin+","+this.className+","+this.loc+","+
					 this.cloc+","+this.dc+","+this.wmc+","+this.bc+"\n";
		
		try (FileWriter f = new FileWriter(pathClass, true); 
			 BufferedWriter b = new BufferedWriter(f); 
			 PrintWriter p = new PrintWriter(b);) { 
			 
			 p.println(csv);  
			 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		
		for(MethodeMetriques methodeMetriques: this.methodeMetriquesList){
			methodeMetriques.writeCSV(pathMethod);
		}

	}

}
