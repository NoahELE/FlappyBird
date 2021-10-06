import bagel.*;

import java.util.LinkedList;

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
    private final Font font = new Font("res/font/slkscr.ttf", 48);
    private final int pipeSpawningInterval = 100;
    private final int weaponSpawningInterval = 200;
    private Image background;
    private LinkedList<Pipe[]> pipes;
    private LinkedList<Weapon> weapons;
    private Bird bird;
    private State state;
    private int levelUpCounter = 0;
    private int score = 0;
    private int level = 0;
    private int pipeSpawningCounter;
    private int timescale = 1;
    private int weaponSpawningCounter;

    /**
     * initialise the game
     */
    public ShadowFlap() {
        super(WIDTH, HEIGHT, "ShadowFlap");
        background = new Image("res/level-0/background.png");
        pipes = new LinkedList<>();
        pipeSpawningCounter = pipeSpawningInterval;
        bird = new Bird(200, 350, level, LEVEL0_MAX_LIFE);
        state = State.NOT_STARTED;
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
                if (levelUpCounter < 150) {
                    s = "LEVEL-UP!";
                    font.drawString(s, (WIDTH - font.getWidth(s)) / 2, (double) HEIGHT / 2);
                } else if (levelUpCounter == 150) {
                    level = 1;
                    background = new Image("res/level-1/background.png");
                    bird = new Bird(200, 350, level, LEVEL1_MAX_LIFE);
                    pipes = new LinkedList<>();
                    weapons = new LinkedList<>();
                    score = 0;
                    weaponSpawningCounter = weaponSpawningInterval / 4;
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
                // bird
                birdBasicMove(input);
                // timescale control
                timescaleControl(input);
                pipeSpawningCounter++;
                if (level == 0) {
                    // generate new pipe pair
                    if (pipeSpawningCounter >= pipeSpawningInterval) {
                        double pos = Pipe.getRandomPos(level);
                        pipes.offer(new Pipe[] {
                                new PlasticPipe(false, pos),
                                new PlasticPipe(true, pos + Pipe.GAP)
                        });
                        pipeSpawningCounter = 0;
                    }
                } else {
                    // generate weapons
                    weaponSpawningCounter++;
                    if (weaponSpawningCounter >= weaponSpawningInterval) {
                        double pos = Pipe.getRandomPos(level);
                        if (Math.random() < 0.5) {
                            weapons.offer(new Rock(pos));
                        } else {
                            weapons.offer(new Bomb(pos));
                        }
                        weaponSpawningCounter = 0;
                    }
                    // move the weapons
                    for (int i = weapons.size() - 1; i >= 0; i--) {
                        Weapon weapon = weapons.get(i);
                        weapon.move(bird, input);
                        weapon.draw();
                        for (Pipe[] pipePair : pipes) {
                            if (weapon.state == Weapon.State.SHOT &&
                                    (pipePair[0].collideWithWeapon(weapon) || pipePair[1].collideWithWeapon(weapon))) {
                                pipePair[0].getHit(weapon);
                                pipePair[1].getHit(weapon);
                                weapon.setUnused();
                                // check whether add score
                                if (weapon instanceof Bomb || pipePair[0] instanceof PlasticPipe) {
                                    score++;
                                }
                            }
                        }
                        // delete if out of border or unused
                        if (weapon.isOutOfBorder() || weapon.isUnused()) {
                            weapons.remove(i);
                        }
                    }
                    // generate new pipe pair
                    if (pipeSpawningCounter >= pipeSpawningInterval) {
                        double pos = Pipe.getRandomPos(level);
                        if (Math.random() < 0.5) {
                            pipes.offer(new Pipe[] {
                                    new PlasticPipe(false, pos),
                                    new PlasticPipe(true, pos + Pipe.GAP)
                            });
                        } else {
                            pipes.offer(new Pipe[] {
                                    new SteelPipe(false, pos),
                                    new SteelPipe(true, pos + Pipe.GAP)
                            });
                        }
                        pipeSpawningCounter = 0;
                    }
                }
                // draw pipes and detect collision
                pipeBasicMove();
                // lose detection
                if (bird.getLives() <= 0) {
                    state = State.LOSE;
                }
                // draw score
                font.drawString("SCORE: " + score, 100, 100);
                // draw life bar
                drawLifeBar(level);
                break;
        }
    }

    private void pipeBasicMove() {
        for (int i = pipes.size() - 1; i >= 0; i--) {
            Pipe[] pipePair = pipes.get(i);
            pipePair[0].move();
            pipePair[1].move();
            pipePair[0].draw();
            pipePair[1].draw();
            if (!pipePair[0].getCollideWithBird() && pipePair[0].collideWith(bird)) {
                pipePair[0].setCollideWithBird(true);
                pipePair[1].setCollideWithBird(true);
                bird.loseLife();
            }
            if (!pipePair[1].getCollideWithBird() && pipePair[1].collideWith(bird)) {
                pipePair[0].setCollideWithBird(true);
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
            // test if the pipes are out of screen
            if (pipePair[0].isOutOfBound() || pipePair[0].isDestroyed()) {
                pipes.remove(i);
            }
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
                Pipe.stepSize /= 1.5;
                Weapon.stepSize /= 1.5;
            }
        }
        if (input.wasPressed(Keys.L)) {
            if (timescale < 5) {
                timescale++;
                Pipe.stepSize *= 1.5;
                Weapon.stepSize *= 1.5;
            }
        }
    }

    enum State {
        NOT_STARTED, WIN, LOSE, LEVELUP, LEVEL
    }
}
