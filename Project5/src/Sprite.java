import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Sprite extends ImageView
{
    boolean dead = false;
    final String type;

    Sprite(double x, double y, int w, int h, String type, Image image)
    {
        super(image);
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    void moveLeft(int speed) {
        setTranslateX(getTranslateX() - speed);
    }

    void moveRight(int speed) {
        setTranslateX(getTranslateX() + speed);
    }

    void moveUp(int speed) {
        setTranslateY(getTranslateY() - speed);
    }

    void moveDown(int speed) {
        setTranslateY(getTranslateY() + speed);
    }
}