/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class Edge <E> {
	// Edge attributes
	private Vertex<E> v1, v2;
	private DLLNode<Edge<E>> incidentPositionV1, incidentPositionV2;
	private String label;
	private double weight;
	private DLLNode<Edge<E>> position;
	private int status;
	
	// Status of an edge
	public static final int UNDISCOVERED = 0;
	public static final int DISCOVERED = 1;
	public static final int BACK = 2;
	public static final int FORWARD = 3;
	public static final int CROSS = 4;
	
	/**
	 * Constructor
	 * @param v1 From vertex
	 * @param v2 To vertex
	 */
	public Edge(Vertex<E> v1, Vertex<E> v2){
		this.v1 = v1;
		this.v2 = v2;
		this.incidentPositionV1 = this.v1.addOutEdge(this);
		this.incidentPositionV2 = this.v2.addInEdge(this);
	}
	
	/**
	 * Get the opposite side of a vertex
	 * @param v
	 * @return Opposite vertex | null
	 */
	public Vertex<E> getOpposite(Vertex<E> v){
		if(v != v1 && v != v2)
			return null;
		return v1 == v ? v2 : v1;
	}
	
	/**
	 * Get 'from' vertex
	 * @return v1
	 */
	public Vertex<E> getV1() {
		return v1;
	}

	/**
	 * Get 'to' vertex
	 * @return v2
	 */
	public Vertex<E> getV2() {
		return v2;
	}

	/**
	 * Get the edge label
	 * @return String label of the edge
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the edge label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Get edge weight
	 * @return weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set edge weight
	 * @param weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Get position where the edge is stored
	 * @return reference of the node storing this edge
	 */
	public DLLNode<Edge<E>> getPosition() {
		return position;
	}

	/**
	 * Set position where the edge is stored
	 * @param node position
	 */
	public void setPosition(DLLNode<Edge<E>> position) {
		this.position = position;
	}

	/**
	 * Get status of the edge
	 * @return edge status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Get status of the edge as a string
	 * @return edge string status
	 */
	public String getStatusString() {
		String statusString[] = {"Undiscovered","Discovered", "Back", "Forward","Cross"};
		return statusString[status];
	}
	
	/**
	 * Set status of the edg0e
	 * @param status Edge status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Get position of this incident edge in v1
	 * @return node storing the edge in the out Edge of a vertex
	 */
	public DLLNode<Edge<E>> getIncidentPositionV1() {
		return incidentPositionV1;
	}

	/**
	 * Get position of this inEdge in v2
	 * @return node storing the edge in the in Edge of a vertex
	 */
	public DLLNode<Edge<E>> getIncidentPositionV2() {
		return incidentPositionV2;
	}

	/**
	 * Print data to String
	 */
	public String toString(){
		return label == null ? String.format("(%s, %s)", v1.toString(),v2.toString()) : String.format("(%s)", label);
	}
}
