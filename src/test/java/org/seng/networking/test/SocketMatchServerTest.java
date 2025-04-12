//package org.seng.networking.test;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.seng.networking.SocketMatchServer;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SocketMatchServerTest {
//
//    private SocketMatchServer server;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        server = new SocketMatchServer(12345);
//    }
//
//    @Test
//    void testStartServer() throws IOException {
//        assertDoesNotThrow(() -> server.start());
//    }
//
//    @Test
//    void testStopServer() throws IOException {
//        assertDoesNotThrow(() -> server.stop());
//    }
//
//    @Test
//    void testServerAcceptsConnections() throws IOException {
//        Socket clientSocket = new Socket("localhost", 12345);
//        assertNotNull(clientSocket);
//
//        clientSocket.close();
//    }
//}

package org.seng.networking.test;

import org.junit.jupiter.api.*;
import org.seng.networking.SocketMatchServer;

import java.io.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

public class SocketMatchServerTest {

    private SocketMatchServer server;
    private ServerSocket testSocket;
    private int testPort;
    private Thread serverThread;

    @BeforeEach
    void setUp() throws IOException {
        // Find a free port
        testSocket = new ServerSocket(0);
        testPort = testSocket.getLocalPort();
        testSocket.close(); // just reserving the port

        server = new SocketMatchServer(testPort);
        serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (IOException ignored) {}
        });
        serverThread.start();

        // Small delay to give time for server to start
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
    }

    @AfterEach
    void tearDown() throws IOException {
        server.stop();
    }

    @Test
    void testServerAcceptsConnection() throws IOException {
        Socket clientSocket = new Socket("localhost", testPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        // Read username prompt from server
        String usernamePrompt = in.readLine();
        assertTrue(usernamePrompt.toLowerCase().contains("username"));

        // Send a username
        out.write("TestUser");
        out.newLine();
        out.flush();

        // Read game prompt
        String gamePrompt = in.readLine();
        assertNotNull(gamePrompt);
        assertTrue(gamePrompt.contains("Select game"));

        // Send game choice
        out.write("3"); // Tic Tac Toe
        out.newLine();
        out.flush();

        clientSocket.close();
    }

    @Test
    void testStopServerDoesNotThrow() {
        assertDoesNotThrow(() -> server.stop());
    }

    @Test
    void testSecondClientTriggersMatchOrWait() throws IOException {
        Socket client1 = new Socket("localhost", testPort);
        BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
        BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(client1.getOutputStream()));

        Socket client2 = new Socket("localhost", testPort);
        BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(client2.getOutputStream()));

        in1.readLine(); // username prompt
        out1.write("Player1");
        out1.newLine();
        out1.flush();

        in1.readLine(); // game selection
        out1.write("3");
        out1.newLine();
        out1.flush();

        in2.readLine(); // username prompt
        out2.write("Player2");
        out2.newLine();
        out2.flush();

        in2.readLine(); // game selection
        out2.write("3");
        out2.newLine();
        out2.flush();

        client1.close();
        client2.close();
    }
}
