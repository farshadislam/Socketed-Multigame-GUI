package guiTest;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.seng.gui.SearchProfileController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchProfileControllerTest {

    private SearchProfileController controller;
    private TextField searchField;
    private ListView<String> resultsList;
    private Button viewProfileButton;
    private ImageView backIcon;

    private void runAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for FX Application Thread.");
        }
    }

    @BeforeAll
    public static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout initializing JavaFX.");
        }
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        controller = new SearchProfileController();
        searchField = new TextField();
        resultsList = new ListView<>();
        viewProfileButton = new Button();
        backIcon = new ImageView();
        setField("searchField", searchField);
        setField("resultsList", resultsList);
        setField("viewProfileButton", viewProfileButton);
        setField("backIcon", backIcon);
        runAndWait(controller::initialize);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        runAndWait(() -> {
            for (Window w : Window.getWindows()) {
                if (w instanceof Stage) {
                    ((Stage) w).close();
                }
            }
        });
    }

    private void setField(String fieldName, Object value) {
        try {
            Field field = SearchProfileController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (Exception e) {
            fail("Could not set field " + fieldName + ": " + e.getMessage());
        }
    }

    private Object callPrivateMethod(String methodName, Class<?>[] paramTypes, Object... params) {
        try {
            Method method = SearchProfileController.class.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(controller, params);
        } catch (Exception e) {
            fail("Could not invoke private method " + methodName + ": " + e.getMessage());
        }
        return null;
    }

    @Test
    @Order(1)
    public void testSearchFieldListener_emptyAndWhitespace() throws InterruptedException {
        runAndWait(() -> {
            searchField.setText("");
            ObservableList<String> items = resultsList.getItems();
            assertTrue(items.isEmpty());
            searchField.setText("   ");
            items = resultsList.getItems();
            assertTrue(items.isEmpty());
        });
    }

    @Test
    @Order(2)
    public void testSearchFieldListener_matches() throws InterruptedException {
        runAndWait(() -> {
            searchField.setText("joy");
            ObservableList<String> items = resultsList.getItems();
            assertEquals(1, items.size());
            assertEquals("JoyThief22", items.get(0));
            searchField.setText("enjoyer");
            items = resultsList.getItems();
            assertEquals(1, items.size());
            assertEquals("FishEnjoyer2", items.get(0));
            searchField.setText("minor");
            items = resultsList.getItems();
            assertEquals(1, items.size());
            assertEquals("notAMinor66", items.get(0));
        });
    }

    @Test
    @Order(3)
    public void testSearchFieldListener_noMatches() throws InterruptedException {
        runAndWait(() -> {
            searchField.setText("xyz");
            ObservableList<String> items = resultsList.getItems();
            assertTrue(items.isEmpty());
        });
    }

    @Test
    @Order(4)
    public void testOpenProfileNoSelection() throws InterruptedException {
        runAndWait(() -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(viewProfileButton));
            stage.setTitle("Default");
            stage.show();
            viewProfileButton.fire();
            assertEquals("Default", stage.getTitle());
            stage.close();
        });
    }
}
