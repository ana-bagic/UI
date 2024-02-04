package ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class model one feature of the decision tree.
 * 
 * @author Ana Bagić
 *
 */
public class Feature implements Iterable<String>, Comparable<Feature> {

	/** Name of the feature. */
	private String name;
	/** Values of the feature. */
	private Set<String> values = new HashSet<>();
	
	/**
	 * Constructor creates new feature with given name.
	 * 
	 * @param name of the feature
	 */
	public Feature(String name) {
		this.name = name;
	}
	
	/**
	 * @return name of the feature
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Adds value to the list of feature values.
	 * 
	 * @param value to add to list
	 */
	public void addValue(String value) {
		values.add(value);
	}

	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}

	@Override
	public int compareTo(Feature o) {
		return name.compareTo(o.name);
	}
	
}
