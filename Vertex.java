/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class Vertex <E> implements Comparable<Vertex<E>>{
	
	// Vertex attributes
	private E data;
	private DoublyLinkedList<Edge<E>> inEdges,outEdges;
	private DLLNode<Vertex<E>> position;
	private int status;
	private int color;
	private final int id;
	
	// Dijkstra options
	private Vertex<E> dijkstra_parent;
	private double dijkstra_value;
	private Edge<E> dijkstra_edge;
	
	// Status
	public static final int UNVISITED = 0;
	public static final int VISITING = 1;
	public static final int VISITED = 2;
	
	// Colors
	public static final int UNCOLORED = 0;
	
	/**
	 * Constructor
	 * @param data
	 * @param id
	 */
	public Vertex(E data, int id) {
		this.data = data;
		this.status = UNVISITED;
		this.color = 0;
		this.id = id;
		inEdges = new DoublyLinkedList<Edge<E>>();
		outEdges = new DoublyLinkedList<Edge<E>>();
	}
	
	/**
	 * Constructor
	 * @param data
	 */
	public Vertex(E data) {
		this(data,0);
	}
	
	/**
	 * Get neighbors of a node
	 * @return array of the neighbor vertices
	 */
	public Vertex<E>[] getNeighbors(){
		Vertex<E>[] neighbors = new Vertex[outEdges.size()];
		NodeIterator<Edge<E>> iter = outEdges.iterator();
		int index = 0;
		Edge<E> current = null;
		while(iter.hasNext()){
			current = iter.next();
			neighbors[index++] = current.getOpposite(this);
		}
		return neighbors;
	}
	
	/**
	 * Get outEdges/incident edges
	 * @return iterator on the out edges
	 */
	public NodeIterator<Edge<E>> getOutEdges(){
		return outEdges.iterator();
	}
	
	/**
	 * Get inEdges edges
	 * @return iterator on the in edges
	 */
	public NodeIterator<Edge<E>> getInEdges(){
		return inEdges.iterator();
	}
	
	/**
	 * Store all out edges
	 * @param e
	 * @return node where the edge has been stored
	 */
	public DLLNode<Edge<E>> addOutEdge(Edge<E> e){
		return outEdges.add(e);
	}
	
	/**
	 * Store all in edges
	 * @param e
	 * @return node where the edge has been stored
	 */
	public DLLNode<Edge<E>> addInEdge(Edge<E> e){
		return inEdges.add(e);
	}
	
	/**
	 * Remove an in edge
	 * @param node
	 */
	public void removeInEdge(DLLNode <Edge<E>> node){
		inEdges.remove(node);
	}
	
	/**
	 * Remove an out edge
	 * @param node
	 */
	public void removeOutEdge(DLLNode <Edge<E>> node){
		outEdges.remove(node);
	}
	
	/**
	 * Get data stored in the vertex
	 * @return data
	 */
	public E getData() {
		return data;
	}
	
	/**
	 * Get the node that stores this vertex
	 * @return node
	 */
	public DLLNode<Vertex<E>> getPosition() {
		return position;
	}
	
	/**
	 * Set the node that stores this vertex
	 * @param position
	 */
	public void setPosition(DLLNode<Vertex<E>> position) {
		this.position = position;
	}

	/**
	 * Set the data of this vertex
	 * @param data
	 */
	public void setData(E data) {
		this.data = data;
	}
	
	/**
	 * Get vertex status
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Set vertex status
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Get vertex color
	 * @return color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Set vertex color
	 * @param color
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * Get vertex parent after calling Dijkstra method
	 * @return parent of vertex
	 */
	public Vertex<E> getDijkstra_parent() {
		return dijkstra_parent;
	}

	/**
	 * Set the parent of a vertex when calling the Dijkstra method
	 * @param dijkstra_parent
	 */
	public void setDijkstra_parent(Vertex<E> dijkstra_parent) {
		this.dijkstra_parent = dijkstra_parent;
	}

	/**
	 * Get the accumulated edge weight when arriving to this vertex
	 * @return accumulated value of a vertex
	 */
	public double getDijkstra_value() {
		return dijkstra_value;
	}

	/**
	 * Set the Dijkstra value of a vertex
	 * @param dijkstra_value
	 */
	public void setDijkstra_value(double dijkstra_value) {
		this.dijkstra_value = dijkstra_value;
	}
	
	/**
	 * Get the edge that connects this vertex to its parent
	 * @return edge
	 */
	public Edge<E> getDijkstra_edge() {
		return dijkstra_edge;
	}

	/**
	 * Set the edge that connects this vertex to its parent
	 * @param dijkstra_edge
	 */
	public void setDijkstra_edge(Edge<E> dijkstra_edge) {
		this.dijkstra_edge = dijkstra_edge;
	}
	
	/**
	 * Get the vertex unique id
	 * @return id
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Compare vertices by Dijkstra value
	 * @param v
	 */
	public int compareTo(Vertex<E> v) {
		if(v.getDijkstra_value() == getDijkstra_value())
			return 0;
		else if(v.getDijkstra_value() < getDijkstra_value())
			return 1;
		else
			return -1;
	}	
	
	/**
	 * Gives data as String
	 */
	public String toString(){
		return String.format("<%s>", data.toString());
	}
}
