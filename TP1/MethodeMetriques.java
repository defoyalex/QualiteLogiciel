import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodeMetriques extends Metriques {
    private String methodName;
    private int cc; //Complexité cyclomatique

    public MethodeMetriques(String chemin, String className, String methodInString, int javadocLineCounter) {

        this.chemin = chemin;
        this.className = className;
		this.cloc = javadocLineCounter;
		this.loc = javadocLineCounter;

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


		//Trouver le pattern qui correspond au nom de méthode
        Pattern p = Pattern.compile("(\\w*)(\\s)?(\\u0028)[\\w\\s\\[\\]\\<\\>\\?,\\.]*(\\u0029)");
        Matcher m = p.matcher(nomMethod); //line[0] est une string vide
        m.find();
		//Remplace les parenthèses par des espaces
		System.out.println(nomMethod);
        String methodName = m.group().replace("(", ",");
		methodName = methodName.replace(")", ",");
	
			System.out.println(this.chemin + this.methodName);
		
		//Sépare tous les mots par les espaces
		String[] methodArray = methodName.split(",");

		//Le premier mot du tableau est le nom de la méthode
		//Chaque index impair suivant est le type d'un attribut
		methodName = methodArray[0];
		for(int i=1; i<methodArray.length; i=i+2){
			methodName += "_" + methodArray[i].split(" ")[0];
		}

		return methodName;
	}

	public int getComplexity(){
		return this.cc;
	}

    private void analyseLines(String[] lines) {
		//Pour calculer la complexité cyclomatique
		//Égal au nombre de (for, if, while, etc.) + 1
		boolean atLeastOneCase = false;
		int countPredicate = 0;

        for (int i = 1; i < lines.length; i++) { //on commence à i=1 puisque i=0 est une string vide
            String line = lines[i];
            String isComment = this.isComment(line);

            switch (isComment) {
                case "Single line":
                    if (isCodeAndComment(line)) {
						//Sépare le commentaire de la ligne de code
						String[] code = line.split("//");
						//On ajoute +1 au compte s'il y a un prédicat ou un case
						if(containsCase(code[0])){
							atLeastOneCase = true;
							countPredicate++;
						}
						if(containsPredicate(code[0])){
							countPredicate++;
						}
                        this.loc++;
                    }
                    cloc++;
                    break;
                case "Multiple line":
                    if (isCodeAndComment(line)) {
                        String[] code = line.split("//");
						if(containsCase(code[0])){
							atLeastOneCase = true;
							countPredicate++;
						}
						if(containsPredicate(code[0])){
							countPredicate++;
						}
                        this.loc++;
                    }
                    i = countLineComment(lines, i);
                    break;
                case "No comment":
					//Si la ligne est vide, on fait rien
                    if(!isEmptyLine(line)){
						if(containsCase(line)){
							atLeastOneCase = true;
							countPredicate++;
						}
						if(containsPredicate(line)){
							countPredicate++;
						}
						this.loc++;
					}
                    break;
            }
        }

		//S'il y a au moins un case, on ajoute pas +1 au compte de prédicat
		if(!atLeastOneCase){
			countPredicate++;
		}
		this.cc = countPredicate;
    }

	//Vérifie si la ligne contient un case ou un default (prédicat switch)
	public boolean containsCase(String line){
		Pattern casePattern = Pattern.compile("\\s(case|default)[:\\s]");
		Matcher caseMatcher = casePattern.matcher(line);

		if (caseMatcher.find()){
			return true;
		}
		return false;
	}

	//Vérifie si la ligne contient un prédicat if, while, for, etc.
	public boolean containsPredicate(String line){
		//u0028 = "(", u007b = "{"
		Pattern predicate = Pattern.compile("\\s(if|for|while)[\\u0028\\s\\u007b]");
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