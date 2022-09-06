package game;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import screen.ScreenHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameHandler {

    ScreenHandler screenHandler;

    @Inject
    public GameHandler(ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
    }

    /**
     * Will play game infinitely until escape is pressed.
     * @param gameState 2-dimensional array representing the game board
     */
    public void doGame(Boolean[][] gameState) {
        screenHandler.setMinSize(gameState[0].length + 1, gameState.length);

        while (true) {
            gameState = doTick(gameState);

            try {
                screenHandler.refreshScreen(gameState);
                KeyStroke input = screenHandler.pollInput();
                if (input != null && input.getKeyType().equals(KeyType.Escape)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Rules:
     *     Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     *     Any live cell with two or three live neighbours lives on to the next generation.
     *     Any live cell with more than three live neighbours dies, as if by overpopulation.
     *     Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
    private Boolean[][] doTick(Boolean[][] initialGameState) {
        doTickDelay();

        Boolean[][] newGameState = new Boolean[initialGameState.length][initialGameState[0].length];
        for (int column = 0; column < initialGameState.length; column++) {
            for (int row = 0; row < initialGameState[0].length; row++) {
                int aliveNeighbors = checkNeighbors(initialGameState, column, row);
                Boolean cellState = initialGameState[column][row];

                // If cell is dead and has 3 live neighbors, it populates
                // If cell is dead and previous condition is not met, it should stay dead
                if (!cellState) {
                    newGameState[column][row] = (aliveNeighbors == 3);
                }
                // If cell is alive and has fewer than 2 or greater than 3 live neighbors, it dies
                else {
                    newGameState[column][row] = !(aliveNeighbors < 2 || aliveNeighbors > 3);
                }
            }
        }

        return newGameState;
    }

    private int checkNeighbors(Boolean[][] gameState, int column, int row) {
        int neighborCount = 0;
        // Check all neighbors 1 cell away
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x == 0 && y ==0) {
                    continue;
                }
                try {
                    if (gameState[column + x][row + y]) {
                        neighborCount++;
                    }
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }
        return neighborCount;
    }

    public void end() throws IOException {
        screenHandler.stopScreen();
    }

    private void doTickDelay() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
