import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ExtendedUIP3 extends Application {
    private Scale scale;
    private GraphDisplay graphDisplay;
    private Pane leftpane;

    private int lockmode = 1;

    @Override
    public void start(Stage stage) {
        int windowHeight = 800;
        int windowWidth = 1500;
        BorderPane mainpane = new BorderPane();
        leftpane = new Pane();
        VBox leftvbox = new VBox();
        leftvbox.getChildren().add(leftpane);
        mainpane.setLeft(leftvbox);
        Pane rightpane = new Pane();
        VBox rightvbox = new VBox();
        rightvbox.getChildren().add(rightpane);
        mainpane.setRight(rightvbox);


        graphDisplay = new GraphDisplay(this, stage);
        graphDisplay.setPrefSize(windowWidth, windowHeight);
        mainpane.setCenter(graphDisplay);

        scale = new Scale(1, 1, 0, 0);
        graphDisplay.getTransforms().add(scale);

        Button resetButton = new Button("Reset Zoom/Pan");
        resetButton.getStyleClass().add("rounded-button");
        resetButton.setPrefSize(250, 50);
        resetButton.setLayoutX(35);
        resetButton.setLayoutY(400);
        resetButton.setOnAction(_ -> resetZoomAndPan());


        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.GRAY);

        Rectangle leftbar = new Rectangle();
        leftbar.widthProperty().set(320);
        leftbar.heightProperty().bind(mainpane.heightProperty());
        leftbar.setFill(javafx.scene.paint.Color.LIGHTGRAY);
        leftbar.setEffect(dropShadow);
        leftpane.getChildren().add(leftbar);

        Text vertices_text = new Text("Vertices");
        vertices_text.setFill(Color.BLACK);
        vertices_text.setFont(new Font(24));
        vertices_text.xProperty().set(20);
        vertices_text.yProperty().set(45);

        Text edges_text = new Text("Edges");
        edges_text.setFill(Color.BLACK);
        edges_text.setFont(new Font(24));
        edges_text.xProperty().set(20);
        edges_text.yProperty().set(100);

        Text algorithm_text = new Text("Algorithm");
        algorithm_text.setFill(Color.BLACK);
        algorithm_text.setFont(new Font(24));
        algorithm_text.xProperty().set(20);
        algorithm_text.yProperty().set(155);

        Text results_text = new Text("Result");
        results_text.setFill(Color.BLACK);
        results_text.setFont(new Font(24));
        results_text.xProperty().set(20);
        results_text.yProperty().set(210);

        Text lowerbound_text = new Text("L-Bound");
        lowerbound_text.setFill(Color.BLACK);
        lowerbound_text.setFont(new Font(24));
        lowerbound_text.xProperty().set(20);
        lowerbound_text.yProperty().set(265);

        Text type_text = new Text("Type");
        type_text.setFill(Color.BLACK);
        type_text.setFont(new Font(24));
        type_text.xProperty().set(20);
        type_text.yProperty().set(320);

        leftpane.getChildren().addAll(vertices_text, edges_text, algorithm_text, results_text);

        TextField vertices_field = new TextField();
        vertices_field.setPrefSize(100, 40);
        vertices_field.layoutXProperty().set(135);
        vertices_field.layoutYProperty().set(17);
        vertices_field.setDisable(true);

        TextField edges_field = new TextField();
        edges_field.setPrefSize(100, 40);
        edges_field.layoutXProperty().set(135);
        edges_field.layoutYProperty().set(73);
        edges_field.setDisable(true);

        TextField algorithm_field = new TextField();
        algorithm_field.setPrefSize(100, 40);
        algorithm_field.layoutXProperty().set(135);
        algorithm_field.layoutYProperty().set(128);
        algorithm_field.setDisable(true);

        TextField results_field = new TextField();
        results_field.setPrefSize(100, 40);
        results_field.layoutXProperty().set(135);
        results_field.layoutYProperty().set(183);
        results_field.setDisable(true);

        TextField lowerbound_field = new TextField();
        lowerbound_field.setPrefSize(100, 40);
        lowerbound_field.layoutXProperty().set(135);
        lowerbound_field.layoutYProperty().set(238);
        lowerbound_field.setDisable(true);


        TextField type_field = new TextField();
        type_field.setPrefSize(100, 40);
        type_field.layoutXProperty().set(135);
        type_field.layoutYProperty().set(293);
        type_field.setDisable(true);

        leftpane.getChildren().addAll(vertices_field, edges_field, algorithm_field, results_field);

        Button lockButton = new Button("Unlock");
        lockButton.getStyleClass().add("rounded-button");
        lockButton.setPrefSize(70, 50);
        lockButton.setOnAction(_ -> {});
        rightpane.getChildren().add(lockButton);
        lockButton.layoutXProperty().bind(rightpane.widthProperty().subtract(lockButton.widthProperty()).add(-20));
        lockButton.layoutYProperty().set(700);
        lockButton.setDisable(true);
        lockButton.setOnAction(_ -> {
            if (lockmode == 0) {
                lockmode = 1;
                lockButton.setText("Unlock");
                graphDisplay.setOnMouseDragged(null);
                graphDisplay.setOnScroll(null);
                graphDisplay.setOnMousePressed(null);
                leftpane.getChildren().remove(resetButton);
                resetZoomAndPan();
            } else {
                lockmode = 0;
                lockButton.setText("Lock");
                Translate translate = new Translate();
                graphDisplay.getTransforms().add(translate);

                graphDisplay.setOnScroll(event -> {
                    double zoomFactor = 1.05;
                    if (event.getDeltaY() < 0) {
                        zoomFactor = 1 / zoomFactor;
                    }
                    scale.setX(scale.getX() * zoomFactor);
                    scale.setY(scale.getY() * zoomFactor);
                    leftpane.getChildren().add(resetButton);
                });

                graphDisplay.setOnMousePressed(event -> {
                    translate.setX(event.getSceneX());
                    translate.setY(event.getSceneY());
                    leftpane.getChildren().add(resetButton);
                });

                graphDisplay.setOnMouseDragged(event -> {
                    double deltaX = event.getSceneX() - translate.getX();
                    double deltaY = event.getSceneY() - translate.getY();
                    translate.setX(event.getSceneX());
                    translate.setY(event.getSceneY());

                    double newTranslateX = graphDisplay.getTranslateX() + deltaX;
                    double newTranslateY = graphDisplay.getTranslateY() + deltaY;

                    double minX = 0;
                    double minY = 0;
                    double maxX = mainpane.getWidth() - graphDisplay.getBoundsInParent().getWidth();
                    double maxY = mainpane.getHeight() - graphDisplay.getBoundsInParent().getHeight();

                    if (newTranslateX < minX) {
                        newTranslateX = minX;
                    } else if (newTranslateX > maxX) {
                        newTranslateX = maxX;
                    }

                    if (newTranslateY < minY) {
                        newTranslateY = minY;
                    } else if (newTranslateY > maxY) {
                        newTranslateY = maxY;
                    }

                    graphDisplay.setTranslateX(newTranslateX);
                    graphDisplay.setTranslateY(newTranslateY);
                    leftpane.getChildren().add(resetButton);
                });
            }
        });

        Button colorButton = new Button("Color Graph");
        colorButton.getStyleClass().add("rounded-button");
        colorButton.setPrefSize(250, 50);
        colorButton.setLayoutX(35);
        colorButton.setLayoutY(600);
        colorButton.setDisable(true);
        colorButton.setOnAction(_ -> graphDisplay.callFillColorsAfterGiveup(graphDisplay));
        leftpane.getChildren().add(colorButton);


        Button uploadButton = new Button("Upload Graph");
        uploadButton.getStyleClass().add("rounded-button");
        uploadButton.setPrefSize(250, 50);
        uploadButton.setLayoutX(35);
        uploadButton.setLayoutY(525);
        uploadButton.setOnAction(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                colorButton.setDisable(true);
                graphDisplay.getChildren().clear();
                leftpane.getChildren().removeAll(lowerbound_field, lowerbound_text, type_field, type_text);
                resetZoomAndPan();
                System.out.println("File selected: " + selectedFile.getAbsolutePath());
                ReadGraph uploadGraph = new ReadGraph(selectedFile);
                uploadGraph.readGraph();
                algorithimDetermination algorithimDetermination = new algorithimDetermination(uploadGraph);
                int algorithm = algorithimDetermination.determineAlgorithm();
                System.out.println("Algorithm: " + algorithm);
                uploadGraph.getResultExtended(algorithm);
                if (uploadGraph.numVertices > 200) {
                    errorPopup.showInfo("Graph too large to be displayed, results will still be calculated");
                } else {
                    graphDisplay.drawGraph(uploadGraph, graphDisplay);
                    colorButton.setDisable(false);
                    lockButton.setDisable(false);
                }
                vertices_field.setText(String.valueOf(uploadGraph.numVertices));
                edges_field.setText(String.valueOf(uploadGraph.numEdges));
                if (uploadGraph.numVertices == 0) {
                    errorPopup.showError("Error, Invalid graph file: no vertices found");
                    return;
                }
                switch (algorithm) {
                    case 1:
                        algorithm_field.setText("Greedy");
                        results_field.setText(String.valueOf(uploadGraph.chromaticNumberGreedy));
                        if (uploadGraph.chromaticNumberLowerbound == 0) {
                            lowerbound_field.setText(String.valueOf(1));
                        } else {
                            lowerbound_field.setText(String.valueOf(uploadGraph.chromaticNumberLowerbound));
                        }
                        type_field.setText(algorithimDetermination.type);
                        leftpane.getChildren().addAll(lowerbound_field, lowerbound_text, type_field, type_text);
                        uploadGraph.chooseGreedy();
                        break;
                    case 0:
                        if (algorithimDetermination.type != null) {
                            type_field.setText(algorithimDetermination.type);
                            leftpane.getChildren().addAll(type_field, type_text);
                        }
                        if (uploadGraph.chromaticNumberBacktrack == -1) {
                            errorPopup.showError("Backtrack algorithm timed out; Greedy algorithm used instead");
                            algorithm_field.setText("Greedy");
                            results_field.setText(String.valueOf(uploadGraph.chromaticNumberGreedy));
                            if (uploadGraph.chromaticNumberLowerbound == 0) {
                                lowerbound_field.setText(String.valueOf(1));
                            } else {
                                lowerbound_field.setText(String.valueOf(uploadGraph.chromaticNumberLowerbound));
                            }
                            leftpane.getChildren().addAll(lowerbound_field, lowerbound_text);
                            uploadGraph.chooseGreedy();
                            return;
                        } else {
                            algorithm_field.setText("Backtrack");
                            results_field.setText(String.valueOf(uploadGraph.chromaticNumberBacktrack));
                            uploadGraph.chooseBacktrack();
                        }
                        break;
                    default:
                        algorithm_field.setText("Error");
                        results_field.setText("Error");
                        break;
                }
            } else {
                System.out.println("File selection cancelled.");
            }
        });
        leftpane.getChildren().add(uploadButton);

        Button clearButton = new Button("Clear Graph");
        clearButton.getStyleClass().add("rounded-button");
        clearButton.setPrefSize(250, 50);
        clearButton.setLayoutX(35);
        clearButton.setLayoutY(675);
        clearButton.setOnAction(_ -> {
            vertices_field.clear();
            edges_field.clear();
            algorithm_field.clear();
            results_field.clear();
            graphDisplay.getChildren().clear();
            colorButton.setDisable(true);
            lockButton.setDisable(true);
            resetZoomAndPan();
            leftpane.getChildren().removeAll(lowerbound_field, lowerbound_text, type_field, type_text);
        });
        leftpane.getChildren().add(clearButton);

        Button toggleButton = new Button(">");
        toggleButton.getStyleClass().add("rounded-button");
        toggleButton.setPrefSize(30, 30);
        toggleButton.setLayoutX(350);
        toggleButton.setLayoutY(10);
        toggleButton.setOnAction(_ -> {
            if (leftpane.isVisible()) {
                leftpane.setVisible(false);
                toggleButton.setText("<");
            } else {
                leftpane.setVisible(true);
                toggleButton.setText(">");
            }
        });
        leftvbox.getChildren().add(toggleButton);

        Scene scene = new Scene(mainpane, windowWidth, windowHeight);
        stage.setScene(scene);
        stage.setTitle("Chromatic Number Calculator");
        stage.show();
    }

    private void resetZoomAndPan() {
        scale.setX(1);
        scale.setY(1);
        graphDisplay.setTranslateX(0);
        graphDisplay.setTranslateY(0);
        graphDisplay.getTransforms().removeIf(transform -> transform instanceof Translate);
        graphDisplay.getTransforms().add(new Translate());
        leftpane.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getText().equals("Reset Zoom/Pan"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
