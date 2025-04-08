
import org.junit.jupiter.api.*;
import org.seng.networking.NetworkingManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NetworkingManagerTest {

    private NetworkingManager manager;

    @BeforeEach
    void setUp() {
        manager = NetworkingManager.getInstance();
        // Disconnect all users before each test to ensure clean state
        for (String user : manager.getConnectedUsers()) {
            manager.disconnectPlayer(user);
        }
    }

    @Test
    void testConnectPlayer() {
        manager.connectPlayer("Alice");
        assertTrue(manager.getConnectedUsers().contains("Alice"));
    }

    @Test
    void testDisconnectPlayer() {
        manager.connectPlayer("Bob");
        manager.disconnectPlayer("Bob");
        assertFalse(manager.getConnectedUsers().contains("Bob"));
    }

    @Test
    void testSendMessageToUser_whenConnected() {
        manager.connectPlayer("Charlie");
        manager.sendMessageToUser("Charlie", "Hello!");
        // Can't assert output here unless capturing System.out
    }

    @Test
    void testSendMessageToUser_whenNotConnected() {
        manager.sendMessageToUser("NotAUser", "Should fail");
    }

    @Test
    void testBroadcastToUsers() {
        manager.connectPlayer("Alice");
        manager.connectPlayer("Bob");

        manager.broadcastToUsers(List.of("Alice", "Bob"), "Broadcasting!");
        // Again, would need to capture System.out to assert output
    }

    @Test
    void testNotifyMatchEnd() {
        manager.connectPlayer("Dave");
        manager.notifyMatchEnd("Dave");
    }

    @Test
    void testPingConnectedUser() {
        manager.connectPlayer("Eve");
        manager.ping("Eve");
    }

    @Test
    void testPingUnconnectedUser() {
        manager.ping("NotConnected");
    }
}
