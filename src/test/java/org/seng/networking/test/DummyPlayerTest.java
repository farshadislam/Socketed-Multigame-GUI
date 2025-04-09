package org.seng.networking.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.DummyPlayer;

import static org.junit.jupiter.api.Assertions.*;

public class DummyPlayerTest {

    private DummyPlayer dummy;

    @BeforeEach
    void setUp() {
        dummy = new DummyPlayer("dummyUser", "dummyPass");
    }

    @Test
    void testLoginAlwaysReturnsTrue() {
        assertTrue(dummy.login("anyPassword"), "DummyPlayer should always return true on login");
    }

//need a test for checking the connection here

    @Test
    void testAutoChatMessagesAreInitialized() {
        assertFalse(getAutoChatMessages().isEmpty(), "Auto chat messages should not be empty");
        assertTrue(getAutoChatMessages().contains("You're donezo bozo"), "Chat message should be present");
    }

    private java.util.List<String> getAutoChatMessages() {
        try {
            java.lang.reflect.Field field = DummyPlayer.class.getDeclaredField("autoChatMessages");
            field.setAccessible(true);
            return (java.util.List<String>) field.get(null);
        } catch (Exception e) {
            fail("Could not access autoChatMessages: " + e.getMessage());
            return null;
        }
    }
}
