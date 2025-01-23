public class Backtrack {

	/* The global variables below are used in the Backtrack class
	 * int[][] col = Matrix that holds the connections of each vertex
	 * int n = number of vertices
	 * int edges = number of edges
	 * int[] colors = array that holds the color for each vertex
	 * int currentcolor = variable used to track the number of colors used
	 * boolean cycle = checks if a graph is a cycle
	 */
	private ReadGraph ReadGraph;
	private Bridge bridge;
	private final int[][] col;
	private int n;
	private final int edges;
	public int[] colors;
	public int chromaticNumber;
	public boolean cycle;
	public int chromaticNumberLowerbound;
	public int[][] adjacencyMatrix;

	//Constructor for the Backtrack class
	public Backtrack(int[][] adjMatrix, int edges) {
		adjacencyMatrix = new int[adjMatrix.length][adjMatrix.length];
		for (int i = 0; i < adjMatrix.length; i++) {
			System.arraycopy(adjMatrix[i], 0, adjacencyMatrix[i], 0, adjMatrix.length);
		}
		colors = new int[n]; //initializing color array (length will be the amount of vertices)
		n = adjMatrix.length;
		this.ReadGraph=ReadGraph;
		this.bridge=bridge;
		this.edges = edges;
		this.cycle = isCyclic(n, adjMatrix);
		col = new int[n][]; //initialize col matrix with the length of the amount of vertices
		//turns the adjacency matrix into an adjacency list inside col
		for (int i = 0; i < n; i++) {
			int connections = 0;
			for (int j = 0; j < n; j++) {
				if (adjMatrix[i][j] == 1)
					connections++; //if connection found, increase the counter
			}
			col[i] = new int[connections];
			int m = 0;
			for (int k = 0; k < n; k++) {
				if (adjMatrix[i][k] == 1) {
					col[i][m] = k; // each row number is the vertex number and the columns are the connections of that vertex
					m++;
				}
			}
		}
		colors = new int[n];
		for (int i = 0; i < n; i++)
			colors[i] = i + 1; //each vertex is assigned a color
		chromaticNumber = n;
		System.out.println("am ajuns"+edges);



		//chromaticNumber is set to the amount of vertices
	}

	//Utility method that performs a depth-first search (DFS) to detect a cycle starting from a given vertex.
	static boolean isCyclicUtil(int v, int[][] adjMatrix, boolean[] visited, int parent) {
		visited[v] = true;
		for (int i = 0; i < adjMatrix[v].length; i++) {
			if (adjMatrix[v][i] == 1) {
				if (!visited[i]) {
					if (isCyclicUtil(i, adjMatrix, visited, v))
						return true;
				} else if (i != parent) {
					return true;
				}
			}
		}
		return false;
	}

	//Method to check if a graph is cyclic
	static boolean isCyclic(int V, int[][] adjMatrix) {
		boolean[] visited = new boolean[V];
		for (int u = 0; u < V; u++) {
			if (!visited[u]) {
				if (isCyclicUtil(u, adjMatrix, visited, -1))	// call the isCycliUtil function and return the same output
					return true;
			}
		}
		return false;
	}


	//Method to search for the chromatic number
	public void search() {
		int[] colors = new int[n];
		//initialize the coloring array with -1
		for (int i = 1; i < n; i++)
			colors[i] = -1;
		colors[0] = 1; //make the first vertex color 1
		backtrackAlgorithm(1, 1, colors);
		LowerBound(colors, chromaticNumber);
	}

	private void backtrackAlgorithm(int currIndex, int currentColor, int[] colors) {

		//Edge case 1: If the number of edges = (n*(n-1))/2, then we have a full graph => chromatic number = n
		if (edges == (n * (n - 1)) / 2) {
			currentColor = n;
			return;
			//Edge case 2: If the number of edges = 0, then we have a graph with no edges => chromatic number = 1
		}
		if (edges <= 0) {
			if(edges==0){
				chromaticNumber = 1;
			}else{
				chromaticNumber=2;
			}

			return;
		}
		Bipartite bipartite =new Bipartite();
		if(bipartite.isBipartite(adjacencyMatrix,n)){
			chromaticNumber=2;
			System.out.print("Bipartite");
			this.colors = bipartite.color;
			return;
		}

		//System.out.print("Special");
		//Edge case 3: If the number of edges = n and n is odd, then we have a circle with n vertices => chromatic number = 3
		if (edges == n && n % 2 != 0) {
			if (isCyclic(n,col)){
				chromaticNumber = 3;
				System.out.print("Special"+edges);
			}
			return;
			//Edge case 4: If the number of edges = n and n is even, then we have a circle with n vertices => chromatic number = 2
		} else if (edges == n && n % 2 == 0) {
			if (isCyclic(n,col)){
				chromaticNumber = 2;
				System.out.print("Special"+edges);
			}
			return;
		}
		//If all vertices are colored, we check if the chromatic number is smaller than the previously one

		if (currIndex == n) {
			if (currentColor < chromaticNumber) {
				chromaticNumber = currentColor; //update the chromatic number
				//copy the coloring array
				if (n >= 0) System.arraycopy(colors, 0, this.colors, 0, n);
			}
			//If all vertices are not colored, and the chromatic number < previous one, we try to color the current vertex with colors from 1 to currentColor
		} else if (currentColor < chromaticNumber) {
			for (int c = 1; c <= currentColor; c++) {
				if (safeToAdd(currIndex, colors, c)) {
					colors[currIndex] = c; //if it is safe to give the vertex the color c, we assign it
					backtrackAlgorithm(currIndex + 1, currentColor, colors);
				}
			}
			//If the vertex cannot be colored with any of the colors from 1 to currentcolor, we assign it the color currentcolor+1
			currentColor++;
			colors[currIndex] = currentColor;
			backtrackAlgorithm(currIndex + 1, currentColor, colors);
		}
	}

	//Method to check if the color is not already assigned to a connected vertex, if not assigned, then it's safe to add
	private boolean safeToAdd(int vertex, int[] colors, int color) {
		for (int v : col[vertex]) {
			if (colors[v] == color)
				return false;
		}
		return true;
	}

	//it will be wrong if receive wrong chromaticnumber which will appear when backtrack meet a circle, test 05, 18 show that 
	public void LowerBound(int[]colors, int chromaticNumber){
		int[] color = new int[chromaticNumber + 1];
		int largestIndependenceNumber = 0;
		for (int k : colors) {
			for (int j = 1; j <= chromaticNumber; j++) {
				if (k == j) {
					color[j]++;
					break;
				}
			}
		}
		for(int i = 0; i < chromaticNumber + 1; i++){
			if(largestIndependenceNumber <= color[i]) {
				largestIndependenceNumber = color[i];
			}
		}
		System.out.println(largestIndependenceNumber+"dadada");
		chromaticNumberLowerbound = n/largestIndependenceNumber;
	}
}
