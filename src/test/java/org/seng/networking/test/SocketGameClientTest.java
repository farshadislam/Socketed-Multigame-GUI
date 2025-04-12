//package org.seng.networking.test;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.seng.networking.SocketGameClient;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SocketGameClientTest {
//
//    private SocketGameClient client;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        client = new SocketGameClient("localhost", 12345);
//    }
//
//    @Test
//    void testSendMessage() throws IOException {
//        String testMessage = "Hello Server!";
//        client.sendMessage(testMessage);
//        assertDoesNotThrow(() -> client.sendMessage(testMessage));
//    }
//
//    @Test
//    void testReceiveMessage() throws IOException {
//        String message = client.receiveMessage();
//        assertNotNull(message);
//    }
//
//    @Test
//    void testClose() throws IOException {
//        assertDoesNotThrow(() -> client.close());
//    }
//}
package org.seng.networking.test;

import org.junit.jupiter.api.*;
import org.seng.networking.SocketGameClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class SocketGameClientTest {

    private ServerSocket mockServer;
    private SocketGameClient client;
    private Socket serverSideSocket;
    private BufferedReader serverIn;
    private BufferedWriter serverOut;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new ServerSocket(0); // assign available port
        int port = mockServer.getLocalPort();

        // Start server-side socket
        new Thread(() -> {
            try {
                serverSideSocket = mockServer.accept();
                serverIn = new BufferedReader(new InputStreamReader(serverSideSocket.getInputStream()));
                serverOut = new BufferedWriter(new OutputStreamWriter(serverSideSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        client = new SocketGameClient("localhost", port);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) client.close();
        if (serverSideSocket != null) serverSideSocket.close();
        if (mockServer != null) mockServer.close();
    }

    @Test
    void testSendMessage() throws IOException {
        String testMessage = "Hello Server!";
        client.sendMessage(testMessage);
        String received = serverIn.readLine();
        assertEquals(testMessage, received);
    }

    @Test
    void testReceiveMessage() throws IOException {
        String expected = "Hello Client!";
        serverOut.write(expected);
        serverOut.newLine();
        serverOut.flush();
        String received = client.receiveMessage();
        assertEquals(expected, received);
    }

    @Test
    void testSendMultipleMessages() throws IOException {
        String[] messages = { "First", "Second", "Third" };
        for (String msg : messages) {
            client.sendMessage(msg);
            String received = serverIn.readLine();
            assertEquals(msg, received);
        }
    }

    @Test
    void testReceiveEmptyMessage() throws IOException {
        serverOut.write(""); // no newLine
        serverOut.newLine();
        serverOut.flush();
        String received = client.receiveMessage();
        assertEquals("", received);
    }

    @Test
    void testCloseDoesNotThrow() {
        assertDoesNotThrow(() -> client.close());
    }

    @Test
    void testCloseClosesSocket() throws IOException {
        client.close();
        assertTrue(client.getSocket().isClosed());
    }

    @Test
    void testReceiveAfterServerDisconnects() throws IOException {
        serverSideSocket.close(); // simulate disconnect
        assertNull(client.receiveMessage());
    }
}
