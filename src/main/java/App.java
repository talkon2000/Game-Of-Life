import com.googlecode.lanterna.screen.VirtualScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
//import dependency.AppComponent;
import game.GameHandler;
import screen.ScreenHandler;

import java.io.IOException;
import java.util.Random;

public class App {

    private static GameHandler gameHandler;

    /**
     * @param args accepts two arguments for size of playing field
     */
    public static void main(String[] args) {
        // Require 2 arguments only
        if (args.length != 2) {
            System.out.println("Requires two Integer parameters for size of playing field. Example for 20x20 field: "
                + "run 20 20");
            return;
        }
        // Populate size and require integers
        Integer[] size = new Integer[2];
        for (int i = 0; i < 2; i++) {
            try {
                size[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                System.out.println("Both size parameters should be Integers");
                return;
            }
        }

        // Create GameHandler and dependencies
        /*AppComponent dagger = DaggerAppComponent.create();
        gameHandler = dagger.provideGameHandler();*/

        try {
            gameHandler = new GameHandler(new ScreenHandler(new VirtualScreen(new DefaultTerminalFactory().createScreen())));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Populate game field randomly
        Boolean[][] gameState = new Boolean[size[0]][size[1]];
        Random rd = new Random();
        for (int width = 0; width < size[0]; width++) {
            for (int height = 0; height < size[1]; height++) {
                gameState[width][height] = rd.nextBoolean();
            }
        }

        // Start the game
        gameHandler.doGame(gameState);

        // End the game
        end();

        System.out.println("Thank you for playing the Game of Life!");
    }

    private static void end() {
        try {
            gameHandler.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
