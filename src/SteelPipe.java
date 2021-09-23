import bagel.Image;

public class SteelPipe extends Pipe {


    public SteelPipe(boolean upright, double height) {
        super(upright);
        image = new Image("res/level-1/steelPipe.png");
        y = upright ? height : (height - image.getHeight());
    }
}
