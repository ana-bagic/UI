package ui;

/**
 * Entry class for running decision trees algorithm.
 * 
 * @author Ana Bagić
 *
 */
public class Solution {

	/**
	 * Main method to run the algorithm.
	 * 
	 * @param args "path_to_train_examples" "path_to_test_examples" "algorithm_parameter (optional)"
	 */
	public static void main(String[] args) {
		DecisionTree model = new DecisionTree(args.length == 3 ? Integer.parseInt(args[2]) : -1);
		
		model.fit(Util.getExamples(args[0], true));
		model.predict(Util.getExamples(args[1], false));
	}
	
}
