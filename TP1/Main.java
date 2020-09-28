import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	
	/* Fait un tableau avec tous les fichiers javas à partir du chemin 
	d'un répertoire */
	public static File[] getListJavaFiles(String directory){
		File files = new File(directory);

		if(!files.isDirectory()){  //si c'est un dossier et non un fichier
			return null;
		}
		System.out.println("files is directory? " + files.isDirectory());
		FileFilter filter = new FileFilter(){ //sert à ne conserver que les fichiers java
			public boolean accept(File files){
				return files.getName().endsWith("java");
			}
		};

		File[]listFiles = files.listFiles(filter);
		return listFiles;
	}
	
	// Lis une liste de fichiers 0
	public static void readFiles(File[] files){
		for(int i=0; i<files.length; i++){
			File file = files[i];
			String fileToString;
			
			try {
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String line = reader.nextLine();
					if(isClass(line)){
						//TODO si la ligne est le début d'une classe
                        System.out.println(""+line);
					}
				}
				reader.close();
			} catch (FileNotFoundException e){
				System.out.println("File not found");
				e.printStackTrace();
			}
		}
				
	}
	
	
	// Pour vérifier si la ligne est le début d'une classe 
	public static boolean isClass(String line){
		//On vérifie si ça match le mot class entourer de whitespace
		Pattern singleLine = Pattern.compile("\\s(class)\\s");

		return false; //TODO
	} 
	




	public static void main (String[] args) {
		String folder = "./classesTest/jfree/chart"; //E:\Documents\GitHub\QualiteLogiciel\TP1\classesTest\jfree\chart
		File[] listFiles = getListJavaFiles(folder);

		System.out.println("taille de listFiles = " +listFiles.length);
        readFiles(listFiles);
	}



}	