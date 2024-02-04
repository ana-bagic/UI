package ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Class models a clause in propositional logic.
 * 
 * @author Ana Bagić
 *
 */
public class Clause implements Iterable<Literal> {

	/** ID of the clause. */
	private int id;
	/** Literals in the clause. */
	private Set<Literal> clause;
	/** First parent of the clause. */
	private Clause parent1 = null;
	/** Second parent of the clause. */
	private Clause parent2 = null;
	
	/**
	 * Parses new clause from the given string.
	 * 
	 * @param id id of the clause
	 * @param clause clause to parse
	 */
	public Clause(int id, String clause) {
		this.id = id;
		this.clause = new HashSet<>();

		for(String lit : clause.split(" v ")) {
			this.clause.add(new Literal(lit.trim()));
		}
	}
	
	/**
	 * Creates new clause from the set of literals.
	 * 
	 * @param id id of the clause
	 * @param literals set of literals in clause
	 */
	public Clause(int id, Set<Literal> literals) {
		this.id = id;
		this.clause = literals;
	}
	
	/**
	 * Sets clause id to given.
	 * 
	 * @param id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return id of the clause
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Adds all given literals to this clause.
	 * 
	 * @param others set of literals to add
	 */
	public void addAll(Set<Literal> others) {
		clause.addAll(others);
	}
	
	/**
	 * @return <code>true</code> if there are no literals in clause, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return clause.isEmpty();
	}
	
	/**
	 * @param literal literal to check complement
	 * @return <code>true</code> if this clause contains complement of the given literal, <code>false</code> otherwise 
	 */
	public boolean containsComplement(Literal literal) {
		return clause.contains(literal.getComplement());
	}
	
	/**
	 * @param literal to remove from set returned
	 * @return a new set of literals without given literal
	 */
	public Set<Literal> getLiteralsWithout(Literal literal) {
		Set<Literal> without = new HashSet<>(clause);
		without.remove(literal);
		return without;
	}
	
	/**
	 * Sets clause parents to given.
	 * 
	 * @param parent1 first parent
	 * @param parent2 second parent
	 */
	public void setParents(Clause parent1, Clause parent2) {
		this.parent1 = parent1;
		this.parent2 = parent2;
	}
	
	/**
	 * @return <code>true</code> if this clause has parents, <code>false</code> otherwise
	 */
	public boolean hasParents() {
		return parent1 != null;
	}
	
	/**
	 * @return first parent of the clause
	 */
	public Clause getParent1() {
		return parent1;
	}
	
	/**
	 * @return second parent of the clause
	 */
	public Clause getParent2() {
		return parent2;
	}
	
	/**
	 * @return <code>true</code> if this clause is tautology, <code>false</code> otherwise
	 */
	public boolean isTautology() {
		for(Literal l : clause) {
			if(containsComplement(l))
				return true;
		}
		return false;
	}
	
	/**
	 * @param set to check with
	 * @return clause that is redundant (contains some other clause), otherwise <code>null</code>
	 */
	public Clause checkRedundant(Set<Clause> set) {
		for(Clause c : set) {
			if(!c.equals(this)) {
				if(c.clause.containsAll(this.clause)) return c;
				if(this.clause.containsAll(c.clause)) return this;
			}
		}
		return null;
	}
	
	/**
	 * @return info of the clause (id, literals, parents)
	 */
	public String clauseInfo() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(id).append(". ");
		sb.append(toString());
		
		if(hasParents()) {
			sb.append(" (").append(parent1.getId());
			sb.append(", ").append(parent2.getId()).append(")");
		}
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		List<Literal> list = new LinkedList<>(clause);
		StringBuilder sb = new StringBuilder();
		
		sb.append(list.get(0));
		for(int i = 1; i < list.size(); i++) {
			sb.append(" v " + list.get(i));
		}
		
		return sb.toString();
	}

	@Override
	public Iterator<Literal> iterator() {
		return clause.iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clause == null) ? 0 : clause.hashCode());
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
		Clause other = (Clause) obj;
		if (clause == null) {
			if (other.clause != null)
				return false;
		} else if (!clause.equals(other.clause))
			return false;
		return true;
	}
	
}
