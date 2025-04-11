package org.seng.networking;

import java.util.HashMap;
import java.util.Map;
import org.seng.authentication.Player;
import java.io.*;

public class GameSessionManager {

    /**
     * Creating a hashmap that stores game session data.
     * The key is a string (session id)
     * The value is a string (player username)
     **/
    private final Map<String, String> activeSessions;

    /**
     * Constructor - initializes the activeSessions hashmap when
     * an object of GameSessionManager is created.
     */
    public GameSessionManager() {
        activeSessions = new HashMap<>();
    }

    /**
     * Starts a game session.
     *
     * @param sessionID the session id
     * @param player    the player whose username is stored as the session identifier
     */
    public void startSession(String sessionID, Player player) {
        activeSessions.put(sessionID, player.getUsername());
        System.out.println("Game session has started for player " + player.getUsername());
    }

    /**
     * Ends a session by its id. If the session exists, it will be removed from the activeSessions map.
     *
     * @param sessionID the session id to remove
     */
    public void endSession(String sessionID) {
        if (activeSessions.containsKey(sessionID)) {
            System.out.println("Game session has ended for player " + activeSessions.get(sessionID));
            activeSessions.remove(sessionID);
        } else {
            System.out.println("Game session not found");
        }
    }

    /**
     * Retrieves the player's username associated with the session id.
     *
     * @param sessionID the session id
     * @return the player's username or null if not found
     */
    public String getPlayerIDBySession(String sessionID) {
        return activeSessions.get(sessionID);
    }
}
