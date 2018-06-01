package game;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class Hero extends Pane {
    public HeroAnimation animation;
    public Point2D movement = new Point2D(0, 0);
    public Label counter;
    Image heroImage = new Image("mario.png");
    ImageView imageView = new ImageView(heroImage);
    int frames = 3;
    int xOffset = 96;
    int yOffset = 80;
    int width = 16;
    int height = 16;
    Unit deadCoin = null;
    private boolean jumpPermission = true;


    public Hero() {
        imageView.setFitHeight(Main.heroSize);
        imageView.setFitWidth(Main.heroSize);
        imageView.setViewport(new Rectangle2D(xOffset, yOffset, width, height));
        animation = new HeroAnimation(this.imageView, Duration.millis(200), frames, xOffset, yOffset, width, height);
        getChildren().addAll(this.imageView);
    }

    public void moveX(int value) {
        boolean movingRight = (value > 0);
        for (int i = 0; i < Math.abs(value); i++) {
            for (Unit texture : Main.textures) {
                if (this.getBoundsInParent().intersects(texture.getBoundsInParent())) {
                    if (movingRight) {
                        if (this.getTranslateX() + Main.heroSize == texture.getTranslateX()) {
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else {
                        if (this.getTranslateX() == texture.getTranslateX() + Main.unitSize) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
            isCoinPickedUp();
            if (Main.score == Main.maxScore) {
                areAllCoinsPickedUp();
            }
        }
    }

    public void moveY(int value) {
        boolean movingDown = (value > 0);
        for (int i = 0; i < Math.abs(value); i++) {
            for (Unit texture : Main.textures) {
                if (getBoundsInParent().intersects(texture.getBoundsInParent())) {
                    if (movingDown) {
                        if (this.getTranslateY() + Main.heroSize == texture.getTranslateY()) {
                            this.setTranslateY(this.getTranslateY() - 1);
                            jumpPermission = true;
                            return;
                        }
                    } else {
                        if (this.getTranslateY() == texture.getTranslateY() + Main.unitSize) {
                            this.setTranslateY(this.getTranslateY() + 1);
                            movement = new Point2D(0, 0);
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
            if (this.getTranslateY() > 650) {
                this.setTranslateX(0);
                this.setTranslateY(530);
                Main.game.setLayoutX(0);
            }
            isCoinPickedUp();
            if (Main.score == Main.maxScore) {
                areAllCoinsPickedUp();
            }
        }
    }

    public void isCoinPickedUp() {
        Main.coins.forEach((coin) -> {
            if (this.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                String takeSound = "C:/Users/-/IdeaProjects/game/src/main/resources/coin.mp3";
                Media sound = new Media(new File(takeSound).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                Main.mediaBackgroundPlayer.pause();
                mediaPlayer.play();
                Main.mediaBackgroundPlayer.play();
                deadCoin = coin;
                Main.app.getChildren().remove(counter);
                Main.score++;
                counter = getCounter(Main.score);
                Main.app.getChildren().add(counter);
            }
        });
        Main.coins.remove(deadCoin);
        Main.textures.remove(deadCoin);
        Main.game.getChildren().remove(deadCoin);
    }

    public void areAllCoinsPickedUp() {
        Stage alertWindow = new Stage();
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Конец игры");
        alertWindow.setMinHeight(200);
        alertWindow.setMinWidth(300);
        Label winInfo = new Label("Ты победил!");
        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> System.exit(0));
        VBox alert = new VBox();
        alert.getChildren().addAll(winInfo, closeButton);
        alert.setAlignment(Pos.CENTER);
        Scene scene = new Scene(alert);
        alertWindow.setScene(scene);
        Main.timer.stop();
        alertWindow.show();
    }

    public Label getCounter(int score) {
        String scoreString = score + "/" + Main.maxScore;
        Label scoreLabel = new Label(scoreString);
        scoreLabel.setTranslateX(30);
        scoreLabel.setTranslateY(50);
        return scoreLabel;
    }

    public void jump() {
        if (jumpPermission) {
            movement = movement.add(0, -30);
            jumpPermission = false;
        }
    }
}
