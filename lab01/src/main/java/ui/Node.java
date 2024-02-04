package ui;

/**
 * Class models one node in graph.
 * 
 * @author Ana Bagić
 *
 */
public class Node {

	/** State of the node. */
	private String state;
	/** Price of the node. */
	private Double price;
	/** Parent of the node. */
	private Node parent;
	/** Value of heuristic value from this node to goal state. */
	private Double heuristic;
	
	/**
	 * Constructor creates node using state, price and parent node.
	 * 
	 * @param state of the node
	 * @param price of the node
	 * @param parent of the node
	 */
	public Node(String state, Double price, Node parent) {
		this.state = state;
		this.price = price;
		this.parent = parent;
	}
	
	/**
	 * Constructor creates node using state, price, parent node, and heuristic value.
	 * 
	 * @param state of the node
	 * @param price of the node
	 * @param parent of the node
	 * @param heuristic heuristic value to goal state
	 */
	public Node(String state, Double price, Node parent, Double heuristic) {
		this(state, price, parent);
		this.heuristic = heuristic;
	}

	/**
	 * @return state of the node
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * @return price of the node
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return parent node
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * @return heuristic value of this node
	 */
	public Double getHeuristic() {
		return heuristic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Node other = (Node) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
}
