package game;

import game.Unit;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    public static final int unitSize = 45;
    public static final int heroSize = 40;
    public static Pane app = new Pane();
    public static Pane game = new Pane();
    public static ArrayList<Unit> textures = new ArrayList<>();
    public static ArrayList<Unit> coins = new ArrayList<>();
    public static int score = 0;
    public static int maxScore = 0;
    public static int widthOfLevel;
    public static AnimationTimer timer;
    private static String takeBackgroundSound = "C:/Users/-/IdeaProjects/game/src/main/resources/backgroundSound.mp3";
    private static Media backgroundSound = new Media(new File(takeBackgroundSound).toURI().toString());
    public static MediaPlayer mediaBackgroundPlayer = new MediaPlayer(backgroundSound);
    public Hero luigi;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private Image backgroundImage = new Image("background.png");
    private Pane counter = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    private void drawTheMap() {
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(212 * unitSize);
        backgroundImageView.setFitHeight(14 * unitSize);
        Label rule = new Label("Collect all the coins for victory!!!");
        rule.setTranslateX(30);
        rule.setTranslateY(30);
        counter.getChildren().add(rule);
        widthOfLevel = DataOfLevel.levelData[0].length() * unitSize;
        for (int i = 0; i < DataOfLevel.levelData.length; i++) {
            String string = DataOfLevel.levelData[i];
            for (int j = 0; j < string.length(); j++) {
                switch (string.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Unit floor = new Unit(Unit.UnitType.FLOOR, j * unitSize, i * unitSize);
                        break;
                    case '2':
                        Unit platform = new Unit(Unit.UnitType.PLATFORM, j * unitSize, i * unitSize);
                        break;
                    case '3':
                        Unit coin = new Unit(Unit.UnitType.COIN, j * unitSize, i * unitSize);
                        break;
                    case '4':
                        Unit stone = new Unit(Unit.UnitType.STONE, j * unitSize, i * unitSize);
                        break;
                    case '5':
                        Unit topOfPipe = new Unit(Unit.UnitType.TOP_OF_PIPE, j * unitSize, i * unitSize);
                        break;
                    case '6':
                        Unit bottomOfPipe = new Unit(Unit.UnitType.BOTTOM_OF_PIPE, j * unitSize, i * unitSize);
                        break;
                    case '7':
                        Unit invisibleUnit = new Unit(Unit.UnitType.INVISIBLE_UNIT, j * unitSize, i * unitSize);
                        break;
                }
            }
        }
        luigi = new Hero();
        luigi.setTranslateX(0);
        luigi.setTranslateY(530);
        luigi.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < widthOfLevel - 640) {
                game.setLayoutX(-(offset - 640));
                backgroundImageView.setLayoutX(-(offset - 640));
            }
        });


        game.getChildren().add(luigi);

        app.getChildren().addAll(backgroundImageView, game);
        app.getChildren().addAll(counter);
        maxScore = coins.size();
    }

    private void update() {
        if (isPressed(KeyCode.RIGHT) && luigi.getTranslateX() + 40 <= widthOfLevel - 5) {
            luigi.setScaleX(1);
            luigi.animation.play();
            luigi.moveX(5);
        }
        if (isPressed(KeyCode.LEFT) && luigi.getTranslateX() >= 5) {
            luigi.setScaleX(-1);
            luigi.animation.play();
            luigi.moveX(-5);
        }
        if (isPressed(KeyCode.UP) && luigi.getTranslateY() >= 5) {
            luigi.jump();
        }
        if (luigi.movement.getY() < 10) {
            luigi.movement = luigi.movement.add(0, 1);
        }
        luigi.moveY((int) luigi.movement.getY());
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        drawTheMap();
        mediaBackgroundPlayer.setVolume(0.3);
        mediaBackgroundPlayer.setCycleCount(100);
        mediaBackgroundPlayer.play();
        Scene scene = new Scene(app, 1200, 620);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
            luigi.animation.stop();
        });
        primaryStage.setTitle("Platformer game");
        primaryStage.setScene(scene);
        primaryStage.show();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
}