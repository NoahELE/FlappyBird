import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Pipe {
    public static final int GAP = 168;
    public static double stepSize = 3;
    protected final boolean upright;
    protected double x;
    protected boolean passedByBird;
    protected boolean collideWithBird;
    protected Image image;
    protected double y;


    public Pipe(boolean upright) {
        this.upright = upright;
        x = ShadowFlap.WIDTH;
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

    public void move() {
        x -= stepSize;
    }

    public void draw() {
        DrawOptions opt = new DrawOptions();
        if (upright) {
            opt.setRotation(Math.PI);
        }
        image.drawFromTopLeft(x, y, opt);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean collideWith(Bird bird) {
        return getRect().intersects(bird.getRect());
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

    public Image getImage() {
        return image;
    }

    public boolean isOutOfBound() {
        return this.x < -image.getWidth();
    }
}
