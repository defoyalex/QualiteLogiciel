import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe parent de ClasseMetrique et MethodeMetrique.
 * Contient les méthodes générales d'analyse de code et calculs de métriques.
 */
public abstract class Metriques {
    protected String chemin;
    protected String className;
    protected int loc = 0; //Nombre de ligne de code
    protected int cloc = 0; // Nombre de ligne de commentaire
    protected double dc; //Densité de commentaire CLOC/LOC

    private boolean doWeCount = false;
    private int javadocLineCounter;

    /**
     * Degré selon lequel une méthode ou class est bien commentée
     *        Méthode : dc/cc
     *        classe : dc/wmc
     */
    protected double bc;

    /**
     * className est utilisé pour identifier les classes lors de leurs analyses;
     * ou pour savoir quelle classe contient quelle(s) méthode(s)
     *
     * @return className
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * loc est le nombre de ligne de code qui est contenu dans une méthode ou classe
     *
     * @return loc
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * cloc est le nombre de ligne de code commentées contenu dans une méthode ou classe
     *
     * @return cloc
     */
    public int getCLOC(){
        return this.cloc;
    }

    /**
     * dc est une mesure de densité de commentaire pour une classe ou méthode donnée
     * elle est calculée en fesant "cloc/loc"
     *
     * @return dc
     */
    public double getDC() {
        return this.dc;
    }

    /**
     * @param line
     * @return retourne si la ligne contient des commentaires et, si oui, on retourne de quel type il s'agit
     */
    public String isComment(String line) {

        if (isSingleLineComment(line)) {
            return "Single line";
        }
		
		if (isJavadocMultipleLineComment(line)){
            return "Javadoc";
        }

        if (isStartMultipleLineComment(line) ||
                isEndMultipleLineComment(line) ) {
            return "Multiple line";
        }
        return "No comment";
    }


    /**
     * @param line
     * @return boolean où true := la ligne contient un commentaire d'une ligne
     */
    public boolean isSingleLineComment(String line) {
        Pattern singleLine = Pattern.compile("//");
        Matcher singleLineMatcher = singleLine.matcher(line);
        if (singleLineMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param line
     * @return boolean où true := la ligne contient le début d'un commentaire de plusieurs lignes
     */
    public boolean isStartMultipleLineComment(String line) {
        Pattern multipleLineBeginning = Pattern.compile("/\\*");  //pour "/*"
        Matcher multipleLineBeginningMatcher = multipleLineBeginning.matcher(line);
        if (multipleLineBeginningMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param line
     * @return boolean où true := la ligne contient la fin d'un commentaire de plusieurs lignes
     */
    public boolean isEndMultipleLineComment(String line) {
        Pattern multipleLineEnding = Pattern.compile("\\*/"); //pour "*/"
        Matcher multipleLineEndingMatcher = multipleLineEnding.matcher(line);
        if (multipleLineEndingMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param line
     * @return boolean où true := la ligne contient la suite d'un commentaire JavaDoc
     */
    public boolean isJavadocMultipleLineComment(String line) {
        Pattern javadoc = Pattern.compile("\\*\\*"); //pour "/**"
        Matcher javadocMatcher = javadoc.matcher(line);
        if (javadocMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param line
     * @return boolean où true := il y a du code avec commentaire sur une même ligne
     */
    public boolean isCodeAndComment(String line) {
        Pattern singleLine = Pattern.compile("\\S+\\s*//");
        Matcher singleLineMatcher = singleLine.matcher(line);

        Pattern multipleLine = Pattern.compile("\\S+\\s*/\\*");
        Matcher multipleLineMatcher = multipleLine.matcher(line);
        if (singleLineMatcher.find() || multipleLineMatcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * Compte la quantité de lignes de commentaires et retourne la nouvelle position
     *     dans la table (contenant les lignes de code) après les commentaires.
     * @param lines
     * @param i
     * @return i
     */
    public int countLineComment(String[] lines, int i) {
        while (i < lines.length && !isEndMultipleLineComment(lines[i])) {
            this.cloc++;
			this.loc++;
            //Si on atteint la fin tu tableau sans trouver la fin du commentaire
            if (i == lines.length) {

                return i;
            }
            i++;
        }
        this.cloc++;
		this.loc++;
        return i;
    }

    /**
     * @param line
     * @return boolean true := si c'est une ligne vide ou ne contenant que des whitespaces
     */
    public boolean isEmptyLine(String line) {
        Pattern nonWhiteSpace = Pattern.compile("\\S+");
        Matcher matcherNonWhiteSpace = nonWhiteSpace.matcher(line);

        if (line.isEmpty() || !matcherNonWhiteSpace.find()) {
            return true;
        }
        return false;
    }

    /**
     * Utilisé dans Main et Class pour compter les lignes de javadoc
     * précédant les déclaractions de classes et méthodes
     *
     * @param line
     */
    public void javadocLineCounter(String line) {

        boolean beginningOfJavadoc = isJavadocMultipleLineComment(line);
        boolean endingOfJavadoc = isEndMultipleLineComment(line);

        if (beginningOfJavadoc && endingOfJavadoc) { //si la javadoc se trouve sur une seule ligne
            javadocLineCounter = 1;
        } else if (beginningOfJavadoc) { //quand on trouve un début d'un bloc de javadoc
            javadocLineCounter++; //on compte la ligne
            doWeCount = true; //et turn "ON" le compteur
        } else if (endingOfJavadoc && doWeCount) { //quand on trouve la fin d'un bloc de javadoc
            javadocLineCounter++; //on compte la dernière ligne
            doWeCount = false; //et on turn "OFF" le compteur
        } else if (doWeCount) { //si on est ni au début ou à la fin d'un bloc de javadoc mais entre les deux
            javadocLineCounter++; //alors on continue de compter
        }
    }

}