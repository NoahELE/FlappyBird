import bagel.Image;

public class SteelPipe extends Pipe {
    private int flameCounter;
    private Flame flame;

    /**
     * create a steel pipe
     *
     * @param upright the direction of the pipe
     * @param height the height of the pipe
     */
    public SteelPipe(boolean upright, double height) {
        super(upright);
        y = upright ? height : (height - image.getHeight());
        flameCounter = 0;
        flame = null;
    }

    @Override
    public void draw() {
        super.draw();
        flameCounter++;
        if (flameCounter == 20 + Flame.lifeLength) {
            flame = new Flame(this);
            flameCounter = 0;
        }
        if (flame != null && flame.getExistCounter() == Flame.lifeLength) {
            flame = null;
        }
        if (flame != null) {
            flame.setX(flame.getX() - Pipe.stepSize);
            flame.draw();
        }
    }

    @Override
    public boolean collideWith(Bird bird) {
        if (getRect().intersects(bird.getRect())) {
            return true;
        }
        return flame != null && flame.getRect().intersects(bird.getRect());
    }

    @Override
    public void getHit(Weapon weapon) {
        if (weapon instanceof Bomb) {
            destroyed = true;
        }
    }

    @Override
    protected Image setImage() {
        return new Image("res/level-1/steelPipe.png");
    }
}
