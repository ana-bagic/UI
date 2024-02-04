package ui;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class used to prove theorems using resolution refutation.
 * 
 * @author Ana Bagić
 *
 */
public class Resolution {

	/** Comparator to sort clauses in set based on their id. */
	private Comparator<Clause> comp = (c1, c2) -> Integer.compare(c1.getId(), c2.getId());
	/** Counter for clause id. */
	private int counter = 1;
	/** Set of original clauses. */
	private Set<Clause> originalClauses = new TreeSet<>(comp);
	/** Goal clause. */
	private Clause goalClause;
	/** Current clauses. */
	private Set<Clause> clauses = new TreeSet<>(comp);
	/** Set of support. */
	private Set<Clause> sos = new TreeSet<>(comp);
	/** NIL clause. */
	private Clause NIL = new Clause(0, "NIL");
	
	/**
	 * Parses and adds given clause to the set of original clauses.
	 * 
	 * @param clause clause to parse and add to the set of clauses
	 */
	public void addClause(String clause) {
		Clause newClause = new Clause(counter++, clause);
		if(!newClause.isTautology()) {
			originalClauses.add(newClause);
			Clause redundant = newClause.checkRedundant(originalClauses);
			if(redundant != null) {
				originalClauses.remove(redundant);
			}
		}
	}
	
	/**
	 * Removes given clause from the set of original clauses.
	 * 
	 * @param clause clause to remove
	 */
	public void removeClause(String clause) {
		Clause toRemove = new Clause(0, clause);
		originalClauses.removeIf(c -> c.equals(toRemove));
	}
	
	/**
	 * Parses and sets goal clause to the given.
	 * 
	 * @param clause clause to parse and set as goal clause
	 */
	public void setGoal(String clause) {
		goalClause = new Clause(0, clause);
		for(Literal l : goalClause) {
			Set<Literal> c = new HashSet<>();
			c.add(l.getComplement());
			sos.add(new Clause(counter++, c));
		}
	}
	
	/**
	 * Runs resolution algorithm and prints results.
	 */
	public void run() {
		clauses.addAll(originalClauses);
		clauses.addAll(sos);
		
		printResult(resolution());
		
		clauses.clear();
		sos.clear();
		counter = 1;
		originalClauses.forEach(c -> c.setId(counter++));
	}

	/**
	 * Runs resolution algorithm and returns NIL clause or <code>null</code> if NIL clause is not found.
	 * 
	 * @return NIL clause or <code>null</code> if NIL clause is not found
	 */
	private Clause resolution() {
		while(true) {
			Set<Clause> resolved = new HashSet<>();
			Set<Clause> toRemove = new HashSet<>();
			for(Clause sosC : sos) {
				for(Clause c : clauses) {
					Set<Clause> newClauses = resolve(c, sosC);
					if(newClauses.contains(NIL)) {
						return NIL;
					}
					
					for(Clause resC : newClauses) {
						resolved.add(resC);
						Clause redundant = resC.checkRedundant(resolved);
						if(redundant != null) {
							resolved.remove(redundant);
						}
						Clause redundant2 = resC.checkRedundant(clauses);
						if(redundant2 == resC) {
							resolved.remove(redundant2);
						} else if(redundant2 != null){
							toRemove.add(redundant2);
						}
					}
				}
			}
			
			clauses.removeAll(toRemove);
			
			if(clauses.containsAll(resolved)) {
				return null;
			}
			
			sos = resolved;
			clauses.addAll(sos);
		}
	}
	
	/**
	 * Resolves two clauses.
	 * 
	 * @param c1 first clause to resolve
	 * @param c2 second clause to resolve
	 * @return list of resolved clauses
	 */
	private Set<Clause> resolve(Clause c1, Clause c2) {
		Set<Clause> resolved = new HashSet<>();
		
		for(Literal l : c1) {
			if(c2.containsComplement(l)) {
				Clause c = new Clause(counter++, c1.getLiteralsWithout(l));
				c.addAll(c2.getLiteralsWithout(l.getComplement()));
				if(c.isTautology()) continue;
				
				if(c.isEmpty()) {
					c = NIL;
					c.setId(counter++);
				}
				
				c.setParents(c1, c2);
				resolved.add(c);
			}
		}
		
		return resolved;
	}
	
	/**
	 * Prints the results of the resolution.
	 * 
	 * @param end NIL clause, or <code>null</code> if NIL clause is not found
	 */
	private void printResult(Clause end) {
		if(end != null) {
			Set<Clause> resultClauses = new TreeSet<>(comp);
			Queue<Clause> tmpClauses = new LinkedList<>();
			
			tmpClauses.add(end);
			while(!tmpClauses.isEmpty()) {
				Clause c = tmpClauses.poll();
				resultClauses.add(c);
				if(c.hasParents()) {
					tmpClauses.add(c.getParent1());
					tmpClauses.add(c.getParent2());
				}
			}
			
			boolean separatorWritten = false;
			int newCounter = 1;
			for(Clause c : resultClauses) {
				if(c.hasParents() && !separatorWritten) {
					System.out.println("===============");
					separatorWritten = true;
				}
				
				c.setId(newCounter++);
				System.out.println(c.clauseInfo());
			}
			
			System.out.println("===============");
		}
		
		System.out.println("[CONCLUSION]: " + goalClause + " is " + (end == null ? "unknown" : "true"));
	}

}
