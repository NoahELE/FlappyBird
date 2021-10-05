import bagel.Image;

public class PlasticPipe extends Pipe {
    public PlasticPipe(boolean upright, double height) {
        super(upright);
        y = upright ? height : (height - image.getHeight());
    }

    @Override
    public void getHit(Weapon weapon) {
        destroyed = true;
    }

    @Override
    protected Image setImage() {
        return new Image("res/level/plasticPipe.png");
    }
}
