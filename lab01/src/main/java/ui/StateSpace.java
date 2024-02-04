package ui;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class models a state space used to run search algorithms on.
 * 
 * @author Ana Bagić
 *
 */
public class StateSpace {

	/** Initial state. */
	private String initialState;
	/** Goal states. */
	private Set<String> goalStates = new HashSet<>();
	/** Successor function. */
	private LinkedHashMap<String, TreeMap<String, Double>> succFunction = new LinkedHashMap<>();;
	/** Heuristic function used in A-STAR algorithm. */
	private LinkedHashMap<String, Double> heuristicFunction = new LinkedHashMap<>();
	/** Path to the heuristic function descriptor used in A-STAR algorithm. */
	private String heuristicPath;

	/**
	 * @return initial state
	 */
	public String getInitState() {
		return initialState;
	}

	/**
	 * @return goal states
	 */
	public Set<String> getGoalStates() {
		return goalStates;
	}

	/**
	 * @return successor function
	 */
	public LinkedHashMap<String, TreeMap<String, Double>> getSuccFunction() {
		return succFunction;
	}

	/**
	 * @return heuristic function
	 */
	public LinkedHashMap<String, Double> getHeurFunction() {
		return heuristicFunction;
	}
	
	/**
	 * @return path to the heuristic function descriptor
	 */
	public String getHeuristicPath() {
		return heuristicPath;
	}
	
	/**
	 * Sets the initial state to given.
	 * 
	 * @param initialState to be set
	 */
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	/**
	 * Sets goal states to given.
	 * 
	 * @param goalStates to be set
	 */
	public void setGoalStates(Set<String> goalStates) {
		this.goalStates = goalStates;
	}

	/**
	 * Adds transitions from given state to other given states.
	 * 
	 * @param from state
	 * @param to state
	 */
	public void addSuccFunction(String fromState, TreeMap<String, Double> toStates) {
		succFunction.put(fromState, toStates);
	}
	
	/**
	 * Sets the heuristic value for the given state.
	 * 
	 * @param state for what the value should be set
	 * @param value to be set
	 */
	public void addHeurFunction(String state, double value) {
		heuristicFunction.put(state, value);
	}
	
	/**
	 * Sets the path to heuristic function descriptor to given.
	 * 
	 * @param heuristicPath path to heuristic function descriptor
	 */
	public void setHeuristicPath(String heuristicPath) {
		this.heuristicPath = heuristicPath;
	}
	
}
