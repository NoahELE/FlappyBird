import bagel.Image;

public class Flame {
    private final Image image = new Image("res/level-1/flame.png");
    private double x;
    private double y;
    private int existCounter;
    private static final int lifeLength = 3;

    public Flame(Pipe pipe) {
        this.x = pipe.getX();
        if (pipe.upright) {
            this.y = pipe.getY() - image.getHeight();
        } else {
            this.y = pipe.getY() + pipe.getImage().getHeight();
        }
        existCounter = 0;
    }

    public void draw() {
        image.drawFromTopLeft(x, y);
        existCounter++;
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

    public int getExistCounter() {
        return existCounter;
    }
}
