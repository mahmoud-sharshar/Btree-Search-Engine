package eg.edu.alexu.csd.filestructure.btree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<K extends Comparable<K>, V> implements IBTreeNode<K,V>{
    private List<K> keys = new ArrayList<K>();
    private List<V> values = new ArrayList<V>();
    private int maxSize=0;
    boolean leaf=true;
    private List<IBTreeNode<K, V>>Children=new ArrayList<IBTreeNode<K, V>>();
    public void insertKey(int index, K key, V value) {
		this.keys.add(index, key);
		this.values.add(index, value);
	}

	public void removeKey(int index) {
		this.keys.remove(index);
		this.values.remove(index);
	}
	public void insertChild(int index, IBTreeNode<K, V> child){
		this.Children.add(index, child);
	}

	public void removeChild(int index) {
		this.Children.remove(index);
	}
	@Override
	public int getNumOfKeys() {
		maxSize=keys.size();
		return maxSize;
	}

	@Override
	public void setNumOfKeys(int numOfKeys) {
		// TODO Auto-generated method stub
		maxSize=numOfKeys;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return Children.isEmpty();
	}

	@Override
	public void setLeaf(boolean isLeaf) {
		// TODO Auto-generated method stub
		leaf = isLeaf;
	}

	@Override
	public List<K> getKeys() {
		// TODO Auto-generated method stub
		return keys;
	}

	@Override
	public void setKeys(List<K> keys) {
		// TODO Auto-generated method stub
		this.keys = keys;
	}

	@Override
	public List<V> getValues() {
		// TODO Auto-generated method stub
		return values;
	}

	@Override
	public void setValues(List<V> values) {
		// TODO Auto-generated method stub
		this.values = values;
	}

	@Override
	public List<IBTreeNode<K, V>> getChildren() {
		// TODO Auto-generated method stub
		return Children;
	}

	@Override
	public void setChildren(List<IBTreeNode<K, V>> children) {
		Children = children;
	}
	
}
