package ui;

import java.util.Set;
import java.util.TreeSet;

/**
 * Class is used to check if given state space is optimistic/consistent.
 * 
 * @author Ana Bagić
 *
 */
public class Check {

	/** State space to check. */
	private StateSpace ss;
	
	/**
	 * Constructor creates new object using given state space.
	 * 
	 * @param ss state space to check
	 */
	public Check(StateSpace ss) {
		this.ss = ss;
	}
	
	/**
	 * Checks if given state space is optimistic and prints the results.
	 */
	public void checkOptimistic() {
		boolean optimistic = true;
		Search search = new Search(ss, Algorithm.UCS);
		var heur = ss.getHeurFunction();
		var states = new TreeSet<>(heur.keySet());
		
		System.out.println("# HEURISTIC-OPTIMISTIC " + ss.getHeuristicPath());
		
		 for(String state : states) {
			 ss.setInitialState(state);
			 Node result = search.runAlgorithm();
			 boolean con = heur.get(state) <= result.getPrice();
			 System.out.print("[CONDITION]: " + (con ? "[OK] " : "[ERR] "));
			 System.out.print("h(" + state + ") <= h*: ");
			 System.out.println(heur.get(state) + " <= " + result.getPrice());
			 
			if(!con) optimistic = false;
		 }
		
		System.out.println("[CONCLUSION]: Heuristic " + (optimistic ? "is" : "is not") + " optimistic.");
	}
	
	/**
	 * Checks if given state space is consistent and prints the results.
	 */
	public void checkConsistent() {
		boolean consistent = true;
		var succ = ss.getSuccFunction();
		var heur = ss.getHeurFunction();
		Set<String> states = new TreeSet<>(succ.keySet());
		
		System.out.println("# HEURISTIC-CONSISTENT " + ss.getHeuristicPath());
		
		for(String state : states) {
			double s1Heur = heur.get(state);
			for(var e : succ.get(state).entrySet()) {
				double s2Heur = heur.get(e.getKey());
				double c = e.getValue();
				boolean con = s1Heur <= s2Heur + c;
				
				System.out.print("[CONDITION]: " + (con ? "[OK] " : "[ERR] "));
				System.out.print("h(" + state + ") <= h(" + e.getKey() + ") + c: ");
				System.out.println(s1Heur + " <= " + s2Heur + " + " + c);
				
				if(!con) consistent = false;
			}
		}
		
		System.out.println("[CONCLUSION]: Heuristic " + (consistent ? "is" : "is not") + " consistent.");
	}
}
