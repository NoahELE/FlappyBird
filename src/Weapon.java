import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Weapon {
    protected static final double SHOOT_SPEED = 5;
    protected static double stepSize = 5;
    protected double y;
    protected double x;
    protected Image image;
    protected State state;

    public Weapon(double y) {
        x = ShadowFlap.WIDTH;
        this.y = y;
        state = State.UNCAUGHT;
    }

    public boolean isOutOfBorder() {
        return x < -image.getWidth();
    }

    public void move(Bird bird, Input input) {
        if (state == State.UNCAUGHT && collideWithBird(bird)) {
            if (bird.getWeapon() != null) {
                bird.getWeapon().state = State.UNUSED;
            }
            state = State.WITH_BIRD;
            bird.setWeapon(this);
        }
        if (this == bird.getWeapon() && input.wasPressed(Keys.S)) {
            bird.setWeapon(null);
            state = State.SHOT;
        }
        switch (state) {
            case UNCAUGHT:
                x -= stepSize;
                break;
            case WITH_BIRD:
                x = bird.getX() + image.getWidth();
                y = bird.getY();
                break;
            case SHOT:
                x += SHOOT_SPEED;
                break;
        }
    }

    public void draw() {
        image.drawFromTopLeft(x, y);
    }

    public boolean collideWithBird(Bird bird) {
        return bird.getRect().intersects(getRect());
    }

    public boolean isUnused() {
        return state == State.UNUSED;
    }

    public void setUnused() {
        state = State.UNUSED;
    }

    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }

    enum State {
        UNCAUGHT, WITH_BIRD, SHOT, UNUSED
    }
}
