import bagel.Image;

public class Flame {
    public static final double SHOOT_SPEED = 5;
    private Image image = new Image("res/level-1/flame.png");
    private double x;
    private double y;

    public Flame(Pipe pipe) {
        this.x = pipe.getX();
        this.y = pipe.getY();
    }
}
