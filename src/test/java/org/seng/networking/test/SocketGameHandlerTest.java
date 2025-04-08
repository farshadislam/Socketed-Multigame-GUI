package org.seng.networking.test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.SocketGameHandler;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class SocketGameHandlerTest {

    private SocketGameHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        Socket socket = new Socket("localhost", 12345); // Assuming server is running at localhost:12345
        handler = new SocketGameHandler(socket, "Player1");
    }

    @Test
    void testSendMessage() throws IOException {
        String testMessage = "Test Message to Opponent";
        handler.sendMessage(testMessage);

        assertDoesNotThrow(() -> handler.sendMessage(testMessage));
    }

    @Test
    void testSetOpponent() throws IOException {
        SocketGameHandler opponentHandler = new SocketGameHandler(new Socket(), "Player2");
        handler.setOpponent(opponentHandler);
        assertEquals(opponentHandler, handler.opponent);
    }

    @Test
    void testRun() {
        assertDoesNotThrow(() -> {
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
            handlerThread.join();  // Wait for the thread to finish execution
        });
    }
}
