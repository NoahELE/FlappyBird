import bagel.Image;

public class Bomb extends Weapon{
    public Bomb(double y) {
        super(y);
        this.image = new Image("res/level-1/bomb.png");
    }
}
