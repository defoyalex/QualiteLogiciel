import java.io.*;
import java.util.Scanner;


public class Classe{
	private File file;
	
	public Classe(File file){
		this.file = file;
		System.out.println(file.isFile());
		
			
	}
	

/*

try {
			for(File file : files){
				System.out.println(file);
				Classe classe = new Classe(file);

				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String line = reader.nextLine();

				}
			
			}
		} catch (FileNotFoundException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
*/


}