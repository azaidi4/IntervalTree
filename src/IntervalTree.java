///////////////////////////////////////////////////////////////////////////////
//	Semester:		CS367 Spring 2017
//  PROJECT:		P4
//  FILE:			IntervalTree.java
//
//  TEAM:    16;
//
// Authors:
// Author1: Ahmad Zaidi, azaidi4@wisc.edu, azaidi4, LEC001
// Author2: Devin Samaranayake, dsamaranayak@wisc.edu>, dsamaranayak, LEC001
//
///////////////////////////////////////////////////////////////////////////////
import java.util.ArrayList;
import java.util.List;

/**
 * IntervalTree is a Binary Search tree. Each node in Interval tree contains an
 * interval. This class allows the user to perform various operations to modify
 * IntervalTree
 * @param <T> A Comparable type that can be used to indicate the start
 * and end times of an interval.
 */
public class IntervalTree<T extends Comparable<T>> implements IntervalTreeADT<T> {

    private IntervalNode<T> root;
	private int size;

	public IntervalTree(){
		this.root = null;
		this.size=0;
	}

    /** Returns the root node of this IntervalTree. */
	public IntervalNode<T> getRoot() {
		return root;
	}

    /**
     * Inserts a new IntervalNode with the given interval into IntervalTree.
     * Items are inserted based on the compare value obtained by comparing
     * start of the interval
     * @param interval the interval (item) to insert in the tree.
     * @throws IllegalArgumentException
     */
	public void insert(IntervalADT<T> interval)
					throws IllegalArgumentException {

		if(interval == null)
			throw new IllegalArgumentException();

		else{
			root = insertHelper(root, interval);
			size++;
		}
	}

    /**
     * Helper method used to insert node into IntervalTree
     * @param node the root used to traverse through the tree
     * @param interval the interval (item) to insert in the tree.
     * @return root node  of the modified tree.
     * @throws IllegalArgumentException
     */
	private IntervalNode<T> insertHelper(IntervalNode<T> node, IntervalADT<T> interval)
            throws IllegalArgumentException {

		if( node == null) 
			return new IntervalNode<T> (interval);

		if(interval.compareTo(node.getInterval()) == 0) // Duplicate Check
			throw new IllegalArgumentException();

		if(interval.compareTo(node.getInterval()) < 0) {
			node.setLeftNode(insertHelper(node.getLeftNode(), interval));
			if(interval.getEnd().compareTo(node.getMaxEnd()) > 0)
				node.setMaxEnd(interval.getEnd());
			return node;
		}
		else {
			node.setRightNode(insertHelper(node.getRightNode(), interval));
			if(interval.getEnd().compareTo(node.getMaxEnd()) > 0)
				node.setMaxEnd(interval.getEnd());
			return node;
		}
	}

    /**
     * This method deletes the node passed in the parameter and changes the
     * shape of the tree so that it follows BST shape and order properties
     * @param interval the interval (item) to insert in the tree.
     * @throws IntervalNotFoundException
     * @throws IllegalArgumentException
     */
	public void delete(IntervalADT<T> interval)
					throws IntervalNotFoundException, IllegalArgumentException {
		if(interval==null)
			throw new IllegalArgumentException();
		else {
			root = deleteHelper(root, interval);
			size--;
		}

	}

    /**
     * Helper method used to delete the the node with the matching interval
     * @param node the interval node that is currently being checked.
     *
     * @param interval the interval to delete.
     *
     * @return
     * @throws IntervalNotFoundException
     * @throws IllegalArgumentException
     */
	public IntervalNode<T> deleteHelper(IntervalNode<T> node,IntervalADT<T> interval)
					throws IntervalNotFoundException, IllegalArgumentException {

		if(node == null)
			throw new IntervalNotFoundException(interval.toString());

		if(interval.compareTo(node.getInterval()) == 0) {
		    if(node.getLeftNode() == null && node.getRightNode() == null)
		        return null;

            if(node.getRightNode() != null){
                node.setInterval(node.getSuccessor().getInterval());
                node.setRightNode(deleteHelper(node.getRightNode(),
                        node.getSuccessor().getInterval()));
                updateMaxEnd(node);
                return node;
            }
            else
                return node.getLeftNode();
        }

		if(interval.compareTo(node.getInterval()) < 0) {        //left subtree
			node.setLeftNode(deleteHelper(node.getLeftNode(), interval));
			node.setMaxEnd(updateMaxEnd(node));
		//	return node;
		}
		else if(interval.compareTo(node.getInterval()) > 0){   //right subtree
            node.setRightNode(deleteHelper(node.getRightNode(), interval));
            node.setMaxEnd(updateMaxEnd(node));
         //   return node;
        }
        return node;
	}

    /**
     * Used to update the maxEnd of a node after the deletion of a node
     * @param node the node whose maxEnd is being updated
     * @return
     */
	private T updateMaxEnd(IntervalNode<T> node) {
		ArrayList<T> temp = new ArrayList<T>();
		if(node.getLeftNode()!= null)
			temp.add(node.getLeftNode().getMaxEnd());
		if(node.getRightNode()!= null)
			temp.add(node.getRightNode().getMaxEnd());
		T swap = node.getInterval().getEnd();
		if(temp != null)
			for(T x : temp) {
				if(x.compareTo(swap)>0)
					swap=x;
			}
        return swap;
	}

    /**
     * Find and return a list of all intervals that overlap with the given interval.
     * @param interval the interval to search for overlapping
     *
     * @return
     */
	public List<IntervalADT<T>> findOverlapping(
					IntervalADT<T> interval) {
        ArrayList<IntervalADT<T>> result = new ArrayList<IntervalADT<T>>();

        findOverlappingHelper(root, interval, result);
		return result;

	}

    /**
     * Helper method used to return list of intervals overlapping with given interval
     * @param node whose interval is being checked
     * @param interval the interval which we are checking overlapping for
     * @param overlaps list containing overlaps
     */
	private void findOverlappingHelper(IntervalNode node, IntervalADT interval, ArrayList<IntervalADT<T>> overlaps) {
		if(node == null) return;

		if(node.getInterval().overlaps(interval) == true)
			overlaps.add(node.getInterval());
		
		if(node.getLeftNode() != null && node.getLeftNode().getMaxEnd().compareTo(interval.getStart()) != 0)
			findOverlappingHelper(node.getLeftNode(), interval, overlaps);
		
		if(node.getRightNode() != null && node.getRightNode().getMaxEnd().compareTo(interval.getStart()) != 0)
			findOverlappingHelper(node.getRightNode(), interval, overlaps);

		
	}
    /**
     * Get the size of the interval tree. The size is the total number of
     * nodes present in the tree.
     *
     * @return int number of nodes in the tree.
     */
	public List<IntervalADT<T>> searchPoint(T point) {
		ArrayList<IntervalADT<T>> searchList = new ArrayList<IntervalADT<T>>();
		
		searchPointHelper(root, point, searchList);
		return searchList;

	}

    /**
     * Helper method
     * @param node
     * @param point
     * @param searchList
     */
	private void searchPointHelper(IntervalNode node, T point, List searchList) {
		if(node.getInterval().contains(point))
			searchList.add(node.getInterval());
		if(node.getLeftNode() != null)
			searchPointHelper(node.getLeftNode(), point, searchList);
		if(node.getRightNode() != null)
			searchPointHelper(node.getRightNode(), point, searchList);
	}

    /**
     * Get the size of the interval tree. The size is the total number of
     * nodes present in the tree.
     *
     * @return
     */
    public int getSize() {
		return size;
	}

    /**
     * Return the height of the interval tree at the root of the tree.
     * @return
     */
	public int getHeight() {
		return getHeightHelper(root, 0);
	}

    /**
     * Helper method
     * @param node
     * @return
     */
	private int getHeightHelper(IntervalNode node, int height) {
        if(node == null)
            return 0;
        if(node.getLeftNode()!= null)
            return height+getHeightHelper(node.getLeftNode(),height)+1;

        if(node.getRightNode() != null)
            return height+getHeightHelper(node.getRightNode(), height)+1;
        return height+1;
	}

    /**
     * Returns true if the tree contains an exact match for the start and end of the given interval.
     * The label is not considered for this operation.
     * @param interval
     * 				target interval for which to search the tree for.
     * @return
     */
	public boolean contains(IntervalADT<T> interval) {
		return contains(root, interval);
	}

    /**
     * Helper method
     * @param node
     * @param interval
     * @return
     */
	private boolean contains(IntervalNode<T> node, IntervalADT<T> interval){
		if(node == null)
			return false;

		if(node.getInterval().equals(interval))
			return true;
		if(interval.compareTo(node.getInterval()) < 0)
			return contains(node.getLeftNode(), interval);

		return contains(node.getRightNode(),interval);
	}

    /**
     * Print the statistics of the tree in the below format
     * <pre>
     *	-----------------------------------------
     *	Height: 2
     *	Size: 3
     *	-----------------------------------------
     *	</pre>
     */
	public void printStats() {
        System.out.println("-----------------------------------------");
        System.out.println("Height: "+ getHeight());
		System.out.println("Size: "+ getSize());
        System.out.println("-----------------------------------------");
    }

}
