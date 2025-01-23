import java.util.Arrays;

public class oldgreedyAlgorithm {
    public ColEdge[] graph;
    public int chromaticNumber;
    public int chromaticNumberLowerbound;
    public int n;
    public int m;

    public oldgreedyAlgorithm(ColEdge[] graph, int n, int m) {
        this.n = n; //assigning given variables in the constructor
        this.m = m;
        this.graph = graph;
        GreedyAlgorithm(n, graph); //calling the greedy algorithm
    }
    private void GreedyAlgorithm(int n, ColEdge[] graph) {
        int[] colors = new int[n];
        Arrays.fill(colors, -1); // initialize all vertex colors as unassigned (-1)
        if (m == (n * (n -1))/2){
            chromaticNumber = n;
            return;
        }
        if (m == 0){
            chromaticNumber = 1;
            return;
        }
        // Assign colors to edges
        for (ColEdge edge : graph) {
            boolean[] available = new boolean[n]; // boolean array to store available colors
            Arrays.fill(available, true); // initialize all colors as available
            // Check colors of adjacent edges
            for (ColEdge next : graph) {
                if (next != edge) {
                    if (next.u == edge.u || next.v == edge.u) {
                        if (next.u < n && colors[next.u] != -1) // if the color is assigned
                            available[colors[next.u]] = false; // set the color as unavailable
                        if (next.v < n && colors[next.v] != -1)
                            available[colors[next.v]] = false;
                    }
                    if (next.u == edge.v || next.v == edge.v) { // same as above but for the other vertex
                        if (next.u < n && colors[next.u] != -1)
                            available[colors[next.u]] = false;
                        if (next.v < n && colors[next.v] != -1)
                            available[colors[next.v]] = false;
                    }
                }
            }
            // Assign the first available color
            for (int color = 0; color < n; color++) {
                if (available[color]) {
                    if (edge.u < n) colors[edge.u] = color;
                    if (edge.v < n) colors[edge.v] = color;
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
        int[] color = new int[chromaticNumber];
        int largestIndependenceNumber = 0;
        for (int k : colorRecord) {
            for (int j = 0; j < chromaticNumber; j++) {
                if (k == j) {
                    color[j]++;
                    break;
                }
            }
        }
        for(int i = 0; i < chromaticNumber; i++){
            if(largestIndependenceNumber <= color[i]) {
                largestIndependenceNumber = color[i];
            }
        }
        chromaticNumberLowerbound = n/largestIndependenceNumber;
    }
}