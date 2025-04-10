package guiTest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.seng.gui.CheckersBoard;
import org.seng.gui.ProfilePageController;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;
public class profilepagecontrollerTest {


    @Test
    void howToPlay() {
    }

    @Test
    void testGoBackMethodRunsSafely() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ProfilePageController controller = new ProfilePageController();

        Platform.startup(() -> {});

        Platform.runLater(() -> {
            assertDoesNotThrow(controller::goBack);
            latch.countDown();
        });

        latch.await();
    }



}


