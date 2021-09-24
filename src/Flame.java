import bagel.Image;

public class Flame {
    public static final double SHOOT_SPEED = 5;
    private final Image image = new Image("res/level-1/flame.png");
    private double x;
    private double y;

    public Flame(Pipe pipe) {
        this.x = pipe.getX();
        if (pipe.upright) {
            this.y = pipe.getY();
        } else {
            this.y = pipe.getY() + pipe.getImage().getHeight() - image.getHeight();
        }
    }

    public void draw() {
        image.drawFromTopLeft(x, y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
