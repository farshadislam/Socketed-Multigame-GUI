package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;
import java.util.*;
import org.seng.authentication.Player;

/**
 * This is the matchmaking system that pairs players into matches.
 * It handles all game queues and creates Match objects when two players are ready.
 */
public class Matchmaking {

    private static Matchmaking instance; // singleton instance

    // holds a waiting queue for each game type
    private final Map<GameType, Queue<Player>> queues;

    private Matchmaking() {
        queues = new HashMap<>();

        // init empty queues for each supported game
        for (GameType game : GameType.values()) {
            queues.put(game, new LinkedList<>());
        }
    }

    // returns the one global matchmaking instance
    public static Matchmaking getInstance() {
        if (instance == null) {
            instance = new Matchmaking();
        }
        return instance;
    }

    public static javafx.css.Match joinMatch(org.seng.authentication.Player player1, org.seng.authentication.Player player2, org.seng.leaderboard_matchmaking.GameType game) {
        return null;
    }

    public static void connectPlayers(javafx.css.Match match) {
    }

    // this tries to add a player to a queue and match them if someone is waiting
    public Match joinQueue(Player player, GameType gameType) {
        Queue<Player> queue = queues.get(gameType);
        if (queue == null) return null;

        // if queue not empty, they match immediately
        if (!queue.isEmpty()) {
            Player opponent = queue.poll();
            Match match = new Match(opponent, player, gameType);

            // this stores match reference in both players
            opponent.setCurrentMatch(match);
            player.setCurrentMatch(match);

            // this also registers in global static queue in Match (for networking system)
            Match.addAvailableMatch(match);

            System.out.println("[MATCH] " + opponent.getUsername() + " vs " + player.getUsername());
            return match;
        } else {
            // if no one waiting, this goes to queue
            queue.offer(player);
            System.out.println("[QUEUE] " + player.getUsername() + " is now waiting for " + gameType);
            return null;
        }
    }
}
