import biuoop.GUI;
import biuoop.KeyboardSensor;
import java.io.File;

/**
* This class handles with stating the game.
*/
public class GameStartTask implements Task<Void> {
    //class members
    private AnimationRunner runner;
    private KeyboardSensor keyboard;
    private HighScoresTable table;
    private GUI gui;
    private File file;

    /**
    * GameStartTask constructor.
    *
    * @param run the animation runner
    * @param key the keyboared sensor
    * @param hst the high scores file
    * @param g the GUI
    * @param f the path for HighScores file
    */
    public GameStartTask(AnimationRunner run, KeyboardSensor key, HighScoresTable hst, GUI g, File f) {
        this.runner = run;
        this.keyboard = key;
        this.table = hst;
        this.gui = g;
        this.file = f;
    }

    /**
    * This function handles with the running and
    * starting the game.
    *
    * @return null for the <Void>
    */
    public Void run() {
        GameFlow flow = new GameFlow(this.runner, this.keyboard, this.table, this.gui);
        flow.runLevels(null);
        //save the HighScores table at the end
        try {
            this.table.save(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
