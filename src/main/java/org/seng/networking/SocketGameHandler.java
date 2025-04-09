package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;
import java.io.*;
import java.net.Socket;

public class SocketGameHandler implements Runnable {

    // this is our network socket for the client connection
    private Socket socket; // this is our network socket for the client connection
    private BufferedReader in; // this reader helps us get messages from the client
    private BufferedWriter out;   // this writer allows us to send text messages to the client
    private String playerName;     // this is the player's name associated with this handler

    public SocketGameHandler opponent;  // this will hold the opponent's socket game handler once matched
    private GameType gameType;   // this holds what game type the player wants to play
    private boolean isReady = false;

    // this constructor sets up the socket and initializes the reader and writer
    public SocketGameHandler(Socket socket, String playerName) throws IOException {
        this.socket = socket; // this stores the socket provided
        this.playerName = playerName; // this stores the player's name
        // this creates a reader for incoming messages from the socket connection
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // this creates a writer to send messages back through the socket connection
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    // this method lets us set the opponent's handler so the two players can talk to each other
    public void setOpponent(SocketGameHandler opponent) {
        this.opponent = opponent;
    }

    // this method stores what game the player chose
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    // this method returns the game type that has been set for this player
    public GameType getGameType() {
        return this.gameType;
    }

    // this method returns this same socket handler
    public SocketGameHandler getSocketHandler() {
        return this;
    }

    // this method sends a message to the client using our writer and adds a newline and flushes the message
    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    // this method returns whether the player has indicated they are ready
    public boolean isReady() {
        return isReady;
    }

    // this sets the ready flag for this player
    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    // this method sends opponent information over to the client
    public void sendOpponentInfo(String opponentName, boolean isPlayerOne) throws IOException {
        String playerPosition;
        if (isPlayerOne) {
            playerPosition = "true";
        } else {
            playerPosition = "false";
        }
        //this sends a message indicating the player's position
        sendMessage("IS_PLAYER_ONE:" + playerPosition);
        // this sends the opponent's name so the client knows who they are matched with
        sendMessage("OPPONENT_NAME:" + opponentName);
    }

    // this is where the socket game handler's main loop runs once started in a thread
    @Override
    public void run() {
        try {
            // so when a client connects a welcome message is sent
            sendMessage("Welcome " + playerName);
            // this informs the client of their player name
            sendMessage("PLAYER_NAME:" + playerName);
            String line;
            // this listens for messages from the client until the connection ends
            while ((line = in.readLine()) != null) {
                // this prints out what the client said to our console
                System.out.println(playerName + " says: " + line);
                // this marks the player as ready ig they select it
                if (line.equalsIgnoreCase("READY")) {
                    setReady(true); // this updates the ready flag
                    System.out.println(playerName + " is now ready.");
                    // this tells the opponennt that the player is ready
                    if (opponent != null) {
                        opponent.sendMessage("OPPONENT_READY");
                    }
                    // this signals to start the game is both players are ready
                    if (opponent != null && opponent.isReady()) {
                        sendMessage("START_GAME");
                        opponent.sendMessage("START_GAME");
                    }
                } else {
                    // this passes the non-ready message to the opponent
                    if (opponent != null) {
                        opponent.sendMessage(playerName + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            // this prints an error if there was an error occurring with the connection
            System.err.println("connection error with " + playerName);
        } finally {
            // this disconnects the player from our networking manager once finished
            NetworkingManager.getInstance().disconnectPlayer(playerName);
        }
    }
}