package ui;

/**
 * Class represents one literal in clause used in propositional logic.
 * 
 * @author Ana Bagić
 *
 */
public class Literal {

	/** Name of the atom. */
	private String atom;
	/** Flag marks if atom is negative. */
	private boolean isNegative = false;
	
	/**
	 * Parses new literal from given string.
	 * 
	 * @param literal to parse
	 */
	public Literal(String literal) {
		if(literal.startsWith("~")) {
			isNegative = true;
			atom = literal.substring(1);
		} else {
			atom = literal;
		}
	}
	
	/**
	 * Creates new literal using atom and negative flag.
	 * 
	 * @param atom name of the atom
	 * @param isNegative if atom is negative
	 */
	private Literal(String atom, boolean isNegative) {
		this.atom = atom;
		this.isNegative = isNegative;
	}
	
	/**
	 * @return complement of the given literal
	 */
	public Literal getComplement() {
		return new Literal(atom, !isNegative);
	}
	
	@Override
	public String toString() {
		return isNegative ? ("~" + atom) : atom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atom == null) ? 0 : atom.hashCode());
		result = prime * result + (isNegative ? 1231 : 1237);
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
		Literal other = (Literal) obj;
		if (atom == null) {
			if (other.atom != null)
				return false;
		} else if (!atom.equals(other.atom))
			return false;
		if (isNegative != other.isNegative)
			return false;
		return true;
	}
	
}
