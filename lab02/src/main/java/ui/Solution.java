package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry class for theorem proving using resolution.
 * 
 * @author Ana Bagić
 *
 */
public class Solution {

	public static void main(String[] args) {
		boolean cooking = args[0].equals("cooking");
		String clausesFile = args[1];
		Resolution resolution = new Resolution();
		
		try(Scanner sc = new Scanner(new File(clausesFile))) {
			String line = readNextLine(sc);
			while(line != null) {
				String nextLine = readNextLine(sc);
				if(!cooking && nextLine == null) {
					resolution.setGoal(line);
					break;
				}
				resolution.addClause(line);
				line = nextLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(cooking) {
			List<String> commands = new LinkedList<>();
			String userCommandsFile = args[2];
			
			try(Scanner sc = new Scanner(new File(userCommandsFile))) {
				String line = readNextLine(sc);
				while(line != null) {
					commands.add(line.trim());
					line = readNextLine(sc);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			Cooking cookingAssistent = new Cooking(resolution, commands);
			cookingAssistent.run();
		} else {
			resolution.run();
		}
	}
	
	/**
	 * Helper method to read next line from given {@link Scanner} ignoring line starting with '#'.
	 * 
	 * @param sc {@link Scanner} from where the line should be read
	 * @return next line
	 */
	private static String readNextLine(Scanner sc) {
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if(line.startsWith("#"))
				continue;	
			return line.trim().toLowerCase();
		}
		return null;
	}

}
