/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph <E> {
	
	// Graph content
	private DoublyLinkedList<Vertex <E>> vertexList;
	private DoublyLinkedList<Edge<E>> edgeList;
	
	// Graph options
	private boolean directed;
	private boolean isCyclic;
	private boolean isConnected;
	private int connectedComponents;
	
	// Unique id for each vertex in a graph. In a cloned graph, cloned vertices will have the same id
	private int unique_id = 0;
	
	/**
	 * Constructor
	 * @param directed true if the graph is directed, false if undirected
	 */
	public Graph(boolean directed) {
		vertexList = new DoublyLinkedList<Vertex<E>>();
		edgeList = new DoublyLinkedList<Edge<E>>();
		this.directed = directed;
	}
	
	/**
	 * Add vertex to the graph
	 * @param data
	 * @return Added vertex
	 */
	public Vertex<E> addVertex(E data){
		return addVertex(data, unique_id++);
	}
	
	/**
	 * Add Edge between two vertices
	 * @param v1
	 * @param v2
	 * @param label
	 * @param weight
	 * @return Array of 2 edges if the graph is undirected, array of 1 edge if the graph is directed
	 */
	public Edge<E>[] addEdge(Vertex<E> v1, Vertex<E> v2, String label, double weight){
		Edge<E> edges[] = new Edge[directed ? 1 : 2];
		
		// Create the first edge from v1 to v2 and set its configuration
		edges[0] = new Edge<E>(v1, v2);
		edges[0].setLabel(label);
		edges[0].setWeight(weight);
		edges[0].setPosition(edgeList.add(edges[0]));
		
		// If graph is undirected, create an edge in the opposite direction
		if(!directed){
			
			// Create the second edge from v2 to v1 and set its configuration
			edges[1] = new Edge<E>(v2, v1);
			edges[1].setLabel(label);
			edges[1].setWeight(weight);
			edges[1].setPosition(edgeList.add(edges[1]));
		}
		return edges;
	}
	
	/**
	 * Add Edge between two vertices
	 * @param v1
	 * @param v2
	 * @param label
	 * @return Array of 2 edges if the graph is undirected, array of 1 edge if the graph is directed
	 */
	public Edge<E>[] addEdge(Vertex<E> v1, Vertex<E> v2, String label){
		return addEdge(v1, v2, label, 0.0);
	}
	
	/**
	 * Add Edge between two vertices
	 * @param v1
	 * @param v2
	 * @param weight
	 * @return Array of 2 edges if the graph is undirected, array of 1 edge if the graph is directed
	 */
	public Edge<E>[] addEdge(Vertex<E> v1, Vertex<E> v2, double weight){
		return addEdge(v1, v2, null, weight);
	}
	
	/**
	 * Add Edge between two vertices
	 * @param v1
	 * @param v2
	 * @return Array of 2 edges if the graph is undirected, array of 1 edge if the graph is directed
	 */
	public Edge<E>[] addEdge(Vertex<E> v1, Vertex<E> v2){
		return addEdge(v1, v2, null);
	}
	
	/**
	 * Remove vertex
	 * @param vertex
	 */
	public void removeVertex(Vertex<E> vertex){
		NodeIterator<Edge<E>> iterOutEdges = vertex.getOutEdges();
		NodeIterator<Edge<E>> iterInEdges = vertex.getInEdges();
		
		while(iterOutEdges.hasNext()){
			Edge<E> currentE = iterOutEdges.next();
			Vertex<E> vTo = currentE.getV2();
			
			// Remove edge from inEdge of V2
			vTo.removeInEdge(currentE.getIncidentPositionV2());
			
			// Remove edge from the graph content
			edgeList.remove(currentE.getPosition());
		}
		
		while(iterInEdges.hasNext()){
			Edge<E> currentE = iterInEdges.next();
			Vertex<E> vFrom = currentE.getV1();
			
			// Remove edge from outEdge of V1
			vFrom.removeOutEdge(currentE.getIncidentPositionV1());
			
			// Remove edge from the graph content
			edgeList.remove(currentE.getPosition());
		}
		
		// Remove vertex
		vertexList.remove(vertex.getPosition());
	}
	
	/**
	 * Remove edge
	 * @param edge
	 */
	public void removeEdge(Edge<E> edge){
		edge.getV1().removeOutEdge(edge.getIncidentPositionV1());
		edge.getV2().removeInEdge(edge.getIncidentPositionV2());
		edgeList.remove(edge.getPosition());
	}
	
	/**
	 * Breadth-First-Search
	 * @return Array of vertices traversed by BFS
	 */
	public Vertex<E>[] BFS(){
		Vertex<E>[] BFS = new Vertex[vertexList.size()];
		int index = 0;
		
		// Mark all vertices as unvisited
		NodeIterator<Vertex<E>> iterV = vertices();
		while(iterV.hasNext())
			iterV.next().setStatus(Vertex.UNVISITED);
		
		// Mark all edges as undiscovered
		NodeIterator<Edge<E>> iterE = edges();
		while(iterE.hasNext())
			iterE.next().setStatus(Edge.UNDISCOVERED);
		
		// Start BFS
		iterV = vertices();
		while(iterV.hasNext()){
			Vertex<E> current = iterV.next();
			if(current.getStatus() == Vertex.UNVISITED){
				
				// Add the starting vertex and mark it as visiting
				Queue<Vertex<E>> q = new LinkedList<Vertex<E>>();
				q.add(current);
				current.setStatus(Vertex.VISITING);
				while(!q.isEmpty()){
					
					// Remove a vertex from the queue and mark it as visited
					Vertex<E> polled = q.poll();
					BFS[index++] = polled;
					polled.setStatus(Vertex.VISITED);
					
					// Iterator on all neighbors of the removed vertex and add them to the queue
					NodeIterator<Edge<E>> incidentEdges = polled.getOutEdges();
					while(incidentEdges.hasNext()){
						Edge<E> edge = incidentEdges.next();
						Vertex<E> oppositeVertex = edge.getV2();
						
						// If neighbor is not already visited, put it in the queue
						if(oppositeVertex.getStatus() == Vertex.UNVISITED){
							
							// Mark edge between the removed vertex and the current neighbor as discovered
							edge.setStatus(Edge.DISCOVERED);
							oppositeVertex.setStatus(Vertex.VISITING);
							q.offer(oppositeVertex);
						
						// If neighbor has already been visited, don't put it in the queue
						}else{
							
							// Mark edge as cross if undiscovered
							if(edge.getStatus() == Edge.UNDISCOVERED)
								edge.setStatus(Edge.CROSS);
						}
					}
				}
			}
		}
		return BFS;
	}
	
	/**
	 * Depth-First-Search
	 * @return Array of vertices traversed by DFS
	 */
	public Vertex<E>[] DFS(){
		Vertex<E>[] DFS = new Vertex[vertexList.size()];
		int index[] = {0};
		
		// Configure Graph options
		this.connectedComponents = 0;
		this.isCyclic = false;
		
		// Mark all vertices as unvisited and uncolored
		NodeIterator<Vertex<E>> iterV = vertices();
		while(iterV.hasNext()){
			Vertex<E> currentV = iterV.next();
			currentV.setStatus(Vertex.UNVISITED);
			currentV.setColor(Vertex.UNCOLORED);
		}
		
		// Mark all edges as undiscovered
		NodeIterator<Edge<E>> iterE = edges();
		while(iterE.hasNext())
			iterE.next().setStatus(Edge.UNDISCOVERED);
		
		// Start DFS
		iterV = vertices();
		while(iterV.hasNext()){
			Vertex<E> current = iterV.next();
			if(current.getStatus() == Vertex.UNVISITED){
				
				// +1 disconnected graph, trigger connection detection
				this.connectedComponents++;
				this.isConnected = this.connectedComponents == 1;
				DFS(current, DFS, index);
			}
		}
		return DFS;
	}
	
	/**
	 * Recursive DFS that generates the content of DFS[]
	 * @param v
	 * @param DFS
	 * @param index
	 */
	private void DFS(Vertex<E> v, Vertex<E>[] DFS, int[] index){
		// Color all vertices with the same color for each vertex start ((v0-> v1) <- v2) [for DiGraph]
		v.setColor(connectedComponents);
		v.setStatus(Vertex.VISITING);
		DFS[index[0]++] = v;
		
		// Iterate on all neighbors of the current vertex
		NodeIterator<Edge<E>> incidentEdges = v.getOutEdges();
		while(incidentEdges.hasNext()){
			Edge<E> edge = incidentEdges.next();
			Vertex<E> oppositeVertex = edge.getV2();
			
			// Recur on neighbor if not visited
			if(oppositeVertex.getStatus() == Vertex.UNVISITED){
				edge.setStatus(Edge.DISCOVERED);
				oppositeVertex.setStatus(Vertex.VISITING);
				DFS(oppositeVertex, DFS,index);
			}else{
				
				// Checks if the undirected/directed graph is cyclic
				if(
						(!directed && oppositeVertex.getStatus() == Vertex.VISITED) ||
						(directed && oppositeVertex.getStatus() == Vertex.VISITING && v.getColor() == oppositeVertex.getColor()) // Third condition is for DiGraph (Check earlier this method...)
				){
					isCyclic = true;
				}
				
				/// Mark edge as cross if the undiscovered
				if(edge.getStatus() == Edge.UNDISCOVERED)
					edge.setStatus(Edge.CROSS);
			}
		}
		
		// Mark vertex as visited if more neighbors needs to be visited
		v.setStatus(Vertex.VISITED);
	}
	
	/**
	 * Get an iterator for the list of vertices
	 * @return NodeIterator of vertices
	 */
	public NodeIterator<Vertex<E>> vertices() {
		return vertexList.iterator();
	}

	/**
	 * Get an iterator for the list of edges
	 * @return NodeIterator of edges
	 */
	public NodeIterator<Edge<E>> edges() {
		return edgeList.iterator();
	}
	
	/**
	 * Get an array of the list of vertices
	 * @return Array of vertices
	 */
	public Vertex<E>[] vertices_array(){
		Vertex<E>[] tmp = new Vertex[vertexList.size()];
		NodeIterator<Vertex<E>> iter = vertices();
		int index = 0;
		while(iter.hasNext())
			tmp[index++] = iter.next();
		return tmp;
	}
	
	/**
	 * Get an array of the list of vertices
	 * @return Array of vertices
	 */
	public Edge<E>[] edges_array(){
		Edge<E>[] tmp = new Edge[edgeList.size()];
		NodeIterator<Edge<E>> iter = edges();
		int index = 0;
		while(iter.hasNext())
			tmp[index++] = iter.next();
		return tmp;
	}

	/**
	 * Checks if the graph is directed or not
	 * @return boolean
	 */
	public boolean isDirected() {
		return directed;
	}
	
	/**
	 * Checks if the graph contains a cycle
	 * @return boolean
	 */
	public boolean isCyclic(){
		DFS();
		return isCyclic;
	}
	
	/**
	 * Checks if the graph is connected
	 * @return boolean
	 */
	public boolean isConnected(){
		if(directed)
			BFS_DiGraph_helper();
		else
			DFS();
		return isConnected;
	}
	
	/**
	 * Gives the number of connected components
	 * @return connected components
	 */
	public int connectedComponents(){
		if(directed)
			BFS_DiGraph_helper();
		else
			DFS();
		return connectedComponents;
	}
	
	/**
	 * Create the shortest path from a vertex to all other vertices
	 * @param v Starting vertex
	 */
	public void dijkstra(Vertex<E> v){
		
		// Mark all vertices as unvisited and reset Dijkstra options
		NodeIterator<Vertex<E>> iterV = vertices();
		while(iterV.hasNext()){
			Vertex<E> currentV = iterV.next();
			currentV.setStatus(Vertex.UNVISITED);
			currentV.setDijkstra_value(Double.MAX_VALUE);
			currentV.setDijkstra_parent(null);
		}
		
		// Mark all edges as undiscovered
		NodeIterator<Edge<E>> iterE = edges();
		while(iterE.hasNext())
			iterE.next().setStatus(Edge.UNDISCOVERED);
		
		// Mark the starting vertex
		v.setDijkstra_value(0);
		
		// Create the Priority Queue (Using a heap)
		PriorityQueue<Vertex<E>> pq = new PriorityQueue<>();
		
		// Start from the starting vertex by putting it in the Priority queue
		pq.offer(v);
		v.setStatus(Vertex.VISITING);
		v.setDijkstra_parent(v);
		while(!pq.isEmpty()){
			
			// Remove the vertex with minimum Dijkstra value
			Vertex<E> polled = pq.poll();
			v.setStatus(Vertex.VISITED);
			NodeIterator<Edge<E>> incidentEdges = polled.getOutEdges();
			
			// Put all the neighbors of the removed vertex in the Priority queue and adjust their Dijkstra value and parent
			while(incidentEdges.hasNext()){
				Edge<E> edge = incidentEdges.next();
				Vertex<E> oppositeVertex = edge.getV2();
				double pathCost = edge.getWeight() + polled.getDijkstra_value();
				
				// If the neighbor has not been visited, mark it visiting and adjust its configuration
				if(oppositeVertex.getStatus() == Vertex.UNVISITED){
					oppositeVertex.setDijkstra_value(pathCost);
					oppositeVertex.setDijkstra_edge(edge);
					edge.setStatus(Edge.DISCOVERED);
					oppositeVertex.setStatus(Vertex.VISITING);
					oppositeVertex.setDijkstra_parent(polled);
					pq.offer(oppositeVertex);
				
				// If the neighbor is still in the priority queue, check for minimum path cost, adjust if the cost can be reduced
				}else if(oppositeVertex.getStatus() == Vertex.VISITING){
					
					if(oppositeVertex.getDijkstra_value() > pathCost){
						oppositeVertex.setDijkstra_value(pathCost);
						edge.setStatus(Edge.DISCOVERED);
						oppositeVertex.setDijkstra_parent(polled);
						oppositeVertex.getDijkstra_edge().setStatus(Edge.FORWARD); // Mark previous edge as FORWARD
						oppositeVertex.setDijkstra_edge(edge); // Update edge that makes it shortest path
					}
				}
			}
		}
	}
	
	/**
	 * Get the shortest path from one vertex to another
	 * @param vFrom
	 * @param vTo
	 * @return Array of shortest edges to go from vFrom to vTo
	 */
	public Edge<E>[] dijkstra(Vertex<E> vFrom, Vertex<E> vTo){
		this.dijkstra(vFrom);
		Stack<Edge<E>> path = new Stack<>();
		Vertex<E> current = vTo;
		
		// Push the path in the stack in backward direction
		while(current.getDijkstra_edge() != null){
			path.push(current.getDijkstra_edge());
			current = current.getDijkstra_parent();
		}
		
		// Store path, in the correct direction, in an array
		Edge<E>[] edges = new Edge[path.size()];
		int index =  0;
		while(!path.isEmpty())
			edges[index++] = path.pop();
		return edges;
	}

	/**
	 * Checks if two vertices are adjacent
	 * @param v1 From 
	 * @param v2 To
	 * @return boolean
	 */
	public boolean areAdjacent(Vertex<E> v1, Vertex<E> v2){
		
		// If directed graph or size of v1 out edges < size of v2 out edges
		Vertex<E> v = directed || (v1.getOutEdges().size() < v2.getOutEdges().size()) ? v1 : v2;
		
		NodeIterator<Edge<E>> iterOutE = v.getOutEdges();
		while(iterOutE.hasNext())
			if( (v == v1 && iterOutE.next().getV2() == v2) || (v == v2 && iterOutE.next().getV2() == v1) )
				return true;
		return false;
	}
	
	/**
	 * Transitive Closure by Floyd–Warshall
	 * Idea: if (i->k->j) then create (i->j) if doesn't already exist
	 */
	public void transitiveClosure(){
		Vertex<E> vertices[] = this.vertices_array();
		for(int k = 0; k < vertices.length; k++){
			for(int i = 0; i < vertices.length; i++){
				// If i = k, then skip
				if(i == k) continue;
				
				// If i and k are adjacent, check for k and j
				if(areAdjacent(vertices[i], vertices[k])){
					for(int j = 0; j < vertices.length; j++){
						// If j = i or j = k, then skip
						if(j == i || j == k) continue;
						
						// If k and j are adjacent AND i and j are not adjacent, create an edge between i and j
						if(areAdjacent(vertices[k], vertices[j]) && !areAdjacent(vertices[i], vertices[j]))
							this.addEdge(vertices[i],vertices[j]);
					}
				}
			}
		}
	}
	
	/**
	 * Clone vertices and edges, but does not clone the data of the vertex
	 * @return cloned graph
	 */
	public Graph<E> clone(){
		
		// Create new graph, (To avoid edge duplication the graph is marked directed, but adjusted at the end)
		Graph<E> graph = new Graph<E>(true);
		
		// Clone Vertices
		NodeIterator<Vertex<E>> iterV = vertexList.iterator();
		while(iterV.hasNext()){
			Vertex<E> vertex = iterV.next();
			graph.addVertex(vertex.getData(), vertex.getID());
		}
		
		// Store vertices in an array so we can clone using unique id and indices
		Vertex<E> vertices[] = graph.vertices_array();
		
		// Clone Edges
		NodeIterator<Edge<E>> iterE = edgeList.iterator();
		while(iterE.hasNext()){
			Edge<E> currentE = iterE.next();
			
			// Binary search is used because the ID are sorted in ascending order
			// Note: Searching for vertices is required because any vertex can be removed, and gaps in IDs will be formed
			Vertex<E> v1 = BinarySearch(vertices, currentE.getV1().getID());
			Vertex<E> v2 = BinarySearch(vertices, currentE.getV2().getID());
			graph.addEdge(v1, v2, currentE.getLabel(), currentE.getWeight());
		}
		
		// Adjust the directed/undirected graph option
		graph.directed = directed;
		return graph;
	}

	/**
	 * Gives all the vertices and edges that form this graph
	 * @return String
	 */
	public String toString(){
		String output = "Vertices:\n";
		for(Vertex<E> v : vertices_array())
			output += String.format("%s ", v.toString());
		
		output += "\n\nEdges:\n";
		
		for(Edge<E> e : edges_array()){
			output += String.format("%s\n", e.toString());
		}
		return output;
	}
	
	//////////////////////////// I/O /////////////////////////////
	
	/**
	 * Read graph from input
	 * @param fileName
	 * @param directed
	 * @return Graph created
	 * @throws FileNotFoundException
	 */
	public static Graph<String> inParser (String fileName, boolean directed) throws FileNotFoundException{
		Graph<String> graph = new Graph<String>(directed);
		
		Scanner scan = new Scanner(new File(fileName));
		String readLine;
		Pattern pattern;
		Matcher matcher;
		
		readLine = scan.nextLine();
		pattern = Pattern.compile("size\\s*=\\s*(\\d+)");
		matcher = pattern.matcher(readLine);
		matcher.find();
		Vertex<String> vertices[] = new Vertex[Integer.parseInt(matcher.group(1))];
		
		while(!(readLine = scan.nextLine()).equals(";") ){
			pattern = Pattern.compile("([^0-9]*)\\s*(\\d+)\\s*=\\s*(.*)");
			matcher = pattern.matcher(readLine);
			matcher.find();
			if(matcher.group(1) == null || matcher.group(1).isEmpty()){
				vertices[Integer.parseInt(matcher.group(2))] = graph.addVertex(matcher.group(3));
			}else if(matcher.group(1).trim().equals("//") || matcher.group(1).trim().equals("#")){
				continue;
			}else{
				throw new InputMismatchException();
			}
		}
		
		while(!(readLine = scan.nextLine()).equals(";") ){
			pattern = Pattern.compile("(.*)\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*(,\\s*(\\d+|\\d+\\.\\d+)\\s*)?\\)(\\s*=\\s*(.*))?");
			matcher = pattern.matcher(readLine);
			matcher.find();
			if(matcher.group(1) == null || matcher.group(1).isEmpty()){
				double weight = 0.0;
				int v1Index = Integer.parseInt(matcher.group(2));
				int v2Index = Integer.parseInt(matcher.group(3));
				if(matcher.group(5) != null)
					weight = Double.parseDouble(matcher.group(5));
				String label = matcher.group(7);
				
				graph.addEdge(vertices[v1Index], vertices[v2Index], label, weight);
			}else if(matcher.group(1).trim().equals("//") || matcher.group(1).trim().equals("#")){
				continue;
			}else{
				throw new InputMismatchException();
			}
		}
		return graph;
	}
	
/////////////////////////////// HELPER ////////////////////////////////

	/**
	 * BFS for detecting connected components and is connected in DiGraphs
	 * Idea is to consider the DiGraph as UnDiGraph by concatenating the in and out edges
	 */
	private Vertex<E>[] BFS_DiGraph_helper() {
		Vertex<E>[] BFS = new Vertex[vertexList.size()];
		int index = 0;
		
		// Configure DiGraph options
		this.connectedComponents = 0;
		
		// Mark all vertices as unvisited
		NodeIterator<Vertex<E>> iterV = vertices();
		while (iterV.hasNext())
			iterV.next().setStatus(Vertex.UNVISITED);

		// Mark all edges as undiscovered
		NodeIterator<Edge<E>> iterE = edges();
		while (iterE.hasNext())
			iterE.next().setStatus(Edge.UNDISCOVERED);

		// Start BFS
		iterV = vertices();
		while (iterV.hasNext()) {
			Vertex<E> current = iterV.next();
			if (current.getStatus() == Vertex.UNVISITED) {
				
				// +1 disconnected graph, trigger connection detection
				this.connectedComponents++;
				this.isConnected = this.connectedComponents == 1;
				
				Queue<Vertex<E>> q = new LinkedList<Vertex<E>>();
				q.add(current);
				current.setStatus(Vertex.VISITING);
				while (!q.isEmpty()) {
					Vertex<E> polled = q.poll();
					BFS[index++] = polled;
					polled.setStatus(Vertex.VISITED);

					NodeIterator<Edge<E>> inOutEdges = polled.getOutEdges().concatenate(polled.getInEdges());
					while (inOutEdges.hasNext()) {
						Edge<E> edge = inOutEdges.next();
						Vertex<E> oppositeVertex = edge.getOpposite(polled);
						if (oppositeVertex.getStatus() == Vertex.UNVISITED) {
							edge.setStatus(Edge.DISCOVERED);
							oppositeVertex.setStatus(Vertex.VISITING);
							q.offer(oppositeVertex);
						} else {
							if (edge.getStatus() == Edge.UNDISCOVERED)
								edge.setStatus(Edge.CROSS);
						}
					}
				}
			}
		}
		return BFS;
	}
	
	/**
	 * Binary search for finding a vertex in an array of vertices using the vertex unique id
	 * @param array
	 * @param target
	 * @return target vertex
	 */
	private Vertex<E> BinarySearch(Vertex<E>[] vertices, int id){
		int left = 0;
		int right = vertices.length-1;
		int mid;
		while(left <= right){
			mid = (left + right) / 2;
			if(vertices[mid].getID() == id)
				return vertices[mid];
			if(vertices[mid].getID() < id)
				left = mid + 1;
			else
				right = mid - 1;
		}
		return null;
	}
	
	/**
	 * Add vertex to the graph with custom ID
	 * Private to avoid possible conflict if used manually
	 * @param data
	 * @return Vertex
	 */
	private Vertex<E> addVertex(E data, int id){
		Vertex<E> vertex = new Vertex<E>(data, id);
		DLLNode<Vertex<E>> node = vertexList.add(vertex);
		vertex.setPosition(node);
		return vertex;
	}
}
