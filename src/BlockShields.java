import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by ShlomiZ on 05/07/2017.
 */
public class BlockShields {

    //class members
    /**
    * Get a list containng the block shields.
    *
    * @return shields the block shields list
    */
    public static List<Block> createShields() {



        List<Block> shields = new ArrayList<Block>();
        int i;

        //adding and creating the left shield
        for (i = 0; i < 20; i++) {
            Block b1 = new Block(100 + (i * 7), 505, 7, 5);
            b1.setColor(Color.CYAN);
            Block b2 = new Block(100 + (i * 7), 510, 7, 5);
            b2.setColor(Color.CYAN);
            Block b3 = new Block(100 + (i * 7), 515, 7, 5);
            b3.setColor(Color.CYAN);
            shields.add(b1);
            shields.add(b2);
            shields.add(b3);
        }

        //adding and creating the middle shield
        for (i = 0; i < 20; i++) {
            Block b1 = new Block(350 + (i * 7), 505, 7, 5);
            b1.setColor(Color.CYAN);
            Block b2 = new Block(350 + (i * 7), 510, 7, 5);
            b2.setColor(Color.CYAN);
            Block b3 = new Block(350 + (i * 7), 515, 7, 5);
            b3.setColor(Color.CYAN);
            shields.add(b1);
            shields.add(b2);
            shields.add(b3);
        }

        //adding and creating the right shield
        for (i = 0; i < 20; i++) {
            Block b1 = new Block(600 + (i * 7), 505, 7, 5);
            b1.setColor(Color.CYAN);
            Block b2 = new Block(600 + (i * 7), 510, 7, 5);
            b2.setColor(Color.CYAN);
            Block b3 = new Block(600 + (i * 7), 515, 7, 5);
            b3.setColor(Color.CYAN);
            shields.add(b1);
            shields.add(b2);
            shields.add(b3);
        }

        return shields;
    }

    /**
    * Get the height line of the shields.
    *
    * @return 505 this shields height line
    */
    public static int getHeightLine() {
        return 505;
    }
}
