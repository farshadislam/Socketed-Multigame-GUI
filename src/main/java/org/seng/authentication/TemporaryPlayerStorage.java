package org.seng.authentication;
import java.util.HashMap;
import java.util.Map;

/**
 * TemporaryPlayerStorage is a utility class used to temporarily store
 * players who have not yet completed email verification during registration.
 *
 * This storage holds players in a static map until their verification is complete.
 * The map uses lowercase usernames as keys to ensure case-insensitive access.
 */
public class TemporaryPlayerStorage {
    /**
     * A map storing unverified players using lowercase usernames as keys.
     */
    private static Map<String, Player> unverifiedPlayers = new HashMap<>();

    /**
     * Adds a player to the temporary unverified player storage.
     *
     * @param username the username of the player (case-insensitive)
     * @param player the Player object to store
     */
    public static void addPlayer(String username, Player player) {
        unverifiedPlayers.put(username.toLowerCase(), player);
    }

    /**
     * Checks if a given username exists in the temporary storage.
     *
     * @param username the username to check (case-insensitive)
     * @return true if the username exists, false otherwise
     */
    public static boolean findUsername(String username){
        return unverifiedPlayers.containsKey(username.toLowerCase());
    }

    /**
     * Retrieves a player from the temporary storage by username.
     *
     * @param username the username of the player to retrieve (case-insensitive)
     * @return the Player object associated with the username, or null if not found
     */
    public static Player getPlayer(String username) {
        return unverifiedPlayers.get(username.toLowerCase());
    }

    /**
     * Removes a player from the temporary storage.
     *
     * @param username the username of the player to remove (case-insensitive)
     */
    public static void removePlayer(String username) {
        unverifiedPlayers.remove(username.toLowerCase());
    }

    /**
     * Clears all entries from the temporary player storage.
     */
    public static void clear() {
        unverifiedPlayers.clear();
    }
}
