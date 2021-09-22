import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Pipe {
    public static final int GAP = 168;
    private final int STEP_SIZE = 5;
    private final Image image;
    private final boolean upright;
    private final int type;
    private final double y;
    private double x;
    private boolean passedByBird;

    private boolean collideWithBird;

    public Pipe(boolean upright, double height, int type) {
        this.type = type; // 0 is plastic, 1 is steel
        if (type == 0) {
            image = new Image("res/level/plasticPipe.png");
        } else {
            image = new Image("res/level-1/steelPipe.png");
        }
        this.upright = upright;
        x = ShadowFlap.WIDTH;
        y = upright ? height : (height - image.getHeight());
        passedByBird = false;
        collideWithBird = false;
    }

    public static double getRandomPos(int level) {
        // generate random position for pipe pair for different level
        if (level == 0) {
            double r = Math.random() * 3;
            if (r < 1) {
                return 100;
            } else if (r < 2) {
                return 300;
            } else {
                return 500;
            }
        } else {
            return 100 + 400 * Math.random();
        }
    }

    public void draw() {
        DrawOptions opt = new DrawOptions();
        if (upright) {
            opt.setRotation(Math.PI);
        }
        image.drawFromTopLeft(x, y, opt);
    }

    public void move() {
        x -= STEP_SIZE;
    }

    public boolean isOutOfBound() {
        return this.x < -image.getWidth();
    }

    public Image getImage() {
        return image;
    }

    public double getX() {
        return x;
    }

    public boolean getCollideWithBird() {
        return collideWithBird;
    }

    public void setCollideWithBird(boolean collideWithBird) {
        this.collideWithBird = collideWithBird;
    }

    public boolean getPassedByBird() {
        return passedByBird;
    }

    public void setPassedByBird(boolean passedByBird) {
        this.passedByBird = passedByBird;
    }

    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }
}
