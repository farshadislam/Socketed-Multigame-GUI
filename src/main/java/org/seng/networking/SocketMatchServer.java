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

            // this creates the actual handler connection to the client socket
            SocketGameHandler handler = new SocketGameHandler(clientSocket, username);
            handler.setGameType(selectedGame); // save the game mode they picked
            newPlayer.setSocketHandler(handler); // attach this handler to their player obj

            // this uses  the Matchmaking system to try joining the queue or get matched
            Matchmaking matchmaking = Matchmaking.getInstance();
            Match match = matchmaking.joinQueue(newPlayer, selectedGame);

            // CASE 1: no one was waiting yet → this player just got queued
            if (match == null) {
                // do nothing yet, just wait for opponent to join and pick up the match later
                System.out.println(username + " is waiting for opponent...");
                return;
            }

            // CASE 2: match got made! this player is second one to join
            Player player1 = match.getPlayer1(); // already waiting
            Player player2 = match.getPlayer2(); // just joined (this guy)

            // mark the match as no longer available (prevents double usage)
            match.markAsNotReady();

            // set opponents
            player1.getSocketHandler().setOpponent(handler);
            handler.setOpponent(player1.getSocketHandler());

            // run both threads
            pool.execute(player1.getSocketHandler());
            pool.execute(handler);

            System.out.println("Match made: " + player1.getUsername() + " vs " + player2.getUsername());

        } catch (IOException e) {
            // this prints out what went wrong if something failed
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
