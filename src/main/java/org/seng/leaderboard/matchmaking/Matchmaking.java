package org.seng.leaderboard.matchmaking;
import org.seng.leaderboard.GameType;
import org.seng.authentication.Player;

import java.util.*;

public class Matchmaking {

    // Total number of ranks in the system (Bronze to Grandmaster)
    private static final int RANK_COUNT = 7;

    // Each rank has 2 queue pairs (Queue A and Queue B)
    private static final int QUEUE_PAIRS_PER_RANK = 2;

    // Main data structure to store queues per game type:
    // For each GameType (TicTacToe, Connect4, Checkers), we map:
    // Rank (1 to 7) -> An array of 2 queues representing Queue A and B
    private final Map<GameType, Map<Integer, Queue<Player>[]>> queuesPerGame;

    // Used for randomized decisions (e.g., which queue pair or queue to join)
    private final Random random;

    // Constructor initializes all queues for each game and rank
    public Matchmaking() {
        queuesPerGame = new HashMap<>();
        random = new Random();

        // For each game we support...
        for (GameType game : GameType.values()) {
            Map<Integer, Queue<Player>[]> gameQueues = new HashMap<>();

            // For each rank (Bronze = 1, ..., Grandmaster = 7)...
            for (int rank = 1; rank <= RANK_COUNT; rank++) {

                // Create an array of 2 queues (Queue A and Queue B)
                Queue<Player>[] pair = new Queue[QUEUE_PAIRS_PER_RANK];

                // Initialize both queues in the pair
                for (int i = 0; i < QUEUE_PAIRS_PER_RANK; i++) {
                    pair[i] = new Queue<>();
                }

                // Associate the pair of queues with the current rank
                gameQueues.put(rank, pair);
            }

            // Associate this game's full rank-based queue system with the game type
            queuesPerGame.put(game, gameQueues);
        }
    }

    /**
     * Adds a player to the appropriate matchmaking queue for the selected game.
     *
     * Logic:
     * - Get the player’s rank for the given game
     * - Get the corresponding pair of queues for that rank
     * - Randomly choose one of the two queues (A or B) to use as the “base”
     * - Between the two queues in that pair, insert the player into the one with fewer players
     * - If both queues are equal in size, pick one randomly
     */
    public void addPlayerToQueue(Player player, GameType game) {

        // Get player's rank for the selected game, convert to 1-based index
        int rank = player.getRank(game).ordinal() + 1;

        // Get the queue pair (array of two queues) for this rank in the selected game
        Queue<Player>[] pair = queuesPerGame.get(game).get(rank);

        // Randomly choose one queue in the pair to start comparing from
        int pairIndex = random.nextInt(2);
        Queue<Player> queueA = pair[pairIndex];
        Queue<Player> queueB = pair[(pairIndex + 1) % 2]; // The other queue in the pair

        // Compare sizes and add player to the queue with fewer players
        if (queueA.size() < queueB.size()) {
            queueA.enqueue(player); // Add to Queue A if it has fewer players
        }
        else if (queueB.size() < queueA.size()) {
            queueB.enqueue(player); // Add to Queue B if it has fewer players
        }
        else {
            // If both queues have the same number of players, pick randomly
            if (random.nextBoolean()) {
                queueA.enqueue(player);
            } else {
                queueB.enqueue(player);
            }
        }
    }

    /**
     * Removes a player from all queues for their current rank in the selected game.
     *
     * Useful when the player cancels matchmaking or disconnects before being matched.
     */
    public void removePlayer(Player player, GameType game) {
        // Get the player’s current rank for the specified game
        int rank = player.getRank(game).ordinal() + 1;

        // Get the queue pair (2 queues) for that rank
        Queue<Player>[] pair = queuesPerGame.get(game).get(rank);

        // Remove the player from both queues in the pair (if they exist in either)
        for (Queue<Player> q : pair) {
            q.remove(player);
        }
    }

    // ==========================================================
    // ========== MATCHMAKING & WAIT LOGIC COMING SOON ==========
    // GOING TO ADD IN MATCHMAKING, CONNECTING PLAYERS, STARTING MATCH, ETC,
    // AND ALSO MOVING QUEUES AFTER WAIT TOMORROW
    // ==========================================================

    /**
     * Prints the current queue status for a selected game.
     * Shows number of players in each queue (A and B) for each rank.
     */
    public void printQueueStatus(GameType game) {
        // Get the rank-based queue structure for the specified game
        Map<Integer, Queue<Player>[]> gameQueues = queuesPerGame.get(game);

        System.out.println("Queue Status for " + game.name());

        // Loop through each rank and display queue sizes
        for (int rank = 1; rank <= RANK_COUNT; rank++) {
            Queue<Player>[] pair = gameQueues.get(rank);
            System.out.println("Rank " + rank + " Queue A: " + pair[0].size() + " players");
            System.out.println("Rank " + rank + " Queue B: " + pair[1].size() + " players");
        }
    }
}
