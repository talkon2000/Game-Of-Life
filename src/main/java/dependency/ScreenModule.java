package dependency;

import com.googlecode.lanterna.screen.VirtualScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import dagger.Module;
import dagger.Provides;
import exception.FailedToCreateScreenException;

import javax.inject.Singleton;
import java.io.IOException;

@Module
public class ScreenModule {

    @Singleton
    @Provides
    public VirtualScreen provideVirtualScreen() throws FailedToCreateScreenException {
        try {
            return new VirtualScreen(new DefaultTerminalFactory().createScreen());
        } catch (IOException e) {
            throw new FailedToCreateScreenException();
        }
    }
}
