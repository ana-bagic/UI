package ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class represents a subtree of a decision tree.
 * 
 * @author Ana Bagić
 *
 */
public class Subtree extends Node implements Iterable<Entry<String, Node>> {

	/** Feature of the subtree. */
	private Feature feature;
	/** Map of the subtrees of this tree. */
	private Map<String, Node> subtrees = new HashMap<>();
	
	/**
	 * Constructor creates new subtree with given depth and feature.
	 * 
	 * @param depth of the subtree
	 * @param feature of the subtree
	 */
	public Subtree(int depth, Feature feature) {
		super(depth);
		this.feature = feature;
	}
	
	/**
	 * @return feature of the subtree
	 */
	public Feature getFeature() {
		return feature;
	}
	
	/**
	 * Adds subtree to this tree.
	 * 
	 * @param value of the subtree
	 * @param subtree root node of the subtree
	 */
	public void addSubtree(String value, Node subtree) {
		subtrees.put(value, subtree);
	}
	
	/**
	 * Returns root node of the subtree with the given value for the feature in this tree.
	 * 
	 * @param value for the feature of the subtree to get
	 * @return root node of that subtree
	 */
	public Node getNode(String value) {
		return subtrees.get(value);
	}

	@Override
	public Iterator<Entry<String, Node>> iterator() {
		return subtrees.entrySet().iterator();
	}
	
	@Override
	public String toString() {
		return getDepth() + ":" + feature.getName() + "=";
	}
	
}
