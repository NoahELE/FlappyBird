import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Weapon {
    protected static final double MOVE_SPEED = Pipe.stepSize;
    protected static final double SHOOT_SPEED = 5;
    protected double y;
    protected double x;
    protected Image image;

    public Weapon(double y) {
        x = ShadowFlap.WIDTH;
        this.y = y;
    }

    public boolean isOutOfBorder() {
        return x < -image.getWidth();
    }

    public void move() {
        x -= MOVE_SPEED;
    }

    public void draw() {
        image.drawFromTopLeft(x, y);
    }

    public void shoot() {
        x += SHOOT_SPEED;
    }

    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }
}
