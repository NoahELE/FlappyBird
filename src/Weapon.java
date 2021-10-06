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
    protected int existCounter = 0;
    protected final Image image = setImage();
    protected State state;

    /**
     * create a weapon
     * @param y the y coordinate that weapon is spawned
     */
    public Weapon(double y) {
        x = ShadowFlap.WIDTH;
        this.y = y;
        state = State.UNCAUGHT;
    }

    /**
     * check if the weapon is out of window
     * @return true if the weapon is out of window and vice versa
     */
    public boolean isOutOfBorder() {
        return x < -image.getWidth();
    }

    /**
     * make the weapon move and check its relationship with bird and input
     * @param bird the bird that might catch the weapon
     * @param input check whether to shoot the weapon
     */
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
                checkRange();
                break;
        }
    }

    /**
     * draw the weapon
     */
    public void draw() {
        image.drawFromTopLeft(x, y);
    }

    /**
     * check if the bird catches the weapon
     * @param bird the bird of the game
     * @return true if bird hits the weapon
     */
    public boolean collideWithBird(Bird bird) {
        return bird.getRect().intersects(getRect());
    }

    /**
     * check if the state of the weapon is unused
     * @return true of the state is unused
     */
    public boolean isUnused() {
        return state == State.UNUSED;
    }

    /**
     * set the state to be unused
     */
    public void setUnused() {
        state = State.UNUSED;
    }

    public Rectangle getRect() {
        return image.getBoundingBoxAt(new Point(x + image.getWidth() / 2, y + image.getHeight() / 2));
    }

    /**
     * make sure the weapon is set unused after out of its range
     */
    protected abstract void checkRange();

    protected abstract Image setImage();

    enum State {
        UNCAUGHT, WITH_BIRD, SHOT, UNUSED
    }
}
