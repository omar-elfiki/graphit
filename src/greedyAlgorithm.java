import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class greedyAlgorithm {
    public ColEdge[] graph;
    public int chromaticNumber;
    public int chromaticNumberLowerbound;
    public int[]colors;
    public int n;
    public int m;

    public greedyAlgorithm(ColEdge[] graph, int n, int m) {
        this.n = n; //assigning given variables in the constructor
        this.m = m;
        this.graph = graph;
        GreedyAlgorithm(n, graph); //calling the greedy algorithm
    }
    private void GreedyAlgorithm(int n, ColEdge[] graph) {
        colors = new int[n];
        Arrays.fill(colors, -1); // initialize all vertex colors as unassigned (-1)

        // Special case: complete graph, all vertices need unique colors
        if (m == (n * (n -1))/2){
            chromaticNumber = n;
            return;
        }

        // Special case: no edges, only one color is needed
        if (m == 0){
            chromaticNumber = 1;
            return;
        }
        // Create an adjacency list
        List<Integer>[] adjacencyList = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjacencyList[i] = new ArrayList<>();
        }
        for (ColEdge edge : graph) {
            adjacencyList[edge.u - 1].add(edge.v - 1);
            adjacencyList[edge.v - 1].add(edge.u - 1);
        }

// Use the adjacency list to check adjacent vertices
        for (int vertex = 0; vertex < n; vertex++) {
            boolean[] available = new boolean[n];
            Arrays.fill(available, true);

            for (int neighbor : adjacencyList[vertex]) {
                if (colors[neighbor] != -1) {
                    available[colors[neighbor]] = false; // Mark neighbor's color as unavailable
                }
            }
            // Assign the first available color
            for (int color = 0; color < n; color++) {
                if (available[color]) {

                    colors[vertex] = color;
                    break;
                }
            }
        }
        // print the total number of colors used
        int maxColor = Arrays.stream(colors).max().orElse(-1);
        chromaticNumber = maxColor + 1; // the chromatic number is the maximum colors + 1 (0-indexed)
        LowerBound(colors, maxColor);
    }
    public void LowerBound(int[]colorRecord, int chromaticNumber){
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