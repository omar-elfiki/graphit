import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.Arrays;
import java.util.Map;

public class fillColorsAfterGiveup {
    private final int n;
    public int[] colors;
    public int chromaticNumber;
    int start = 1;
    int[] indexOfColoredVertex;

    public fillColorsAfterGiveup(int[][] adjMatrix, int numVertices, int edges, Map<Integer, Color> vertexColors) {
        indexOfColoredVertex = new int[vertexColors.size()];
        n = numVertices;
//        ColEdge[] graph = ReadGraph.convert(adjMatrix);
//        colors = GreedyAlgorithm(adjMatrix.length, graph, new int[adjMatrix.length]);
        colors = ReadGraph.color;
        for (int i = 0; i < colors.length; i++) {
            System.out.println(colors[i]);
        }

    }

    public int[] convertColorToInt(Map<Integer, Color> vertexColors){
        int[] colorsHaveUsed = new int[n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            if((vertexColors.containsKey(i))&&(colorsHaveUsed[i] == 0)){
                colorsHaveUsed[i] = start;
                start++;
                indexOfColoredVertex[index] = i;
                index++;
            }
            else
                break;
            for (int j = 0; j < n; j++) {
                if(vertexColors.get(i) == vertexColors.get(j)){
                    colorsHaveUsed[j] = colorsHaveUsed[i];
                }
            }
        }
        return colorsHaveUsed;
    }

    public void fillColors(double[][] positions,Map<Integer, Color> vertexColors, GraphDisplay graphDisplay, int[][] adjMatrix, Color[] previousColors){
        graphDisplay.getChildren().clear();
        String[] color = new String[2*adjMatrix.length];
        for(int i = 0; i < 2*adjMatrix.length; i++){
            color[i] = ColorGenerator.generateRandomHexColor();
        }
//        Backtrack backtrack = new Backtrack(adjMatrix, adjMatrix.length);
//        backtrack.search();

        graphDisplay.getChildren().removeIf(node -> node instanceof Line);
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] == 1) {
                    Line edge = new Line(positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
                    graphDisplay.getChildren().add(edge);
                    Circle vertex;
                    vertex = new Circle(positions[i][0], positions[i][1], 20, Color.web(color[colors[i]]));
                    graphDisplay.getChildren().add(vertex);
                    vertex = new Circle(positions[j][0], positions[j][1], 20, Color.web(color[colors[j]]));
                    graphDisplay.getChildren().add(vertex);
                }
            }
        }
    }

    public int[] GreedyAlgorithm(int n, ColEdge[] graph, int[] colors) {
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
        return colors;
    }

}



