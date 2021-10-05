import bagel.Image;

public class Bomb extends Weapon {
    private int existCounter = 0;

    public Bomb(double y) {
        super(y);
    }

    @Override
    protected void checkRange() {
        existCounter++;
        if (existCounter > 25) {
            setUnused();
        }
    }

    @Override
    protected Image setImage() {
        return new Image("res/level-1/bomb.png");
    }
}
