package eg.edu.alexu.csd.filestructure.btree;


import java.util.ArrayList;

import javax.management.RuntimeErrorException;

public class Tree<K extends Comparable<K>, V>  implements IBTree<K,V>{
    private IBTreeNode<K, V> root = null;
    private int t=0;
    private IBTreeNode<K, V> x_search = root;
    private IBTreeNode<K, V> lastParent=null;
    private IBTreeNode<K, V> grandFather=null;   
    private boolean pre=true;
    private boolean merge=false;
    private int index=0;
    private int index2=0;
    private boolean rooted=false;
    private boolean formatted=false;
    public Tree(int t){
    	if(t<2)
    	{
        	throw new RuntimeErrorException(null);
    	}
    	this.t=t;	
    }
	@Override
	public int getMinimumDegree() {
		// TODO Auto-generated method stub
		return t;
	}

	@Override
	public IBTreeNode<K, V> getRoot() {
        if(root==null)
        	throw new RuntimeErrorException(null);// TODO Auto-generated method stub
		return root;
	}

	@Override
	public void insert(K key, V value) {
		if(root==null){
			root=new TreeNode<K, V>();
			x_search=root;
		}
		if(value == null){
        	throw new RuntimeErrorException(null);
		}
		if(search(key)==null){
		IBTreeNode<K, V> r =root;
		if(r.getNumOfKeys()==2*t-1){
			IBTreeNode<K, V> s = new TreeNode<K,V>();
			root=s;
			s.setLeaf(false);
			s.setNumOfKeys(0);
			s.getChildren().add(r);
			split(s,0);
			insertNonFull(s,key,value);		
		}
		else 			
			insertNonFull(r,key,value);
		x_search = root;}
	}

	private void insertNonFull(IBTreeNode<K, V> s, K key, V value) {
		// TODO Auto-generated method stub
		int i=0;
		if(s.isLeaf()){
			while(i<s.getNumOfKeys()&&key.compareTo((s.getKeys().get(i)))>0){
				i++;
			}
			s.getKeys().add(i, key);
			s.getValues().add(i, value);
			s.setNumOfKeys(s.getNumOfKeys()+1);
		}
		else 
		{
			while(i<s.getNumOfKeys()&&key.compareTo((s.getKeys().get(i)))>0){
			i++;
			}
			if(s.getChildren().get(i).getNumOfKeys()==2*t-1){
				split(s,i);
				if(key.compareTo(s.getKeys().get(i))>0){
					i++;
				}
			}
			insertNonFull(s.getChildren().get(i),key,value);
		}
	}
	@Override
	public V search(K key) {
		if(key==null)
		{
        	throw new RuntimeErrorException(null);
		}
        x_search=root;
        V result = searchKey(key);
        return result;
	}
    private V searchKey(K key){
    	if(this.root == null)
    		return null;
    	int i =1;
        while(i <= x_search.getNumOfKeys()){
        	K y = x_search.getKeys().get(i-1);
        	if(key.compareTo(y)<=0){
        		break;
        	}
        	i++;
        }
		if( i<=x_search.getNumOfKeys()&&key.compareTo(x_search.getKeys().get(i-1))==0){
			return x_search.getValues().get(i-1);
		}
		else if (x_search.isLeaf()){
			return null;
		}
		else {
			x_search=x_search.getChildren().get(i-1);
			return searchKey(key);
		}
    	
    }
    private IBTreeNode<K, V> searchNode(K key, IBTreeNode<K, V> node){
    	int i =1;
		pre=true;
    	x_search=node;
        while(i <= x_search.getNumOfKeys()){
        	K y = x_search.getKeys().get(i-1);
        	if(key.compareTo(y)<=0){
        		break;
        	}
        	i++;
        }
        if(node.getNumOfKeys()<t-1&&node!=root){
        	fix(root,x_search.getChildren().get(0),key);
        	 i =1;
    		pre=true;
        	x_search=root;
            while(i <= x_search.getNumOfKeys()){
            	K y = x_search.getKeys().get(i-1);
            	if(key.compareTo(y)<=0){
            		break;
            	}
            	i++;
            }       
            }
		if( i<=x_search.getNumOfKeys()&&key.compareTo(x_search.getKeys().get(i-1))==0){
			if(!x_search.isLeaf()){
			lastParent= x_search.getChildren().get(i-1);
			if(lastParent.getNumOfKeys()>t-1 && x_search.getChildren().size()>i-1){}
			else if(lastParent.getNumOfKeys()<t && x_search.getChildren().get(i).getNumOfKeys()>t-1){
				lastParent=x_search.getChildren().get(i);
			    pre=false;	
			}
			else 
			{
				if(x_search.getKeys().contains(key)&&!x_search.isLeaf()){
				merge=true;
				mergeNodes(x_search.getChildren().get(i-1),x_search.getChildren().get(i),x_search,i-1,true);
				if(x_search.getChildren().size()!=0&&i<=x_search.getChildren().size())
	        	fix(root,x_search.getChildren().get(i-1),key);}

			}
			}
			return x_search;
		}
		else if (x_search.isLeaf()){
			return null;
		}
		else {
			grandFather=lastParent;
			index2=index;
			index=i-1;
			lastParent=x_search;
			x_search=x_search.getChildren().get(i-1);
			return searchNode(key,x_search);
		}
    	
    }
	public void mergeNodes(IBTreeNode<K, V> ibTreeNode,
			IBTreeNode<K, V> ibTreeNode2, IBTreeNode<K, V> x_search2, int i,boolean remove) {
		ibTreeNode.getKeys().add(x_search2.getKeys().get(i));
    	ibTreeNode.getValues().add(x_search2.getValues().get(i));
		    for(int j=0;j<ibTreeNode2.getNumOfKeys();j++){
		    	ibTreeNode.getKeys().add(ibTreeNode2.getKeys().get(j));
		    	ibTreeNode.getValues().add(ibTreeNode2.getValues().get(j));
		    }
		    int j=0;
		    while(j<ibTreeNode2.getChildren().size()){
		    	ibTreeNode.getChildren().add(ibTreeNode2.getChildren().get(j));
		    j++;	
		    }
		    ibTreeNode.setNumOfKeys(ibTreeNode.getNumOfKeys()+ibTreeNode2.getNumOfKeys());
		    x_search2.getChildren().set(i,ibTreeNode );
		    x_search2.getChildren().remove(i+1);
		    if(remove){
			     K key2 =x_search2.getKeys().get(i);
		     x_search2.getKeys().remove(i);
		     x_search2.getValues().remove(i);
		     merge=false;
		     deleteNode(ibTreeNode,key2);
		     merge=true;
		    }
		    }
	@Override
	public boolean delete(K key) {
//		if(key == null)
//        	throw new RuntimeErrorException(null);
//        lastParent=null;
//		IBTreeNode<K, V> node =searchNode(key,root);
//		if(merge){
//			merge=false;
//			return true;
//		}
//		if(node==null)
//			return false;
//		x_search=root;
//		if(node==root){
//			Dint k =0;
//			while(key.compareTo(node.getKeys().get(k))!=0)
//			{
//				k++;
//			}
//			lastParent=node;
//			if(!node.isLeaf()){
//			node=node.getChildren().get(k);
//			while(node.getChildren().size()>0){
//				lastParent=node;
//				node=node.getChildren().get(node.getChildren().size()-1);
//			}
//			root.getKeys().set(k,node.getKeys().get(node.getKeys().size()-1));
//			root.getValues().set(k,node.getValues().get(node.getValues().size()-1));
//			deleteNode(node, node.getKeys().get(node.getKeys().size()-1));
//			}
//			if(node==root&&node.isLeaf()){
//				deleteNode(node,key);
//			}
//		}
//		
//		else deleteNode(node, key);
//		merge=false;
//		return true;
		if (key == null)
			throw new RuntimeErrorException(null);
		IBTreeNode<K, V> node = this.root;
		while (true) {
			int i = 0;
			int n = node.getNumOfKeys();
			while (i < n && key.compareTo((node).getKeys().get(i)) > 0)
				i++;
			if (i < n && key.compareTo((node).getKeys().get(i)) == 0) {
				if (node.isLeaf()) {
					((TreeNode<K, V>) node).removeKey(i);
					return true;
				} else {
					IBTreeNode<K, V> leftChild = ( node).getChildren().get(i);
					IBTreeNode<K, V> rightChild = ( node).getChildren().get(i+1);
					if (leftChild.getNumOfKeys() >= t) {
						((TreeNode<K, V>) node).removeKey(i);
						TreeNode<K, V> predecessorNode = (TreeNode<K, V>) leftChild;
						while (!predecessorNode.isLeaf())
							predecessorNode = (TreeNode<K, V>) predecessorNode.getChildren().get(predecessorNode.getNumOfKeys());
						K predecessorKey = predecessorNode.getKeys().get(predecessorNode.getNumOfKeys() - 1);
						V predecessorValue = predecessorNode.getValues().get(predecessorNode.getNumOfKeys() - 1);
						((TreeNode<K, V>) node).insertKey(i, predecessorKey, predecessorValue);
						node = leftChild;
						key = predecessorKey;
					} else if (rightChild.getNumOfKeys() >= t) {
						((TreeNode<K, V>) node).removeKey(i);
						TreeNode<K, V> successorNode = (TreeNode<K, V>) rightChild;
						while (!successorNode.isLeaf())
							successorNode = (TreeNode<K, V>) successorNode.getChildren().get(0);
						K successorKey = successorNode.getKeys().get(0);
						V successorValue = successorNode.getValues().get(0);
						((TreeNode<K, V>) node).insertKey(i, successorKey, successorValue);
						node = rightChild;
						key = successorKey;
					} else {
						((TreeNode<K, V>) leftChild).insertKey(t - 1, key, (node).getValues().get(i));
						
						for (int j = 0; j < t -1; j++)
							((TreeNode<K, V>) leftChild).insertKey(j + t,(rightChild).getKeys().get(j),(rightChild).getValues().get(j));
						if (!rightChild.isLeaf()) {
							((TreeNode<K, V>) leftChild).insertChild(t , (rightChild).getChildren().get(0));
							for (int j = 1; j < t; j++)
								((TreeNode<K, V>) leftChild).insertChild(j + t, ((TreeNode<K, V>) rightChild).getChildren().get(j));
						}
						((TreeNode<K, V>) node).removeChild(i + 1);
						((TreeNode<K, V>) node).removeKey(i);
						node = leftChild;
						if (root.getNumOfKeys() == 0) {
							root = node;
						}
					}
				}
			} else if (!node.isLeaf()) {
				IBTreeNode<K, V> parent = node;
				node = ((TreeNode<K, V>) parent).getChildren().get(i);
				if (node.getNumOfKeys() == t - 1) {
					IBTreeNode<K, V> lsibling = null;
					IBTreeNode<K, V> rSibling = null;
					if (i != 0)
						lsibling = ((TreeNode<K, V>) parent).getChildren().get(i - 1);
					if (i < parent.getNumOfKeys())
						rSibling = ((TreeNode<K, V>) parent).getChildren().get(i + 1);
					if (lsibling != null && lsibling.getNumOfKeys() >= t) {
						((TreeNode<K, V>) node).insertKey(0, (parent).getKeys().get(i - 1),(parent).getValues().get(i - 1));
						((TreeNode<K, V>) parent).removeKey(i - 1);
						if (!node.isLeaf()) {
							((TreeNode<K, V>) node).insertChild(0, (lsibling).getChildren().get(lsibling.getNumOfKeys()));
							((TreeNode<K, V>) lsibling).removeChild(lsibling.getNumOfKeys());
						}
						((TreeNode<K, V>) parent).insertKey(i - 1, (lsibling).getKeys().get(lsibling.getNumOfKeys() - 1),
								(lsibling).getValues().get(lsibling.getNumOfKeys() - 1));
						((TreeNode<K, V>) lsibling).removeKey(lsibling.getNumOfKeys() - 1);
					} else if (rSibling != null && rSibling.getNumOfKeys() >= t) {
						((TreeNode<K, V>) node).insertKey(t - 1, (parent).getKeys().get(i),(parent).getValues().get(i));
						((TreeNode<K, V>) parent).removeKey(i);
						if (!node.isLeaf()) {
							((TreeNode<K, V>) node).insertChild(t, ((TreeNode<K, V>)rSibling).getChildren().get(0));
							((TreeNode<K, V>) rSibling).removeChild(0);
						}
						((TreeNode<K, V>) parent).insertKey(i, (rSibling).getKeys().get(0), (rSibling).getValues().get(0));
						((TreeNode<K, V>) rSibling).removeKey(0);
					} else {
						if (lsibling != null) {
							((TreeNode<K, V>) node).insertKey(0, (parent).getKeys().get(i - 1),(parent).getValues().get(i - 1));
							((TreeNode<K, V>) parent).removeKey(i - 1);
							((TreeNode<K, V>) parent).removeChild(i - 1);
							for (int j = t - 2; j >= 0; j--) {
								((TreeNode<K, V>) node).insertKey(0, (lsibling).getKeys().get(j),(lsibling).getValues().get(j));
							}
							if (!node.isLeaf()) {
								for (int j = t - 1; j >= 0; j--)
									((TreeNode<K, V>) node).insertChild(0, (lsibling).getChildren().get(j));
							}
						} else {
							((TreeNode<K, V>) node).insertKey(t - 1, ( parent).getKeys().get(i),(parent).getValues().get(i));
							((TreeNode<K, V>) parent).removeKey(i);
							((TreeNode<K, V>) parent).removeChild(i + 1);
							for (int j = 0; j < t - 1; j++) {
								((TreeNode<K, V>) node).insertKey(j + t, ( rSibling).getKeys().get(j),(rSibling).getValues().get(j));
							}
							if (!node.isLeaf()) {
								for (int j = 0; j < t; j++)
									((TreeNode<K, V>) node).insertChild(j + t, ((TreeNode<K, V>) rSibling).getChildren().get(j));
							}
						}
						if (root.getNumOfKeys() == 0){
							root = node;
						}
					}
				}
			} else
				//the key is not found in the tree.
				return false;
		}
	}

	
	private void merge(IBTreeNode<K, V> ibTreeNode,
			IBTreeNode<K, V> ibTreeNode2, IBTreeNode<K, V> x_search2, int i,boolean remove){
		ibTreeNode.getKeys().add(x_search2.getKeys().get(i));
		ibTreeNode.getValues().add(x_search2.getValues().get(i));
		for(int j=0;j<ibTreeNode2.getNumOfKeys();j++){
	    	ibTreeNode.getKeys().add(ibTreeNode2.getKeys().get(j));
	    	ibTreeNode.getValues().add(ibTreeNode2.getValues().get(j));
	    }
		for(int j=0;j<ibTreeNode2.getChildren().size();j++){
			ibTreeNode.getChildren().add(ibTreeNode2.getChildren().get(j));
		}
	    x_search2.getChildren().set(i,ibTreeNode );
	    x_search2.getChildren().remove(i+1);
	    if(remove){
	    x_search2.getKeys().remove(i);
	    x_search2.getValues().remove(i);
	    x_search2.setNumOfKeys(x_search2.getNumOfKeys()-1);
	    }
	    if(root.getNumOfKeys()==0)
	    {   formatted=true;
	    	root=ibTreeNode;
	    	rooted=true;
	    }
	    ibTreeNode.setNumOfKeys(ibTreeNode.getNumOfKeys()+ibTreeNode2.getNumOfKeys()+1);
	    if(x_search2.getNumOfKeys()<t)
	    {

	    }
	}
	private boolean deleteNode(IBTreeNode<K, V> node,K key){
		int i=1;
		formatted=false;
	    rooted=false;
		boolean found=false;
		if(node==null)
		{
			System.out.println("hhh");
		}
		if(node.isLeaf()){
			while(i <= node.getNumOfKeys()){
	        	K y = node.getKeys().get(i-1);
	        	if(key.compareTo(y)==0){
	        		found=true;
	        		break;
	        	}
	        	i++;
	        }
			if(!found){
				return false;
			}
			if(node.getNumOfKeys()==t-1&&node!=root){
				int number=0;
              while(number<lastParent.getNumOfKeys()){
            	  if(lastParent.getKeys().get(number).compareTo(key)>=0)
            	  break;
        		  number++;

              }
              number++;
              int size=0;
              if(number>1&&lastParent.getChildren().get(number-1).getNumOfKeys()==t-1&&lastParent.getChildren().get(number-2).getNumOfKeys()==t-1){
        		   size=lastParent.getChildren().get(number-2).getKeys().size();
        		  
        	  mergeNodes(lastParent.getChildren().get(number-2), lastParent.getChildren().get(number-1), lastParent,  number-2,false);
        	  lastParent.getKeys().remove(number-2);
        	  lastParent.getValues().remove(number-2);
        	  delete(key);
        	  if(lastParent.getKeys().size()<t-1){
        		  fix(root, lastParent, key);
        	  }
        	  if(root==lastParent){
        		  root=lastParent.getChildren().get(number-2);
        	  }
              }
              else if(lastParent.getChildren().size()>=number+1&&lastParent.getChildren().get(number-1).getNumOfKeys()==t-1&&lastParent.getChildren().get(number).getNumOfKeys()==t-1){
            	  size=lastParent.getChildren().get(number-1).getKeys().size();
            	  mergeNodes(lastParent.getChildren().get(number-1), lastParent.getChildren().get(number), lastParent,  number-1,false);
            	  lastParent.getKeys().remove(number-1);
            	  lastParent.getValues().remove(number-1);
            	  delete(key);
            	  if(lastParent.getKeys().size()<t-1){
            		  fix(root, lastParent, key);
            	  }
            	  if(root.getKeys().size()==0){
            		  root=lastParent.getChildren().get(number-1);
            	  }
                  size=size++;
                  size=size--;
            	  }
              else if(number>1&&lastParent.getChildren().get(number-2).getNumOfKeys()>t-1&&lastParent.getChildren().get(number-1).getNumOfKeys()==t-1){
            	  K keyTemp=lastParent.getChildren().get(number-2).getKeys().get(lastParent.getChildren().get(number-2).getNumOfKeys()-1);
            	  V valueTemp=lastParent.getChildren().get(number-2).getValues().get(lastParent.getChildren().get(number-2).getNumOfKeys()-1);
            	  lastParent.getChildren().get(number-2).getKeys().remove(lastParent.getChildren().get(number-2).getNumOfKeys()-1);
            	  lastParent.getChildren().get(number-2).getValues().remove(lastParent.getChildren().get(number-2).getNumOfKeys()-1);
            	  lastParent.getChildren().get(number-2).setNumOfKeys(lastParent.getChildren().get(number-2).getNumOfKeys()-1);
            	  K keyTemp2=lastParent.getKeys().get(number-2);
            	  V valueTemp2=lastParent.getChildren().get(number-2).getValues().get(0);
            	  lastParent.getKeys().set(number-2, keyTemp);
            	  lastParent.getValues().set(number-2, valueTemp);
            	  lastParent.getChildren().get(number-1).getKeys().remove(i-1);
            	  lastParent.getChildren().get(number-1).getKeys().add(0,keyTemp2);
            	  lastParent.getChildren().get(number-1).getValues().remove(i-1);
            	  lastParent.getChildren().get(number-1).getValues().add(0,valueTemp2);
              }
              else if(lastParent.getChildren().size()>=number+1&&lastParent.getChildren().get(number-1).getNumOfKeys()==t-1&&lastParent.getChildren().get(number).getNumOfKeys()>t-1){
            	  K keyTemp=lastParent.getChildren().get(number).getKeys().get(0);
            	  V valueTemp=lastParent.getChildren().get(number).getValues().get(0);
            	  lastParent.getChildren().get(number).getKeys().remove(0);
            	  lastParent.getChildren().get(number).getValues().remove(0);
            	  lastParent.getChildren().get(number).setNumOfKeys(lastParent.getChildren().get(number).getNumOfKeys()-1);
            	  K keyTemp2=lastParent.getKeys().get(number-1);
            	  V valueTemp2=lastParent.getChildren().get(number-1).getValues().get(0);
            	  lastParent.getKeys().set(number-1, keyTemp);
            	  lastParent.getValues().set(number-1, valueTemp);
            	  lastParent.getChildren().get(number-1).getKeys().remove(i-1);
            	  lastParent.getChildren().get(number-1).getKeys().add(keyTemp2);
            	  lastParent.getChildren().get(number-1).getValues().remove(i-1);
            	  lastParent.getChildren().get(number-1).getValues().add(valueTemp2);
              }
			}
			else{
			node.getKeys().remove(i-1);
			node.getValues().remove(i-1);
			node.setNumOfKeys(node.getNumOfKeys()-1);
			if(grandFather!=null){
			IBTreeNode<K, V> node1=grandFather.getChildren().get(index2);
			IBTreeNode<K, V> node2=null;
			IBTreeNode<K, V> node3=null;
			if(grandFather.getChildren().size()<index2+2){}
			else{
			node2=grandFather.getChildren().get(index2+1);
			}
			if(index2!=0){
				node3=grandFather.getChildren().get(index2-1);
			}
			if(node1.getNumOfKeys()==t-1){
				if(node2!=null&&node2.getNumOfKeys()==t-1){
					merge(node1, node2, grandFather, index2, true);
				}
				else if(node2!=null&&node2.getNumOfKeys()==t-1){
					merge(node1, node3, grandFather, index2+1, true);
				}
			}
			}
		}}
		else{
			searchNode(key,node);
			int size=lastParent.getNumOfKeys();
			while(i <= node.getNumOfKeys()){
	        	K y = node.getKeys().get(i-1);
	        	if(key.compareTo(y)==0){
	        		found=true;
	        		break;
	        	}
	        	i++;
	        }
			if(!found)
				return false;
			if(pre){
				IBTreeNode<K, V> node3 =predessecor(lastParent);
			size=node3.getKeys().size();
			node.getKeys().set(i-1, node3.getKeys().get(size-1));
			node.getValues().set(i-1, node3.getValues().get(size-1));
			deleteNode(node3, node3.getKeys().get(size-1));}
			else{
				IBTreeNode<K, V> node3=successecor(lastParent);
				node.getKeys().set(i-1, node3.getKeys().get(0));
				node.getValues().set(i-1, node3.getValues().get(0));
				deleteNode(node3, node3.getKeys().get(0));}
		}
		return false;
		
	}
	private IBTreeNode<K, V> successecor(IBTreeNode<K, V> node) {
		IBTreeNode<K, V> node2=node;
		while(!node2.isLeaf()){
			lastParent=node2;
			node2=node2.getChildren().get(0);
		}
		return node2;
	}
	private IBTreeNode<K, V> predessecor(IBTreeNode<K, V> node) {
		IBTreeNode<K, V> node2=node;
		while(!node2.isLeaf()){
			lastParent=node2;
			node2=node2.getChildren().get(node2.getChildren().size()-1);
		}
		return node2;

	}
	private void fix(IBTreeNode<K, V> node,IBTreeNode<K, V> node2,K key){
		merge=!!merge;
    	rooted=!!rooted;
    	formatted=!!formatted;
		if(node.getKeys().contains(key))
		{
		return;
		}
		boolean fixed=false;
		int number=0;
		while(number<node.getChildren().size()-1&&node.getKeys().get(number).compareTo(key)<=0){
			number++;	
		}
		number++;
		int size=0;
		size=size+1;
 		size--;
		if(node.getChildren().isEmpty())
			return;
 		if(number>1&&node.getChildren().get(number-1).getNumOfKeys()<=t-1&&node.getChildren().get(number-2).getNumOfKeys()<=t-1){
  		  size=node.getChildren().get(number-2).getKeys().size();
      	  merge(node.getChildren().get(number-2), node.getChildren().get(number-1), node,  number-2,true);
      	  node2=node.getChildren().get(number-2);
        fixed=true;
		}
        else if(node.getChildren().size()>=number+1&&node.getChildren().get(number-1).getNumOfKeys()<=t-1&&node.getChildren().get(number).getNumOfKeys()<=t-1){
      		  merge(node.getChildren().get(number-1), node.getChildren().get(number), node,  number-1,true);
              fixed=true;
          	  node2=node.getChildren().get(number-1);
        }
        else if(number>1&&node.getChildren().get(number-2).getNumOfKeys()>t-1&&node.getChildren().get(number-1).getNumOfKeys()<t-1){
      	  K keyTemp=node.getChildren().get(number-2).getKeys().get(node.getChildren().get(number-2).getNumOfKeys()-1);
      	  V valueTemp=node.getChildren().get(number-2).getValues().get(node.getChildren().get(number-2).getNumOfKeys()-1);
      	  node.getChildren().get(number-2).setNumOfKeys(node.getChildren().get(number-2).getNumOfKeys()-1);
      	  K keyTemp2=node.getKeys().get(number-2);
      	  V valueTemp2=node.getChildren().get(number-2).getValues().get(0);
      	  node.getKeys().set(number-2, keyTemp);
      	  node.getValues().set(number-2, valueTemp);
      	  node.getChildren().get(number-1).getChildren().add(0,node.getChildren().get(number-2).getChildren().get(node.getChildren().get(number-2).getChildren().size()-1));
      	node.getChildren().get(number-2).getChildren().remove(node.getChildren().get(number-2).getChildren().size()-1);
      	  node.getChildren().get(number-2).getKeys().remove(node.getChildren().get(number-2).getKeys().size()-1);
      	  node.getChildren().get(number-2).getValues().remove(node.getChildren().get(number-2).getKeys().size()-1);
      	  node.getChildren().get(number-1).getKeys().add(0,keyTemp2);
      	  node.getChildren().get(number-1).getValues().add(0,valueTemp2);
      	  node.getChildren().get(number-1).setNumOfKeys(node.getChildren().get(number-1).getNumOfKeys()+1);
          fixed=true;
      	  node2=node.getChildren().get(number-2);
        }
        else if(node.getChildren().size()>=number+1&&node.getChildren().get(number-1).getNumOfKeys()<t-1&&node.getChildren().get(number).getNumOfKeys()>t-1){
      	  K keyTemp=node.getChildren().get(number).getKeys().get(0);
      	  V valueTemp=node.getChildren().get(number).getValues().get(0);
      	  node.getChildren().get(number).setNumOfKeys(node.getChildren().get(number).getNumOfKeys()-1);
      	  K keyTemp2=node.getKeys().get(number-1);
      	  V valueTemp2=node.getChildren().get(number-1).getValues().get(0);
      	  node.getKeys().set(number-1, keyTemp);
      	  node.getValues().set(number-1, valueTemp);
      	  node.getChildren().get(number-1).getChildren().add(node.getChildren().get(number).getChildren().get(0));
          node.getChildren().get(number).getChildren().remove(0);
      	  node.getChildren().get(number).getKeys().remove(0);
    	  node.getChildren().get(number).getValues().remove(0);
      	  node.getChildren().get(number-1).getKeys().add(keyTemp2);
      	  node.getChildren().get(number-1).getValues().add(valueTemp2);
      	  node.getChildren().get(number-1).setNumOfKeys(node.getChildren().get(number-1).getNumOfKeys()+1);
          fixed=true;
      	  node2=node.getChildren().get(number-1);
        }
		if(fixed)
		fix(root,node2,key);
		else 
			{if(node.getChildren().get(number-1)!=node&&node.getKeys().size()>0){
			fix(node.getChildren().get(number-1),node2,key);
			}
			}
	}
    public void split(IBTreeNode<K, V> x ,int i){
    	IBTreeNode<K, V> z= new TreeNode<K,V>();
    	IBTreeNode<K, V> y = x.getChildren().get(i);
    	z.setLeaf(y.isLeaf());
    	z.setNumOfKeys(t-1);
    	for(int j=0 ;j<t-1;j++){
    		java.util.List<K> keys = z.getKeys();
    		java.util.List<V> values = z.getValues();
    		keys.add(y.getKeys().get(j+t));
    		values.add(y.getValues().get(j+t));
    	}
    	if(!y.isLeaf()){
    		for(int j=0 ;j<t;j++){
        		java.util.List<IBTreeNode<K, V>> children = z.getChildren();
        		children.add(y.getChildren().get(j+t));
    		}
    	}
    	y.setNumOfKeys(t-1);
		
    	x.getChildren().add(i+1, z);
    	x.getKeys().add(i, y.getKeys().get(t-1));
    	x.getValues().add(i, y.getValues().get(t-1));
    	x.setNumOfKeys(x.getNumOfKeys()+1);
    	ArrayList <K> keys=new ArrayList<K>();
    	ArrayList <V> values=new ArrayList<V>();
    	ArrayList <IBTreeNode<K, V>>  children=new ArrayList<IBTreeNode<K, V>> ();
    	for(int j=0;j<t-1;j++){
    		keys.add(y.getKeys().get(j));
    		values.add(y.getValues().get(j));
    	}
    	for(int j=0;j<y.getChildren().size();j++){
    		if(children.size()<t)
    		children.add(y.getChildren().get(j));
    	}
    	y.setKeys(keys);
		y.setValues(values);
		y.setChildren(children);
    }
}
