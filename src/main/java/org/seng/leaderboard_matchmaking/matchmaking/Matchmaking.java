package org.seng.leaderboard_matchmaking.matchmaking;

// Import necessary libraries
import java.util.*;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Import game-specific classes
import leaderboard_matchmaking.*;


public class Matchmaking {

    // Total number of ranks (e.g., Bronze to Grandmaster)
    private static final int RANK_COUNT = 7;

    // Each rank has 2 queue pairs
    private static final int QUEUE_PAIRS_PER_RANK = 2;

    // Main storage for all queues: GameType -> (Rank + PairIndex) -> 2 Queues [Queue A and B]
    private final Map<GameType, Map<Integer, java.util.Queue<Player>[]>> queuesPerGame;

    // Random instance used for decisions like choosing queues randomly
    private final Random random;

    // ============================
    // ===== CONSTRUCTOR ==========
    // ============================

    public Matchmaking() {

        // ==============================================================
        // STEP 1: Initialize the top-level data structure that stores
        // queues for all games. Each game type (e.g., TicTacToe, Connect4)
        // maps to its own set of rank-based queue pairs.
        // ==============================================================
        queuesPerGame = new HashMap<>();

        // ==============================================================
        // STEP 2: Create a random number generator for later use
        // (used when randomly placing players into queues).
        // ==============================================================
        random = new Random();

        // ==============================================================
        // STEP 3: Loop through each supported game type.
        // GameType is likely an enum with values like TIC_TAC_TOE, CONNECT4, etc.
        // ==============================================================
        for (GameType game : GameType.values()) {

            // Create a new map to hold queues for all ranks within this game
            Map<Integer, java.util.Queue<Player>[]> gameQueues = new HashMap<>();

            // ==============================================================
            // STEP 4: For each of the 7 ranks (e.g., Bronze to Grandmaster),
            // we will initialize 2 queue pairs per rank.
            // ==============================================================
            for (int rank = 1; rank <= RANK_COUNT; rank++) {

                // ==============================================================
                // STEP 5: Each rank has 2 queue pairs: pairIndex = 0 and 1.
                // We generate a unique key for each pair using getPairKey(),
                // which ensures they don’t overwrite each other (rank + 0 or +7).
                // ==============================================================
                for (int pairIndex = 0; pairIndex < 2; pairIndex++) {

                    // Compute a unique key for this (rank, pairIndex) combo
                    int key = getPairKey(rank, pairIndex);

                    // ==============================================================
                    // STEP 6: Each pair consists of 2 queues — Queue A and Queue B.
                    // We initialize both queues and store them in an array.
                    // ==============================================================
                    java.util.Queue<Player>[] pair = new java.util.Queue[2];

                    for (int i = 0; i < 2; i++) {
                        pair[i] = new java.util.Queue<>();
                    }

                    // Store the queue pair (A and B) in the rank map using the key
                    gameQueues.put(key, pair);
                }
            }

            // ==============================================================
            // STEP 7: Once all ranks and pairs are initialized for the game,
            // store the full rank-pair queue map under this game in the global map.
            // ==============================================================
            queuesPerGame.put(game, gameQueues);
        }
    }


    // ==============================
    // ===== ADD PLAYER TO QUEUE ====
    // ==============================

    /**
     * Adds a player to one of the queues based on their game-specific rank,
     * load balancing logic, and starts their personal queue-hopping thread.
     */
    public void addPlayerToQueue(Player player, GameType game) {

        // ==============================================================
        // STEP 1: Get the player's rank for this game.
        // The rank enum is 0-indexed, so we shift it to a 1-based index.
        // ==============================================================
        int rank = player.getRank(game).ordinal() + 1;

        // ==============================================================
        // STEP 2: Get the two queue pairs for this rank in this game.
        // Each pair contains two queues: A and B.
        // Pair 0 has key = rank
        // Pair 1 has key = rank + 7 (based on our getPairKey helper).
        // ==============================================================
        java.util.Queue<Player>[] pair0 = queuesPerGame.get(game).get(getPairKey(rank, 0));
        java.util.Queue<Player>[] pair1 = queuesPerGame.get(game).get(getPairKey(rank, 1));

        // ==============================================================
        // STEP 3: Count how many players are in each pair.
        // This will help us decide where to place the player.
        // ==============================================================
        int pair0Count = pair0[0].size() + pair0[1].size();
        int pair1Count = pair1[0].size() + pair1[1].size();

        // ==============================================================
        // STEP 4: Decide which queue pair to assign the player to.
        // This logic prioritizes active matchmaking and balancing.
        // We also track the chosen pair index for pair-hopping later.
        // ==============================================================
        java.util.Queue<Player>[] chosenPair;
        int chosenPairIndex;

        if (pair0Count == 0 && pair1Count == 0) {
            // Both pairs are empty → random choice
            chosenPair = random.nextBoolean() ? pair0 : pair1;
            chosenPairIndex = (chosenPair == pair0) ? 0 : 1;

        } else if (pair0Count == 1 && pair1Count != 1) {
            // Pair 0 has exactly one player → match quickly
            chosenPair = pair0;
            chosenPairIndex = 0;

        } else if (pair1Count == 1 && pair0Count != 1) {
            // Pair 1 has exactly one player → match quickly
            chosenPair = pair1;
            chosenPairIndex = 1;

        } else if ((pair0Count == 0 && pair1Count >= 2) || (pair1Count == 0 && pair0Count >= 2)) {
            // One is empty and one has 2+ players → random choice
            chosenPair = random.nextBoolean() ? pair0 : pair1;
            chosenPairIndex = (chosenPair == pair0) ? 0 : 1;

        } else {
            // All other conditions → random fallback
            chosenPair = random.nextBoolean() ? pair0 : pair1;
            chosenPairIndex = (chosenPair == pair0) ? 0 : 1;
        }

        // ==============================================================
        // STEP 5: Decide which queue (A or B) within the chosen pair to enter.
        // Prefer the queue with fewer players to balance load.
        // If tied, pick randomly.
        // ==============================================================
        java.util.Queue<Player> queueA = chosenPair[0];
        java.util.Queue<Player> queueB = chosenPair[1];

        if (queueA.size() < queueB.size()) {
            queueA.enqueue(player);

        } else if (queueB.size() < queueA.size()) {
            queueB.enqueue(player);

        } else {
            if (random.nextBoolean()) {
                queueA.enqueue(player);
            } else {
                queueB.enqueue(player);
            }
        }

        // ==============================================================
        // STEP 6: Start the queue-hopping timer for this specific player.
        // This thread allows them to switch between pair 0 and pair 1
        // every 60 seconds if they remain unmatched.
        // ==============================================================
        startPairHopper(player, game, chosenPairIndex);
    }



    // ================================
    // ===== REMOVE PLAYER FROM QUEUE =
    // ================================

    /**
     * Removes a player from matchmaking for a given game.
     * This method ensures the player is removed from any queue they may be in.
     */
    public void removePlayer(Player player, GameType game) {

        // ========================================================
        // Step 1: Determine the player's rank for this specific game
        // The rank enum is 0-indexed, so we convert to 1-based indexing
        // ========================================================
        int rank = player.getRank(game).ordinal() + 1;

        // ========================================================
        // Step 2: Iterate through both queue pairs (Pair 0 and Pair 1)
        // Each rank has 2 pairs → Pair 0 (key = rank), Pair 1 (key = rank + 7)
        // ========================================================
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {

            // Use the helper method to get the correct key for this pair
            java.util.Queue<Player>[] pair = queuesPerGame.get(game).get(getPairKey(rank, pairIndex));

            // ========================================================
            // Step 3: Iterate through the 2 queues in this pair (Queue A and Queue B)
            // Remove the player if present in either queue
            // Note: remove() does nothing if the player is not found
            // ========================================================
            for (java.util.Queue<Player> q : pair) {
                q.remove(player);
            }
        }
    }


    // ===============================
    // ===== MATCHMAKING LOOP ========
    // ===============================

    /**
     * Starts a scheduled matchmaking loop that runs every 5 seconds.
     *
     * The loop:
     * - Iterates through each game (TicTacToe, Connect4, Checkers)
     * - Then iterates through each rank (1 to 7)
     * - Then checks both queue pairs (Pair 0 and Pair 1)
     * - If the heads of both queues in a pair are populated, it dequeues them and starts a match
     */
    public void startMatchmakingLoop() {

        // =========================
        // SET UP PERIODIC MATCHMAKING THREAD
        // =========================

        // Create a scheduler that runs on a single thread
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule the matchmaking task to run every 5 seconds, starting immediately
        scheduler.scheduleAtFixedRate(() -> {

            // ============================================
            // FOR EACH GAME MODE (TicTacToe, Connect4, etc)
            // ============================================
            for (GameType game : GameType.values()) {

                // Retrieve the queue map for the current game
                Map<Integer, java.util.Queue<Player>[]> gameQueues = queuesPerGame.get(game);

                // =====================
                // FOR EACH RANK (1 to 7)
                // =====================
                for (int rank = 1; rank <= RANK_COUNT; rank++) {

                    // =================================
                    // FOR EACH PAIR INDEX (0 and 1 only)
                    // =================================
                    for (int pairIndex = 0; pairIndex < 2; pairIndex++) {

                        // Retrieve the correct pair using rank and pairIndex (via getPairKey)
                        java.util.Queue<Player>[] pair = gameQueues.get(getPairKey(rank, pairIndex));

                        // Separate the two queues in this pair: Queue A and Queue B
                        java.util.Queue<Player> queueA = pair[0];
                        java.util.Queue<Player> queueB = pair[1];

                        // Skip this pair if either queue is currently empty (no match possible)
                        if (queueA.isEmpty() || queueB.isEmpty()) continue;

                        // Peek at the front of both queues (players waiting the longest)
                        Player player1 = queueA.peek();
                        Player player2 = queueB.peek();

                        // If both queues have players (not null), proceed to match them
                        if (player1 != null && player2 != null) {

                            // Remove both players from their respective queues
                            player1 = queueA.dequeue();
                            player2 = queueB.dequeue();

                            // Request an available match (from external logic/class)
                            Match match = Match.getAvailableMatch();  // Note: must be implemented by networking team

                            // Link both players to the retrieved match (assign them)
                            joinMatch(player1, player2, match);       // Note: must also be implemented externally
                        }
                    }
                }
            }

        }, 0, 5, TimeUnit.SECONDS); // Start immediately (0s delay), repeat every 5 seconds
    }


    // ====================================
    // ===== PLAYER QUEUE PAIR HOPPER =====
    // ====================================

    /**
     * Starts a background thread that periodically moves a player between two queue pairs
     * in an attempt to find a match more efficiently if they’ve been waiting too long.
     *
     * The player will remain in matchmaking, alternating between queue pairs every 60 seconds
     * until they are removed from all queues (i.e., matched or cancelled).
     *
     * @param player            The player entering matchmaking.
     * @param game              The specific game they are queuing for.
     * @param initialPairIndex  The queue pair they were initially placed in (0 or 1).
     */
    public void startPairHopper(Player player, GameType game, int initialPairIndex) {

        // Launch a new thread dedicated to this specific player
        new Thread(() -> {

            // Determine the player’s rank for the game (1-based index)
            int rank = player.getRank(game).ordinal() + 1;

            // Keep track of which pair the player is currently in (starts from initial)
            int currentPair = initialPairIndex;

            try {
                // Keep hopping indefinitely until the player is no longer in any matchmaking queue
                while (true) {

                    // Wait for 60 seconds before attempting the next hop
                    Thread.sleep(60000);

                    // If the player is no longer in any queue for this rank/game, stop the thread
                    if (!isPlayerInAnyPair(player, game, rank)) break;

                    // Calculate the opposite pair to hop to (toggles between 0 and 1)
                    int newPair = (currentPair + 1) % 2;

                    // Access the current queue pair (based on current rank and pair index)
                    java.util.Queue<Player>[] currentPairQueues = queuesPerGame.get(game).get(getPairKey(rank, currentPair));

                    // Remove the player from both queues in their current pair (A and B)
                    currentPairQueues[0].remove(player);
                    currentPairQueues[1].remove(player);

                    // Access the new pair the player will hop into
                    java.util.Queue<Player>[] targetPair = queuesPerGame.get(game).get(getPairKey(rank, newPair));

                    // Retrieve the two queues (A and B) in the new pair
                    java.util.Queue<Player> queueA = targetPair[0];
                    java.util.Queue<Player> queueB = targetPair[1];

                    // Insert the player into the less populated queue within the new pair
                    if (queueA.size() < queueB.size()) {
                        queueA.enqueue(player);
                    } else if (queueB.size() < queueA.size()) {
                        queueB.enqueue(player);
                    } else {
                        // If both queues are equal, pick randomly
                        if (random.nextBoolean()) {
                            queueA.enqueue(player);
                        } else {
                            queueB.enqueue(player);
                        }
                    }

                    // Log the switch for debugging or monitoring
                    System.out.println("Player " + player.getUsername() +
                            " hopped from Pair " + currentPair + " to Pair " + newPair +
                            " (Game: " + game + ", Rank: " + rank + ")");

                    // Update current pair for the next cycle
                    currentPair = newPair;
                }

            } catch (InterruptedException e) {
                // Catch any thread interruption exceptions and print stack trace
                e.printStackTrace();
            }

        }).start(); // Start the hopping thread immediately
    }


    // ============================================================
    // ===== CHECK IF PLAYER EXISTS IN ANY QUEUE PAIR FOR RANK ====
    // ============================================================

    /**
     * Checks if the given player is still present in any of the queue pairs (Pair 0 or Pair 1)
     * for their current rank in the specified game.
     *
     * This is mainly used by the Pair Hopper system to determine whether the player is still
     * actively in matchmaking, and if so, it should continue running their hopping logic.
     *
     * @param player The player to search for in the queues.
     * @param game   The game type the player is queuing for.
     * @param rank   The player's rank (1-based).
     * @return true if the player is found in any of the queues in either pair; false otherwise.
     */
    private boolean isPlayerInAnyPair(Player player, GameType game, int rank) {

        // Iterate through both queue pairs for this rank: Pair 0 and Pair 1
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {

            // Get the queue pair (i.e., two queues: A and B) using the composite key (rank + offset)
            java.util.Queue<Player>[] pair = queuesPerGame.get(game).get(getPairKey(rank, pairIndex));

            // Check if the player exists in either of the two queues in this pair
            if (pair != null && (pair[0].contains(player) || pair[1].contains(player))) {
                // If found in Queue A or Queue B, return true immediately
                return true;
            }
        }

        // If not found in either pair, return false
        return false;
    }


    // ===================================================
    // ====== PRINT DEBUGGING INFO: QUEUE STATUS =========
    // ===================================================

    /**
     * Prints the status of all queues for a given game.
     *
     * For each rank (1 to 7), this method displays the number of players
     * currently waiting in both Queue A and Queue B for each of the two queue pairs (Pair 0 and Pair 1).
     * This helps visualize the distribution of players across the matchmaking system.
     *
     * @param game The game type (e.g., TicTacToe, Connect4, Checkers) whose queues are being inspected.
     */
    public void printQueueStatus(GameType game) {

        // Get the full queue structure for the selected game
        // This map contains keys from 1–14 representing queue pairs (7 per game, 2 pairs per rank)
        Map<Integer, java.util.Queue<Player>[]> gameQueues = queuesPerGame.get(game);

        // Print header to indicate which game's queues we're looking at
        System.out.println("Queue Status for " + game.name());

        // Iterate through each of the 7 ranks (from Bronze = 1 to Grandmaster = 7)
        for (int rank = 1; rank <= RANK_COUNT; rank++) {

            // Retrieve Pair 0 for this rank (key will be 1–7)
            java.util.Queue<Player>[] pair0 = gameQueues.get(getPairKey(rank, 0));

            // Retrieve Pair 1 for this rank (key will be 8–14)
            Queue<Player>[] pair1 = gameQueues.get(getPairKey(rank, 1));

            // Print the number of players in Queue A and Queue B of Pair 0
            System.out.println(
                    "Rank " + rank + " - Pair 0 Queue A: " + pair0[0].size() +
                            " | Queue B: " + pair0[1].size()
            );

            // Print the number of players in Queue A and Queue B of Pair 1
            System.out.println(
                    "Rank " + rank + " - Pair 1 Queue A: " + pair1[0].size() +
                            " | Queue B: " + pair1[1].size()
            );
        }
    }




    // ====================================================
    // ===== GET KEY FOR A SPECIFIC QUEUE PAIR ============
    // ====================================================

    /**
     * Computes a unique key for each queue pair based on the player's rank and which pair (0 or 1) they belong to.
     *
     * Each rank (1 to 7) has two queue pairs:
     *    - Pair 0 maps directly to keys 1 through 7 (i.e., rank itself).
     *    - Pair 1 maps to keys 8 through 14 (i.e., rank + 7).
     *
     * This function ensures that each (rank, pairIndex) combination gets a unique integer key
     * so that all 14 pairs (7 ranks × 2 pairs) can be uniquely identified in a flat map structure.
     *
     * @param rank       The player's rank (1-based index, from Bronze = 1 to Grandmaster = 7)
     * @param pairIndex  Index of the pair: 0 for the first pair, 1 for the second pair within the same rank
     * @return An integer that uniquely identifies the specific queue pair within a rank
     */
    private static int getPairKey(int rank, int pairIndex) {
        // Multiply the pair index (0 or 1) by RANK_COUNT (7) to offset to the second set of keys (8–14 for pair 1)
        // Add the player's rank (1–7) to finalize the unique key for that queue pair
        return rank + (pairIndex * RANK_COUNT);
    }

}
