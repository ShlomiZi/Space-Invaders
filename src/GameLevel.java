import biuoop.DrawSurface;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import biuoop.KeyboardSensor;

/**
 * This class is responsible for the whole game,
 * from creating objects and to running the game
 * animation loop.
 *
 * @author Shlomi Zidmi
 */
public class GameLevel implements Animation {

    //class members
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private int screenWidth;
    private int screenHeight;
    private Counter blockCounter;
    private Counter ballCounter;
    private Counter score;
    private Counter numberOfLives;
    private biuoop.KeyboardSensor keyboard;
    private AnimationRunner runner;
    private boolean running;
    private LevelInformation level;
    private Paddle paddle;
    private PauseScreen pause;
    private AlienFormation formation;
    private double speedOfLevel;
    private int levelNumber;
    private List<Alien> aliensList;
    private boolean shorFired;
    private long milliSeconds;
    private long alienShootingTiming;
    private boolean alienShotFired;
    private BlockRemover remover;
    private BallRemover ballRem;
    private Counter alienCounter;
    private BlockRemover alienRemover;

    /**
     * Game constructor.
     * This method creates an instance of class Game
     * and initializes some of his attributes.
     *
     * @param key       the keyboard
     * @param run       the animation runner instance
     * @param gameScore the ongoing game score
     * @param lives     the lives in the whole game
     * @param speed the inital speed of aliens
     * @param lvl the battle number
     */
    public GameLevel(KeyboardSensor key, AnimationRunner run, Counter gameScore, Counter lives, double speed, int lvl) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.screenWidth = 800;
        this.screenHeight = 600;
        this.blockCounter = new Counter();
        this.ballCounter = new Counter();
        this.score = gameScore;
        this.numberOfLives = lives;
        this.keyboard = key;
        this.running = false;
        this.runner = run;
        this.paddle = null;
        this.pause = new PauseScreen(key);
        this.formation = new AlienFormation(this.environment, speed);
        this.levelNumber = lvl;
        this.aliensList = new ArrayList<Alien>();
        this.shorFired = false;
        this.alienShotFired = false;
        this.alienCounter = new Counter();
        this.speedOfLevel = speed;
    }

    /**
     * Returns whether or not game should be stopped.
     *
     * @return !this.running false if game should stop, true otherwise
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * This methods deals with a single frame animation,
     * and draw it.
     *
     * @param d  the surface to be drawn on
     * @param dt the fps change rate
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.sprites.drawAllOn(d);

        int rightestX = AlienFormation.getRightMostXFromList(this.aliensList);
        int leftestX = AlienFormation.getLeftMostXFromList(this.aliensList);

        if (rightestX >= this.screenWidth || leftestX < 0) {
            for (Collidable c : this.environment.getCollidableList()) {
                if (c.getClass().toString().equals("class Alien")) {
                    Alien a = (Alien) c;
                    a.changeDirection();
                }
            }
        }

        for (Sprite s : this.sprites.getSpritesCollection()) {
            if (s.getClass().toString().equals("class Ball")) {
                Ball b = (Ball) s;
                int x = b.getX();
                int y = b.getY();
                if (x < 0 || x > this.screenWidth || y <= 0 || y > this.screenHeight) {
                    this.sprites.removeSpriteFromCollection(s);
                }

            }
        }

        //this.detectPaddleHit();
        this.sprites.notifyAllTimePassed(dt);
        this.status();


        if (!this.alienShotFired) {
            if (this.alienCounter.getValue() > 0) {
                Alien a = this.randomShooter();
                this.sprites.addSprite(a.shoot());
                this.alienShotFired = true;
                this.alienShootingTiming = System.currentTimeMillis();
            }
        }
        if (this.alienShotFired) {
            if (System.currentTimeMillis() - this.alienShootingTiming >= (0.5 * 1000)) {
                this.alienShotFired = false;
            }
        }


        if (this.keyboard.isPressed("space")) {
            if (!this.shorFired) {
                this.sprites.addSprite(paddle.shoot());
                this.shorFired = true;
                this.milliSeconds = System.currentTimeMillis();
            }
            if (this.shorFired) {
                if (System.currentTimeMillis() - this.milliSeconds >= (0.35 * 1000)) {
                    this.shorFired = false;
                }
            }
        }

        //check for paused game
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, "space", this.pause));
        }
    }

    /**
     * This method adds a Collidable to the
     * Collidables list.
     *
     * @param c the Collidable to be added
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * This method adds a Sprite to the
     * Sprites list.
     *
     * @param s the Sprite to be added
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * This method the initializes a new game,
     * by adding blocks, balls, edges, paddle
     * and any other game related object.
     * Obects are being created and initialized in this method.
     */
    public void initialize() {

        //creating listeners
        //BallRemover ballRem = new BallRemover(this, this.ballCounter);
        //BlockRemover remover = new BlockRemover(this, this.blockCounter);
        ballRem = new BallRemover(this, this.ballCounter);
        remover = new BlockRemover(this, this.blockCounter);
        alienRemover = new BlockRemover(this, alienCounter);
        ScoreTrackingListener scoreListener = new ScoreTrackingListener(this.score);
        ScoreIndicator scr = new ScoreIndicator(this.score);
        LivesIndicator lives = new LivesIndicator(this.numberOfLives);
        LevelIndicator levelName = new LevelIndicator("Battle No. " + this.levelNumber);

        //adding listeners to the level
        this.environment.setBoundariesBlocks(this.screenWidth, this.screenHeight, ballRem);
        this.sprites.addSprite(new BackgroundCreator("color:black "));
        //this.sprites.addSprite(this.level.getBackground());
        this.addToGame(scr);
        this.addToGame(lives);
        this.addToGame(levelName);
        //adding blocks to level
        this.addBlocks(scoreListener);
    }

    /**
     * This method is responsible for running
     * the game for one turn(until no balls or blocks are left).
     */
    public void playOneTurn() {
        AlienFormation.resetFormationFromList(this.environment.getCollidableList());

        //change game mode to running
        this.running = true;
        //create balls and paddle
        this.createPaddle();
        this.paddle.resetLocation();
        //countdown before turn starts.
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        this.runner.run(this);
    }

    /**
     * This method adds a Collidable instance to the game.
     *
     * @param c the Collidable to be added
     */
    public void addToGame(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * This method adds a Sprite instance to the game.
     *
     * @param s the Sprite to be added
     */
    public void addToGame(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Removes a collidable from the game.
     * Using the remove function of GameEnvironment class.
     *
     * @param c the collidable to be removed
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidableFromEnvironment(c);
    }

    /**
     * Removes a sprite from the game.
     * Using the remove function of class SpriteCollection
     *
     * @param s the sprite to be removed
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSpriteFromCollection(s);
    }

    /**
     * This methdos add paddle and balls to a current
     * starting level.
     * Paddle is added only once in a turn.
     * Balls are added in accordance to LevelInformation
     */
    private void createPaddle() {
        GameLevel thisGameLevel = this;
        /**
         * Paddle hit listener.
         */
        class PaddleHitListener implements HitListener {

            /**
             * This method is called whenever the beingHit object is hit.
             * The hitter parameter is the shapes.Ball that's doing the hitting.
             *
             * @param beingHit shapes.Block that been hit.
             * @param hitter   The ball that been hit the block.
             */
            @Override
            public void hitEvent(Block beingHit, Ball hitter) {
                //Remove paddle to create new one the next time.
                //removeSprite(paddle);
                //removeCollidable(paddle);
                //Set running to false.
                running = false;
                //Set end reason.
                //Decrease live.
                numberOfLives.decrease(1);

                for (Sprite s : sprites.getSpritesCollection()) {
                    if (s.getClass().toString().equals("class Ball")) {
                        sprites.removeSpriteFromCollection(s);
                    }
                }
            }
        }

        //creation of paddle, if not exists
        if (!this.environment.alreadyHasPaddle()) {
            Paddle p = new Paddle(keyboard, this.environment, 60, 650);
            this.paddle = p;
            this.environment.addCollidable(paddle);
            this.sprites.addSprite(paddle);
            this.paddle.addHitListener(new PaddleHitListener());
        }
    }

    /**
     * This method checks what is the status of the game,
     * and determines if it should be stopped or not.
     */
    private void status() {
        //check if no blocks remained, if so level is over
        if (this.alienCounter.getValue() == 0) {
            //this.score.increase(100);
            this.running = false;
        }
        int height = BlockShields.getHeightLine();
        if (AlienFormation.getLowestYFromList(this.environment.getCollidableList()) >= height) {
            this.numberOfLives.decrease(1);
            if (this.numberOfLives.getValue() <= 0) {
                this.running = false;
            } else {
                this.playOneTurn();
            }
        }
    }

    /**
     * This methods deals with adding from the
     * given LevelInformatin, to the current level;
     * Also added are a block remover listener,
     * and a score track listener.
     *
     * @param scr     the score listener to attcah to all blocks
     */
    private void addBlocks(ScoreTrackingListener scr) {
        //get the blocks information for each level
        Alien[][] aliens = this.formation.getFormation();
        List<Block> blocks = BlockShields.createShields();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                aliens[i][j].addHitListener(alienRemover);
                aliens[i][j].addHitListener(scr);
                aliens[i][j].addHitListener(ballRem);
                this.environment.addCollidable(aliens[i][j]);
                this.sprites.addSprite(aliens[i][j]);
                this.alienCounter.increase(1);
                this.aliensList.add(aliens[i][j]);
            }
        }

        //add the shields
        for (Block b : blocks) {
            b.addHitListener(remover);
            b.addHitListener(ballRem);
            this.environment.addCollidable(b);
            this.sprites.addSprite(b);
        }
    }

    /**
     * Returns the amounr of remaining blocks
     * in this level.
     *
     * @return remaining the amount of remaining blocks
     */
    public int remainingBlocks() {
        int remaining = this.alienCounter.getValue();
        return remaining;
    }

    /**
     * This function picks randomly from all columns of aliens.
     *
     * @return col the picked column
     */
    public int pickRandomColumn() {
        Random ran = new Random();
        int col = ran.nextInt(10);
        return col;
    }

    /**
     * This function chooses a random alien for shooting.
     *
     * @return chosen the chosen alien for shooting
     */
    public Alien randomShooter() {
        int column = this.pickRandomColumn();
        Alien chosen = null;
        int lowestY = 0;
        //int temp;
        while (chosen == null) {
            for (Collidable c : this.environment.getCollidableList()) {
                if (c.getClass().toString().equals("class Alien")) {
                    Alien alien = (Alien) c;
                    if (alien.getColumn() == column) {
                        if (alien.getCollisionRectangle().getLowerY() > lowestY) {
                            lowestY = alien.getCollisionRectangle().getLowerY();
                            chosen = alien;
                        }
                    }
                }
            }
            column = this.pickRandomColumn();
        }
        return chosen;
    }
}