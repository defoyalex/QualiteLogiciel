import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodeMetriques extends Metriques {
    private String methodName;
    private int cc; //Complexité cyclomatique

    public MethodeMetriques(String chemin, String className, String methodInString, int javadocCount) {
		System.out.println(javadocCount);
		System.out.println("MEETHOOODE"+ " in " + className +methodInString);

        this.chemin = chemin;
        this.className = className;
		this.loc = javadocCount;
		this.cloc = javadocCount;


		String lines[] = methodInString.split("\\r?\\n");

		this.methodName = findMethodName(lines);

        analyseLines(lines);
		this.dc = (double)this.cloc/(double)this.loc;

		this.bc = this.dc/this.cc;

    }

	//Renvoie le nom de la méthode et ave le type des arguments en attribut
	public String findMethodName(String[] lines){
		//Sépare le string par les retours de ligne

		String nomMethod = lines[1];

		//Pour les cas où la méthode est sur plusieurs lignes
		//Ne fonctionnera pas s'il y a une parenthèse dans un commentaire
		int j=2;
		while(!nomMethod.contains(")")){

			nomMethod+=lines[j];
			j++;

		}
		System.out.println("Méthode" + nomMethod);


		//Trouver le pattern qui correspond au nom de méthode
        Pattern p = Pattern.compile("(\\w*)(\\s)?(\\u0028)[\\w\\s\\[\\]\\<\\>\\?,\\.]*(\\u0029)");
        Matcher m = p.matcher(nomMethod); //line[0] est une string vide
        m.find();
		//Remplace les parenthèses par des espaces
        String methodName = m.group().replace("(", ",");
		methodName = methodName.replace(")", ",");
	
		
		//Sépare tous les mots par les espaces
		String[] methodArray = methodName.split(",");
		
		for(int i=0; i<methodArray.length; i++){
			System.out.println(i + methodArray[i]);
		}
		

		//Le premier mot du tableau est le nom de la méthode
		//Chaque index impair suivant est le type d'un attribut
		methodName = methodArray[0];

		for(int i=1; i<methodArray.length; i++){
			String[] attributes = methodArray[i].split(" ");
			for(int k=0; k<attributes.length; k++){
				if(!isEmptyLine(attributes[k])){
					methodName += "_" + attributes[k];
					k=attributes.length;
				}
			}
		}

		return methodName;
	}

	public int getComplexity(){
		return this.cc;
	}
	
	public int getLOC(){
		return this.loc;
	}
	
	public int getCLOC(){
		return this.cloc;
	}

    private void analyseLines(String[] lines) {
		//Pour calculer la complexité cyclomatique
		//Égal au nombre de (for, if, while, etc.) + 1
		int countPredicate = 1;

        for (int i = 1; i < lines.length; i++) { //on commence à i=1 puisque i=0 est une string vide
            String line = lines[i];
            String isComment = this.isComment(line);

            switch (isComment) {
                case "Single line":
                    if (isCodeAndComment(line)) {
						//Sépare le commentaire de la ligne de code
						String[] code = line.split("//");
						//On ajoute +1 au compte s'il y a un prédicat ou un case
						if(containsPredicate(code[0])){
							countPredicate++;
						}
                    }
                    this.cloc++;
					this.loc++;
                    break;
                case "Multiple line":
                    if (isCodeAndComment(line)) {
                        String[] code = line.split("/*"); //*/
						if(containsPredicate(code[0])){
							countPredicate++;
						}
                    }
					
                    i = countLineComment(lines, i);
                    break;
				case "Javadoc":
					i = countLineComment(lines, i);
					break;
                case "No comment":
					//Si la ligne n'est pas vide, on vérifie s'il y a un prédicat
					//Sinon on fait rien
                    if(!isEmptyLine(line)){
						if(containsPredicate(line)){
							countPredicate++;
						}
						this.loc++;
					}
                    break;
            }
        }
		this.cc = countPredicate;
    }


	//Vérifie si la ligne contient un prédicat if, while, for, etc.
	public boolean containsPredicate(String line){
		//u0028 = "(", u007b = "{"
		Pattern predicate = Pattern.compile("\\s(if|for|while|case|default)[:\\u0028\\s\\u007b]");
		Matcher predicateMatcher = predicate.matcher(line);

		if (predicateMatcher.find()){
			return true;
		}
		return false;
	}

	public void writeCSV(String pathMethod){

		String csv = this.chemin+","+this.className+","+this.methodName+","+
					 this.loc+","+this.cloc+","+this.dc+","+this.cc+","+
					 this.bc+"\n";


		try (FileWriter f = new FileWriter(pathMethod, true);
			 BufferedWriter b = new BufferedWriter(f);
			 PrintWriter p = new PrintWriter(b);) {

			 p.println(csv);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}