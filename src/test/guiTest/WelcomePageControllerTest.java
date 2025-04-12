package guiTest;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.seng.gui.WelcomePageController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WelcomePageControllerTest {

    private WelcomePageController controller;
    private VBox loginBox;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button createAccountButton;
    private Hyperlink forgotPasswordLink;
    private Label titleLabel;
    private Pane iconPane;
    private Label errorLabel;

    private void runAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(3, TimeUnit.SECONDS)) {
            fail("Timeout waiting for FX Application Thread.");
        }
    }

    @BeforeAll
    public static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        if (!latch.await(3, TimeUnit.SECONDS)) {
            fail("Timeout initializing JavaFX.");
        }
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        controller = new WelcomePageController();
        loginBox = new VBox();
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button();
        createAccountButton = new Button();
        forgotPasswordLink = new Hyperlink();
        titleLabel = new Label();
        iconPane = new Pane();
        errorLabel = new Label();
        errorLabel.setVisible(false);
        setField("loginBox", loginBox);
        setField("usernameField", usernameField);
        setField("passwordField", passwordField);
        setField("loginButton", loginButton);
        setField("createAccountButton", createAccountButton);
        setField("forgotPasswordLink", forgotPasswordLink);
        setField("titleLabel", titleLabel);
        setField("iconPane", iconPane);
        setField("errorLabel", errorLabel);
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
            Field field = WelcomePageController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (Exception e) {
            fail("Could not set field " + fieldName + ": " + e.getMessage());
        }
    }

    private Object callPrivateMethod(String methodName, Class<?>[] paramTypes, Object... params) {
        try {
            Method method = WelcomePageController.class.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(controller, params);
        } catch (Exception e) {
            fail("Could not invoke private method " + methodName + ": " + e.getMessage());
        }
        return null;
    }

    @Test
    @Order(1)
    public void testInitializeSetup() throws InterruptedException {
        runAndWait(() -> {
            assertEquals(-200, titleLabel.getTranslateY(), 0.001, "titleLabel translateY should be -200 initially");
            assertNotNull(loginButton.getOnAction(), "loginButton should have action");
            assertNotNull(createAccountButton.getOnAction(), "createAccountButton should have action");
            assertNotNull(forgotPasswordLink.getOnAction(), "forgotPasswordLink should have action");
        });
    }

    @Test
    @Order(2)
    public void testAddControllerIcons() throws InterruptedException {
        runAndWait(() -> {
            iconPane.setPrefSize(400, 400);
            callPrivateMethod("addControllerIcons", new Class<?>[0]);
            assertFalse(iconPane.getChildren().isEmpty(), "iconPane should have child icons after addControllerIcons()");
            iconPane.getChildren().forEach(node -> {
                assertTrue(node.getStyleClass().contains("controller-icon"), "Child node should have 'controller-icon' style");
                assertTrue(node instanceof ImageView, "Child node should be an ImageView");
            });
        });
    }

    @Test
    @Order(3)
    public void testHandleCreateAccount() throws InterruptedException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
        try {
            runAndWait(() -> {
                try {
                    Method method = WelcomePageController.class.getDeclaredMethod("handleCreateAccount");
                    method.setAccessible(true);
                    method.invoke(controller);
                } catch (Exception e) {
                    fail("Could not invoke handleCreateAccount: " + e.getMessage());
                }
            });
        } finally {
            System.setOut(originalOut);
        }
        String output = capturedOut.toString();
        assertTrue(output.contains("Create Account Clicked"), "handleCreateAccount() should print 'Create Account Clicked'");
    }

    @Test
    @Order(4)
    public void testDisplayErrorMessage() throws InterruptedException {
        runAndWait(() -> {
            try {
                Method method = WelcomePageController.class.getDeclaredMethod("displayErrorMessage", String.class);
                method.setAccessible(true);
                method.invoke(controller, "Testing Error");
            } catch (Exception e) {
                fail("Error calling displayErrorMessage: " + e.getMessage());
            }
            assertEquals("Testing Error", errorLabel.getText(), "errorLabel should have the provided text");
            assertTrue(errorLabel.isVisible(), "errorLabel should be visible");
        });
        Thread.sleep(2100);
        runAndWait(() -> assertFalse(errorLabel.isVisible(), "errorLabel should no longer be visible after 2s"));
    }
}
