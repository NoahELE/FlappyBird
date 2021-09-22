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
    public final int LEVEL0_MAX_LIFE = 3;
    public final int LEVEL1_MAX_LIFE = 6;
    private final Image fullLife = new Image("res/level/fullLife.png");
    private final Image noLife = new Image("res/level/noLife.png");
    private Image background;
    private final Font font = new Font("res/font/slkscr.ttf", 48);
    private Queue<Pipe[]> pipes;
    private Bird bird;
    private boolean started;
    private boolean win;
    private boolean lose;
    private boolean levelUp;
    private int levelUpCounter;
    private int score;
    private int level;
    private int pipeSpawningCounter;
    private int pipeSpawningInterval;
    private int timescale;

    public ShadowFlap() {
        super(WIDTH, HEIGHT, "ShadowFlap");
        level = 0;
        background = new Image("res/level-0/background.png");
        pipes = new LinkedList<>();
        pipeSpawningInterval = 100;
        pipeSpawningCounter = pipeSpawningInterval;
        timescale = 1;
        bird = new Bird(200, 350, level, LEVEL0_MAX_LIFE);
        started = false;
        win = false;
        lose = false;
        levelUp = false;
        levelUpCounter = 0;
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
        // score detection
        if (level == 0 && score >= LEVEL0_MAX_SCORE) {
            levelUp = true;
        } else if (level == 1 && score >= LEVEL1_MAX_SCORE) {
            win = true;
        }
        // draw background
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
        } else if (levelUp) {
            String s = "LEVEL-UP!";
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
            if (levelUpCounter == 20) {
                level = 1;
                background = new Image("res/level-1/background.png");
                bird = new Bird(200, 350, level, LEVEL1_MAX_LIFE);
                pipes = new LinkedList<>();
                levelUp = false;
            }
            levelUpCounter++;
        } else if (level == 0) {
            // bird
            bird.fall();
            if (input.wasPressed(Keys.SPACE)) {
                bird.fly();
            }
            bird.move();
            bird.draw();
            if (bird.isOutOfBound()) {
                bird.loseLife();
                bird.setY(350);
            }
            // pipes
            if (input.wasPressed(Keys.K)) {
                if (timescale > 1) {
                    timescale--;
                    Pipe.setStepSize(Pipe.getStepSize() * 0.5);
                }
            }
            if (input.wasPressed(Keys.L)) {
                if (timescale < 5) {
                    timescale++;
                    Pipe.setStepSize(Pipe.getStepSize() * 1.5);
                }
            }
            pipeSpawningCounter++;
            if (pipeSpawningCounter >= pipeSpawningInterval) {
                double pos = Pipe.getRandomPos(level);
                pipes.offer(new Pipe[] {
                        new Pipe(false, pos, 0),
                        new Pipe(true, pos + Pipe.GAP, 0)
                });
                pipeSpawningCounter = 0;
            }
            for (Pipe[] pipePair : pipes) {
                pipePair[0].move();
                pipePair[1].move();
                pipePair[0].draw();
                pipePair[1].draw();
                if (!pipePair[0].getCollideWithBird() && bird.collideWith(pipePair[0])) {
                    pipePair[0].setCollideWithBird(true);
                    bird.loseLife();
                }
                if (!pipePair[1].getCollideWithBird() && bird.collideWith(pipePair[1])) {
                    pipePair[1].setCollideWithBird(true);
                    bird.loseLife();
                }
                // score
                if (!pipePair[0].getPassedByBird() &&
                        pipePair[0].getX() + pipePair[0].getImage().getWidth() < bird.getX()) {
                    score++;
                    pipePair[0].setPassedByBird(true);
                    pipePair[1].setPassedByBird(true);
                }
            }
            // test if the leftmost pipes are out of screen
            if (pipes.peek() != null && pipes.peek()[0].isOutOfBound()) {
                pipes.poll();
            }
            if (bird.getLives() <= 0) {
                win = false;
                lose = true;
            }
            // draw score
            font.drawString("SCORE: " + score, 100, 100);
            // draw life bar
            double startX = 100;
            double lifeWidth = fullLife.getWidth();
            for (int i = 0; i < bird.getLives(); i++) {
                fullLife.drawFromTopLeft(startX, 15);
                startX += 50 + lifeWidth;
            }
            for (int i = 0; i < LEVEL0_MAX_LIFE - bird.getLives(); i++) {
                noLife.drawFromTopLeft(startX, 15);
                startX += 50 + lifeWidth;
            }
        } else if (level == 1) {
            String s = "LEVEL-1!";
            font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
        }
    }
}
