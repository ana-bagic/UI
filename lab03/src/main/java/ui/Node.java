package ui;

/**
 * Class represents a node in a decision tree.
 * 
 * @author Ana Bagić
 *
 */
public abstract class Node {

	/** Depth of the node. */
	private int depth;
	
	/**
	 * Constructor creates new node with given depth.
	 * 
	 * @param depth of the node
	 */
	public Node(int depth) {
		this.depth = depth;
	}
	
	/**
	 * @return depth of the node
	 */
	public int getDepth() {
		return depth;
	}
}
