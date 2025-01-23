import java.util.*;

class Bipartite {
    public int[] color;

    // Function to check if the graph is Bipartite using BFS
    boolean isBipartite(int[][] adjMatrix, int V){
        color = new int[V];

        // Initialize all as -1 (uncolored)
        Arrays.fill(color, -1);
        int index = 0;
        color[index] = 0;
        checkSingleVertice(index, adjMatrix, V, color);
//        for(int i = 0; i < V; i++){
//             for(int j = 0; j < V; j++){
//                 System.out.println(i +" "+ j +" "+ adjMatrix[i][j]);
//             }
//            System.out.println(color[i]);
//        }
        return checkSingleVertice(index, adjMatrix, V, color);
    }

    boolean checkSingleVertice(int index, int[][] adjMatrix, int V, int color[]){
        for (int i = 0; i < V; i++) {

            if ((adjMatrix[index][i] == 1)||(adjMatrix[i][index] == 1)) {
                if(color[i] == -1){
                    color[i] = 1 - color[index]; 
                    if(!checkSingleVertice(i, adjMatrix, V, color)){
                        return false;
                    }
                }
                else if(color[i] == color[index]){
                    return false;
                }
                else if(color[i] == 1 - color[index]){
                }

            }
        }
        // If no conflicts in coloring, graph is bipartite
        return true;
    }
}

