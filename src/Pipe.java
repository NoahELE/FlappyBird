import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Pipe {
    public static final int GAP = 168;
    public static double stepSize = 5;
    protected final boolean upright;
    protected final Image image = setImage();
    protected double x;
    protected boolean passedByBird;
    protected boolean collideWithBird;
    protected boolean destroyed;
    protected double y;

    /**
     * initialise a pipe with direction
     *
     * @param upright whether the pipe is upright or downward
     */
    public Pipe(boolean upright) {
        this.upright = upright;
        x = ShadowFlap.WIDTH;
        passedByBird = false;
        collideWithBird = false;
    }

    /**
     * get a random position to create pipes
     *
     * @param level the level number
     * @return the generated random position
     */
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

    /**
     * move the pipe
     */
    public void move() {
        x -= stepSize;
    }

    /**
     * draw the pipe
     */
    public void draw() {
        DrawOptions opt = new DrawOptions();
        if (upright) {
            opt.setRotation(Math.PI);
        }
        image.drawFromTopLeft(x, y, opt);
    }

    /**
     * Is destroyed boolean.
     *
     * @return the boolean
     */
    public boolean isDestroyed() {
        return destroyed;
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
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * test whether the pipe collide with the bird
     * @param bird the bird
     * @return true if there is collision and vice versa
     */
    public boolean collideWith(Bird bird) {
        boolean res = getRect().intersects(bird.getRect());
        if (res) destroyed = true;
        return res;
    }

    /**
     * Gets collide with bird.
     *
     * @return the collide with bird
     */
    public boolean getCollideWithBird() {
        return collideWithBird;
    }

    /**
     * Sets collide with bird.
     *
     * @param collideWithBird the collide with bird
     */
    public void setCollideWithBird(boolean collideWithBird) {
        this.collideWithBird = collideWithBird;
    }

    /**
     * Gets passed by bird.
     *
     * @return the passed by bird
     */
    public boolean getPassedByBird() {
        return passedByBird;
    }

    /**
     * Sets passed by bird.
     *
     * @param passedByBird the passed by bird
     */
    public void setPassedByBird(boolean passedByBird) {
        this.passedByBird = passedByBird;
    }

    /**
     * get the Rectangle of the image at the coordinate
     * @return Rectangle with right coordinate
     */
    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * check if the pipe is out of the window
     * @return true if the pipe is out of window
     */
    public boolean isOutOfBound() {
        return this.x < -image.getWidth();
    }

    /**
     * check if the weapon hits the pipe
     * @param weapon the weapon to be tested
     * @return true if the weapon hits the pipe
     */
    public boolean collideWithWeapon(Weapon weapon) {
        return getRect().intersects(weapon.getRect());
    }

    /**
     * change the state after hit by weapon
     * @param weapon the weapon hits the pipe
     */
    public abstract void getHit(Weapon weapon);

    /**
     * Sets image.
     *
     * @return the image
     */
    protected abstract Image setImage();
}
