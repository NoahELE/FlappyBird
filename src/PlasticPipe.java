import bagel.Image;

public class PlasticPipe extends Pipe {
    /**
     * create a plastic pipe
     *
     * @param upright the direction of the pipe
     * @param height the height of the pipe
     */
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
