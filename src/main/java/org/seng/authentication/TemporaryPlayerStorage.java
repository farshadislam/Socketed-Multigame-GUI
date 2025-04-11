package org.seng.authentication;
import java.util.HashMap;
import java.util.Map;

public class TemporaryPlayerStorage {
    private static Map<String, Player> unverifiedPlayers = new HashMap<>();

    // Add a player to temporary storage
    public static void addPlayer(String username, Player player) {
        unverifiedPlayers.put(username.toLowerCase(), player);
    }
    public static boolean findUsername(String username){
        return unverifiedPlayers.containsKey(username.toLowerCase());
    }
    // Get a player from temporary storage
    public static Player getPlayer(String username) {
        return unverifiedPlayers.get(username.toLowerCase());
    }

    // Remove a player from temporary storage
    public static void removePlayer(String username) {
        unverifiedPlayers.remove(username.toLowerCase());
    }
    public static void clear() {
        unverifiedPlayers.clear();
    }
}
