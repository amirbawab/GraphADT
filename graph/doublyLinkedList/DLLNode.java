package graph.doublyLinkedList;

/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class DLLNode<E>{
	protected DLLNode<E> next, previous;
	private E data;
	
	/**
	 * Constructor
	 * @param data
	 * @param previous
	 * @param next
	 */
	protected DLLNode(E data, DLLNode<E> previous, DLLNode<E> next){
		this.data = data;
		this.previous = previous;
		this.next = next;
	}

	/**
	 * Overloaded constructor
	 * @param data
	 */
	protected DLLNode(E data){
		this(data,null,null);
	}
	
	/**
	 * Checks if there is a next node
	 * @return boolean
	 */
	public boolean hasNext(){
		return next != null;
	}
	
	/**
	 * Checks if there is a previous node
	 * @return boolean
	 */
	public boolean hasPrevious(){
		return previous != null;
	}
	
	/**
	 * Get the data stored in the node
	 * @return data in the node
	 */
	public E getData() {
		return data;
	}
	
	/**
	 * Set the data stored in the node
	 * @param data
	 */
	public void setData(E data) {
		this.data = data;
	}
	
	/**
	 * Get next
	 * @return next
	 */
	public DLLNode<E> next(){
		return next;
	}
	
	/**
	 * Get previous
	 * @return previous
	 */
	public DLLNode<E> previous(){
		return previous;
	}
	
	/**
	 * Destroy a node
	 */
	protected void destroy(){
		data = null;
		next = null;
		previous = null;
	}
	
	/** 
	 * To string <Object>
	 */
	public String toString(){
		return String.format("%s", data.toString());
	}
}