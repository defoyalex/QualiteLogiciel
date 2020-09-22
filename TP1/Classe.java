import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Classe{
	private File file;
	private Methode[] methodes;
	private int loc;
	private int cloc;
	private double dc;
	private double wmc;
	
	public Classe(File file){
		this.file = file;
		
	}
	
	public void calculateMetriques(){
		try {
			Scanner reader = new Scanner(this.file);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
			}
			reader.close();
		} catch (FileNotFoundException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
	}

}