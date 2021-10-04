import bagel.Image;

public class Bomb extends Weapon {
    private int existCounter = 0;
    public Bomb(double y) {
        super(y);
        this.image = new Image("res/level-1/bomb.png");
    }

    @Override
    protected void checkRange() {
        existCounter++;
        if (existCounter > 25) {
            setUnused();
        }
    }
}
