import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Classe pour les classes trouvées dans le code analysé.
 * Contient les calculs pour les métriques propres aux classes.
 */
public class ClasseMetriques extends Metriques {

	/**
	 * Weighted Methods per Class (WMC) égale à la somme pondérée de complexité des méthodes
	 */
    private float wmc;
	private ArrayList<MethodeMetriques> methodeMetriquesList; //liste des méthodes détectées dans le code de la classe
	private boolean isClassJavadoc;

	/**
	 * Constructor pour instancier les classes que l'on détecte dans le code des fichiers java
	 *
	 * @param path
	 * @param classInString
	 * @param numberImportLines
	 */
    public ClasseMetriques(String path, String classInString, int numberImportLines) {

		this.loc = numberImportLines;
        Pattern p = Pattern.compile("[\\w-]*\\.java");
        Matcher m = p.matcher(path);
        m.find();
        this.className = m.group().replace(".java", "");
        this.chemin = path.replace(m.group(), "");
        countLines(classInString);
		

		this.addMethodLines(); //Ajoute le loc et cloc des méthodes
		this.dc = (double)this.cloc/(double)this.loc;
		this.calculateWMC();
		this.bc = this.dc/this.wmc;

    }

	/**
	 * @return liste des méthodes contenues dans la classe actuellement analysée
	 */
    public ArrayList<MethodeMetriques> getMethodeMetriquesList(){
    	return this.methodeMetriquesList;
	}

	/**
	 * Calcule la métrique Weighted Method per Class
	 */
	public void calculateWMC(){
		float sommePondérée = 0;
		for(MethodeMetriques methodeMetrique: this.methodeMetriquesList){
			sommePondérée+=methodeMetrique.getComplexity();
		}
		this.wmc = sommePondérée;
	}

	/**
	 * Données en entrée sont la liste des méthodes déclarées dans la classe actuelle.
	 * Cette fonction ajoute le contenu des méthodes aux métriques loc et cloc de la classe.
	 */
	public void addMethodLines(){
		for(MethodeMetriques methodeMetrique: this.methodeMetriquesList){
			this.loc += methodeMetrique.getLOC();
			this.cloc += methodeMetrique.getCLOC();
		}
	}

	/**
	 * Prend en entrée la classe sous la forme d'une longue string.
	 * Analyse le contenu de la string pour détecter la déclaration de méthodes
	 * et compte les LOC et CLOC lorsque l'on est pas à l'intérieur d'une déclaration de méthode.
	 *
	 * @param classInString
	 */
    public void countLines(String classInString) {
        String lines[] = classInString.split("\\r?\\n");
		int javadocLineCount = 0;
		this.isClassJavadoc = lines[0].contains("/**")||lines[1].contains("/**");

        ArrayList<MethodeMetriques> methodeMetriquesList = new ArrayList<MethodeMetriques>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            //Incrémente le compteur de Javadocline au cas où il appartienne
			//à une méthode
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

				//On incrémente/décrémente le nombre de bracket jusqu'à obtenir 0, ce qui indique
				//la fin de la méthode
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
					//on sait qu'on a la dernière ligne de la méthode lorsque on arrive à zéro
					if (numberOfBrackets == 0 && firstBracketFound) {
						methodToString = methodToString + "\n" + currentLine;
						MethodeMetriques newMethod = new MethodeMetriques(this.chemin, this.className,
								methodToString, javadocLineCount);
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

	/**
	 * @param lines
	 * @param i
	 * @return boolean où true := si cette ligne contient le début d'une déclaration de méthode
	 */
    public boolean isMethod(String[] lines, int i) {
        Pattern patternStart = Pattern.compile("(public|private|protected)(\\s)+(\\w*\\s)?(\\S*\\s)?[\\w]*\\s?(\\u0028)(.*)");
		Pattern patternCodeLine = Pattern.compile(";");
		String methodStart = lines[i];

		/*Tant qu'on n'arrive pas à la fin de la méthode ou qu'on ne trouve pas
		  une ligne de code, on concatène les lignes pour vérifier si c'est une méthode
		  déclarées sur plusieurs lignes */
		if(patternStart.matcher(lines[i]).find()){
			while(i<lines.length && !patternCodeLine.matcher(lines[i]).find()){
				if(patternStart.matcher(methodStart).find()){
					return true;
				}
				i++;
				methodStart += lines[i];
			}
		}

        return false; //s'il trouve notre pattern à l'intérieur de la ligne de code
    }

	/**
	 * Update le fichier CSV pour les classes en y mettant les métriques de la classe actuellement analysée
	 *
	 * @param pathClass
	 * @param pathMethod
	 */
	public void writeCSV(String pathClass, String pathMethod){

		String csv = this.chemin+","+this.className+","+this.loc+","+
					 this.cloc+","+this.dc+","+this.wmc+","+this.bc;
		
		try (FileWriter f = new FileWriter(pathClass, true); 
			 BufferedWriter b = new BufferedWriter(f); 
			 PrintWriter p = new PrintWriter(b);) { 
			 
			 p.println(csv);  
			 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}

		//On écrit chacune des méthodes de l'instantation dans le fichier au chemin
		// pathMethod
		for(MethodeMetriques methodeMetriques: this.methodeMetriquesList){
			methodeMetriques.writeCSV(pathMethod);
		}

	}

}
