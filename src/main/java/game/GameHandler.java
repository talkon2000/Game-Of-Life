package game;

import com.googlecode.lanterna.input.KeyType;
import screen.ScreenHandler;

import java.io.IOException;

public class GameHandler {

    ScreenHandler screenHandler;

    public GameHandler(ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
    }

    /**
     * Will play game infinitely until escape is pressed.
     * @param gameState 2-dimensional array representing the game board
     */
    public void doGame(Boolean[][] gameState) {
        screenHandler.setMinSize(gameState.length, gameState[0].length);

        while (true) {
            try {
                screenHandler.refreshScreen(gameState);
                if (screenHandler.pollInput().getKeyType().equals(KeyType.Escape)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            gameState = doTick(gameState);
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

        Boolean[][] newGameState = initialGameState.clone();
        for (int row = 0; row < initialGameState.length; row++) {
            for (int column = 0; column < initialGameState[0].length; column++) {
                int aliveNeighbors = checkNeighbors(initialGameState, row, column);

                Boolean cellState = initialGameState[row][column];
                // If cell is dead and has 3 live neighbors, it populates
                if (!cellState && aliveNeighbors == 3) {
                    newGameState[row][column] = true;
                }
                // If cell is alive and has fewer than 2 or greater than 3 live neighbors, it dies
                else if (cellState && (aliveNeighbors < 2 || aliveNeighbors > 3)) {
                    newGameState[row][column] = false;
                }
            }
        }

        return newGameState;
    }

    private int checkNeighbors(Boolean[][] gameState, int row, int column) {
        int neighborCount = 0;
        // Check all neighbors 1 cell away
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                try {
                    if (gameState[row + x][column + y]) {
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
