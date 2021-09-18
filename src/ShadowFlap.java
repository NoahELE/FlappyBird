import bagel.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 * <p>
 * Please filling your name below
 *
 * @author: Xinhao Chen
 */
public class ShadowFlap extends AbstractGame {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public final int LEVEL0_MAX_SCORE = 10;
    public final int LEVEL1_MAX_SCORE = 30;
    private Image background;
    private final Image level0Background = new Image("res/level-0/background");
    private final Image level1Background = new Image("res/level-1/background");
    private final Font font = new Font("res/font/slkscr.ttf", 48);
    private final Queue<Pipe[]> pipes;
    private final Bird bird;
    private boolean started;
    private boolean birdPassPipe;
    private boolean win;
    private boolean lose;
    private int score;
    private int level;

    public ShadowFlap() {
        super(WIDTH, HEIGHT, "ShadowFlap");
        pipes = new LinkedList<>();
        level = 0;
        double pos = Pipe.getRandomPos(level);
        pipes.offer(new Pipe[] {
                new Pipe(false, pos),
                new Pipe(true, pos + Pipe.GAP)
        });
        bird = new Bird(200, 350, level);
        started = false;
        birdPassPipe = false;
        win = false;
        lose = false;
        score = 0;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (level == 0 && score >= LEVEL0_MAX_SCORE) {
            level = 1;
            score = 0;
        } else if (level == 1 && score >= LEVEL1_MAX_SCORE) {
            win = true;
        }
        // figure out the right background
        if (level == 0) {
            background = level0Background;
        } else if (level == 1) {
            background = level1Background;
        }
        background.drawFromTopLeft(0, 0);
        // game logic
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        if (!started) {
            String s = "PRESS SPACE TO START";
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
            if (input.wasPressed(Keys.SPACE)) {
                started = true;
            }
        } else if (win) {
            String s = "CONGRATULATIONS!";
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
            s = "FINAL SCORE: " + score;
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2 + 75);
        } else if (lose) {
            String s = "GAME OVER";
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
            s = "FINAL SCORE: " + score;
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2 + 75);
        } else if (level == 0) {
            // pipes
            pipes[0].move();
            pipes[1].move();
            if (pipes[0].isOutOfBound()) {
                double pos = Pipe.getRandomPos();
                pipes[0] = new Pipe(false, pos);
                pipes[1] = new Pipe(true, pos + Pipe.GAP);
                birdPassPipe = false;
            }
            pipes[0].draw();
            pipes[1].draw();
            // bird
            bird.fall();
            if (input.wasPressed(Keys.SPACE)) {
                bird.fly();
            }
            bird.move();
            bird.draw();
            if (bird.collideWith(pipes[0]) || bird.collideWith(pipes[1]) || bird.isOutOfBound()) {
                lose = true;
                win = false;
            }
            // score
            if (!birdPassPipe && pipes[0].getX() + pipes[0].getImage().getWidth() < bird.getX()) {
                win = true;
                birdPassPipe = true;
                score++;
            }
            font.drawString("SCORE: " + score, 100, 100);
        }

    }
}
