package ui;

/**
 * Types of search algorithms.
 * 
 * @author Ana Bagić
 *
 */
public enum Algorithm {

	/** Breadth-first search algorithm. */
	BFS("BFS"),
	
	/** Uniform-cost search algorithm. */
	UCS("UCS"),
	
	/** A* heuristic algorithm. */
	ASTAR("A-STAR");

	/** Name of the algorithm. */
	private String name;
	
	/**
	 * Sets algorithm name.
	 * @param name
	 */
	Algorithm(String name) {
		this.name = name;
	}
	
	/**
	 * @return algorithm name
	 */
	String getName() {
		return name;
	}
}
