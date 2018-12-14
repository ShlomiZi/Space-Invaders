import biuoop.GUI;
import java.io.File;
import biuoop.KeyboardSensor;

/**
 * Created by ShlomiZ on 05/07/2017.
 */
public class Ass7Game {

    /**
     * Main function.
     * This function is responsible for running the game.
     *
     * @param args no command line arguments
     * @throws Exception if an Exception was found
     */
    public static void main(String[] args) throws Exception {

        //initialize game surroundings
        GUI gui = new GUI("Space Invaders", 800, 600);
        KeyboardSensor key = gui.getKeyboardSensor();
        AnimationRunner runner = new AnimationRunner(gui, 60);
        //creating high scores table
        HighScoresTable scores = new HighScoresTable(5);
        //create the high scores table animation
        HighScoresAnimation scoresAnimation = new HighScoresAnimation(scores);
        File f = new File("highscores");
        //save a new HighScores or load an existing one
        if (f.exists()) {
            scores.load(f);
        } else {
            scores.save(f);
        }

        //creating the game menu
        Menu<Task<Void>> menu = new MenuAnimation<Task<Void>>("Space Invaders", key, runner);
        menu.addSelection("s", "Start Game", new GameStartTask(runner, key, scores, gui, f));
        menu.addSelection("h", "High scores", new ShowHiScoresTask(runner, scoresAnimation, key));
        menu.addSelection("q", "Quit", new ExitTask());

        //running the game loop
        while (true) {
            runner.run(menu);
            // wait for user selection
            Task<Void> task = menu.getStatus();
            task.run();
            menu.reset();
        }
    }
}
