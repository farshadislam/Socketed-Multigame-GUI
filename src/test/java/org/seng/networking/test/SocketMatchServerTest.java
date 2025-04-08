
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.SocketMatchServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class SocketMatchServerTest {

    private SocketMatchServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new SocketMatchServer(12345);
    }

    @Test
    void testStartServer() throws IOException {
        assertDoesNotThrow(() -> server.start());
    }

    @Test
    void testStopServer() throws IOException {
        assertDoesNotThrow(() -> server.stop());
    }

    @Test
    void testServerAcceptsConnections() throws IOException {
        Socket clientSocket = new Socket("localhost", 12345);
        assertNotNull(clientSocket);

        clientSocket.close();
    }
}
