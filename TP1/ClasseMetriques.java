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
	private ArrayList<MethodeMetriques> methodeMetriques;

    public ClasseMetriques(String path, String classInString, int numberJavadocLines) {

		this.cloc = numberJavadocLines;
        Pattern p = Pattern.compile("(\\w*).java");
        Matcher m = p.matcher(path);
        m.find();
        this.className = m.group().replace(".java", "");
        this.chemin = path.replace(m.group(), "");
        countLines(classInString);

		
		this.dc = this.cloc/this.loc;
		this.calculateWMC();
		this.bc = this.dc/this.wmc;

    }
	
	//Calcul la métrique Weighted Method per Class
	public void calculateWMC(){
		float sommePondérée = 0;
		for(MethodeMetriques methodeMetrique: this.methodeMetriques){
			sommePondérée+=methodeMetrique.getComplexity();
		}
		this.wmc = sommePondérée/this.methodeMetriques.size();
	}

	//Compte LOC, CLOC et trouve les méthodes
    public void countLines(String classInString) {
        String lines[] = classInString.split("\\r?\\n");

        ArrayList<MethodeMetriques> methodeMetriques = new ArrayList<MethodeMetriques>();

        var metriques = new Metriques(); //utilisé pour compter les lignes de javadoc précédant déclaration de méthodes

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String isComment = this.isComment(line);

			metriques.javadocLineCounter(line);

			if (this.isMethod(line)) {

				int numberOfBrackets = 0;
				String methodToString = "";

				while (true) {
					String currentLine = lines[i];

					Pattern patternOpeningBracket = Pattern.compile("(\\u007b)"); //on regarde pour une braquette ouvrante
					Pattern patternClosingBracket = Pattern.compile("(\\u007d)"); //on regarde pour une braquette fermante
					Matcher OpeningBracket = patternOpeningBracket.matcher(currentLine);
					Matcher ClosingBracket = patternClosingBracket.matcher(currentLine);

					if (ClosingBracket.find()) { //on décrémente par le nombre de braquette fermante sur la ligne
						numberOfBrackets--;
					}
					if (OpeningBracket.find()) { //on incrémente par le nombre de braquette fermante sur la ligne
						numberOfBrackets++;
					}
					if (numberOfBrackets == 0) { //on sait qu'on a la dernière ligne de la méthode lorsque on arrive à zéro
						methodToString = methodToString + "\n" + currentLine;
						int currentJavadocLinesNumber = metriques.getJavadocLineCounter();
						metriques.resetJavadocLineCounter();
						MethodeMetriques newMethod = new MethodeMetriques(this.chemin, this.className, methodToString, currentJavadocLinesNumber);
						methodeMetriques.add(newMethod);

						break;
					} else {
						i++;
						methodToString = methodToString + "\n" + currentLine;
					}
				}
			} else {
				//Switch selon le type de commentaire ou s'il n'y a pas de commentaire
				switch (isComment) {
					case "Single line":
						if (isCodeAndComment(line)) {
							this.loc++;
							//System.out.println(line);
						}
						cloc++;
						break;
					case "Multiple line":
						if (isCodeAndComment(line)) {
							this.loc++;
							//System.out.println(line);
						}
						int indexBeforeCountChange = i; //on garde une copie de i avant qu'il change
						i = countLineComment(lines, i);
						int difference = i - indexBeforeCountChange - 1; //une fois qu'on a calculer le nombre de lignes de commentaires de javadoc
						metriques.increaseJavadocLineCounter(difference); //on l'ajoute à notre compteur
						break;
					case "No comment":
					    //Si la ligne est vide, on fait rien
						if (!isEmptyLine(line)) {
							this.loc++;
							//System.out.println(line);
						}
						break;
				}
			}
     			
        }
		this.methodeMetriques = methodeMetriques;
    }

    public boolean isMethod(String line) {
        Pattern pattern = Pattern.compile("(public|private|protected)(\\s)+.*(\\u0028)(.*)(\\u0029)(\\s)*(\\u007b)");
        //u0028 = "(", u0029 = ")" et u007b = "{"

        return pattern.matcher(line).find(); //s'il trouve notre pattern à l'intérieur de la ligne de code
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
		
		for(MethodeMetriques methodeMetriques: this.methodeMetriques){
			methodeMetriques.writeCSV(pathMethod);
		}

	}

}
