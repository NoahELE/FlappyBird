import bagel.Image;

public class Rock extends Weapon {
    public Rock(double y) {
        super(y);
        this.image = new Image("res/level-1/rock.png");
    }

    @Override
    protected void checkRange() {
        existCounter++;
        if (existCounter > 25) {
            setUnused();
        }
    }
}
