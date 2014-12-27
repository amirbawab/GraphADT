/**
* Graph ADT 
* Coded by Amir El Bawab
* Date: 22 December 2014
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
import java.io.FileNotFoundException;

public class GraphDriver {
	public static void main(String[] args) {
		try {
			Graph<String> graph = Graph.inParser("MIT.txt", true);
			
			System.out.println("Print graph state\n");
			System.out.println(graph);
			
			System.out.println("BFS:");
			for(Vertex<String> v : graph.BFS())
				System.out.print(v + " ");
			
			System.out.println("\n\nDFS");
			for(Vertex<String> v : graph.DFS())
				System.out.print(v + " ");
			
			System.out.println("\n\nIs connected: "+graph.isConnected());
			System.out.println("Is directed: "+graph.isDirected());
			System.out.println("Is cyclic: "+graph.isCyclic());
			System.out.println("Number of Connected components: "+graph.connectedComponents());
			
			System.out.println("\nClone graph ... ");
			Graph<String> cloned = graph.clone();
			
			System.out.println("Apply Transitive closure to the cloned graph");
			cloned.transitiveClosure();
			
			System.out.println("Print state of the new graph\n");
			System.out.println(cloned);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
