package ui;

/**
 * Class represents a leaf of a decision tree.
 * 
 * @author Ana Bagić
 *
 */
public class Leaf extends Node {

	/** Value of the leaf. */
	private String value;
	
	/**
	 * Constructor creates new leaf with given depth and value.
	 * 
	 * @param depth of the leaf
	 * @param value of the leaf
	 */
	public Leaf(int depth, String value) {
		super(depth);
		this.value = value;
	}
	
	/**
	 * @return value of the leaf
	 */
	public String getValue() {
		return value;
	}
}
