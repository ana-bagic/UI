package ui;

import java.util.List;

/**
 * Class models cooking assistant.
 * 
 * @author Ana Bagić
 *
 */
public class Cooking {

	/** Resolution to use. */
	private Resolution resolution;
	/** List of user commands. */
	private List<String> commands;
	
	/**
	 * Initiates new cooking assistant using given resolution and user commands.
	 * 
	 * @param resolution to use in cooking assistant
	 * @param commands list of user commands
	 */
	public Cooking(Resolution resolution, List<String> commands) {
		this.resolution = resolution;
		this.commands = commands;
	}
	
	/**
	 * Runs cooking assistant.
	 */
	public void run() {
		for(String c : commands) {
			String clause = c.substring(0, c.length() - 1).trim();
			char cmd = c.charAt(c.length() - 1);
			
			System.out.println("User's command: " + c);
			
			switch(cmd) {
			case '?' -> {
				resolution.setGoal(clause);
				resolution.run();
			}
			case '+' -> {
				resolution.addClause(clause);
				System.out.println("Added " + clause);
			}
			case '-' -> {
				resolution.removeClause(clause);
				System.out.println("Removed " + clause);
			}
			default -> System.out.println("Unknown command.");
			}
			
			System.out.println();
		}
	}
}
