import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;

public class gameField {
    int gameMode;
    int chromaticNumber;
    int toggleLabel = 0;
    boolean firstHint = true;
    String mode1 = "To the Bitter End";
    String mode2 = "Random Order";
    String mode3 = "I Changed My Mind";

    ReadGraph graphData;

    gameField(int gameMode) {
        this.gameMode = gameMode;
        showPage(new Stage());
    }

    @SuppressWarnings("unused")

    public void showPage(Stage stage) {
        TimeService gameTimer = new TimeService();
        String currentGameMode = switch (gameMode) {
            case 1 -> mode1;
            case 2 -> mode2;
            case 3 -> mode3;
            default -> "Invalid Game Mode";
        };

        int windowHeight = 700;
        int windowWidth = 1200;

        stage.setTitle("Graph it!");
        // Main layout
        BorderPane borderPane = new BorderPane();

        // Top Pane
        Pane topPane = new Pane();
        HBox hbox_top = new HBox();
        hbox_top.getChildren().add(topPane);
        borderPane.setTop(hbox_top);

        // Left Pane
        Pane leftPane = new Pane();
        VBox vbox_left = new VBox();
        vbox_left.getChildren().add(leftPane);
        borderPane.setLeft(vbox_left);

        // Bottom Pane
        Pane bottomPane = new Pane();
        HBox hbox_bottom = new HBox();
        hbox_bottom.getChildren().add(bottomPane);
        borderPane.setBottom(hbox_bottom);

        GraphDisplay graphDisplay = new GraphDisplay(this, stage, gameTimer, gameMode, toggleLabel);
        graphDisplay.setPrefSize(1200, 700);
        graphDisplay.setScaleX(0.7);
        graphDisplay.setScaleY(0.7);
        borderPane.setCenter(graphDisplay);

        Rectangle top_bar = new Rectangle();
        top_bar.setFill(Color.web("#C9C9C9"));
        top_bar.widthProperty().bind(borderPane.widthProperty());
        top_bar.heightProperty().set(70);

        Rectangle bottom_bar = new Rectangle();
        bottom_bar.setFill(Color.web("#C9C9C9"));
        bottom_bar.widthProperty().bind(borderPane.widthProperty());
        bottom_bar.heightProperty().set(100);

        //Clock and stopwatch section
        Rectangle clock_bg = new Rectangle(80, 35);
        clock_bg.setArcHeight(35);
        clock_bg.setArcWidth(35);
        clock_bg.setFill(Color.web("#FFFFFF"));
        clock_bg.xProperty().bind(borderPane.widthProperty().subtract(100));
        clock_bg.yProperty().set(18);
        Label timeLabel = new Label("00:00");
        timeLabel.setFont(new Font(18));
        timeLabel.setTextFill(Color.BLACK);
        timeLabel.layoutXProperty().bind(clock_bg.xProperty().add(18));
        timeLabel.layoutYProperty().bind(clock_bg.yProperty().add(4));
        topPane.getChildren().addAll(top_bar, clock_bg, timeLabel);
        gameTimer.startTime(timeLabel);
        //...................................

        Rectangle left_bar = new Rectangle();
        left_bar.setFill(Color.web("#C9C9C9"));
        left_bar.widthProperty().set(350);
        left_bar.heightProperty().bind(borderPane.heightProperty());
        leftPane.getChildren().add(left_bar);

        Text gameModeDesc = new Text(currentGameMode);
        gameModeDesc.setFill(Color.BLACK);
        gameModeDesc.setFont(new Font(24));
        gameModeDesc.xProperty().set(20);
        gameModeDesc.yProperty().set(45);
        topPane.getChildren().add(gameModeDesc);

        Text vertices_desc = new Text("Vertices");
        Text edges_desc = new Text("Edges");
        vertices_desc.setFill(Color.BLACK);
        edges_desc.setFill(Color.BLACK);
        vertices_desc.setFont(new Font(24));
        edges_desc.setFont(new Font(24));

        vertices_desc.xProperty().set(25);
        vertices_desc.yProperty().set(35);
        edges_desc.xProperty().set(25);
        edges_desc.yProperty().set(90);
        leftPane.getChildren().addAll(vertices_desc, edges_desc);

        TextField vertices_txt = new TextField();
        TextField edges_txt = new TextField();
        vertices_txt.setPromptText("Number");
        edges_txt.setPromptText("Number");
        vertices_txt.getStyleClass().add("rounded-text-field");
        edges_txt.getStyleClass().add("rounded-text-field");
        vertices_txt.setPrefSize(73, 40);
        edges_txt.setPrefSize(73, 40);
        vertices_txt.layoutXProperty().set(250);
        vertices_txt.layoutYProperty().set(10);
        edges_txt.layoutXProperty().set(250);
        edges_txt.layoutYProperty().set(65);
        leftPane.getChildren().addAll(vertices_txt, edges_txt);

        Image pause_icon = null;
        try {
            pause_icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/icons/pause_button.png")));
        } catch (Exception e) {
            System.out.println("Error loading pause icon: " + e.getMessage());
        }

        ImageView pause_icon_view = new ImageView(pause_icon);
        pause_icon_view.setFitHeight(40);
        pause_icon_view.setFitWidth(40);

        Button pauseButton = new Button();
        pauseButton.setGraphic(pause_icon_view);
        pauseButton.getStyleClass().add("rounded-button");
        pauseButton.setPrefSize(40, 40);
        pauseButton.layoutXProperty().set(355);
        pauseButton.layoutYProperty().set(12);
        pauseButton.setOnAction(e -> {
            if (gameTimer.gameTime.getStatus() == Timeline.Status.RUNNING) {
                gameTimer.gameTime.pause();
                pauseMenu pauseMenu = new pauseMenu(this, gameTimer);
                pauseMenu.pause(stage.getScene());
            } else {
                System.out.println("Game time is not running or paused.");
            }
        });
        pauseButton.setDisable(true);

        CheckBox showLabels = new CheckBox("Show Labels");
        showLabels.layoutXProperty().set(30);
        showLabels.layoutYProperty().set(500);
        showLabels.setOnAction(e -> {
            if (showLabels.isSelected()) {
                toggleLabel = 1;
                graphDisplay.updateToggleLabel(toggleLabel);
            } else {
                toggleLabel = 0;
                graphDisplay.updateToggleLabel(toggleLabel);
            }
        });

        if (gameMode == 2 || gameMode == 3) {
            showLabels.setDisable(true);
            toggleLabel = 1;
        }


        leftPane.getChildren().add(showLabels);

        Button generateButton = new Button("Generate");
        generateButton.getStyleClass().add("rounded-button");
        generateButton.setPrefSize(300, 40);
        generateButton.layoutXProperty().set(25);
        generateButton.layoutYProperty().set(125);

        leftPane.getChildren().add(generateButton);

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("rounded-button");
        clearButton.setPrefSize(300, 40);
        clearButton.layoutXProperty().set(25);
        clearButton.layoutYProperty().set(175);
        clearButton.setOnAction(e -> {
            graphDisplay.getChildren().clear();
            vertices_txt.clear();
            edges_txt.clear();
            generateButton.setDisable(false);
            showLabels.setDisable(false);
        });
        leftPane.getChildren().add(clearButton);

        Button undoButton = new Button("Undo");
        undoButton.getStyleClass().add("rounded-button");
        undoButton.setPrefSize(300, 40);
        undoButton.layoutXProperty().set(25);
        undoButton.layoutYProperty().set(225);
        undoButton.setOnAction(e -> graphDisplay.undoColor());
        undoButton.setDisable(!graphDisplay.isColored());
        graphDisplay.isColoredProperty().addListener((observable, oldValue, newValue) -> undoButton.setDisable(!newValue));
        if (gameMode != 2) {
            leftPane.getChildren().add(undoButton);
        }


        Text gamemodeDesc = getGamemodeDesc();
        TextFlow gamemodeDescFlow = new TextFlow(gamemodeDesc);
        gamemodeDescFlow.setPrefSize(300, 200);
        gamemodeDescFlow.layoutXProperty().set(25);
        gamemodeDescFlow.layoutYProperty().set(280);
        leftPane.getChildren().add(gamemodeDescFlow);


        Button giveUpButton = new Button("Give Up");
        giveUpButton.getStyleClass().add("give-up-button");
        giveUpButton.setPrefSize(100, 40);
        giveUpButton.layoutXProperty().set(420);
        giveUpButton.layoutYProperty().set(15);
        giveUpButton.setOnAction(e -> {
            if (vertices_txt.getText().isEmpty() || edges_txt.getText().isEmpty()) {
                graphComplete endScreen = new graphComplete(false, currentGameMode, 0, gameTimer);
                endScreen.showPage(stage);
            } else {
                graphDisplay.callFillColorsAfterGiveup(graphDisplay);
                PauseTransition delay = new PauseTransition(Duration.millis(3000));
                delay.setOnFinished(event -> {
                    graphComplete endScreen = new graphComplete(false, currentGameMode, 0, gameTimer);
                    endScreen.showPage(stage);
                });
                delay.play();
            }
        });

        topPane.getChildren().addAll(pauseButton, giveUpButton);

        Button uploadButton = new Button("Upload");
        uploadButton.getStyleClass().add("rounded-button");
        uploadButton.setPrefSize(200, 50);
        uploadButton.layoutXProperty().set(35);
        uploadButton.layoutYProperty().set(25);
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                System.out.println("File selected: " + selectedFile.getAbsolutePath());
                ReadGraph uploadGraph = new ReadGraph(selectedFile);
                uploadGraph.readGraph();
                uploadGraph.getGraphData();
                graphDisplay.drawGraph(uploadGraph, graphDisplay);
                vertices_txt.setText(String.valueOf(uploadGraph.numVertices));
                edges_txt.setText(String.valueOf(uploadGraph.numEdges));
                gameTimer.gameTime.play();
            } else {
                System.out.println("File selection cancelled.");
            }
        });

        Button hintButton = new Button("Hint");
        hintButton.getStyleClass().add("rounded-button");
        hintButton.setPrefSize(75, 50);
        hintButton.layoutXProperty().set(245);
        hintButton.layoutYProperty().set(25);
        hintButton.setOnAction(e -> {
            if (firstHint) {
                errorPopup.showInfo("Hint: Click on the color buttons to select a color.");
                firstHint = false;
            } else {
                graphDisplay.doHintColor();
            }
        });
        hintButton.setDisable(true);

        bottomPane.getChildren().addAll(bottom_bar, uploadButton, hintButton);


        vertices_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                errorPopup.showError("Please enter a valid integer for vertices.");
                vertices_txt.setText(oldValue);
            }
        });

        edges_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                errorPopup.showError("Please enter a valid integer for edges.");
                edges_txt.setText(oldValue);
            }
        });

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportWidth(850); // Adjust the width as needed

        HBox buttonGrid = new HBox(0);
        scrollPane.setContent(buttonGrid);
        for (int i = 0; i < 5; i++) {
            String color = ColorGenerator.generateRandomHexColor();
            Button button = createColorButtons(color);
            button.setOnAction(e -> {
                graphDisplay.selectedColor = Color.web(color);
                System.out.println("Selected color: " + graphDisplay.selectedColor);
            });
            buttonGrid.getChildren().add(button);
        }

        Button addColorButton = new Button("Add Color");
        addColorButton.setPrefSize(100, 100);
        addColorButton.setOnAction(e -> {
            String color = ColorGenerator.generateRandomHexColor();
            Button newColorButton = createColorButtons(color);
            newColorButton.setOnAction(e2 -> {
                graphDisplay.selectedColor = Color.web(color);
                System.out.println("Selected color: " + graphDisplay.selectedColor);
            });
            buttonGrid.getChildren().remove(addColorButton);
            buttonGrid.getChildren().add(newColorButton);
            buttonGrid.getChildren().add(addColorButton);
        });
        buttonGrid.getChildren().add(addColorButton);
        scrollPane.layoutXProperty().set(350);
        scrollPane.layoutYProperty().set(0);
        bottomPane.getChildren().add(scrollPane);

        generateButton.setOnAction(e -> {
            if (vertices_txt.getText().isEmpty() || edges_txt.getText().isEmpty()) {
                errorPopup.showError("Please enter a valid number of vertices and edges.");
            } else if ((Integer.parseInt(vertices_txt.getText()) > 75 || Integer.parseInt(edges_txt.getText()) > 75)) {
                errorPopup.showError("Please enter a number of vertices and edges less than 75.");
            } else {
                graphDisplay.getChildren().clear();
                graphData = null;
                graphData = new ReadGraph(Integer.parseInt(vertices_txt.getText()), Integer.parseInt(edges_txt.getText()));
                graphDisplay.drawGraph(vertices_txt, edges_txt, graphDisplay);
                graphData.setAdjList(graphDisplay.adjMatrix);
                chromaticNumber = graphData.getGraphData();
                graphData.chooseBacktrack();
                gameTimer.gameTime.play();
                pauseButton.setDisable(false);
                generateButton.setDisable(true);
                showLabels.setDisable(true);
                hintButton.setDisable(false);
            }
        });

        // BorderPane & Scene Final Lines *DO NOT WRITE CODE UNDERLINE*
        Scene scene = new Scene(borderPane, windowWidth, windowHeight);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    private Text getGamemodeDesc() {
        Text gamemodeDesc = switch (gameMode) {
            case 1 ->
                    new Text("How to play: \nIn this mode, you must color the graph as quickly as you can. Less colors = Higher Score. No two adjacent vertices can have the same color.");
            case 2 ->
                    new Text("How to play: \nIn this mode, the computer generates a random ordering of the vertices and you have to pick the colours of the vertices in exactly that order. Once the colour of a vertex has been chosen, it cannot be changed again. You can always add a new color. The goal is to use the least amount of colors possible.");
            case 3 ->
                    new Text("How to play: \nIn this mode, the computer generates a random ordering of the vertices and you have to pick the colours of the vertices in exactly that order. You are allowed to undo colorings in the order they were made. You can always add a new color. The goal is to use the least amount of colors possible.");
            default -> null;
        };
        assert gamemodeDesc != null;
        gamemodeDesc.setFill(Color.BLACK);
        gamemodeDesc.setFont(new Font(18));
        return gamemodeDesc;
    }

    private Button createColorButtons(String color) {
        Button button = new Button();
        button.setStyle("-fx-background-color: " + color + ";");
        button.setPrefSize(100, 100);
        return button;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void endGame(Stage stage, int vertices, int edges, int gameMode, TimeService gameTimer, GraphDisplay graphDisplay) {
        String currentGameMode = switch (gameMode) {
            case 1 -> mode1;
            case 2 -> mode2;
            case 3 -> mode3;
            default -> "Invalid Game Mode";
        };
        scoreCalculate scoreCalculate = new scoreCalculate(vertices, edges, chromaticNumber, graphDisplay.getColorsUsed(), gameTimer);
        graphComplete endScreen = new graphComplete(true, currentGameMode, scoreCalculate.calculateScore(), gameTimer);
        endScreen.showPage(stage);
    }
}
