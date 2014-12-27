/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class DoublyLinkedList <E> {
	
	private DLLNode <E> head,tail;
	private int size;
	
	/**
	 * Constructor
	 */
	public DoublyLinkedList() {
		size = 0;
		head = null;
		tail = null;
	}
	
	/**
	 * Add to the tail
	 * @param data
	 * @return added node
	 */
	public DLLNode<E> add(E data){
		DLLNode<E> node = new DLLNode<E>(data);
		
		// If list is empty, add to the head
		if(size == 0){
			head = node;
		
		// If list is not empty, add to the tail
		}else{
			tail.next = node;
			node.previous = tail;
		}
		
		// Adjust the tail and size
		tail = node;
		size++;
		return node;
	}
	
	/**
	 * Remove a node
	 * @param node
	 */
	public void remove(DLLNode<E> node){
		
		// If removing the head
		if(head == node){
			
			// If only one node in the list, then make head points to null
			if(size == 1){
				head = null;
			
			// If more then one node exits, the head points to the second node
			}else{
				node.next.previous = null;
				head = node.next;
			}
			
		// If removing the tail, make tail points to second to last
		}else if(tail == node){
			node.previous.next = null;
			tail = node.previous;
			
		// If removing in the middle
		}else{
			node.previous.next = node.next;
			node.next.previous =  node.previous;
		}
		
		// Destroy the node configuration and adjust list size
		node.destroy();
		size--;
	}
	
	/**
	 * Size of the DLL
	 * @return size
	 */
	public int size(){
		return size;
	}
	
	/**
	 * To String [1,2,3,4 ... ]
	 */
	public String toString(){
		String output = "[";
		DLLNode<E> tmp = head;
		
		while(tmp != null){
			output += tmp.toString();
			if(tmp.next != null)
				output += ", ";
			tmp = tmp.next;
		}
		output += "]";
		return output;
	}
	
	/**
	 * Iterator sits in between nodes or before head or after tail
	 * @return iterator
	 */
	public NodeIterator<E> iterator(){
		
		// Create an anonymous class that implements NodeIterator
		return new NodeIterator<E>() {
			DLLNode<E> position = head;
			
			/**
			 * Get next element in the list
			 */
			public E next(){
				DLLNode<E> node = position;
				position = position.next;
				return node.getData();
			}
			
			/**
			 * Checks if there's a next node
			 */
			public boolean hasNext(){
				return position != null;
			}
			
			/**
			 * Concatenate two list
			 * @param secondIter
			 * @return new list
			 */
			public NodeIterator<E> concatenate(NodeIterator<E> secondIter){
				DoublyLinkedList<E> newList = new DoublyLinkedList<E>();
				while(this.hasNext())
					newList.add(this.next());
				while(secondIter.hasNext())
					newList.add(secondIter.next());
				return newList.iterator();
			}
			
			/**
			 * Get size of iterator
			 */
			public int size(){
				return DoublyLinkedList.this.size();
			}
			
			/**
			 * to String inherits the outer class
			 */
			public String toString(){
				return DoublyLinkedList.this.toString();
			}
		};
	}
}
