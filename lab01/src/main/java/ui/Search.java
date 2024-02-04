package ui;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Class is used to search given state space using BFS, UCS or A* algorithm.
 * 
 * @author Ana Bagić
 *
 */
public class Search {

	/** State space to search. */
	private StateSpace ss;
	/** Algorithm used to search. */
	private Algorithm algorithm;
	/** Queue of open nodes. */
	private Queue<Node> open;
	/** Maps states to node in open list (used so the contains is o(1)). */
	private Map<String, Node> openHelp;
	/** Map of closed nodes used for optimization. */
	private Map<String, Node> closed;
	
	/**
	 * Constructor creates new search based on given state space and algorithm.
	 * 
	 * @param ss state space to search
	 * @param algorithm to use while searching
	 */
	public Search(StateSpace ss, Algorithm algorithm) {
		this.ss = ss;
		this.algorithm = algorithm;
		closed = new HashMap<>();
		openHelp = new HashMap<>();
	}
	
	/**
	 * Runs search algorithm.
	 * 
	 * @return result node of the search algorithm, or <code>null</code> if node is not found
	 */
	public Node runAlgorithm() {
		closed.clear();
		openHelp.clear();
		
		Comparator<Node> compPrice = (n1, n2) -> n1.getPrice().compareTo(n2.getPrice());
		Comparator<Node> compState = (n1, n2) -> n1.getState().compareTo(n2.getState());
		Comparator<Node> compHeuristic = (n1, n2) ->
			Double.compare(n1.getPrice() + n1.getHeuristic(), n2.getPrice() + n2.getHeuristic());
		switch(algorithm) {
		case BFS -> {
			open = new LinkedList<>();
		}
		case UCS -> {
			open = new PriorityQueue<>(compPrice.thenComparing(compState));
		}
		case ASTAR -> {
			open = new PriorityQueue<>(compHeuristic.thenComparing(compState));
		}
		}
		
		Node result = run();
		return result;
	}
	
	/**
	 * Helper method to run search algorithm.
	 * 
	 * @return result node for algorithm
	 */
	private Node run() {
		boolean heur = algorithm == Algorithm.ASTAR;
		Node initial = !heur ? new Node(ss.getInitState(), 0.0, null)
			: new Node(ss.getInitState(), 0.0, null, ss.getHeurFunction().get(ss.getInitState()));
		open.add(initial);
		openHelp.put(initial.getState(), initial);
		var goal = ss.getGoalStates();
		var succ = ss.getSuccFunction();
		var heurF = ss.getHeurFunction();
		
		while(!open.isEmpty()) {
			Node n = open.poll();
			openHelp.remove(n.getState());
			closed.put(n.getState(), n);
			if(goal.contains(n.getState())) {
				return n;
			}
			
			var successors = succ.get(n.getState());
			if(successors != null) {
				for(Entry<String, Double> e : successors.entrySet()) {
					Node m = !heur ? new Node(e.getKey(), e.getValue() + n.getPrice(), n)
						: new Node(e.getKey(), e.getValue() + n.getPrice(), n, heurF.get(e.getKey()));
					if(!containsClosed(m) && !containsOpen(m)) {
						open.add(m);
						openHelp.put(m.getState(), m);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Helper method that checks if node with given node's state already exists in closed list.
	 * If it does and its price is greater than given node's price, it is removed from list.
	 * 
	 * @param node to check if closed list contains it
	 * @return <code>true</code> if closed already contains node's state and its price is lower then node's,
	 * otherwise <code>false</code>
	 */
	private boolean containsClosed(Node node) {
		Node n = closed.get(node.getState());
		if(n != null) {
			if(n.getPrice() > node.getPrice()) {
				closed.remove(node.getState());
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Helper method that checks if node with given node's state already exists in open list.
	 * If it does and its price is greater than given node's price, it is removed from list.
	 * 
	 * @param node to check if open list contains it
	 * @return <code>true</code> if open already contains node's state and its price is lower then node's,
	 * otherwise <code>false</code>
	 */
	private boolean containsOpen(Node node) {
		Node n = openHelp.get(node.getState());
		if(n != null) {
			if(n.getPrice() > node.getPrice()) {
				open.remove(node);
				openHelp.remove(node.getState());
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Method that prints the results of search.
	 * 
	 * @param n result node of search algorithm
	 */
	public void printResult(Node n) {
		Stack<Node> path = new Stack<>();
		Node parent = n;
		while(parent != null) {
			path.push(parent);
			parent = parent.getParent();
		}
		
		System.out.print("# " + algorithm.getName());
		System.out.println(algorithm == Algorithm.ASTAR ? (" " + ss.getHeuristicPath()) : "");
		if(n != null) {
			System.out.println("[FOUND_SOLUTION]: yes");
			System.out.println("[STATES_VISITED]: " + closed.size());
			System.out.println("[PATH_LENGTH]: " + path.size());
			System.out.println("[TOTAL_COST]: " +  n.getPrice());
			System.out.print("[PATH]: " + path.pop().getState());
			while (!path.isEmpty()) {
				System.out.print(" => " + path.pop().getState());
			}
			System.out.println();
		} else {
			System.out.println("[FOUND_SOLUTION]: no");
		}

	}
}
