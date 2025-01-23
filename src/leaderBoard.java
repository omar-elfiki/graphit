import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import java.time.LocalDate;


@SuppressWarnings("CallToPrintStackTrace")
public class leaderBoard{


    //the set of button in startmenu
//            leaderboardButton.setOnAction(_ -> {
//        leaderBoard leaderBoard = new leaderBoard();
//        leaderBoard.createRecordFile(1);
//        leaderBoard.start(primaryStage,leaderBoard.readRecord());
//    });



    //record score after each game
//    recordScore
//    leaderBoard.recordScore(1, 1, "abc", "scoreRecord_gm1.txt");



    //should be called when user open startMenu first time
    public void createRecordFile() {
        try {
            File scoreRecord_gm1 = new File("scoreRecord_gm1.txt");
            if (scoreRecord_gm1.createNewFile()) {
                System.out.println("File created: " + scoreRecord_gm1.getName());
                initializeRecord(1);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            File scoreRecord_gm2 = new File("scoreRecord_gm2.txt");
            if (scoreRecord_gm2.createNewFile()) {
                System.out.println("File created: " + scoreRecord_gm2.getName());
                initializeRecord(2);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            File scoreRecord_gm3 = new File("scoreRecord_gm3.txt");
            if (scoreRecord_gm3.createNewFile()) {
                System.out.println("File created: " + scoreRecord_gm3.getName());
                initializeRecord(3);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //initialize five 0 scores to avoid ArrayList overflow
    public void initializeRecord(int gamemode) {
        for(int i =0; i< 5; i++){
            recordScore(0,  "nobody" , gamemode);
        }
    }


    //should be called after each game to write score
    //write the score to the end of txt
    public void recordScore(int score, String name, int gamemode) {

        String fileName = "";
        if(gamemode == 1)
            fileName = "scoreRecord_gm1.txt";
        else if(gamemode == 2)
            fileName = "scoreRecord_gm2.txt";
        else if(gamemode == 3)
            fileName = "scoreRecord_gm3.txt";

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("Score: " + score +" " +name +" "+LocalDate.now() + "\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //should be called every time when user back to start men
    //read the record txt and return the first five score as ArrayList
    public ArrayList<String> readRecord(int gamemode) {
        String fileName = "";
        if (gamemode == 1)
            fileName = "scoreRecord_gm1.txt";
        else if (gamemode == 2)
            fileName = "scoreRecord_gm2.txt";
        else if (gamemode == 3)
            fileName = "scoreRecord_gm3.txt";

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String record;
            ArrayList<Integer> score = new ArrayList<>();
            ArrayList<Integer> scoreRecord = new ArrayList<>();
            ArrayList<String> name = new ArrayList<>();
            ArrayList<String> date = new ArrayList<>();
            ArrayList<String> sum = new ArrayList<>();
            String currentLine;
            String[] codeAndText;
            String space = "                ";

            while ((record = br.readLine()) != null) {
                if (record.startsWith("Score:")) {
                    currentLine = record.substring(6);
                    codeAndText = currentLine.split(" ", 4);
                    score.add(Integer.parseInt(codeAndText[1]));
                    scoreRecord.add(Integer.parseInt(codeAndText[1]));
                    name.add(codeAndText[2]);
                    date.add(codeAndText[3]);
                }
            }
            score.sort(Collections.reverseOrder());

            for (int i = 0; i < Math.min(5, score.size()); i++) {
                sum.add(score.get(i) + space + gamemode + space + "    " + name.get(scoreRecord.indexOf(score.get(i))) + space + date.get(scoreRecord.indexOf(score.get(i))));
            }

            // Fill remaining slots with default values if there are less than 5 records
            while (sum.size() < 5) {
                sum.add("0" + space + gamemode + space + "N/A" + space + "N/A");
            }

            return sum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(Stage primaryStage, ArrayList<String> sum) {

        int windowHeight = 700;
        int windowWidth = 1200;

        //load background image
        Image backgroundImage = new Image("assets/images/background.jpeg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(windowWidth);  // Fit image to window width
        backgroundImageView.setFitHeight(windowHeight);  // Fit image to window height

        //main layout
        Pane pane = new Pane();

        pane.getChildren().add(backgroundImageView);

        Rectangle rectangle = new Rectangle (150, 100, 900, 500 );
        rectangle.setFill(Color.web("#fdffbf"));
        rectangle.setOpacity(0.7);

        String space = "        ";
        Text rank = new Text ("Rank" + space + "Score" + space + "Mode" + space + "Name" + space + "Date");
        rank.xProperty().set(240);
        rank.yProperty().set(150);
        rank.setFont(new Font(28));

        Text score1 = new Text ("1" + space + sum.getFirst());
        score1.xProperty().set(240);
        score1.yProperty().set(250);
        score1.setFont(new Font(28));

        Text score2 = new Text ("2" + space + sum.get(1));
        score2.xProperty().set(240);
        score2.yProperty().set(300);
        score2.setFont(new Font(28));

        Text score3 = new Text ("3" + space + sum.get(2));
        score3.xProperty().set(240);
        score3.yProperty().set(350);
        score3.setFont(new Font(28));

        Text score4 = new Text ("4" + space + sum.get(3));
        score4.xProperty().set(240);
        score4.yProperty().set(400);
        score4.setFont(new Font(28));

        Text score5 = new Text ("5" + space + sum.get(4));
        score5.xProperty().set(240);
        score5.yProperty().set(450);
        score5.setFont(new Font(28));

        Button returnToMain = new Button ("Return to Main Menu");
        returnToMain.setPrefSize(340, 60);
        returnToMain.setFont(new Font(36));
        returnToMain.layoutXProperty().set(450);
        returnToMain.layoutYProperty().set(500);
        returnToMain.setOnAction(_ -> {
            startMenu leaderBoard = new startMenu();
            try{
                leaderBoard.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        pane.getChildren().addAll(rectangle,rank, score1, score2, score3, score4, score5, returnToMain);

        Scene scene = new Scene(pane, windowWidth, windowHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

