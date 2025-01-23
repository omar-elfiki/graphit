import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimeService {
    public int minutes =0;
    public int seconds =0;
    public Timeline gameTime;
    public TimeService() {
    }
    public void startTime(Label timeLabel) { // Start the timer
        gameTime = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            String[] timeParts = timeLabel.getText().split(":");
            minutes = Integer.parseInt(timeParts[0]);
            seconds = Integer.parseInt(timeParts[1]);
            seconds++;
            if (seconds == 60) { // Reset seconds to 0 and increment minutes
                seconds = 0;
                minutes++;
            }
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        }));
        gameTime.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely-

    }

    public String formatTime() {
        if (gameTime == null) {
            System.out.println("Timeline is not initialized.");
            return "00:00";
        }
        if (gameTime.getStatus() != Timeline.Status.RUNNING) {
            System.out.println("Timeline is not running.");
            return "00:00";
        }

        System.out.println("Current time in minutes: " + minutes);
        System.out.println("Current time in seconds: " + seconds);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static int formatTimetoSeconds(TimeService gametime){
        return gametime.minutes*60 + gametime.seconds;
    }

    public Timeline getGameTime() {
        return gameTime;
    }
}
