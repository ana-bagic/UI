package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Model of the decision tree used to predict labels based on the training data.
 * 
 * @author Ana Bagić
 *
 */
public class DecisionTree {

	/** Examples used to train model. */
	private List<Map<Feature, String>> trainExamples;
	/** Root node of the built decision tree. */
	private Node root;
	/** Maximum depth of the tree. */
	private int maxDepth;
	
	/**
	 * Constructor creates new decision tree with given maximum depth.
	 * If no maximum depth is wanted than put -1.
	 * 
	 * @param maxDepth of the tree
	 */
	public DecisionTree(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Trains decision tree with given examples.
	 * 
	 * @param examples to train the model with
	 */
	public void fit(List<Map<Feature, String>> examples) {
		trainExamples = examples;
		
		Set<Feature> features = new TreeSet<>(Util.FEATURES);
		features.remove(Util.LABEL);
		root = id3(trainExamples, examples, features, 1);
		
		System.out.println("[BRANCHES]:");
		printBranches(root, new StringBuilder());
	}
	
	/**
	 * Predicts the labels for the given examples based on the trained model.
	 * 
	 * @param examples to test
	 */
	public void predict(List<Map<Feature, String>> examples) {
		List<String> predictions = new ArrayList<>();
		System.out.print("[PREDICTIONS]:");
		for(var example : examples) {
			predictions.add(predictExample(example, root, new HashMap<>()));
		}
		predictions.forEach(p -> System.out.print(" " + p));
		System.out.println();
		
		System.out.println("[ACCURACY]: " + String.format("%.5f", Util.calculateAccuracy(examples, predictions)));
		
		System.out.println("[CONFUSION_MATRIX]:");
		int[][] matrix = Util.calculateConfusionMatrix(examples, predictions);
		for(int row = 0; row < matrix.length; row++) {
			for(int col = 0; col < matrix[row].length; col++) {
				if(col != 0) System.out.print(" ");
				System.out.print(matrix[row][col]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Algorithm that trains the model.
	 * 
	 * @param currExamples examples relevant for the current node
	 * @param parentExamples examples relevant to the parent node
	 * @param currFeatures features to chose from for the current node
	 * @param depth of the current node in tree
	 * @return root node of the trained tree
	 */
	private Node id3(List<Map<Feature, String>> currExamples, List<Map<Feature, String>> parentExamples, Set<Feature> currFeatures, int depth) {
		if(currExamples.isEmpty()) {
			return null;
		}
		
		String mostFreqLabel = Util.getMostFrequent(currExamples);
		if(depth - 1 == maxDepth || currFeatures.isEmpty()
				|| Util.getSubExamples(currExamples, Util.LABEL, mostFreqLabel).equals(currExamples)) {
			return new Leaf(depth, mostFreqLabel);
		}
		
		Feature maxInfoGain = Util.getMaxInfoGain(currExamples, currFeatures);
		Subtree subtree = new Subtree(depth, maxInfoGain);
		
		for(String value : maxInfoGain) {
			List<Map<Feature, String>> nextExamples = Util.getSubExamples(currExamples, maxInfoGain, value);
			Set<Feature> subFeatures = new TreeSet<>(currFeatures);
			subFeatures.remove(maxInfoGain);
			
			Node node = id3(nextExamples, currExamples, subFeatures, depth + 1);
			if(node != null) {
				subtree.addSubtree(value, node);
			}
			
		}
		
		return subtree;
	}
	
	/**
	 * Prints the branches of the tree.
	 * 
	 * @param node root node of the tree
	 * @param sb {@link StringBuilder} used to build a branch
	 */
	private void printBranches(Node node, StringBuilder sb) {
		if(node instanceof Leaf) {
			System.out.println(sb.toString() + ((Leaf)node).getValue());
			return;
		}
		
		Subtree tree = (Subtree)node;
		sb.append(tree);
		for(var subtree : tree) {
			StringBuilder sbNew = new StringBuilder(sb);
			sbNew.append(subtree.getKey()).append(" ");
			printBranches(subtree.getValue(), sbNew);
		}
	}
	
	/**
	 * Predicts a label for the example.
	 * 
	 * @param example example for which the label should be predicted
	 * @param node current node of the tree
	 * @param passedFeatures features calculated up until the current node
	 * @return label predicted for the example
	 */
	private String predictExample(Map<Feature, String> example, Node node, Map<Feature, String> passedFeatures) {
		if(node instanceof Leaf) {
			return ((Leaf)node).getValue();
		}
		
		Subtree tree = (Subtree)node;
		Feature feature = tree.getFeature();
		String value = example.get(feature);
		Node nextNode = tree.getNode(value);
		
		if(nextNode == null) {
			List<Map<Feature, String>> subExamples = trainExamples;
			for(var entry : passedFeatures.entrySet()) {
				subExamples = Util.getSubExamples(subExamples, entry.getKey(), entry.getValue());
			}
			
			return Util.getMostFrequent(subExamples);
		}
		
		passedFeatures.put(feature, value);
		return predictExample(example, nextNode, passedFeatures);
	}

}
