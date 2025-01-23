import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;

public class ReadGraph {
    public final static boolean DEBUG = true;
    public final static String COMMENT = "//";

    File file;
    int numVertices = -1;
    int numEdges = -1;

    int chromaticNumberBacktrack = -1;
    int chromaticNumberGreedy = -1;
    int chromaticNumberLowerbound = -1;

    static int[] color;

    int[][] adjList;

    ColEdge[] e;

    Backtrack backtrackAlgorithm;
    greedyAlgorithm greedy;


    ReadGraph(File file) {
        this.file = file;
    }

    ReadGraph(int numVertices, int numEdges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
    }

    public static int[][] convert(ColEdge[] edges, int n) { //method used to convert the colEdge object to an adjacency matrix to make it easier to identify edges between vertices when backtracking
        int[][] adjMatrix = new int[n][n];
        for (ColEdge edge : edges) {
            if (edge.u >= 0 && edge.u <= n && edge.v >= 0 && edge.v <= n) {
                adjMatrix[edge.u - 1][edge.v - 1] = 1;
                adjMatrix[edge.v - 1][edge.u - 1] = 1;
            }
        }
        return adjMatrix;
    }

    public static ColEdge[] convert(int[][] adjMatrix) {
        int n = adjMatrix.length;
        int numEdges = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjMatrix[i][j] == 1) {
                    numEdges++;
                }
            }
        }
        ColEdge[] edges = new ColEdge[numEdges];
        int edgeIndex = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjMatrix[i][j] == 1) {
                    edges[edgeIndex] = new ColEdge();
                    edges[edgeIndex].u = i;
                    edges[edgeIndex].v = j;
                    edgeIndex++;
                }
            }
        }
        return edges;
    }

    public void readGraph() {
        if (file == null) {
            System.out.println("Error! No filename specified.");
            return;
        }

        boolean[] seen = null;

        // ! e will contain the edges of the graph
        ColEdge[] e = null;

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String record;

            // ! THe first few lines of the file are allowed to be comments, staring with a
            // // symbol.
            // ! These comments are only allowed at the top of the file.

            // ! -----------------------------------------
            while ((record = br.readLine()) != null) {
                if (record.startsWith("//"))
                    continue;
                break; // Saw a line that did not start with a comment -- time to start reading the
                // data in!
            }

            assert record != null;
            if (record.startsWith("VERTICES = ")) {
                numVertices = Integer.parseInt(record.substring(11));
                if (DEBUG)
                    System.out.println(COMMENT + " Number of vertices = " + numVertices);
            }

            seen = new boolean[numVertices + 1];

            record = br.readLine();

            if (record.startsWith("EDGES = ")) {
                numEdges = Integer.parseInt(record.substring(8));
                if (DEBUG)
                    System.out.println(COMMENT + " Expected number of edges = " + numEdges);
            }

            e = new ColEdge[numEdges];

            for (int d = 0; d < numEdges; d++) {
                if (DEBUG)
                    System.out.println(COMMENT + " Reading edge " + (d + 1));
                record = br.readLine();
                String[] data = record.split(" ");
                if (data.length != 2) {
                    System.out.println("Error! Malformed edge line: " + record);
                    System.exit(0);
                }
                e[d] = new ColEdge();

                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);

                seen[e[d].u] = true;
                seen[e[d].v] = true;

                if (DEBUG)
                    System.out.println(COMMENT + " Edge: " + e[d].u + " " + e[d].v);

            }

            String surplus = br.readLine();
            if (surplus != null) {
                if (surplus.length() >= 2)
                    if (DEBUG)
                        System.out.println(
                                COMMENT + " Warning: there appeared to be data in your file after the last edge: '"
                                        + surplus + "'");
            }

        } catch (IOException ex) {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file " + file);
            System.exit(0);
        }

        for (int x = 1; x <= numVertices; x++) {
            if (!seen[x]) {
                if (DEBUG)
                    System.out.println(COMMENT + " Warning: vertex " + x
                            + " didn't appear in any edge : it will be considered a disconnected vertex on its own.");
            }
        }

        this.e = e;
        adjList = convert(e, numVertices);
        System.out.println("Graph read successfully");
    }

    public void getResultExtended(int algorithm) {
        System.out.println("Calculating chromatic number...");
        Bridge bridge = new Bridge(numVertices, adjList);
        bridge.bridgeCalculate();
        int [][] badjList = bridge.giveFixedMatrix(adjList);
        int bnumEdges = numEdges - bridge.count;
        if(bnumEdges == 0){
            if(numEdges==bridge.count&&numEdges!=0){
                bnumEdges=-1;
            }
        }
        backtrackAlgorithm = new Backtrack(badjList, bnumEdges); //creating backtrackAlgorithm object with adjacency list and number of edges
        greedy = new greedyAlgorithm(e, numVertices, numEdges); //creating greedy object with ColEdge object, number of vertices and edges

        if (algorithm == 0) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future<?> future = executor.submit(backtrackAlgorithm::search);
                try {
                    future.get(30, TimeUnit.SECONDS); //will attempt to run backtrack.search() found at line 122
                    System.out.println("Chromatic Number: " + backtrackAlgorithm.chromaticNumber); //if successful, print the chromatic number and upperbound/lowerbound
                    chromaticNumberBacktrack = backtrackAlgorithm.chromaticNumber;
                    System.out.println("Upper-bound: " + greedy.chromaticNumber);
                    chromaticNumberGreedy = greedy.chromaticNumber;
                    System.out.println("Lower-bound: " + backtrackAlgorithm.chromaticNumberLowerbound);
                    chromaticNumberLowerbound = backtrackAlgorithm.chromaticNumberLowerbound;

                } catch (TimeoutException te) { //if the search times out, print the upperbound and lowerbound
                    System.out.println("Exact search timed out. Calculating upper-bound.");
                    System.out.println("Upper-bound: " + greedy.chromaticNumber);
                    chromaticNumberGreedy = greedy.chromaticNumber;
                    System.out.println("Lower-bound: " + greedy.chromaticNumberLowerbound);
                    chromaticNumberLowerbound = backtrackAlgorithm.chromaticNumberLowerbound;

                } catch (InterruptedException | ExecutionException ie) {
                    //prints error stack track if there is an error
                    System.out.println("Error! Problem reading graph" + ie);
                }
            } finally {
                executor.shutdown(); //shutdown the executor when complete
            }
        } else if (algorithm == 1) {
            System.out.println("Upper-bound: " + greedy.chromaticNumber);
            chromaticNumberGreedy = greedy.chromaticNumber;
            System.out.println("Lower-bound: " + greedy.chromaticNumberLowerbound);
            chromaticNumberLowerbound = greedy.chromaticNumberLowerbound;
        }
    }

    public void setAdjList(int[][] adjList) {
        this.adjList = adjList;
        e = convert(adjList);
    }

    public int getGraphData() {
        backtrackAlgorithm = new Backtrack(adjList, numEdges); //creating backtrackAlgorithm object with adjacency list and number of edges
        oldgreedyAlgorithm greedy = new oldgreedyAlgorithm(e, numVertices, numEdges); //creating greedy object with ColEdge object, number of vertices and edges

        ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<?> future = executor.submit(backtrackAlgorithm::search);
			try {
				future.get(10, TimeUnit.SECONDS); //will attempt to run backtrack.search() found at line 122
				System.out.println("Chromatic Number: " + backtrackAlgorithm.chromaticNumber); //if successful, print the chromatic number and upperbound/lowerbound
				System.out.println("Upper-bound: " + greedy.chromaticNumber);
				System.out.println("Lower-bound: " + backtrackAlgorithm.chromaticNumberLowerbound);
                return backtrackAlgorithm.chromaticNumber;
			} catch (TimeoutException te) { //if the search times out, print the upperbound and lowerbound
				System.out.println("Exact search timed out. Calculating upper-bound.");
				System.out.println("Upper-bound: " + greedy.chromaticNumber);
				System.out.println("Lower-bound: " + greedy.chromaticNumberLowerbound);
                return greedy.chromaticNumber;
			} catch (InterruptedException | ExecutionException ie) {
				//prints error stack track if there is an error
				System.out.println("Error! Problem reading graph" + ie);
                return -1;
			}
		} finally {
			executor.shutdown(); //shutdown the executor when complete
		}
    }
    public void chooseGreedy() {
        color = greedy.colors;
    }
    public void chooseBacktrack() {
        color = backtrackAlgorithm.colors;
    }

}