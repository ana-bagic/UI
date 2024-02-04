package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Class with helper methods to run decision tree algorithm.
 * 
 * @author Ana Bagić
 *
 */
public class Util {
	
	/** Label of the example set. */
	public static Feature LABEL;
	/** List of features in the example set. */
	public static List<Feature> FEATURES;

	
	
	/************ READING FROM FILE *******************/
	
	
	
	/**
	 * Reads all examples from given file.
	 * 
	 * @param pathToFile path to file with examples
	 * @param train flag that marks if examples are train or test ones
	 * @return list of examples
	 */
	public static List<Map<Feature, String>> getExamples(String pathToFile, boolean train) {
		List<Map<Feature, String>> examples = new LinkedList<>();
		
		try(Scanner sc = new Scanner(new File(pathToFile))) {
			String line = readNextLine(sc);
			if(train) {
				FEATURES = getFeatures(line.split(","));
			}
			
			line = readNextLine(sc);
			while(line != null) {
				examples.add(getExample(line.split(",")));
				line = readNextLine(sc);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return examples;
	}
	
	/**
	 * Returns a list of features read from file header and sets label.
	 * 
	 * @param f array of feature names
	 * @return a list of features
	 */
	private static List<Feature> getFeatures(String[] f) {
		List<Feature> features = new ArrayList<>();
		
		for(int i = 0; i < f.length; i++) {
			features.add(new Feature(f[i]));
		}
		
		LABEL = features.get(features.size() - 1);
		return features;
	}
	
	/**
	 * Returns an example mapped from feature to value.
	 * 
	 * @param ex array of example values
	 * @return an example
	 */
	private static Map<Feature, String> getExample(String[] ex) {
		Map<Feature, String> example = new HashMap<>();
		
		for(int i = 0; i < ex.length; i++) {
			Feature f = FEATURES.get(i);
			example.put(f, ex[i]);
			f.addValue(ex[i]);
		}
		
		return example;
	}
	
	/**
	 * Helper method to read next line from given {@link Scanner} ignoring line starting with '#'.
	 * 
	 * @param sc {@link Scanner} from where the line should be read
	 * @return next line
	 */
	private static String readNextLine(Scanner sc) {
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if(line.startsWith("#"))
				continue;	
			return line.trim();
		}
		return null;
	}

	
	
	/************ TRAINING THE DECISION TREE *******************/
	
	
	
	/**
	 * Returns the most frequent label value of the given examples.
	 * If two labels have the same frequency, the alphabetically first one is chosen.
	 * 
	 * @param examples to find the most frequent label from
	 * @return the most frequent label
	 */
	public static String getMostFrequent(List<Map<Feature, String>> examples) {
		Map<String, Integer> frequencies = getFrequencies(examples);
		Set<String> mostFrequent = new TreeSet<>((s1, s2) -> s1.compareTo(s2));
		int max = -1;
		
		for(var entry : frequencies.entrySet()) {			
			if(entry.getValue() == max) {
				mostFrequent.add(entry.getKey());
			}
			
			if(entry.getValue() > max) {
				mostFrequent.clear();
				mostFrequent.add(entry.getKey());
				max = entry.getValue();
			}
		}
		
		return mostFrequent.iterator().next();
	}
	
	/**
	 * Returns a sublist of examples that have the given value mapped for the given feature.
	 * 
	 * @param examples to sublist
	 * @param feature to check
	 * @param value to check for given feature
	 * @return a sublist of examples that satisfy feature-value combination
	 */
	public static List<Map<Feature, String>> getSubExamples(List<Map<Feature, String>> examples, Feature feature, String value) {
		return examples.stream().filter(e -> e.get(feature).equals(value)).collect(Collectors.toList());
	}
	
	/**
	 * Calculates the feature with the maximum information gain on given examples.
	 * 
	 * @param examples to calculate the maximum information gain for
	 * @param features to calculate the maximum information gain from
	 * @return the feature with the maximum information gain
	 */
	public static Feature getMaxInfoGain(List<Map<Feature, String>> examples, Set<Feature> features) {
		Map<Feature, Double> infoGains = calculateInformationGain(examples, features);
		Set<Feature> maxGain = new TreeSet<>();
		double max = -1;
		
		for(var entry : infoGains.entrySet()) {
			if(Math.abs(entry.getValue() - max) < 0.00001) {
				maxGain.add(entry.getKey());
			}
			
			if(entry.getValue() > max) {
				maxGain.clear();
				maxGain.add(entry.getKey());
				max = entry.getValue();
			}
		}
		
		return maxGain.iterator().next();
	}
	
	/**
	 * Returns a map of frequencies for every label value on the given examples.
	 * 
	 * @param examples to calculate the frequencies from
	 * @return a map of frequencies for every label
	 */
	private static Map<String, Integer> getFrequencies(List<Map<Feature, String>> examples) {
		Map<String, Integer> frequencies = new HashMap<>();
		LABEL.forEach(l -> frequencies.put(l, 0));
		
		for(var e : examples) {
			String labelClass = e.get(LABEL);
			int prev = frequencies.get(labelClass);
			frequencies.put(labelClass, prev + 1);
		}
		
		return frequencies;
	}
	
	/**
	 * Calculates information gain for every feature given from the list of given examples.
	 * 
	 * @param examples to calculate information gain from
	 * @param features for which the information gain should be calculated
	 * @return a map of information gains for given features
	 */
	private static Map<Feature, Double> calculateInformationGain(List<Map<Feature, String>> examples, Set<Feature> features) {
		Map<Feature, Double> infoGain = new HashMap<>();
		double entropyAll = calculateEntropy(examples);
		
		for(Feature feature : features) {
			double IG = entropyAll;
			
			for(String value : feature) {
				List<Map<Feature, String>> subExamples = getSubExamples(examples, feature, value);
				double entropy = calculateEntropy(subExamples);
				IG -= (1.0*subExamples.size()/examples.size()) * entropy;
			}
			
			infoGain.put(feature, IG);
		}
		
		infoGain.entrySet().forEach(g -> {
			System.out.print("IG(" + g.getKey().getName() + ")=" + String.format("%.4f", g.getValue()) + " ");
		});
		System.out.println();

		return infoGain;
	}
	
	/**
	 * Calculates entropy for the given examples.
	 * 
	 * @param examples to calculate entropy for
	 * @return entropy for the given examples
	 */
	private static double calculateEntropy(List<Map<Feature, String>> examples) {
		Map<String, Integer> frequencies = getFrequencies(examples);
		double entropy = 0;
		
		for(Integer freq : frequencies.values()) {
			if(freq == 0) continue;
			double p = 1.0*freq/examples.size();
			entropy -= p*(Math.log(p) / Math.log(2));
		}
		
		return entropy;
	}
	
	
	
	/************ TESTING THE DECISION TREE *******************/
	
	

	/**
	 * Calculates the accuracy of the decision tree model.
	 * 
	 * @param examples used to test the model
	 * @param predictions that the model predicted
	 * @return the accuracy of the model
	 */
	public static double calculateAccuracy(List<Map<Feature, String>> examples, List<String> predictions) {
		int correct = 0;
		
		for(int i = 0; i < examples.size(); i++) {
			if(examples.get(i).get(LABEL).equals(predictions.get(i))) {
				correct++;
			}
		}
		
		return 1.0*correct/examples.size();
	}

	/**
	 * Calculates the confusion matrix from the given examples and predictions.
	 * 
	 * @param examples used to calculate confusion matrix
	 * @param predictions used to calculate confusion matrix
	 * @return the confusion matrix
	 */
	public static int[][] calculateConfusionMatrix(List<Map<Feature, String>> examples, List<String> predictions) {
		Set<String> valuesSet = new TreeSet<>(predictions);
		examples.forEach(e -> valuesSet.add(e.get(LABEL)));
		
		Map<String, Integer> values = new HashMap<>();
		int counter = 0;
		for(String s : valuesSet) values.put(s, counter++); 
		
		int[][] matrix = new int[values.size()][values.size()];
		
		for(int i = 0; i < examples.size(); i++) {
			String real = examples.get(i).get(LABEL);
			String predicted = predictions.get(i);
			matrix[values.get(real)][values.get(predicted)]++;
		}
		
		return matrix;
	}

}
