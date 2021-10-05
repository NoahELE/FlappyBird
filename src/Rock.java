import bagel.Image;

public class Rock extends Weapon {
    public Rock(double y) {
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
        return new Image("res/level-1/rock.png");
    }
}
