import java.util.Stack;

public class algorithimDetermination {
    ReadGraph graph;
    String type = "N/A";

    public algorithimDetermination(ReadGraph graph) {
        this.graph = graph;
    }

    public int determineAlgorithm() {
        System.out.println("Determining algorithm..");
        Bridge bridge = new Bridge(graph.numVertices, graph.adjList);
        bridge.bridgeCalculate();
        System.out.println("Bridges calculated");
        boolean[] visited = new boolean[graph.numVertices];
        System.out.println("Checking for full graph");
        if (graph.numEdges == (graph.numVertices * (graph.numVertices - 1)) / 2) {
            //special case full graph aka test 2
            type = "Full Graph";
            return 1; //1 is for greedy
        }
        System.out.println("Checking star/tree graph");
        if (graph.numEdges == bridge.count&&graph.numEdges!=0) {
            type = "Star/Tree";
            return 1;
        }else if(graph.numEdges == bridge.count&&graph.numEdges==0){
            type="No edges";
            return 1;
        }
        Bipartite bipartite =  new Bipartite();
        if(bipartite.isBipartite(graph.adjList,graph.numVertices)){
            type = "Bipartite";
            System.out.println("Checking for bipartite");
            return 0;
        }
        System.out.println("Checking for full cycle");
      /*  if (isFullCycle(0, ReadGraph.convert(graph.e, graph.numVertices), visited)) {
            type = "Full Cycle / Bipartite";
            return 1;
        }*/
        System.out.println("Checking for cyclic");
        if (Backtrack.isCyclic(graph.numVertices, ReadGraph.convert(graph.e, graph.numVertices))) {
            System.out.println(Backtrack.isCyclic(graph.numVertices, ReadGraph.convert(graph.e, graph.numVertices)));
            System.out.println(bridge.count);

            if (bridge.count== 0&&graph.numVertices== graph.numEdges){
                type = "Cycle";
                return 1;
            }else{
                return 0;
            }

        }
        return 0;
    }

    int counter = 1;

    public boolean isFullCycle(int start, int[][] adjMatrix, boolean[] visited) {
        int n = adjMatrix.length;
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        visited[start] = true;
        int counter = 1;

        while (!stack.isEmpty()) {
            int v = stack.peek();
            boolean hasUnvisitedNeighbor = false;

            for (int i = 0; i < n; i++) {
                if (adjMatrix[v][i] == 1 && !visited[i]) {
                    stack.push(i);
                    visited[i] = true;
                    counter++;
                    hasUnvisitedNeighbor = true;
                    break;
                }
            }

            if (!hasUnvisitedNeighbor) {
                stack.pop();
            }

            if (counter == n && adjMatrix[stack.peek()][start] == 1) {
                return true;
            }
        }

        return false;
    }
}
