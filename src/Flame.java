import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Flame {
    public static final int lifeLength = 30;
    private final Image image = new Image("res/level-1/flame.png");
    private final boolean upright;
    private double x;
    private double y;
    private int existCounter;

    /**
     * create a flame
     * @param pipe the pipe that the flame belongs to
     */
    public Flame(Pipe pipe) {
        this.x = pipe.getX();
        this.upright = pipe.upright;
        if (upright) {
            this.y = pipe.getY() - image.getHeight() - 10;
        } else {
            this.y = pipe.getY() + pipe.getImage().getHeight() + 10;
        }
        existCounter = 0;
    }

    /**
     * draw the flame
     */
    public void draw() {
        DrawOptions opt = new DrawOptions();
        if (upright) {
            opt.setRotation(Math.PI);
        }
        image.drawFromTopLeft(x, y, opt);
        existCounter++;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets exist counter.
     *
     * @return the exist counter
     */
    public int getExistCounter() {
        return existCounter;
    }

    /**
     * Gets rect.
     *
     * @return the rect
     */
    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }
}
