import java.util.*;
/**
 * Class for a Decision tree with decision nodes being represented as key, value pairs and their left and right subtrees
 * Leaves are represented as map's with the occurrences of classifications to represent probability
 * @author Scott Stuart
 *
 * @param <K>
 * @param <V>
 */
public class DecisionTree<K, V>{
	private int key;
	private int value;
	private DecisionTree<K, V> left, right;
	private HashMap<K, V> probabilities;
	
	public DecisionTree(int key, int value) {
		this.key = key;
		this.value = value;
	}
	
	public DecisionTree(int key, int value, DecisionTree<K, V> left, DecisionTree<K, V> right) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}
	
	public DecisionTree(HashMap<K, V> probabilites) {
		this.probabilities = probabilites;
	}
	
	public boolean hasLeft() {
		return left != null;
	}
	
	public boolean hasRight() {
		return right != null;
	}
	
	public int getKey() {
		return key;
	}
	
	public int getValue() {
		return value;
	}
	
	public DecisionTree<K, V> getLeft(){
		return left;
	}
	
	public DecisionTree<K, V> getRight(){
		return right;
	}
	
	public boolean isLeaf() {
		return (left == null && right == null);
	}
	
	public HashMap<K, V> getProbabilities(){
		return probabilities;
	}
}
