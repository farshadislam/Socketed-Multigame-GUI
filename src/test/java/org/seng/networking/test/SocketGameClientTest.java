
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.SocketGameClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SocketGameClientTest {

    private SocketGameClient client;

    @BeforeEach
    void setUp() throws IOException {
        client = new SocketGameClient("localhost", 12345);
    }

    @Test
    void testSendMessage() throws IOException {
        String testMessage = "Hello Server!";
        client.sendMessage(testMessage);
        assertDoesNotThrow(() -> client.sendMessage(testMessage));
    }

    @Test
    void testReceiveMessage() throws IOException {
        String message = client.receiveMessage();
        assertNotNull(message);
    }

    @Test
    void testClose() throws IOException {
        assertDoesNotThrow(() -> client.close());
    }
}
