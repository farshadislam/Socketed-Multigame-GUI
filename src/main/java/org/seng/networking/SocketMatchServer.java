package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.seng.authentication.Player;
import java.io.*;

// this class is the actual multiplayer game server that accepts player connections
// and handles matchmaking them into games using the queue
public class SocketMatchServer {

    private ServerSocket serverSocket; // this is the actual server that listens for connections

    // this is a thread pool so we can run lots of clients at once (if many different pairs wanna play)
    private ExecutorService pool = Executors.newCachedThreadPool();

    private final Matchmaking matchmaking = Matchmaking.getInstance(); // this is our matchmaking system
    private final NetworkingManager netManager = NetworkingManager.getInstance(); // this tracks who's connected

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

            // this marks this player as connected in the network manager
            netManager.connectPlayer(username); // this tracks that the player has joined

            // next it then asks them what game they want to play
            out.write("Select game:\n1. Checkers\n2. Connect Four\n3. Tic Tac Toe\nYour choice: ");
            out.flush();
            String choice = in.readLine(); // this reads what they typed
            GameType selectedGame;

            // this maps the input to an actual GameType
            if (choice.equals("1")) {
                selectedGame = GameType.CHECKERS;
            } else if (choice.equals("2")) {
                selectedGame = GameType.CONNECT4;
            } else if (choice.equals("3")) {
                selectedGame = GameType.TICTACTOE;
            } else {
                selectedGame = GameType.CHECKERS; // this defaults to Checkers if invalid input
            }

            // this creates a new player object from the input username
            Player newPlayer = new Player(username, "", "");

            // this creates the actual handler connection to the client socket
            SocketGameHandler handler = new SocketGameHandler(clientSocket, username);
            handler.setGameType(selectedGame); // this saves the game mode they picked
            newPlayer.setSocketHandler(handler); // this stores the handler in their player obj

            // try to join matchmaking system
            Match match = matchmaking.joinQueue(newPlayer, selectedGame);

            // this is if no one matched yet and the player waits
            if (match == null) {
                System.out.println(username + " is waiting for opponent...");
                return;
            }

            // this is if the players have matched and identifies which player is who
            Player opponent;
            if (match.getPlayer1().equals(newPlayer)) {
                opponent = match.getPlayer2(); // this person is second to join
            } else {
                opponent = match.getPlayer1(); // this person is second to join
            }

            // this hooks up both socket handlers to each other
            opponent.getSocketHandler().setOpponent(handler); // tells the opponent who joined
            handler.setOpponent(opponent.getSocketHandler()); // this tells this player too

            // this determines the player positions
            boolean isNewPlayerOne = match.getPlayer1().equals(newPlayer);

            // this sends opponent info to both players
            handler.sendOpponentInfo(opponent.getUsername(), isNewPlayerOne);
            opponent.getSocketHandler().sendOpponentInfo(newPlayer.getUsername(), !isNewPlayerOne);

            // this notifies both players the match is made
            netManager.notifyPlayersMatched(newPlayer, opponent, match);

            // this launches both threads
            pool.execute(opponent.getSocketHandler());
            pool.execute(handler);

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