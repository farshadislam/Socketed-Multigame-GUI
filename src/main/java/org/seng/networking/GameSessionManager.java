package org.seng.networking;

import java.util.HashMap;
import java.util.Map;

public class GameSessionManager {



    /**
     * creating a hashmap that stores game session data
     * the key is a string(session id)
     * the value is a string(playerID)
     **/
    private final Map<String, String> activeSessions;

    /**
     * constructor
     * initializes the activeSession hashmap when object of GameSessionManager is created
     */
    public GameSessionManager() {
        activeSessions = new HashMap<>();
    }

    /**
     * @param sessionID
     * @param player    the session(sessionID) gets stored in activeSessions, which links the session id to the player
     */
    public void startSession(String sessionID, Player player) {
        activeSessions.put(sessionID, String.valueOf(player.getPlayerID()));
        System.out.println("Game session has started for player" + player.getPlayerID());
    }

    /**
     * @param sessionID checks to see if the session first exists, if it exists then removes the sessionID from the active Sessions
     */
    public void endSession(String sessionID) {
        if (activeSessions.containsKey(sessionID)) {
            System.out.println("Game session has ended for Player" + activeSessions.get(sessionID));
            activeSessions.remove(sessionID);
        } else {
            System.out.println("Game session not found");
        }
    }

    public String getPlayerIDBySession(String sessionID) {
        return activeSessions.get(sessionID);
    }
}




