import java.io.*;
import java.util.Scanner;

public class Methode{
	
		public void parseClass(File[] files){
		try {
			for(File file : files){
				System.out.println(file);

				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String line = reader.nextLine();

				}
			
			}
		} catch (FileNotFoundException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
	}

}