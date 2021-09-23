import bagel.Image;

public class PlasticPipe extends Pipe {
    public PlasticPipe(boolean upright, double height) {
        super(upright);
        image = new Image("res/level/plasticPipe.png");
        y = upright ? height : (height - image.getHeight());
    }
}
