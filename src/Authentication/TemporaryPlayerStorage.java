package Authentication;
import java.util.HashMap;
import java.util.Map;

public class TemporaryPlayerStorage {
    private static Map<String, Player> unverifiedPlayers = new HashMap<>();

    // Add a player to temporary storage
    public static void addPlayer(String username, Player player) {
        unverifiedPlayers.put(username, player);
    }

    // Get a player from temporary storage
    public static Player getPlayer(String username) {
        return unverifiedPlayers.get(username);
    }

    // Remove a player from temporary storage
    public static void removePlayer(String username) {
        unverifiedPlayers.remove(username);
    }
}

