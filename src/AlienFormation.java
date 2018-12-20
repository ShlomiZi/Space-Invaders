import java.util.ArrayList;
import java.util.List;
import java.awt.Image;

/**
 * This class represents a formation of aliens.
 */
public class AlienFormation {
    //class members
    private Alien[][] aliens;
    private Image image;
    private double speed;
    private int aliensCounter;
    private int leftXBorder;
    private int rightXBorder;
    private GameEnvironment environment;
    private List<Alien> aliensList;


    /**
     * AlienFormation constructor.
     * This function build a matrix of aliens, aligned to
     * specific locations.
     *
     * @param game the game collidables
     * @param speed the inital speed of the formation
     */
    public AlienFormation(GameEnvironment game, double speed) {
        this.aliens = new Alien[5][10];
        this.aliensCounter = 0;
        this.speed = 1;
        int xMargin = 10;
        int yMargin = 10;
        int width = 40;
        int height = 30;
        this.environment = game;
        aliensList = new ArrayList<Alien>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                GameEnvironment env = this.environment;
                Alien a = new Alien((width * j) + (xMargin * j), (height * (i + 1)) + (yMargin * i), env, speed);
                a.setColumn(j);
                this.aliens[i][j] = a;
                aliensList.add(a);
                this.aliensCounter++;
            }
        }
    }

    /**
     * This function get a list of aliens and resets their locations.
     *
     * @param list a list of aliens
     */
    public static void resetFormationFromList(List<Collidable> list) {

        for (Collidable c : list) {
            if (c != null) {
                if (c.getClass().toString().equals("class Alien")) {
                    Alien a = (Alien) c;
                    a.resetLocation();
                }
            }
        }
    }

    /**
     * This function returns a matrix of aliens.
     *
     * @return this.aliens the matrix of aliens
     */
    public Alien[][] getFormation() {
        return this.aliens;
    }

    /**
     * This function calculates the right most x value
     * of an alien from a list of aliens.
     *
     * @param list a list of aliens
     * @return maxX the right most x value
     */
    public static int getRightMostXFromList(List<Alien> list) {
        int temp = 0;
        int maxX = 0;

        for (Alien a : list) {
            if (a != null) {
                temp = a.getCollisionRectangle().getRightX();
                if (temp > maxX) {
                    maxX = temp;
                }
            }
        }
        return maxX;
    }

    /**
     * This function calculates the left most X value,
     * from a list of aliens.
     *
     * @param list a list of aliens
     * @return minX the left Most X
     */
    public static int getLeftMostXFromList(List<Alien> list) {
        int temp = 0;
        int minX = Integer.MAX_VALUE;

        for (Alien a : list) {
            if (a != null) {
                temp = a.getCollisionRectangle().getLeftX();
                if (temp < minX) {
                    minX = temp;
                }
            }
        }
        return minX;
    }

    /**
     * This function calculates the Y location of the lowest
     * alien from a List of aliens.
     *
     * @param list a list of aliens
     * @return maxY the max Y value
     */
    public static int getLowestYFromList(List<Collidable> list) {
        int maxY = 0;
        int temp = 0;

        for (Collidable c : list) {
            if (c != null) {
                if (c.getClass().toString().equals("class Alien")) {
                    temp = c.getCollisionRectangle().getLowerY();
                    if (maxY < temp) {
                        maxY = temp;
                    }
                }
            }
        }
        return maxY;
    }
}
