package org.seng.networking;

import java.io.*;
import java.net.Socket;

// this is the class that actually handles one player's connection
// it reads and writes data to/from the client and sends it to their opponent
public class SocketGameHandler implements Runnable {
    private Socket socket;
    private BufferedReader in; // for reading messages sent from the client
    private BufferedWriter out; // for sending messages back to the client
    private String playerName;
    public SocketGameHandler opponent; // reference to the other player

    private GameType gameType; // this is the game type this player is playing

    private boolean isReady = false; // this tracks if the player pressed ready

    public SocketGameHandler(Socket socket, String playerName) throws IOException {
        this.socket = socket;
        this.playerName = playerName;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void setOpponent(SocketGameHandler opponent) {
        this.opponent = opponent;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public SocketGameHandler getSocketHandler() {
        return this;
    }

    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    @Override
    public void run() {
        try {
            // greet the player after they connect
            sendMessage("Welcome " + playerName);
            String line;

            // loop forever listening to this player's input
            while ((line = in.readLine()) != null) {
                System.out.println(playerName + " says: " + line);

                // check if player is ready
                if (line.equalsIgnoreCase("READY")) {
                    setReady(true);
                    System.out.println(playerName + " is now ready.");

                    // notify opponent this player is ready
                    if (opponent != null) opponent.sendMessage("OPPONENT_READY");

                    // if both are ready, tell both to start the game
                    if (opponent != null && opponent.isReady()) {
                        sendMessage("START_GAME");
                        opponent.sendMessage("START_GAME");
                    }
                } else {
                    // treat it like a chat message and pass to opponent
                    if (opponent != null) opponent.sendMessage(playerName + ": " + line);
                }
            }

        } catch (IOException e) {
            System.err.println("Connection error with " + playerName); // show issue if disconnected
        } finally {
            // when connection closes (either normally or by error), clean up the networking manager
            NetworkingManager.getInstance().disconnectPlayer(playerName); // removes from list of online users
        }
    }

}
