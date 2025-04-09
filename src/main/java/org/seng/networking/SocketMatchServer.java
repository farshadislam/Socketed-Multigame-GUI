package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// this class is the actual multiplayer game server that accepts player connections
// and handles matchmaking them into games using the queue
public class SocketMatchServer {

    private ServerSocket serverSocket; // this is the actual server that listens for connections

    // this is a thread pool so we can run lots of clients at once (if many different pairs wanna play)
    private ExecutorService pool = Executors.newCachedThreadPool();

    private final Matchmaking matchmaking = Matchmaking.getInstance(); // our matchmaking system
    private final NetworkingManager netManager = NetworkingManager.getInstance(); // manage active users

    // this sets up the server socket on the port we pass in
    public SocketMatchServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    // this is where the server actually runs and starts accepting connections forever
    public void start() throws IOException {
        System.out.println("Server waiting for players...");

        // this infinite loop keeps accepting new players forever
        while (true) {
            Socket clientSocket = serverSocket.accept(); // this waits for a player to connect
            System.out.println("New client connected: " + clientSocket.getInetAddress());

            // this sets up a new thread to handle new players whenever they end up joining
            new Thread(() -> handleNewPlayer(clientSocket)).start();
        }
    }

    // this handles all the logic for connecting a single new player
    private void handleNewPlayer(Socket clientSocket) {
        try {
            // this is setup for reading input and sending output to this specific player
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // this then asks the player for their username and wait for input
            out.write("Enter your username:\n");
            out.flush();
            String username = in.readLine();

            // connect player to system
            netManager.connectPlayer(username); // mark this player as connected

            // next it then asks them what game they want to play
            out.write("Select game:\n1. Checkers\n2. Connect Four\n3. Tic Tac Toe\nYour choice: ");
            out.flush();
            String choice = in.readLine(); // this reads what they typed
            GameType selectedGame;

            // this maps the input to an actual GameType
            switch (choice) {
                case "1": selectedGame = GameType.CHECKERS; break;
                case "2": selectedGame = GameType.CONNECT4; break;
                case "3": selectedGame = GameType.TICTACTOE; break;
                default:  selectedGame = GameType.CHECKERS; break; // this is default just in case
            }

            // this creates a new player object from the input username
            Player newPlayer = new Player(username, "");
            SocketGameHandler handler = new SocketGameHandler(clientSocket, username);
            handler.setGameType(selectedGame);
            newPlayer.setSocketHandler(handler);

            // try to join matchmaking system
            Match match = matchmaking.joinQueue(newPlayer, selectedGame);

            if (match == null) {
                // this means they’re still waiting
                System.out.println(username + " is waiting for opponent...");
                return;
            }

            // this means opponent already existed in queue
            Player opponent;
            if (match.getPlayer1().equals(newPlayer)) {
                opponent = match.getPlayer2();
            } else {
                opponent = match.getPlayer1();
            }

            // hook up both socket handlers to each other
            opponent.getSocketHandler().setOpponent(handler);
            handler.setOpponent(opponent.getSocketHandler());

            // this notifies both players of match success
            netManager.notifyPlayersMatched(newPlayer, opponent, match); // ✅ uses NetworkingManager!

            // this start threads for both players
            pool.execute(opponent.getSocketHandler());
            pool.execute(handler);

        } catch (IOException e) {
            System.err.println("Failed to handle new player: " + e.getMessage());
        }
    }

    // this closes the server socket and stops the thread pool
    public void stop() throws IOException {
        serverSocket.close();
        pool.shutdown();
    }

    public static void main(String[] args) {
        try {
            SocketMatchServer server = new SocketMatchServer(12345);
            server.start(); // this starts accepting connections
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}
