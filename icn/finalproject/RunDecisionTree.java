import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class for training and testing a Decision Tree using entropy or Gini impurities and cross-validation
 * Data is individual women's demographic and socio-economic features
 * Data obtained from:
 * 			http://archive.ics.uci.edu/ml/datasets/Contraceptive+Method+Choice
 * Sources:
 * 		http://dataaspirant.com/2017/01/30/how-decision-tree-algorithm-works/
 * 		https://en.wikipedia.org/wiki/ID3_algorithm
 * 		https://en.wikipedia.org/wiki/C4.5_algorithm
 * 
 * @author Scott Stuart
 *
 */
public class RunDecisionTree {
	public static HashMap<Integer, String> predictions;
	public static String inputFile;
	public static ArrayList<String> questions;
	public static int lastIndex;
	public static final int NUM_LINES = 1473;
	
	/**
	 * returns the a list of women created from the inputFile, either including or excluding the values from start to end
	 * @param start
	 * @param end
	 * @param include
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Woman> makeListOfWomen(int start, int end, boolean include) throws IOException{
		ArrayList<Woman> women = new ArrayList<Woman>();
		BufferedReader totalWomenInfo = new BufferedReader(new FileReader(inputFile));
		int count = 0;
		String woman;
		
		while((woman = totalWomenInfo.readLine()) != null) {
			String[] w = woman.split(",");
			if ((include && (count>= start && count<= end)) || (!include && (count< start || count> end))) {
				women.add(new Woman(Integer.parseInt(w[0]), Integer.parseInt(w[1]), Integer.parseInt(w[2]), Integer.parseInt(w[3]), Integer.parseInt(w[4]), Integer.parseInt(w[5]), Integer.parseInt(w[6]), Integer.parseInt(w[7]), Integer.parseInt(w[8]), Integer.parseInt(w[9])));
				}
			count++;
			}
		
		totalWomenInfo.close();
		return women;
	}
	
	/**
	 * method to get a map containing values that equal occurrences of contraceptive methods 
	 * @param women
	 * @return
	 */
	public static HashMap<Integer, Integer> getOccurrenceMap(ArrayList<Woman> women){
		HashMap<Integer, Integer> appearances = new HashMap<Integer, Integer>();
		
		for(Woman currWoman : women) {
			int contraceptiveMethod = currWoman.getContraceptiveMethod();
			if(!appearances.containsKey(contraceptiveMethod)) appearances.put(contraceptiveMethod, 0);
			appearances.put(contraceptiveMethod, appearances.get(contraceptiveMethod)+1);
		}
		
		return appearances;
	}
	
	/**
	 * method to compute the information gain of a split list
	 * @param leftList
	 * @param rightList
	 * @param totalUncertainty
	 * @return
	 */
	public static Double informationGain(ArrayList<Woman> leftList, ArrayList<Woman> rightList, double totalUncertainty) {
		double total = 1.0 * (leftList.size() + rightList.size());
		
		//return totalUncertainty - (((leftList.size()*1.0) / total) * entropy(leftList)) - (((1.0 * rightList.size()) / total) * entropy(rightList));			
		return totalUncertainty - (((leftList.size()*1.0) / total) * gini(leftList)) - (((1.0 * rightList.size()) / total) * gini(rightList));
		}
	
	/**
	 * method to split data by a given feature, dependent on the measure
	 * @param women
	 * @param featureIndex
	 * @param feature
	 * @return
	 */
	public static ArrayList<ArrayList<Woman>> split(ArrayList<Woman> women, int featureIndex, int measure){
		ArrayList<ArrayList<Woman>> splitList = new ArrayList<ArrayList<Woman>>();
		ArrayList<Woman> leftList = new ArrayList<Woman>();
		ArrayList<Woman> rightList = new ArrayList<Woman>();
		
		for(Woman currWoman : women) {
			if(currWoman.getInfo().get(featureIndex) <= measure) {
				leftList.add(currWoman);
			}
			else {
				rightList.add(currWoman);
			}
		}
		
		splitList.add(leftList);
		splitList.add(rightList);
		return splitList;
	}
	
	/**
	 * method to compute the Gini impurity of a given list of data
	 * @param women
	 * @return
	 */
	public static Double gini(ArrayList<Woman> women) {
		HashMap<Integer, Integer> appearances = getOccurrenceMap(women);
		double impurity = 1;
		
		for(int feature : appearances.keySet()) {
			double featureProb = appearances.get(feature)/(women.size()*1.0);
			impurity -= (featureProb*featureProb);
		}
		
		return impurity;
	}
	
	/**
	 * method to compute the entropy of a given list of data
	 * @param women
	 * @return
	 */
	public static Double entropy(ArrayList<Woman> women) {
		HashMap<Integer, Integer> appearances = getOccurrenceMap(women);
		double impurity = 0;
		
		for(int feature : appearances.keySet()) {
			double featureProb = appearances.get(feature)/(women.size()*1.0);
			impurity -= featureProb *(Math.log(featureProb)/Math.log(2.0));
		}
		
		return impurity;
	}
	
	/**
	 * method to find the best split for the list of input data
	 * @param women
	 * @return the index of the feature in each women's corresponding info list, along with the measure to be used
	 * 		   if there is no information gain from the data, returns -1
	 */
	public static ArrayList<Integer> getBestSplit(ArrayList<Woman> women) {
		double bestInfoGain = 0;
		int bestFeature = -1;
		int bestFeatureIndex = -1;
		// calculate the total impurity, can also use entropy, but I found it to be the same
		double totalImpurity = gini(women);
		//double totalUncertainty = entropy(women);
		
		for(int i = 0; i < lastIndex; i++) {
			ArrayList<Integer> features = new ArrayList<Integer>();
			
			// obtain only one value of the features present in the input data 
			for(Woman currWoman : women) {
				int feature = currWoman.getInfo().get(i);
				if(!features.contains(feature)) {
					features.add(feature);
				}
			}
			
			// go through each feature
			for(int feature : features) {
				// split the input data by the current feature
				ArrayList<ArrayList<Woman>> split = split(women, i, feature);
				
				// get the current information gain from the split
				double currentGain = informationGain(split.get(0), split.get(1), totalImpurity);
				// change "best" variables if it is greater than bestInfoGain
				if(currentGain > bestInfoGain) {
					bestFeature = feature;
					bestInfoGain = currentGain;
					bestFeatureIndex = i;
				}
			}
		}
		
		ArrayList<Integer> split = new ArrayList<Integer>();
		// if no gain was found, have split just contain -1
		if(bestInfoGain == 0) {
			split.add(-1);
		}
		
		// otherwise give it the index of the best feature and the measure of the best feature
		else {
			split.addAll(Arrays.asList(bestFeatureIndex, bestFeature));
		}
		
		return split;
	}

	/**
	 * method that returns the string representation of a question for a decision node
	 * @param index
	 * @param measure
	 * @return
	 */
	public static String question(int index, int measure) {
		String result = questions.get(index);
		
		if(index == 4 || index == 5 || index == 8) {
			return result;
		}
		
		return result + measure + "?";
	}
	
	/**
	 * method that returns the integer representation of the highest probability classification for a leaf
	 * @param map
	 * @return
	 */
	public static int maxProb(HashMap<Integer, Integer> map) {
		int max= 0;
		int result = 0;
		
		for(int key : map.keySet()) {
			if(map.get(key)>max) {
				max = map.get(key);
				result = key;
			}
		}
		
		return result;
	}
	
	/**
	 * method for testing the decision tree
	 * @param testWomen
	 * @param tree
	 * @return
	 */
	public static double testTree(ArrayList<Woman> testWomen, DecisionTree<Integer, Integer> tree) {
		// remember the root so that tree may later be set back to it
		DecisionTree<Integer, Integer> root = tree;
		double correct = 0;
		
		for(Woman test : testWomen) {
			while(!tree.isLeaf()) {
				boolean goRight = test.getInfo().get(tree.getKey()) <= tree.getValue();
				// traverse to the right
				if(goRight) {
					tree = tree.getRight();
				}
				// traverse to the left
				else {
					tree = tree.getLeft();
				}
			}
			
			// check whether the tree's predicted output matches the correct output
			int prediction = maxProb(tree.getProbabilities());
			if(prediction == test.getContraceptiveMethod()) {
				correct++;
			}
			// set tree back to equaling the root
			tree = root;
		}
		
		// return the correct percentage of predictions
		double percentCorrect = 100.0* correct / testWomen.size();
		return percentCorrect;
	}
	
	/**
	 * method for training a decision tree
	 * @param women is the training data
	 * @return
	 */
	public static DecisionTree<Integer, Integer> trainTree(ArrayList<Woman> women) {
		// gets index of the best split, as well as the measure
		ArrayList<Integer> bestFeatureList = getBestSplit(women);
		
		// if the data is homogeneous, we return a leaf
		if(bestFeatureList.get(0) == -1) {
			return new DecisionTree<>(getOccurrenceMap(women));
		}
		
		// split the input data by the feature measure
		ArrayList<ArrayList<Woman>> splitWomen = split(women, bestFeatureList.get(0), bestFeatureList.get(1));
		// recurse to the left with the data corresponding to "no"
		DecisionTree<Integer, Integer> leftList = trainTree(splitWomen.get(0));
		// recurse to the right with the data corresponding to "yes"
		DecisionTree<Integer, Integer> rightList = trainTree(splitWomen.get(1));

		// return the tree with the found best split, and it's children
		return new DecisionTree<Integer, Integer>(bestFeatureList.get(0), bestFeatureList.get(1), rightList, leftList);
	}
	
	/**
	 * method for cross-validation
	 * @param increment is the number of lines being read at a time
	 * @throws IOException
	 */
	public static void crossValidate(int increment) throws IOException {
		double bestPercent = 0;
		int bestStart = 0;
		int bestEnd = 0;
		
		// traversing through the lines of data in chunks determined by the increment
		for(int i = 0; i < NUM_LINES; i +=increment) {
			int start = i;
			// ensure that end is no greater than the number of lines
			int end = Math.min(i+increment, NUM_LINES);
			// get the input data (list of women), excluding lines start to end
			ArrayList<Woman> women = makeListOfWomen(start, end, false);
			// train with the input data
			DecisionTree<Integer, Integer> tree = trainTree(women);
			// get the testing data (list of women), including only lines start to end
			women = makeListOfWomen(start, end, true);
			// test with the training data
			double percent = testTree(women, tree);
			if(percent>bestPercent) {
				bestPercent = percent;
				bestStart = start;
				bestEnd = end;
			}
		}
		System.out.println(bestPercent+ "% correct when lines " + bestStart + " to " + bestEnd + " were not used to train, and then used to test");
	}
	
	public static void main(String args[]) throws IOException{
		// name of the input file
		inputFile = "inputs/cmc.data.txt";
		// alternative input file which is the first one shuffled, results in poorer test performance
		//inputFile = "inputs/cmc.data2.txt";
		
		// initializing map for key: integer representation of predicted contraceptive method choice to value: string representation
		predictions = new HashMap<Integer, String>();
		predictions.put(1, "No-use");
		predictions.put(2, "Long term use");
		predictions.put(3, "Short term use");
		
		// initialize ArrayList of strings who's indices correspond with the 8 attributes, giving the questions in String representation
		questions = new ArrayList<String>();
		questions.addAll(Arrays.asList("Is wife's age less than ","Is wife's ducation less than ","Is husband's education less than ","Is number of children ever born less than ","Is wife religious?","Is wife working? ","Is husband's occupation category less than ","Is standard-of-living index less than ","Is media exposure good?"));
		
		// initialize the last index so it is not used in testing
		lastIndex = 9;
		
		System.out.println("Using Gini Impurity, best cross-validation produced:");
		crossValidate(100);
	}
}
