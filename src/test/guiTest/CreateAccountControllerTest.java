package guiTest;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.gui.CreateAccountController;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for CreateAccountController.
 *
 * To run these tests:
 * - Ensure the JavaFX platform is initialized.
 * - Place dummy fxml and css files in src/test/resources/org/seng/gui/ as described above.
 */
public class CreateAccountControllerTest {

    private CreateAccountController controller;

    // A helper method to run code on the JavaFX Application Thread and wait for its completion.
    private void runAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        // Wait up to 5 seconds for FX actions to complete.
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout waiting for Platform.runLater.");
        }
    }

    // Since JavaFX needs to be initialized, we do this once per test run.
    @BeforeAll
    public static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            // No need to do anything here.
            latch.countDown();
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("JavaFX initialization took too long.");
        }
    }

    // Use this method to inject dummy controls into the controller.
    private void injectControls(CreateAccountController ctrl) {
        setField(ctrl, "usernameField", new TextField());
        setField(ctrl, "emailField", new TextField());
        setField(ctrl, "passwordField", new PasswordField());
        setField(ctrl, "confirmPasswordField", new PasswordField());
        setField(ctrl, "registerButton", new Button());
        setField(ctrl, "backButton", new Button());
    }

    // Helper: sets a private field by reflection.
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            fail("Failed to set field: " + fieldName);
        }
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        controller = new CreateAccountController();
        injectControls(controller);
        // Initialize the controller on the FX thread.
        runAndWait(() -> controller.initialize());
    }

    // After each test, close any opened windows to avoid test interference.
    @AfterEach
    public void tearDown() throws InterruptedException {
        runAndWait(() -> {
            for (Window window : Window.getWindows()) {
                if (window instanceof Stage) {
                    ((Stage) window).close();
                }
            }
        });
    }

    @Test
    public void testInitializeSetsUpButtonsAndListeners() throws InterruptedException {
        // Check that the register button text is set.
        assertEquals("Register", ((Button) getField(controller, "registerButton")).getText());

        // Check that the text listeners are present by simulating an error condition then typing.
        TextField usernameField = (TextField) getField(controller, "usernameField");
        // Manually simulate an error prompt.
        usernameField.getStyleClass().add("error-prompt");
        usernameField.setPromptText("Some error");

        // Change the text to trigger the listener.
        runAndWait(() -> usernameField.setText("newUsername"));
        // Give time for listener (almost immediate)
        Thread.sleep(50);
        // The resetPrompt method should have removed the error-prompt style and set the prompt to "Username"
        assertFalse(usernameField.getStyleClass().contains("error-prompt"));
        assertEquals("Username", usernameField.getPromptText());
    }

    // Helper: get value from a private field via reflection.
    private Object getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            fail("Failed to get field: " + fieldName);
            return null;
        }
    }

    @Test
    public void testHandleRegistrationValidInputOpensVerificationPage() throws InterruptedException {
        // Set valid values on FX thread
        runAndWait(() -> {
            TextField usernameField = (TextField) getField(controller, "usernameField");
            TextField emailField = (TextField) getField(controller, "emailField");
            PasswordField passwordField = (PasswordField) getField(controller, "passwordField");
            PasswordField confirmPasswordField = (PasswordField) getField(controller, "confirmPasswordField");
            Button registerButton = (Button) getField(controller, "registerButton");

            usernameField.setText("validUser");
            emailField.setText("valid@example.com");
            passwordField.setText("pass123");
            confirmPasswordField.setText("pass123");

            // Set up a dummy stage and scene for the register button
            Stage dummyStage = new Stage();
            Pane dummyPane = new Pane();
            Scene dummyScene = new Scene(dummyPane);
            dummyPane.getChildren().add(registerButton);
            dummyStage.setScene(dummyScene);
            dummyStage.show();

            // Fire the register button action
            registerButton.fire();
        });

        // Wait for the window operations to complete
        Thread.sleep(500);

        // Verify on FX thread
        runAndWait(() -> {
            boolean found = false;
            for (Window window : Window.getWindows()) {
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;
                    if ("Verify Your Email".equals(stage.getTitle())) {
                        found = true;
                        assertEquals(700, stage.getScene().getWidth(), 0.1);
                        assertEquals(450, stage.getScene().getHeight(), 0.1);
                        stage.close(); // Clean up
                    }
                }
            }
            assertTrue(found, "Verification page stage should be opened.");
        });
    }

    @Test
    public void testHandleRegistrationEmptyUsername() throws InterruptedException {
        TextField usernameField = (TextField) getField(controller, "usernameField");
        TextField emailField = (TextField) getField(controller, "emailField");
        PasswordField passwordField = (PasswordField) getField(controller, "passwordField");
        PasswordField confirmPasswordField = (PasswordField) getField(controller, "confirmPasswordField");
        Button registerButton = (Button) getField(controller, "registerButton");

        runAndWait(() -> {
            usernameField.setText("");
            emailField.setText("valid@example.com");
            passwordField.setText("pass123");
            confirmPasswordField.setText("pass123");
        });

        runAndWait(() -> registerButton.fire());

        // Immediately the username field should show an error and be cleared.
        assertTrue(usernameField.getStyleClass().contains("error-prompt"));
        assertEquals("Please enter a valid username", usernameField.getPromptText());
        assertEquals("", usernameField.getText());

        // Wait for the PauseTransition (plus a small buffer) so the prompt is reset.
        Thread.sleep(2100);
        assertFalse(usernameField.getStyleClass().contains("error-prompt"));
        assertEquals("Username", usernameField.getPromptText());
    }

    @Test
    public void testHandleRegistrationEmptyEmail() throws InterruptedException {
        TextField usernameField = (TextField) getField(controller, "usernameField");
        TextField emailField = (TextField) getField(controller, "emailField");
        PasswordField passwordField = (PasswordField) getField(controller, "passwordField");
        PasswordField confirmPasswordField = (PasswordField) getField(controller, "confirmPasswordField");
        Button registerButton = (Button) getField(controller, "registerButton");

        runAndWait(() -> {
            usernameField.setText("validUser");
            emailField.setText("");
            passwordField.setText("pass123");
            confirmPasswordField.setText("pass123");
        });

        runAndWait(() -> registerButton.fire());

        // Check email field error
        assertTrue(emailField.getStyleClass().contains("error-prompt"));
        assertEquals("Please enter a valid email", emailField.getPromptText());
        assertEquals("", emailField.getText());

        Thread.sleep(2100);
        assertFalse(emailField.getStyleClass().contains("error-prompt"));
        assertEquals("Email", emailField.getPromptText());
    }

    @Test
    public void testHandleRegistrationPasswordMismatch() throws InterruptedException {
        TextField usernameField = (TextField) getField(controller, "usernameField");
        TextField emailField = (TextField) getField(controller, "emailField");
        PasswordField passwordField = (PasswordField) getField(controller, "passwordField");
        PasswordField confirmPasswordField = (PasswordField) getField(controller, "confirmPasswordField");
        Button registerButton = (Button) getField(controller, "registerButton");

        runAndWait(() -> {
            usernameField.setText("validUser");
            emailField.setText("valid@example.com");
            passwordField.setText("pass123");
            confirmPasswordField.setText("different");
        });

        runAndWait(() -> registerButton.fire());

        // Both password fields should show error prompt and be cleared.
        assertTrue(passwordField.getStyleClass().contains("error-prompt"));
        assertTrue(confirmPasswordField.getStyleClass().contains("error-prompt"));
        assertEquals("Passwords do not match", passwordField.getPromptText());
        assertEquals("Passwords do not match", confirmPasswordField.getPromptText());
        assertEquals("", passwordField.getText());
        assertEquals("", confirmPasswordField.getText());
    }

    @Test
    public void testReturnToLogin() throws InterruptedException {
        runAndWait(() -> {
            Button backButton = (Button) getField(controller, "backButton");

            // Set up a dummy stage for the back button
            Stage dummyStage = new Stage();
            Pane dummyPane = new Pane();
            Scene dummyScene = new Scene(dummyPane);
            dummyPane.getChildren().add(backButton);
            dummyStage.setScene(dummyScene);
            dummyStage.show();

            // Fire the back button action
            backButton.fire();
        });

        // Wait for the window operations to complete
        Thread.sleep(500);

        // Verify on FX thread
        runAndWait(() -> {
            boolean found = false;
            for (Window window : Window.getWindows()) {
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;
                    if ("OMG Platform".equals(stage.getTitle())) {
                        found = true;
                        assertEquals(700, stage.getScene().getWidth(), 0.1);
                        assertEquals(450, stage.getScene().getHeight(), 0.1);
                        stage.close(); // Clean up
                    }
                }
            }
            assertTrue(found, "Login page stage should be opened.");
        });
    }

    @Test
    public void testResetPrompt() throws InterruptedException {
        TextField usernameField = (TextField) getField(controller, "usernameField");

        // Simulate an error state.
        runAndWait(() -> {
            usernameField.getStyleClass().add("error-prompt");
            usernameField.setPromptText("Error occurred");
        });

        // Directly call the resetPrompt method via the text listener mechanism.
        // (Since resetPrompt is private, we trigger it by simulating a text change as done in initialize.)
        runAndWait(() -> usernameField.setText("something"));

        // Allow listener to run.
        Thread.sleep(50);
        assertFalse(usernameField.getStyleClass().contains("error-prompt"));
        assertEquals("Username", usernameField.getPromptText());
    }

    @Test
    public void testDisplayPasswordError() throws InterruptedException {
        PasswordField passwordField = (PasswordField) getField(controller, "passwordField");
        PasswordField confirmPasswordField = (PasswordField) getField(controller, "confirmPasswordField");

        // Invoke displayPasswordError via reflection. (Since it's private, we can simulate registration with mismatched passwords.)
        // Alternatively, call the register button action with mismatched passwords.
        runAndWait(() -> {
            passwordField.setText("pass1");
            confirmPasswordField.setText("pass2");
        });
        Button registerButton = (Button) getField(controller, "registerButton");
        runAndWait(() -> registerButton.fire());

        // Test that both password fields now display the error.
        assertTrue(passwordField.getStyleClass().contains("error-prompt"));
        assertTrue(confirmPasswordField.getStyleClass().contains("error-prompt"));
        assertEquals("Passwords do not match", passwordField.getPromptText());
        assertEquals("Passwords do not match", confirmPasswordField.getPromptText());
        assertEquals("", passwordField.getText());
        assertEquals("", confirmPasswordField.getText());
    }
}
