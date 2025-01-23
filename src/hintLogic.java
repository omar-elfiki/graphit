import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Map;

public class hintLogic {
    private final int n;
    public int[] colors;
    int start = 1;
    int[] indexOfColoredVertex;

    public hintLogic(int[][] adjMatrix, int numVertices, int edges, Map<Integer, Color> vertexColors) {
        indexOfColoredVertex = new int[vertexColors.size()];
        n = numVertices;
        this.colors = convertColorToInt(vertexColors);
        ColEdge[] graph = ReadGraph.convert(adjMatrix);
        fillColorsAfterGiveup algo = new fillColorsAfterGiveup(adjMatrix, numVertices, edges, vertexColors);
        colors = algo.GreedyAlgorithm(edges, graph, colors);
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


    public String giveHintInfo(int[] previousVertices, int counter,double[][] positions,int index, Map<Integer, Color> vertexColors){
        ArrayList<Color> colorRecord = new ArrayList<>();
        ArrayList<Double> positionRecordX = new ArrayList<>();
        ArrayList<Double> positionRecordY = new ArrayList<>();
        ArrayList<Integer> coloredVertex = new ArrayList<>();
        String[] color = new String[n-start+100];

        for(int i = 0; i < (n-start+100); i++){
            if(color[i] == null){
                color[i] = ColorGenerator.generateRandomHexColor();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int k : indexOfColoredVertex) {
                if (colors[i] == colors[k]) {
                    Circle vertex = new Circle(positions[i][0], positions[i][1], 20, vertexColors.get(k));
                    colorRecord.add(vertexColors.get(k));
                    positionRecordX.add(positions[i][0]);
                    positionRecordY.add(positions[i][1]);
                }
            }
            Circle vertex;
            if(vertexColors.containsKey(i)){
                vertex = new Circle(positions[i][0], positions[i][1], 20, vertexColors.get(i));
                colorRecord.add(vertexColors.get(i));
            }
            else{
                vertex = new Circle(positions[i][0], positions[i][1], 20, Color.web(color[colors[i]]));
                colorRecord.add(Color.web(color[colors[i]]));
            }
            positionRecordX.add(positions[i][0]);
            positionRecordY.add(positions[i][1]);
        }
        for(int i = 0; i < counter; i++){
            for(int j = 0; j < n; j++){
                if(positions[previousVertices[i]][0] == positions[j][0]){
                    if(positions[previousVertices[i]][1] == positions[j][1]){
                        coloredVertex.add(j);
                    }
                }
            }
        }
        ArrayList<Integer> colorHaveAppeared = new ArrayList<>();
        for(int i = 0; i < index; i++){
            colorHaveAppeared.add(colors[coloredVertex.get(i)]);
        }
        if(colorHaveAppeared.contains(colors[index])) {
            errorPopup.showInfo("Hint: Use the color you already used!");
        }
        else {
            errorPopup.showInfo("Hint: Use a new color!");
        }
        return "";
    }
}
