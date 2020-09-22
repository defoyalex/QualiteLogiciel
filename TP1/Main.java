import java.io.*;
import java.util.Scanner;

public class Main {

	

	public static File[] getListJavaFiles(String directory){
		File files = new File(directory);
		FileFilter filter = new FileFilter(){

			public boolean accept(File files){
				return files.getName().endsWith("java");
			}
		};

		File[] listFiles = files.listFiles(filter);
		return listFiles;
	
	}

	public static Classe[] getListClasseFromFile(File[] files){
		Classe[] classes = new Classe[files.length];
		for(int i=0; i<files.length; i++){
			File file = files[i];
			Classe classe = new Classe(file);
			classes[i] = classe;
		}
		
		return classes;
	}
	
	public static void calculateMetriques(Classe[] listeClasses){
		
		for(Classe classe : listeClasses){
			
			
		}
		
	}



	public static void main (String[] args) {
		String folder = "./classesTest";
		File[] listFiles = getListJavaFiles(folder);
		System.out.println(listFiles.length);
		Classe[] listeClasses = getListClasseFromFile(listFiles);

		

	}



}	