import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class GraphDisplay extends Pane {
    private final BooleanProperty colored = new SimpleBooleanProperty(false);
    private final Stage stage;
    private final Map<Integer, Color> vertexColors = new HashMap<>();
    private final List<Text> labels = new ArrayList<>();
    private final List<Circle> vertices = new ArrayList<>();
    gameField game;
    ExtendedUIP3 ui;
    double[][] positions;
    Color selectedColor;
    Color[] previousColors = new Color[100];
    int[][] adjMatrix;
    int numVertices;
    int numEdges;
    int colorsUsed = 0;
    int gameMode;
    TimeService gameTimer;
    int[] previousVertices;
    int[] verticesCreated;
    int counter = 0;
    private int lastColoredVertex = -1;
    int toggleLabel = 1;

    public GraphDisplay(gameField game, Stage stage, TimeService gameTimer, int gameMode, int toggleLabel) {
        this.game = game;
        this.stage = stage;
        this.gameTimer = gameTimer;
        this.gameMode = gameMode;
        this.toggleLabel = toggleLabel;
    }

    public GraphDisplay(ExtendedUIP3 ui, Stage stage) {
        this.ui = ui;
        this.stage = stage;
        gameMode = 1;
    }

    public void drawGraph(TextField vertices_txt, TextField edges_txt, GraphDisplay graphDisplay) {
        try {
            numVertices = Integer.parseInt(vertices_txt.getText());
            numEdges = Integer.parseInt(edges_txt.getText());

            if (numVertices < 2 || numEdges < numVertices - 1 || numEdges > numVertices * (numVertices - 1) / 2) {
                errorPopup.showError("Invalid input, Please enter a valid number of vertices and edges.");
                return;
            }

            /*
            // Initialize positions if they are null or the number of vertices has changed
            if (graphDisplay.positions == null || graphDisplay.positions.length != numVertices) {
                graphDisplay.positions = new double[numVertices][2];
                for (int i = 0; i < numVertices; i++) {
                    boolean overlap;
                    do {
                        overlap = false;
                        graphDisplay.positions[i][0] = 100 + Math.random() * 400;
                        graphDisplay.positions[i][1] = 100 + Math.random() * 400;
                        for (int j = 0; j < i; j++) {
                            if (Math.hypot(graphDisplay.positions[i][0] - graphDisplay.positions[j][0], graphDisplay.positions[i][1] - graphDisplay.positions[j][1]) < 40) {
                                overlap = true;
                                break;
                            }
                        }
                    } while (overlap);
                }
            }

             */

            // Initialize adjacency matrix
            adjMatrix = new int[numVertices][numVertices];
            verticesCreated = new int[numVertices];

            for(int i = 0; i < numVertices; i++) {
                verticesCreated[i] = -1;
            }
            int verticesCounter = 2;
            int edgesCreated = 1;

            int v1 = (int) (Math.random() * numVertices);
            int v2 = (int) (Math.random() * numVertices);
            if (v1 != v2) {
                adjMatrix[v1][v2] = 1;
                adjMatrix[v2][v1] = 1;
                verticesCreated[v1] = v1;
                verticesCreated[v2] = v2;
            }

            while (verticesCounter < numVertices) {
                v1 = verticesCreated[(int) (Math.random() * numVertices)];
                v2 = (int) (Math.random() * numVertices);
                if (v1 != v2 && v1 != -1 && verticesCreated[v2] == -1) {
                    adjMatrix[v1][v2] = 1;
                    adjMatrix[v2][v1] = 1;
                    verticesCreated[v2] = v2;
                    verticesCounter++;
                    edgesCreated++;
                }
            }

            double degreeChange = 360 / (double) numVertices;
            double degree = -90;
            double xCenter = 520;
            double yCenter = 395;
            double radius = 350;
            double xradius = 470;
            double yradius = 350;
            double bigRadius;


            //double distance;
            //distance = (Math.PI * radius * degreeChange)/180;
            double minDistance = Math.PI * 20.455;
            double minDegreeCh;

            if (graphDisplay.positions == null || graphDisplay.positions.length != numVertices) {
                graphDisplay.positions = new double[numVertices][2];
                for (int i = 0; i < numVertices; i++) {
                    int x = verticesCreated[i];
                    minDegreeCh = (minDistance*180)/(Math.PI*radius);
                    bigRadius = Math.sqrt(xradius * xradius + yradius * yradius);
                    if (degreeChange < minDegreeCh) {
                        degreeChange = minDegreeCh;
                    }
                    /*
                    if (Math.cos(Math.toRadians(degree)) * bigRadius > xradius || -(Math.cos(Math.toRadians(degree)) * bigRadius) > xradius) {
                        graphDisplay.positions[x][0] = xCenter + xradius * (Math.cos(Math.toRadians(degree)) / Math.abs(Math.cos(Math.toRadians(degree))));
                        graphDisplay.positions[x][1] = yCenter + yradius * Math.sin(Math.toRadians(degree));
                    }
                    if (Math.sin(Math.toRadians(degree)) * bigRadius > yradius || -(Math.sin(Math.toRadians(degree)) * bigRadius) > yradius) {
                        graphDisplay.positions[x][0] = xCenter + xradius * Math.cos(Math.toRadians(degree));
                        graphDisplay.positions[x][1] = yCenter + yradius * (Math.sin(Math.toRadians(degree)) / Math.abs(Math.sin(Math.toRadians(degree))));;
                    }

                     */
                    graphDisplay.positions[x][0] = xCenter + xradius * Math.cos(Math.toRadians(degree));
                    graphDisplay.positions[x][1] = yCenter + yradius * Math.sin(Math.toRadians(degree));
                    degree = degree + degreeChange;
                    System.out.println(degree);
                    if (degree > 271 - minDegreeCh) {
                        degree = -90;
                        xradius = xradius - 90;
                        yradius = yradius - 90;
                        radius = radius - 90;
                    }
                }
            }

            // Randomly generate edges
            while (edgesCreated < numEdges) {
                v1 = (int) (Math.random() * numVertices);
                v2 = (int) (Math.random() * numVertices);
                if (v1 != v2 && adjMatrix[v1][v2] == 0) {
                    adjMatrix[v1][v2] = 1;
                    adjMatrix[v2][v1] = 1;
                    edgesCreated++;
                }
            }

            // Clear the previous graph and draw the new one
            graphDisplay.getChildren().clear();
            if (toggleLabel == 1) {
                labels.clear();
            }

            // Draw edges
            graphDisplay.drawEdges(graphDisplay, adjMatrix, toggleLabel);

            // Draw vertices and add drag functionality
            for (int i = 0; i < numVertices; i++) {
                Circle vertex = graphDisplay.getCircle(graphDisplay, i, adjMatrix, toggleLabel);
                graphDisplay.getChildren().add(vertex);

                if (toggleLabel == 1) {
                    Text label = new Text(String.valueOf(i + 1));
                    label.setX(positions[i][0] - 4); // Adjust the position to center the label
                    label.setY(positions[i][1] + 4); // Adjust the position to center the label
                    label.setFill(Color.WHITE);
                    labels.add(label);
                    graphDisplay.getChildren().add(label);
                }
            }

        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid integer for vertices and edges.");
        }
    }

    public Circle getCircle(GraphDisplay graphDisplay, int i, int[][] adjMatrix, int toggleLabel) {
        Circle vertex = new Circle(positions[i][0], positions[i][1], 15, Color.BLACK);
        final int index = i;
        vertex.setUserData(true);
        ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BLACK);

        vertices.add(vertex);
        previousVertices = new int[numVertices];

        vertex.setOnMousePressed(_ -> {
            if ((boolean) vertex.getUserData() && selectedColor != null) {
                if ((gameMode == 2 || gameMode == 3) && index == lastColoredVertex + 1) {
                    vertex.setFill(selectedColor);
                    color.set(selectedColor);
                    previousColors[index] = selectedColor;
                    previousVertices[counter] = index;
                    counter++;
                    Set<Color> uniqueColors = new HashSet<>(Arrays.asList(previousColors));
                    uniqueColors.remove(null); // Remove null values if any
                    colorsUsed = uniqueColors.size();
                    vertexColors.put(index, selectedColor);
                    colored.set(true);
                    lastColoredVertex = index; // Update the last colored vertex
                    if (allVerticesColored()) {
                        game.endGame(stage, numVertices, numEdges, gameMode, gameTimer, graphDisplay);
                    }
                    vertex.setUserData(false);
                } else if (gameMode != 2 && gameMode != 3) {
                    vertex.setFill(selectedColor);
                    color.set(selectedColor);
                    previousColors[index] = selectedColor;
                    previousVertices[counter] = index;
                    counter++;
                    Set<Color> uniqueColors = new HashSet<>(Arrays.asList(previousColors));
                    uniqueColors.remove(null); // Remove null values if any
                    colorsUsed = uniqueColors.size();
                    vertexColors.put(index, selectedColor);
                    colored.set(true);
                    if (allVerticesColored()) {
                        game.endGame(stage, numVertices, numEdges, gameMode, gameTimer, graphDisplay);
                    }
                    vertex.setUserData(false);
                }
            }
        });

        vertex.setOnMouseDragged(event -> {
            double newX = event.getX();
            double newY = event.getY();

            // Constrain the movement within the specified area
            if (newX >= 0 && newX <= 850 && newY >= 0 && newY <= 530) {
                positions[index][0] = newX;
                positions[index][1] = newY;
                vertex.setCenterX(newX);
                vertex.setCenterY(newY);

                if (toggleLabel == 1) {
                    labels.get(index).setX(newX - 4);
                    labels.get(index).setY(newY + 4);
                }

                drawEdges(graphDisplay, adjMatrix, toggleLabel);
            }
        });

        return vertex;
    }

    public void drawEdges(GraphDisplay graphDisplay, int[][] adjMatrix, int toggleLabel) {
        graphDisplay.getChildren().removeIf(node -> node instanceof Line || node instanceof Text && ((Text) node).getText().startsWith("Edge"));
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = i + 1; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] == 1) {
                    Line edge = new Line(positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
                    graphDisplay.getChildren().add(edge);
                    edge.toBack();

                    if (toggleLabel == 1) {
                        Text edgeLabel = getEdgeLabel(i, j);
                        graphDisplay.getChildren().add(edgeLabel);
                    }
                }
            }
        }
    }

    private Text getEdgeLabel(int i, int j) {
        double midX = (positions[i][0] + positions[j][0]) / 2;
        double midY = (positions[i][1] + positions[j][1]) / 2;
        Text edgeLabel = new Text(midX, midY, "Edge " + (i + 1) + "-" + (j + 1));
        edgeLabel.setFill(Color.BLACK);

        double angle = Math.toDegrees(Math.atan2(positions[j][1] - positions[i][1], positions[j][0] - positions[i][0]));
        if (angle > 90 || angle < -90) {
            angle += 180;
        }
        edgeLabel.setRotate(angle);

        edgeLabel.setX(midX - edgeLabel.getLayoutBounds().getWidth() / 2);
        edgeLabel.setY(midY - edgeLabel.getLayoutBounds().getHeight() / 2);
        return edgeLabel;
    }

    public void drawGraph(ReadGraph file, GraphDisplay graphDisplay) {
        try {
            int numVertices = file.numVertices;
            adjMatrix = file.adjList;

            double degreeChange = 360 / (double) numVertices;
            double degree = -90;
            double xCenter = 520;
            double yCenter = 395;
            double radius = 350;
            double xradius = 470;
            double yradius = 350;
            double bigRadius;
            double minDistance = Math.PI * 20.455;
            double minDegreeCh;

            if (graphDisplay.positions == null || graphDisplay.positions.length != numVertices) {
                graphDisplay.positions = new double[numVertices][2];
                for (int i = 0; i < numVertices; i++) {
                    minDegreeCh = (minDistance*180)/(Math.PI*radius);
                    bigRadius = Math.sqrt(xradius * xradius + yradius * yradius);
                    if (degreeChange < minDegreeCh) {
                        degreeChange = minDegreeCh;
                    }

                    graphDisplay.positions[i][0] = xCenter + xradius * Math.cos(Math.toRadians(degree));
                    graphDisplay.positions[i][1] = yCenter + yradius * Math.sin(Math.toRadians(degree));
                    degree = degree + degreeChange;
                    //System.out.println(degree);
                    if (degree > 271 - minDegreeCh) {
                        degree = -90;
                        xradius = xradius - 90;
                        yradius = yradius - 90;
                        radius = radius - 90;
                    }
                }
            }

            // Clear the previous graph and draw the new one
            graphDisplay.getChildren().clear();
            if (toggleLabel == 1) {
                labels.clear();
            }

            // Draw edges
            graphDisplay.drawEdges(graphDisplay, adjMatrix, toggleLabel);

            // Draw vertices and add drag functionality
            for (int i = 0; i < numVertices; i++) {
                Circle vertex = graphDisplay.getCircle(graphDisplay, i, file.adjList, toggleLabel);
                graphDisplay.getChildren().add(vertex);

                if (toggleLabel == 1) {
                    Text label = new Text(String.valueOf(i + 1));
                    label.setX(positions[i][0] - 4); // Adjust the position to center the label
                    label.setY(positions[i][1] + 4); // Adjust the position to center the label
                    label.setFill(Color.WHITE);
                    labels.add(label);
                    graphDisplay.getChildren().add(label);
                }
            }

        } catch (Exception ex) {
            System.out.println("Error reading graph from file.");
            System.out.println(ex.getMessage());
        }
    }

    public void undoColor() {
        for (int i = numVertices - 1; i >= 0; i--) {
            if (previousVertices[i] != 0) {
                Circle vertex = vertices.get(previousVertices[i]);
                vertex.setFill(Color.BLACK);
                vertex.setUserData(true);
                previousVertices[i] = 0;
                lastColoredVertex -= 1;
                counter--;
                break;
            }
        }
    }

    public BooleanProperty isColoredProperty() {
        return colored;
    }

    public boolean isColored() {
        return colored.get();
    }

    private boolean allVerticesColored() {
        for (int i = 0; i < numVertices; i++) {
            if (vertexColors.get(i) == null) {
                System.out.println("not all vertices colored");
                return false;
            }
        }
        System.out.println("all vertices colored");
        return true;
    }

    public int getColorsUsed() {
        System.out.println("Colors used: " + colorsUsed);
        return colorsUsed;
    }

    public void callFillColorsAfterGiveup(GraphDisplay graphDisplay) {
        fillColorsAfterGiveup p = new fillColorsAfterGiveup(adjMatrix, numVertices, numEdges, vertexColors);
        p.fillColors(positions, vertexColors, graphDisplay, adjMatrix, previousColors);
    }

    public void doHintColor() {
        hintLogic p = new hintLogic(adjMatrix, numVertices, numEdges, vertexColors);
        String hint = p.giveHintInfo(previousVertices, counter, positions, lastColoredVertex, vertexColors);
        System.out.println(hint);
    }

    public void updateToggleLabel(int toggleLabel) {
        if (toggleLabel != 0 && toggleLabel != 1) {
            toggleLabel = 0;
        } else {
            this.toggleLabel = toggleLabel;
        }
    }

}