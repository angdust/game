package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class Unit extends Pane {
    Image unitImage = new Image("texture.png");
    ImageView unit;
    public enum UnitType {
        FLOOR, PLATFORM, COIN, TOP_OF_PIPE, BOTTOM_OF_PIPE, INVISIBLE_UNIT, STONE
    }
    public Unit(UnitType unitType, int x, int y) {
        unit = new ImageView(unitImage);
        unit.setFitWidth(Main.unitSize);
        unit.setFitHeight(Main.unitSize);
        setTranslateX(x);
        setTranslateY(y);
        boolean coin = false;

        switch (unitType) {
            case FLOOR:
                unit.setViewport(new Rectangle2D(0, 0, 16, 16));
                break;
            case PLATFORM:
                unit.setViewport(new Rectangle2D(16, 0, 16, 16));
                break;
            case COIN:
                unit.setViewport(new Rectangle2D(384.5, 16.5, 15.5, 15.2));
                coin = true;
                break;
            case TOP_OF_PIPE:
                unit.setViewport(new Rectangle2D(0, 128, 32, 16));
                unit.setFitWidth(Main.unitSize * 2);
                break;
            case BOTTOM_OF_PIPE:
                unit.setViewport(new Rectangle2D(0, 145, 32, 14));
                unit.setFitWidth(Main.unitSize * 2);
                break;
            case INVISIBLE_UNIT:
                unit.setViewport(new Rectangle2D(0, 0, 16, 16));
                unit.setOpacity(0);
                break;
            case STONE:
                unit.setViewport(new Rectangle2D(0, 16, 16, 16));
                break;
        }
        getChildren().add(unit);
        Main.textures.add(this);
        if (coin) {
            Main.coins.add(this);
            coin = false;
        }
        Main.game.getChildren().add(this);
    }
}
