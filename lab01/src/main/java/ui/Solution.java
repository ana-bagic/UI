package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Main program to run search algorithms or check heuristic.<br>
 * Use parameters:<br>
 * --alg bfs/ucs/astar <br>
 * --ss path_to_state_space<br>
 * --h pah_to_heuristic_function<br>
 * --check-optimistic<br>
 * --check-consistent<br>
 * 
 * @author Ana Bagić
 *
 */
public class Solution {

	public static void main(String[] args) {
		String algorithm = null;
		String ssPath = null;
		String hPath = null;
		boolean checkOptimistic = false;
		boolean checkConsistent = false;
		
		for(int i = 0; i < args.length; i++) {
			switch(args[i]) {
			case "--alg" -> algorithm = args[++i];
			case "--ss" -> ssPath = args[++i];
			case "--h" -> hPath = args[++i];
			case "--check-optimistic" -> checkOptimistic = true;
			case "--check-consistent" -> checkConsistent = true;
			}
		}
		
		StateSpace ss = new StateSpace();
		
		try(Scanner sc = new Scanner(new File(ssPath))) {
			ss.setInitialState(readNextLine(sc));
			ss.setGoalStates(new HashSet<String>(Arrays.asList(readNextLine(sc).split(" "))));
			String line = readNextLine(sc).trim();
			while(line != "") {
				if(!line.endsWith(":")) {
					String[] split = line.split(" ");
					String from = split[0].substring(0, split[0].length()-1);
					TreeMap<String, Double> to = new TreeMap<>();
					for(int i = 1; i < split.length; i++) {
						String[] nextState = split[i].split(",");
						to.put(nextState[0], Double.parseDouble(nextState[1]));
					}
					ss.addSuccFunction(from, to);
				}
				line = readNextLine(sc).trim();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(hPath != null) {
			try(Scanner sc = new Scanner(new File(hPath))) {
				String line = readNextLine(sc).trim();
				while(line != "") {
					String[] split = line.split(":");
					ss.addHeurFunction(split[0], Double.parseDouble(split[1]));
					line = readNextLine(sc).trim();
				}
				ss.setHeuristicPath(hPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if(algorithm != null) {
			Algorithm al = switch(algorithm.toLowerCase()) {
			case "bfs" -> Algorithm.BFS;
			case "ucs" -> Algorithm.UCS;
			case "astar" -> Algorithm.ASTAR;
			default -> throw new IllegalArgumentException("Wrong algorithm: " + algorithm);
			};
			
			Search search = new Search(ss, al);
			search.printResult(search.runAlgorithm());
			return;
		}
		
		Check check = new Check(ss);
		
		if(checkConsistent) {
			check.checkConsistent();
		} else if(checkOptimistic) {
			check.checkOptimistic();
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
			return line;
		}
		return "";
	}

}
