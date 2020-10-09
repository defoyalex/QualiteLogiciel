Pour utiliser notre programme, il suffit de le démarrer.
Ensuite, il demandera d'entrer le chemin pour atteindre le dossier ou fichier désiré.
S'il s'agit d'un dossier, veuillez vous assurer qu'il y ait un "\" à la fin du chemin.

Les fichiers .csv seront créés dans le dossier dans lequel le fichier java runnable .jar
est appelé.


Notes sur le parsing: 
	- Les commentaires qui viennent avant les "import" dans les classes ne sont 
	  pas considérés.
 	- La ligne de code indiquant le package n'est pas comptée dans le loc des classes.
	- Les lignes de code "import" avant les classes comptes pour toutes les classes 
	  d'un fichier java.
	- Si un commentaire qui vient avant une méthode l'explique, mais n'est pas un 
	  Javadoc (donc ne commence pas par /**), ce commentaire comptera dans la classe 
	  parente, mais pas pour la méthode.
 	- Les méthodes sont "trouvées" grâce entre autres aux accolades "{}". Dans le cas 
	  d'interfaces qui n'implémentent pas les méthodes, les accolades ne sont pas
	  présentes. Elles seront donc considéres seulement comme des lignes de code et 
	  le nombre dans la classe et le nombre de méthode de ces classes sera égal à 0.

Notes sur les métriques : 
	- Dans le cas d'une interface, puisqu'il n'y a pas de méthode selon notre parsing, 
	  la métrique wmc sera égale à zéro et la métrique bc sera infinie. Nous avons 
	  choisi de laisser la valeur infinie puisqu'il y aura effectivement une certaine
 	  quantité de commentaires pour une complexité cyclomatique de méthode nulle.


ANALYSE DES COMMENTAIRES DE JFREECHART: 
	Bien qu'il serait possible de trop commenter, notre analyse focus sur les classes 
	et méthodes qui ne sont pas suffisament commentées.


CLASSES: 


	\jfree\chart\plot\XYPlot : 
		Densité de commentaire : 0,457
		WMC : 615 *La plus élevée du projet.
		BC : 7.44E-04 *La classe avec le plus faible degré BC.
		Analyse : Bien qu'elle possède une densité de commentaire qui s'approche de la
			  moyenne, cette classe n'est pas suffisamment commentée. Une classe
			  nécessite généralement plus de commentaires si elle est complexe. La 
			  métrique WMC montre que cette classe possède beaucoup de prédicats sans
			  les expliquer.

	\jfree\chart\renderer\xy\AbstractXYItemRenderer : 
		Densité de commentaire : 0,355
		WMC : 162
		BC : 0,00219
		Analyse : Contient une faible densité de commentaire comparativement à la somme de
			    complexité cyclomatique des méthodes. Plusieurs méthodes ne sont
				pas bien expliquées et aurait nécessité plus d'explications dans le
				Javadoc et plus de commentaires à l'intérieur de la méthode. Un exemple
				est la méthode DrawDomainMarker à la ligne 971 qui contient environ 150
				lignes, mais ne contient que deux lignes la décrivant dans le Javadoc la
				précédant et 4 lignes de commentaire à l'intérieur. Il est difficile de 
				comprendre ce que fait le code. Malgré que cette classe ne soit pas la 
				classe avec le plus bas BC, le code est moins évident à comprendre que d'autres
				classes avec un plus faible BC.

	jfree\data\statistics\Regression :    
		Densité de commentaire : 0,245
		WMC : 36
		BC : 0,0068
		Analyse : Contient une faible densité de commentaires alliée à un wmc élevé. De plus,
				bien que cette classe possède un BC un peu plus élevé que d'autres classes, elle
				possède aussi du code plus complexes. Par exemple, la méthode getPolynomialRegression
				à la ligne 250 parce qu'elle est complexe, mais possède aucun commentaire à 
				l'intérieur de la méthode. Puisque ce sont des formules mathématiques, il serait
				utile d'ajouter des commentaires pour expliquer les démarches pour débugger ou
				faire de la maintenance.