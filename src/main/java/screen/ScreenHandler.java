package screen;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.VirtualScreen;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class ScreenHandler {

    VirtualScreen screen;

    @Inject
    public ScreenHandler(VirtualScreen screen) {
        this.screen = screen;
        try {
            initializeScreen();
        } catch (IOException e ) {
            e.printStackTrace();
            System.out.println("Could not start screen");
        }
    }

    public void initializeScreen() throws IOException {
        screen.startScreen();
    }

    public void refreshScreen(Boolean[][] gameState) throws IOException {
        // First needs to write to screen
            writeToScreen(gameState);

        // Updates the screen contents
        screen.refresh();
    }

    private void writeToScreen(Boolean[][] gameState) {
        for (int row = 0; row < gameState.length; row++) {
            for (int column = 0; row < gameState[0].length; row++) {
                screen.setCharacter(row, column, TextCharacter.fromCharacter('A')[0]);
            }
        }
    }

    public KeyStroke pollInput() throws IOException {
        return screen.pollInput();
    }

    public void stopScreen() throws IOException {
        screen.stopScreen();
    }

    public void setMinSize(int rows, int columns) {
        screen.setMinimumSize(new TerminalSize(rows, columns));
    }
}
