import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bird {
    private final Image birdWingUp;
    private final Image birdWingDown;
    private final double x;
    private Image image;
    private int wingCounter;
    private double y;
    private double speed;
    private int lives;
    private Weapon weapon = null;

    /**
     * create a bird
     *
     * @param x the start x coordinate
     * @param y the start y coordinate
     * @param level the level that the game is in
     * @param lives the lives that the bird will have
     */
    public Bird(double x, double y, int level, int lives) {
        this.x = x;
        this.y = y;
        if (level == 0) {
            birdWingDown = new Image("res/level-0/birdWingDown.png");
            birdWingUp = new Image("res/level-0/birdWingUp.png");
        } else {
            birdWingDown = new Image("res/level-1/birdWingDown.png");
            birdWingUp = new Image("res/level-1/birdWingUp.png");
        }
        this.lives = lives;
        image = birdWingDown;
        wingCounter = 0;
        speed = 0;
    }

    /**
     * draw the bird
     */
    public void draw() {
        wingCounter++;
        if (wingCounter == 10) {
            wingCounter = 0;
            if (this.image == birdWingDown) {
                this.image = birdWingUp;
            } else {
                this.image = birdWingDown;
            }
        }
        image.draw(x, y);
    }

    /**
     * make the bird fall
     */
    public void fall() {
        speed += 0.4;
        if (speed >= 10) {
            speed = 10;
        }
    }

    /**
     * make the bird fly
     */
    public void fly() {
        speed = -6;
    }

    /**
     * make the bird move
     */
    public void move() {
        y += speed;
    }

    /**
     * Gets weapon.
     *
     * @return the weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Sets weapon.
     *
     * @param weapon the weapon
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * check if the bird is out of window
     *
     * @return true if the bird is out of window and vice versa
     */
    public boolean isOutOfBound() {
        return y < -image.getHeight() || y > ShadowFlap.HEIGHT;
    }

    /**
     * lose a life
     */
    public void loseLife() {
        lives--;
    }

    /**
     * Gets lives.
     *
     * @return the lives
     */
    public int getLives() {
        return lives;
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
     * Sets y.
     *
     * @param d the d
     */
    public void setY(double d) {
        y = d;
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
