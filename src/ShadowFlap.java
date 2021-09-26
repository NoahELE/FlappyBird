import bagel.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 * <p>
 * Please filling your name below
 *
 * @author: Xinhao Chen
 */
public class ShadowFlap extends AbstractGame {
    enum State {
        NOT_STARTED, WIN, LOSE, LEVELUP, LEVEL
    }

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public final int LEVEL0_MAX_SCORE = 3;
    public final int LEVEL1_MAX_SCORE = 30;
    public final int LEVEL0_MAX_LIFE = 3;
    public final int LEVEL1_MAX_LIFE = 6;
    private final Image fullLife = new Image("res/level/fullLife.png");
    private final Image noLife = new Image("res/level/noLife.png");
    private Image background;
    private final Font font = new Font("res/font/slkscr.ttf", 48);
    private Queue<Pipe[]> pipes;
    private List<Weapon> weapons;
    private Bird bird;
    private State state;
    private int levelUpCounter;
    private int score;
    private int level;
    private int pipeSpawningCounter;
    private final int pipeSpawningInterval;
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
        state = State.NOT_STARTED;
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
            state = State.LEVELUP;
        } else if (level == 1 && score >= LEVEL1_MAX_SCORE) {
            state = State.WIN;
        }
        // draw background
        background.drawFromTopLeft(0, 0);
        // game logic
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        String s;
        switch (state) {
            case NOT_STARTED:
                s = "PRESS SPACE TO START";
                font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
                if (input.wasPressed(Keys.SPACE)) {
                    state = State.LEVEL;
                }
                break;
            case WIN:
                s = "CONGRATULATIONS!";
                font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
                s = "FINAL SCORE: " + score;
                font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2 + 75);
                break;
            case LOSE:
                s = "GAME OVER";
                font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
                s = "FINAL SCORE: " + score;
                font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2 + 75);
                break;
            case LEVELUP:
                if (levelUpCounter < 20) {
                    s = "LEVEL-UP!";
                    font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
                } else if (levelUpCounter == 20) {
                    level = 1;
                    background = new Image("res/level-1/background.png");
                    bird = new Bird(200, 350, level, LEVEL1_MAX_LIFE);
                    pipes = new LinkedList<>();
                    weapons = new LinkedList<>();
                    score = 0;
                } else {
                    s = "PRESS SPACE TO START";
                    font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
                    s = "PRESS 'S' TO SHOOT";
                    font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2 + 68);
                    if (input.wasPressed(Keys.SPACE)) {
                        state = State.LEVEL;
                    }
                }
                levelUpCounter++;
                break;
            case LEVEL:
                if (level == 0) {
                    // bird
                    birdBasicMove(input);
                    // timescale control
                    timescaleControl(input);
                    // pipes
                    // generate new pipe pair
                    pipeSpawningCounter++;
                    if (pipeSpawningCounter >= pipeSpawningInterval) {
                        double pos = Pipe.getRandomPos(level);
                        pipes.offer(new Pipe[] {
                                new PlasticPipe(false, pos),
                                new PlasticPipe(true, pos + PlasticPipe.GAP)
                        });
                        pipeSpawningCounter = 0;
                    }
                    // draw pipes and detect collision
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
                    // lose detection
                    if (bird.getLives() <= 0) {
                        state = State.LOSE;
                    }
                    // draw score
                    font.drawString("SCORE: " + score, 100, 100);
                    // draw life bar
                    drawLifeBar(level);
                } else {
                    // bird
                    birdBasicMove(input);
                    // timescale control
                    timescaleControl(input);
                    // pipes
                    // generate new pipe pair
                    pipeSpawningCounter++;
                    if (pipeSpawningCounter >= pipeSpawningInterval) {
                        double pos = Pipe.getRandomPos(level);
                        if (Math.random() < 0.5) {
                            pipes.offer(new Pipe[] {
                                    new PlasticPipe(false, pos),
                                    new PlasticPipe(true, pos + PlasticPipe.GAP)
                            });
                        } else {
                            pipes.offer(new Pipe[] {
                                    new SteelPipe(false, pos),
                                    new SteelPipe(true, pos + PlasticPipe.GAP)
                            });
                        }
                        pipeSpawningCounter = 0;
                    }
                    // draw pipes and detect collision
                    for (Pipe[] pipePair : pipes) {
                        pipePair[0].move();
                        pipePair[1].move();
                        pipePair[0].draw();
                        pipePair[1].draw();
                        // shoot fire if it is steel pipe
                        if (pipePair[0] instanceof SteelPipe) {

                        }
                        // collision
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
                    // lose detection
                    if (bird.getLives() <= 0) {
                        state = State.LOSE;
                    }
                    // draw score
                    font.drawString("SCORE: " + score, 100, 100);
                    // draw life bar
                    drawLifeBar(level);
                }
                break;
        }
    }

    private void drawLifeBar(int level) {
        double startX = 100;
        double maxLife = level == 0 ? LEVEL0_MAX_LIFE : LEVEL1_MAX_LIFE;
        double lifeWidth = fullLife.getWidth();
        for (int i = 0; i < bird.getLives(); i++) {
            fullLife.drawFromTopLeft(startX, 15);
            startX += 50 + lifeWidth;
        }
        for (int i = 0; i < maxLife - bird.getLives(); i++) {
            noLife.drawFromTopLeft(startX, 15);
            startX += 50 + lifeWidth;
        }
    }

    private void birdBasicMove(Input input) {
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
    }

    private void timescaleControl(Input input) {
        if (input.wasPressed(Keys.K)) {
            if (timescale > 1) {
                timescale--;
                Pipe.setStepSize(Pipe.getStepSize() / 1.5);
            }
        }
        if (input.wasPressed(Keys.L)) {
            if (timescale < 5) {
                timescale++;
                Pipe.setStepSize(Pipe.getStepSize() * 1.5);
            }
        }
    }
}
