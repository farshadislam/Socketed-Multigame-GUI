package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;
import org.seng.authentication.Player;

import java.io.*;
import java.net.Socket;

// this class handles a single client connection in the multiplayer game server.
// it receives and processes messages from a client (such as readiness, moves, etc.)
// and, when appropriate, it forwards messages (for example, move commands) to the opponent.
public class SocketGameHandler implements Runnable {

    // this is the socket connection with the client
    private Socket socket;
    // the reader to capture messages coming from the client
    private BufferedReader in;
    // the writer to send messages to the client
    private BufferedWriter out;
    // the name of the connected player
    private String playerName;

    // this references the opponents handler
    public SocketGameHandler opponent;

    // this is the game type that the player has chosen
    private GameType gameType;
    // this flag indicates whether the client is ready to start
    private boolean isReady = false;

    // this tracks if the client is closed by sending the target message
    private boolean waitingRoomClosed = false;

    // this constructor sets up the socket connection and I/O streams and stores the player's name.
    public SocketGameHandler(Socket socket, String playerName) throws IOException {
        this.socket = socket;
        this.playerName = playerName;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    // this sets the opponents SocketGameHandler when players are matched.
    public void setOpponent(SocketGameHandler opponent) {
        this.opponent = opponent;
    }

    // this sets the game type chosen by the client
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    // this return the game type
    public GameType getGameType() {
        return this.gameType;
    }

    // this return this SocketGameHandler instance
    public SocketGameHandler getSocketHandler() {
        return this;
    }

    // this send a message to the connected client.
    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    // this returns the readiness fla
    public boolean isReady() {
        return isReady;
    }

    // this sets the readiness flag
    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    // this sets the flag that the client has closed its waiting room
    public void setWaitingRoomClosed(boolean closed) {
        waitingRoomClosed = closed;
    }

    // this get the flags if the client has closed its waiting room.
    public boolean isWaitingRoomClosed() {
        return waitingRoomClosed;
    }

    // this sends the role assignment and opponenent name
    // this also informs the client whether it is Player 1 (X) or Player 2 (O).
    public void sendOpponentInfo(String opponentName, boolean isPlayerOne) throws IOException {
        if (isPlayerOne) {
            sendMessage("IS_PLAYER_ONE:true");
        } else {
            sendMessage("IS_PLAYER_ONE:false");
        }
        sendMessage("OPPONENT_NAME:" + opponentName);
    }

    @Override
    public void run() {
        try {
            // this sends initial welcome messages to the connected client
            sendMessage("Welcome " + playerName);
            sendMessage("PLAYER_NAME:" + playerName);

            String line;
            // this loop reads messages from the client
            while ((line = in.readLine()) != null) {
                System.out.println("[DEBUG SERVER] " + playerName + " says: " + line);

                // this marks the it as raady and notifies the opponent if the client sends ready
                if (line.equalsIgnoreCase("READY")) {
                    setReady(true);
                    System.out.println("[DEBUG SERVER] " + playerName + " is now ready.");
                    if (opponent != null) {
                        opponent.sendMessage("OPPONENT_READY");
                    }
                    // this starts the game by sending start game to both
                    if (opponent != null && opponent.isReady()) {
                        sendMessage("START_GAME");
                        opponent.sendMessage("START_GAME");
                    }
                }
                // this is for when the client signals that it has closed
                else if (line.equals("WAITING_ROOM_CLOSED")) {
                    setWaitingRoomClosed(true);
                    System.out.println("[DEBUG SERVER] Received WAITING_ROOM_CLOSED from " + playerName);
                    // this is if both have closed
                    if (opponent != null && opponent.isWaitingRoomClosed()) {
                        System.out.println("[DEBUG SERVER] Both clients have closed the waiting room.");
                    }
                }
                // this forwards tic-tac-toe messages, to both clients if both waiting rooms have been closed
                else if (line.startsWith("MOVE:TICTACTOE:")) {
                    if (opponent != null) {
                        if (this.isWaitingRoomClosed() && opponent.isWaitingRoomClosed()) {
                            System.out.println("[DEBUG SERVER] Forwarding move: " + line + " to opponent of " + playerName);
                            opponent.sendMessage(line);
                        } else {
                            System.out.println("[DEBUG SERVER] Received move but waiting room not closed on both ends. Ignoring move: " + line);
                        }
                    }
                }
                // these are for any other messages
                else {
                    if (opponent != null) {
                        System.out.println("[DEBUG SERVER] Forwarding message from " + playerName + " to opponent.");
                        opponent.sendMessage(playerName + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[DEBUG SERVER] Connection error with " + playerName + ": " + e.getMessage());
        } finally {
            // this disconnects the player using the NetworkingManager
            NetworkingManager.getInstance().disconnectPlayer(playerName);
        }
    }
}
