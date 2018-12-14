import biuoop.DrawSurface;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.Image;

/**
 * This class extends the Block class, for
 * the new SpaceInvaders game enemies.
 *
 * @author Shlomi Zidmi
 */
public class Alien extends Block {

    //class members
    private double xSpeed;
    private int yJump;
    private final int widthOfAlien = 40;
    private final int heightOfAlien = 30;
    private int startingX;
    private int startingY;
    private Image image;
    private int column;
    private GameEnvironment environment;
    private double initalSpeedOfAlien;
    private Rectangle rect;


    /**
     * Class constructor.
     *
     * @param x the X location of the alien
     * @param y the Y location of the alien
     * @param game the GameEnviroment
     * @param initialSpeed the initial speed of the alien
     */
    public Alien(int x, int y, GameEnvironment game, double initialSpeed) {
        super(x, y, 40, 30);
        this.startingX = x;
        this.startingY = y;
        this.loadBackgroundImage();
        this.xSpeed = initialSpeed;
        this.yJump = 0;
        this.environment = game;
        this.initalSpeedOfAlien = initialSpeed;
        this.rect = this.getBlockRectangle();
    }

    /**
     * Moving the alien.
     *
     * @param xChange the X change rate
     * @param yChange the Y change rate
     */
    public void move(double xChange, double yChange) {
        double newX = this.rect.getUpperLeft().getX() + xChange;
        double newY = this.rect.getUpperLeft().getY() + yChange;

        double width = this.rect.getWidth();
        double height = this.rect.getHeight();

        //creating a rectangle with the updated location
        this.rect = new Rectangle(newX, newY, width, height);
        this.setRectangle(rect);
        this.yJump = 0;
    }

    /**
     * Returning the alien width.
     *
     * @return this.WIDTH the alien's width
     */
    public int getWidth() {
        return this.widthOfAlien;
    }

    /**
     * Returning the alien height.
     *
     * @return this.HEIGHT the alien's height
     */
    public int getHeight() {
        return this.heightOfAlien;
    }

    /**
     * Returning the starting X value of the alien.
     *
     * @return this.startingX
     */
    public int getStartingX() {
        return this.startingX;
    }

    /**
     * Returning the starting Y value of the alien.
     *
     * @return this.startingY
     */
    public int getStartingY() {
        return this.startingY;
    }

    /**
     * This function reset the location of the alien,
     * to its original one.
     */
    public void resetLocation() {
        this.rect = new Rectangle(startingX, startingY, widthOfAlien, heightOfAlien);
        this.xSpeed = this.initalSpeedOfAlien;
    }

    /**
     * This function loads the background imagae of the alien.
     */
    public void loadBackgroundImage() {
        try {
            String path = "block_images/enemy.png";
            this.image = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            System.out.println("Error while trying to load enemy's image");
        }
    }

    /**
     * This function draws the alien.
     *
     * @param d the instance to be drawn on
     */
    @Override
    public void drawOn(DrawSurface d) {
        int x = this.getCollisionRectangle().getLeftX();
        int y = this.getCollisionRectangle().getUpperY();
        d.drawImage(x, y, this.image);
    }

    /**
     * This function tells an alien time passed, and he
     * needs to be moved.
     *
     * @param dt the fps change rate
     */
    @Override
    public void timePassed(double dt) {
        this.move(xSpeed, yJump);
    }

    /**
     * This function changes the movement direction of an alien.
     */
    public void changeDirection() {
        this.xSpeed = xSpeed * (-1) * 1.1;
        this.yJump = 30;
    }

    /**
     * This function sets the column for an alien.
     *
     * @param col the column to be set
     */
    public void setColumn(int col) {
        this.column = col;
    }

    /**
     * This function gets the original column of the alien.
     *
     * @return this.column the original column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * This function handles with the alien's shooting.
     *
     * @return b the shot fired (Ball)
     */
    public Ball shoot() {
        int startX = this.getCollisionRectangle().getLeftX();
        int endX = this.getCollisionRectangle().getRightX();
        int x = (startX + endX) / 2;
        int y = this.rect.getLowerY() + 15;
        Ball b = new Ball(x, y, 4, this.environment);
        b.setVelocity(0, 500);
        b.setColor(Color.RED);
        return b;
    }
}
