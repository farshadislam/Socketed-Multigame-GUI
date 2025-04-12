
package org.seng.networking.test;

import org.junit.jupiter.api.*;
import org.seng.networking.SocketGameHandler;
import org.seng.leaderboard_matchmaking.GameType;

import java.io.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

public class SocketGameHandlerTest {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private SocketGameHandler handler;
    private BufferedWriter clientOut;
    private BufferedReader clientIn;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();

        new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                handler = new SocketGameHandler(socket, "Player1");
                new Thread(handler).start();
            } catch (IOException e) {
                fail("Failed to start server thread: " + e.getMessage());
            }
        }).start();

        Thread.sleep(100); // Ensure server is ready

        clientSocket = new Socket("localhost", port);
        clientOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
    }

    @Test
    public void testSendMessage() throws IOException {
        handler.sendMessage("Hello Client!");
        String msg = clientIn.readLine();
        assertEquals("Hello Client!", msg);
    }

    @Test
    public void testSendOpponentInfo() throws IOException, InterruptedException {
        handler.sendOpponentInfo("Player2", true);
        Thread.sleep(50); // Let it flush

        assertEquals("IS_PLAYER_ONE:true", clientIn.readLine());
        assertEquals("OPPONENT_NAME:Player2", clientIn.readLine());
    }
//
    @Test
    public void testSetGameTypeAndGet() {
        handler.setGameType(GameType.TICTACTOE);
        assertEquals(GameType.TICTACTOE, handler.getGameType());
    }

    @Test
    public void testWaitingRoomCloseFlag() {
        handler.setWaitingRoomClosed(true);
        assertTrue(handler.isWaitingRoomClosed());
    }

    @Test
    public void testIsReadyFlag() throws IOException, InterruptedException {
        clientOut.write("READY");
        clientOut.newLine();
        clientOut.flush();

        Thread.sleep(100); // Let handler process it

        assertTrue(handler.isReady());
    }

    @Test
    public void testHandlerWelcomeMessages() throws IOException {
        String welcome = clientIn.readLine();
        String playerName = clientIn.readLine();

        assertTrue(welcome.contains("Welcome Player1"));
        assertEquals("PLAYER_NAME:Player1", playerName);
    }
}
