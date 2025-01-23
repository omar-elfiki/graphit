import javafx.application.Application;
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


@SuppressWarnings("unused")
public class startMenu extends Application {
    @Override
    public void start(Stage primaryStage) {
        int windowHeight = 700;
        int windowWidth = 1200;

        //load background image
        Image backgroundImage = new Image("assets/images/background.jpeg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(windowWidth);
        backgroundImageView.setFitHeight(windowHeight);

        //main layout
        Pane pane = new Pane();

        pane.getChildren().add(backgroundImageView);

        Rectangle rectangle = new Rectangle (150, 100, 900, 500 );
        rectangle.setFill(Color.web("#fdffbf"));
        rectangle.setOpacity(0.7);

        Text complete = new Text ("GRAPH IT!");
        complete.xProperty().set(210);
        complete.yProperty().set(210);
        complete.setFont(new Font(90));

        Text mode = new Text ("Select Mode:");
        mode.setFont(new Font(28));
        mode.xProperty().set(210);
        mode.yProperty().set(490);

        ChoiceBox<String> selectMode = new ChoiceBox<>(FXCollections.observableArrayList(
                "Select Mode","To The Bitter End", "Random Order", "I Changed My mind"
        ));
        selectMode.setValue("Select Mode");
        selectMode.setPrefSize(365, 45);
        selectMode.layoutXProperty().set(210);
        selectMode.layoutYProperty().set(505);

        Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setFont(new Font(20));
        leaderboardButton.setPrefSize(365, 45);
        leaderboardButton.layoutXProperty().set(625);
        leaderboardButton.layoutYProperty().set(505);
        leaderboardButton.setDisable(true);
        leaderboardButton.setOnAction(_ -> {
            leaderBoard leaderBoard = new leaderBoard();
            leaderBoard.createRecordFile();
            if (selectMode.getValue().equals("To The Bitter End")) {
                leaderBoard.start(primaryStage,leaderBoard.readRecord(1));
            } else if (selectMode.getValue().equals("Random Order")) {
                leaderBoard.start(primaryStage,leaderBoard.readRecord(2));
            } else if (selectMode.getValue().equals("I Changed My mind")) {
                leaderBoard.start(primaryStage,leaderBoard.readRecord(3));
            }
        });

        Button startButton = new Button ("Start");
        startButton.setPrefSize(370, 60); // 185
        startButton.setFont(new Font(28));
        startButton.layoutXProperty().set(415);
        startButton.layoutYProperty().set(300);
        startButton.setDisable(true);

        startButton.setOnAction(_ -> {
            String selectedMode = selectMode.getValue();
            switch (selectedMode) {
                case "Select Mode" -> errorPopup.showError("You need to select a Game Mode to start.");
                case "To The Bitter End" -> {
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
                case "I Changed My mind" -> {
                    // Open gameField
                    gameField game = new gameField(3);

                    // Close the current window
                    primaryStage.close();
                }
            }
        });

        selectMode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Select Mode")) {
                leaderboardButton.setDisable(false);
                startButton.setDisable(false);

            } else {
                leaderboardButton.setDisable(true);
                startButton.setDisable(true);
            }
        });

        pane.getChildren().addAll(rectangle, complete, leaderboardButton, startButton, mode, selectMode);

        Scene scene = new Scene(pane, windowWidth, windowHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
