import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class graphComplete {
    boolean endScreen;
    String previousMode;
    int score;
    TimeService time;

    public graphComplete(boolean endScreen, String previousMode, int score, TimeService time) {
        this.endScreen = endScreen;
        this.previousMode = previousMode;
        this.score = score;
        this.time = time;
    }

    @SuppressWarnings("unused")
    public void showPage(Stage primaryStage) {
        int scoreVal = score;
        String timeVal = time.formatTime();

        int windowHeight = 700;
        int windowWidth = 1200;

        leaderBoard leaderBoard = new leaderBoard();

        switch (previousMode) {
            case "To the Bitter End" -> leaderBoard.recordScore(scoreVal, "test player", 1);
            case "Random Order" -> leaderBoard.recordScore(scoreVal, "test player", 2);
            case "I Changed My Mind" -> leaderBoard.recordScore(scoreVal, "test player", 3);
        }


        //load background image
        Image backgroundImage = new Image("assets/images/background.jpeg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(windowWidth);  // Fit image to window width
        backgroundImageView.setFitHeight(windowHeight);  // Fit image to window height
        //backgroundImageView.setPreserveRatio(true);  NEED TO MAKE THE IMAGE THE CORRECT RATIO

        //main layout
        Pane pane = new Pane();

        pane.getChildren().add(backgroundImageView);

        Rectangle rectangle = new Rectangle(150, 100, 900, 500);
        rectangle.setFill(Color.web("#fdffbf"));
        rectangle.setOpacity(0.7);


        Text complete = new Text("GRAPH COMPLETE!");
        complete.xProperty().set(210);
        complete.yProperty().set(210);
        complete.setFont(new Font(90));

        Text gaveUp = new Text("BETTER LUCK NEXT TIME :(");
        gaveUp.xProperty().set(210);
        gaveUp.yProperty().set(210);
        gaveUp.setFont(new Font(70));


        Text timeTaken = new Text("Time taken: " + timeVal);
        timeTaken.xProperty().set(480);
        timeTaken.yProperty().set(290);
        timeTaken.setFont(new Font(28));

        Text score = new Text("Score: " + scoreVal);
        score.xProperty().set(540);
        score.yProperty().set(250);
        score.setFont(new Font(28));


        Button returnToMain = new Button("Return to Main Menu");
        returnToMain.setPrefSize(370, 60); // 185
        returnToMain.setFont(new Font(28));
        returnToMain.layoutXProperty().set(415);
        returnToMain.layoutYProperty().set(350);
        returnToMain.setOnAction(_ -> {
            startMenu game = new startMenu();
            try {
                game.start(primaryStage);
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
        });


        Text mode = new Text("Select Mode:");
        mode.setFont(new Font(28));
        mode.xProperty().set(210);
        mode.yProperty().set(490);

        ChoiceBox<String> selectMode = new ChoiceBox<>(FXCollections.observableArrayList(
                "To the Bitter End", "Random Order", "I Changed My Mind"
        ));
        selectMode.setValue(previousMode);
        selectMode.setPrefSize(365, 45);
        selectMode.layoutXProperty().set(210);
        selectMode.layoutYProperty().set(505);


        Button restart = new Button("Restart Graph");
        restart.setFont(new Font(20));
        restart.setPrefSize(365, 45);
        restart.layoutXProperty().set(625);
        restart.layoutYProperty().set(505);
        restart.setOnAction(_ -> {
            String selectedMode = selectMode.getValue();
            switch (selectedMode) {
                case "To the Bitter End" -> {
                    // Open gameField
                    gameField game = new gameField(1);
                    // Close the current window
                    primaryStage.close();
                }
                case "Random Order" -> {
                    // Open gameField
                    gameField game = new gameField(2);
                    // Close the current window
                    primaryStage.close();
                }
                case "I Changed My Mind" -> {
                    // Open gameField
                    gameField game = new gameField(3);
                    // Close the current window
                    primaryStage.close();
                }
            }
        });


        pane.getChildren().addAll(rectangle, returnToMain, restart, score, mode, selectMode);

        if (endScreen) {
            pane.getChildren().addAll(complete, timeTaken);
        } else {
            pane.getChildren().addAll(gaveUp);
        }

        Scene scene = new Scene(pane, windowWidth, windowHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
