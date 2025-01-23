import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class pauseMenu extends Stage {
    gameField game;
    TimeService gameTime;

    public pauseMenu(gameField game, TimeService gameTime) {
        this.game = game;
        this.gameTime = gameTime;
    }

    public void pause(Scene screen) {
        GaussianBlur blur = new GaussianBlur(5);
        screen.getRoot().setEffect(blur);
        Pane pauseOverlay = new Pane();
        Button resumeButton = new Button("                  PAUSED\npress again here to continue");
        resumeButton.getStyleClass().add("square-button");
        resumeButton.setStyle("-fx-background-color: #3498db;-fx-font-size: 50px; -fx-text-fill: blue;");
        resumeButton.setPrefSize(900, 535);//1500,900
        resumeButton.setLayoutX(350);
        resumeButton.setLayoutY(70);
        resumeButton.setOnAction(_ -> {
            Timeline timeline = gameTime.getGameTime();
            play(screen, pauseOverlay, timeline); // Remove blur
        });
        pauseOverlay.getChildren().add(resumeButton);
        ((Pane) screen.getRoot()).getChildren().add(pauseOverlay);
        //Need to change something in gameField+deactivate button
    }
    public static void play(Scene screen, Pane pauseOverlay, Timeline timeline) {
        screen.getRoot().setEffect(null);
        ((Pane) screen.getRoot()).getChildren().remove(pauseOverlay);
        timeline.play();
    }
}

