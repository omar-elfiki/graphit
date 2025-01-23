

// A Java program to find bridges in a given undirected graph
import java.util.*;

// This class represents a undirected graph using adjacency list
// representation
public class Bridge
{
    private int V;   // No. of vertices

    // Array  of lists for Adjacency List Representation
    private final LinkedList[] adj;
    int time = 0;
    static final int NIL = -1;
    int[][] bridges;
    public int count = 0;
    boolean bool=false;
    // Constructor
    public Bridge(int v, int[][]adjList)
    {
        V = v;
        bridges = new int[V][V];
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList<>();
        for(int i=0; i<v; i++){
            for (int j = 0; j < v; j++) {
                if(adjList[i][j]==1){
                    addEdge(i, j);
                }
            }
        }
    }

    // Function to add an edge into the graph
    void addEdge(int v, int w)
    {
        adj[v].add(w);  // Add w to v's list.
        adj[w].add(v);    //Add v to w's list
    }

    // A recursive function that finds and prints bridges
    // using DFS traversal
    // u --> The vertex to be visited next
    // visited[] --> keeps track of visited vertices
    // disc[] --> Stores discovery times of visited vertices
    // parent[] --> Stores parent vertices in DFS tree
    void bridgeUtil(int u, boolean visited[], int disc[],
                    int low[], int parent[])
    {

        // Mark the current node as visited
        visited[u] = true;

        // Initialize discovery time and low value
        disc[u] = low[u] = ++time;

        // Go through all vertices adjacent to this
        Iterator<Integer> i = adj[u].iterator();
        while (i.hasNext())
        {
            int v = i.next();  // v is current adjacent of u

            // If v is not visited yet, then make it a child
            // of u in DFS tree and recur for it.
            // If v is not visited yet, then recur for it
            if (!visited[v])
            {
                parent[v] = u;
                bridgeUtil(v, visited, disc, low, parent);

                // Check if the subtree rooted with v has a
                // connection to one of the ancestors of u
                low[u]  = Math.min(low[u], low[v]);

                // If the lowest vertex reachable from subtree
                // under v is below u in DFS tree, then u-v is
                // a bridge
                if (low[v] > disc[u]){
                    bridges[u][v]=1;
                    bridges[v][u]=1;
                    count++;
                    bool=true;
                }

            }

            // Update low value of u for parent function calls.
            else if (v != parent[u])
                low[u]  = Math.min(low[u], disc[v]);
        }
    }


    // DFS based function to find all bridges. It uses recursive
    public void bridgeCalculate()
    {
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];
        int[] disc = new int[V];
        int[] low = new int[V];
        int[] parent = new int[V];


        // Initialize parent and visited, and ap(articulation point)
        // arrays
        for (int i = 0; i < V; i++)
        {
            parent[i] = NIL;
            visited[i] = false;
        }

        // Call the recursive helper function to find Bridges
        // in DFS tree rooted with vertex 'i'
        for (int i = 0; i < V; i++)
            if (!visited[i])
                bridgeUtil(i, visited, disc, low, parent);
    }

    public void printBridges(int[][]adjList){
        for(int i=0; i<V; i++){
            for(int j=0; j<V; j++){
                if(bridges[i][j]==1){
                    System.out.println("bridge "+i+" "+j+" bridge");
                }
            }
        }
        System.out.println();
        for(int i=0; i<V; i++){
            for(int j=0; j<V; j++){
                System.out.println(i+ " " + j + " "+ adjList[i][j] + " ");
            }
        }
    }

    public int[][] giveFixedMatrix(int[][]adjList){
        int [][] fixedMatrix = new int[V][V];
        for (int i = 0; i < V; i++) {
            System.arraycopy(adjList[i], 0, fixedMatrix[i], 0, V);
        }
        for(int i=0; i<V; i++){
            for(int j=0; j<V; j++){
                if(bridges[i][j]==1){
                    fixedMatrix[i][j]=0;
                }
            }
        }
        return fixedMatrix;
    }

}