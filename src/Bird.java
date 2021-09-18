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

    public Bird(double x, double y, int level) {
        this.x = x;
        this.y = y;
        if (level == 0) {
            birdWingDown = new Image("res/level-0/birdWingDown");
            birdWingUp = new Image("res/level-0/birdWingUp");
            lives = 3;
        } else {
            birdWingDown = new Image("res/level-1/birdWingDown");
            birdWingUp = new Image("res/level-1/birdWingUp");
            lives = 6;
        }
        image = birdWingDown;
        wingCounter = 0;
        speed = 0;
    }

    public double getX() {
        return x;
    }

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

    public void fall() {
        speed += 0.4;
        if (speed >= 10) {
            speed = 10;
        }
    }

    public void fly() {
        speed = -6;
    }

    public void move() {
        y += speed;
    }

    public boolean isOutOfBound() {
        return y < -image.getHeight() || y > ShadowFlap.HEIGHT;
    }

    public boolean collideWith(Pipe pipe) {
        return getRect().intersects(pipe.getRect());
    }

    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }
}