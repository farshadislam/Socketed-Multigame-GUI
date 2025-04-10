package org.seng.leaderboard_matchmaking.matchmaking;

// Import necessary libraries
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Import game-specific classes from elsewhere
import org.seng.networking.leaderboard_matchmaking.GameType;
import org.seng.leaderboard_matchmaking.*;
//import org.seng.networking.*;
import org.seng.authentication.Player;
import org.seng.networking.Match;
import org.seng.networking.NetworkingManager;
import org.seng.leaderboard_matchmaking.*;


    // this class is the matchmaking system that pairs players into matches.
// it handles all game queues and creates Match objects when two players are ready.
// the idea is that you instantiate this class once at the start of your server session,
// and then call startMatchmakingLoop() to run the background task that continuously checks the queues.
    public class QueueMatchMaker {

        // Total number of ranks available (for example, Bronze to Grandmaster = 7 ranks)
        private static final int RANK_COUNT = 7;

        // Each rank has 2 queue pairs (we will randomly place players into one pair or the other)
        private static final int QUEUE_PAIRS_PER_RANK = 2;

        // Main storage for all queues: maps each GameType to a map that holds, per (rank, pair),
        // an array of 2 queues (Queue A and Queue B). The keys in this inner map are computed via getPairKey().
        private final Map<GameType, Map<Integer, Queue<Player>[]>> queuesPerGame;

        // Random instance used for load balancing and random decisions when placing players in queues
        private final Random random;

        // ------------------------------
        // ===== CONSTRUCTOR ============
        // ------------------------------

        public QueueMatchMaker() {

            // STEP 1: Initialize the top-level data structure that stores queues for all game types.
            // Each GameType (e.g., TicTacToe, Connect4, Checkers) maps to its own set of rank-based queue pairs.
            queuesPerGame = new HashMap<>();

            // STEP 2: Create a random number generator for later use (used when randomly assigning players to queues).
            random = new Random();

            // STEP 3: Loop through each supported GameType (assumed to be defined in an enum).
            for (GameType game : GameType.values()) {

                // Create a new map that will hold the queue pairs for each rank in this game.
                Map<Integer, Queue<Player>[]> gameQueues = new HashMap<>();

                // STEP 4: For each of the 7 ranks (e.g., Bronze = 1, …, Grandmaster = 7)
                for (int rank = 1; rank <= RANK_COUNT; rank++) {

                    // STEP 5: Each rank has 2 queue pairs (pairIndex 0 and 1).
                    // Use getPairKey(rank, pairIndex) to compute a unique key for each pair.
                    for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
                        int key = getPairKey(rank, pairIndex);

                        // STEP 6: Each pair consists of 2 queues — Queue A and Queue B.
                        // We initialize an array of two empty queues.
                        Queue<Player>[] pair = new Queue[2];
                        for (int i = 0; i < 2; i++) {
                            pair[i] = new Queue<>();
                        }

                        // Store the queue pair in the map using the computed key.
                        gameQueues.put(key, pair);
                    }
                }

                // STEP 7: Store the full rank-pair queue map under this game type in our global map.
                queuesPerGame.put(game, gameQueues);
            }
        }

        // ------------------------------
        // ===== ADD PLAYER TO QUEUE =====
        // ------------------------------

        /**
         * Adds a player to the matchmaking queue for a specific game based on their rank.
         * This method also starts a personal queue-hopping thread that will periodically
         * switch the player between queue pairs if they remain unmatched.
         *
         * @param player the player to be added to the matchmaking system.
         * @param game   the GameType the player is queuing for.
         */
        public void addPlayerToQueue(Player player, GameType game) {
            // STEP 1: Obtain the player's rank for this game.
            // The player's rank is determined via their game stats (converted to a 1-based index).
            GeneralStats stats;
            if (game == GameType.CONNECT4) {
                stats = player.getConnect4Stats();
            } else if (game == GameType.TICTACTOE) {
                stats = player.getTicTacToeStats();
            } else {
                stats = player.getCheckersStats();
            }
            int rank = stats.getRank().ordinal() + 1;

            // STEP 2: Retrieve the two queue pairs for this rank.
            // Pair 0 uses key = rank, Pair 1 uses key = rank + 7.
            Queue<Player>[] pair0 = queuesPerGame.get(game).get(getPairKey(rank, 0));
            Queue<Player>[] pair1 = queuesPerGame.get(game).get(getPairKey(rank, 1));

            // STEP 3: Count the total players in each pair.
            int pair0Count = pair0[0].size() + pair0[1].size();
            int pair1Count = pair1[0].size() + pair1[1].size();

            // STEP 4: Decide which queue pair to assign the player to,
            // based on load balancing and quick matchmaking if one pair has exactly one player.
            Queue<Player>[] chosenPair;
            int chosenPairIndex;
            if (pair0Count == 0 && pair1Count == 0) {
                chosenPair = random.nextBoolean() ? pair0 : pair1;
                chosenPairIndex = (chosenPair == pair0) ? 0 : 1;
            } else if (pair0Count == 1 && pair1Count != 1) {
                chosenPair = pair0;
                chosenPairIndex = 0;
            } else if (pair1Count == 1 && pair0Count != 1) {
                chosenPair = pair1;
                chosenPairIndex = 1;
            } else if ((pair0Count == 0 && pair1Count >= 2) || (pair1Count == 0 && pair0Count >= 2)) {
                chosenPair = random.nextBoolean() ? pair0 : pair1;
                chosenPairIndex = (chosenPair == pair0) ? 0 : 1;
            } else {
                chosenPair = random.nextBoolean() ? pair0 : pair1;
                chosenPairIndex = (chosenPair == pair0) ? 0 : 1;
            }

            // STEP 5: Within the chosen pair, decide which individual queue (A or B) to add the player into.
            Queue<Player> queueA = chosenPair[0];
            Queue<Player> queueB = chosenPair[1];
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

            // STEP 6: Start the personal pair hopper thread for this player (60s default interval).
            startPairHopper(player, game, chosenPairIndex);
        }

        // ------------------------------
        // ===== REMOVE PLAYER FROM QUEUE =====
        // ------------------------------

        /**
         * Removes a player from all matchmaking queues for a given game.
         *
         * @param player the player to remove.
         * @param game   the GameType the player was queued for.
         */
        public void removePlayer(Player player, GameType game) {
            // Determine player's rank (1-based).
            GeneralStats stats;
            if (game == GameType.CONNECT4) {
                stats = player.getConnect4Stats();
            } else if (game == GameType.TICTACTOE) {
                stats = player.getTicTacToeStats();
            } else {
                stats = player.getCheckersStats();
            }
            int rank = stats.getRank().ordinal() + 1;

            // For each of the two queue pairs (Pair 0 and Pair 1) for this rank, remove the player.
            for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
                Queue<Player>[] pair = queuesPerGame.get(game).get(getPairKey(rank, pairIndex));
                for (Queue<Player> q : pair) {
                    q.remove(player);
                }
            }
        }

        // ------------------------------
        // ===== MATCHMAKING LOOP =====
        // ------------------------------

        /**
         * Starts a scheduled matchmaking loop that runs every 5 seconds.
         * This loop iterates over all game modes and all rank queues to find pairs of players
         * and create a match for them.
         */
        public void startMatchmakingLoop() {
            // create a single-threaded scheduler
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            // schedule the matchmaking task to run every 5 seconds, starting immediately
            scheduler.scheduleAtFixedRate(() -> {
                // for each game type in our queues
                for (GameType game : GameType.values()) {
                    Map<Integer, Queue<Player>[]> gameQueues = queuesPerGame.get(game);
                    // iterate through each rank (1 through 7)
                    for (int rank = 1; rank <= RANK_COUNT; rank++) {
                        // for each pair index (0 and 1)
                        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
                            Queue<Player>[] pair = gameQueues.get(getPairKey(rank, pairIndex));
                            Queue<Player> queueA = pair[0];
                            Queue<Player> queueB = pair[1];

                            // if either queue is empty, no match can be formed; skip this pair
                            if (queueA.isEmpty() || queueB.isEmpty()) continue;

                            // peek at the first player in each queue
                            Player player1 = queueA.peek();
                            Player player2 = queueB.peek();

                            if (player1 != null && player2 != null) {
                                // dequeue both players for a match
                                player1 = queueA.dequeue();
                                player2 = queueB.dequeue();

                                // create a new match instance
                                Match match = new Match(player1, player2, game);

                                // store the match in each player's record
                                player1.setCurrentMatch(match);
                                player2.setCurrentMatch(match);

                                // add the match to the global pool (if used for tracking)
                                Match.addAvailableMatch(match);

                                // if both players are connected via sockets, hook up their handlers
                                if (player1.getSocketHandler() != null && player2.getSocketHandler() != null) {
                                    player1.getSocketHandler().setOpponent(player2.getSocketHandler());
                                    player2.getSocketHandler().setOpponent(player1.getSocketHandler());
                                }

                                // notify players that they have been matched
                                NetworkingManager.getInstance().notifyPlayersMatched(player1, player2, match);
                            }
                        }
                    }
                }
            }, 0, 5, TimeUnit.SECONDS); // no initial delay; run every 5 seconds
        }

        // ------------------------------
        // ===== PLAYER QUEUE PAIR HOPPER =====
        // ------------------------------

        /**
         * Starts a dedicated background thread that periodically moves a player between the two queue pairs
         * (Pair 0 and Pair 1) for their rank if they remain unmatched. This helps reduce waiting times.
         *
         * @param player           The player entering matchmaking.
         * @param game             The game the player is queuing for.
         * @param initialPairIndex The initial pair index (0 or 1) where the player is placed.
         */
        public void startPairHopper(Player player, GameType game, int initialPairIndex) {
            startPairHopperGeneral(player, game, initialPairIndex, 60000); // default is 60 seconds
        }

        /**
         * General method to start the pair hopper with a customizable interval.
         *
         * @param player           The player.
         * @param game             The game type.
         * @param initialPairIndex The initial queue pair index.
         * @param milliSeconds     The interval in milliseconds.
         */
        public void startPairHopperGeneral(Player player, GameType game, int initialPairIndex, long milliSeconds) {
            new Thread(() -> {
                // Get player's rank (1-based) for this game.
                GeneralStats stats;
                if (game == GameType.CONNECT4) {
                    stats = player.getConnect4Stats();
                } else if (game == GameType.TICTACTOE) {
                    stats = player.getTicTacToeStats();
                } else {
                    stats = player.getCheckersStats();
                }
                int rank = stats.getRank().ordinal() + 1;

                int currentPair = initialPairIndex; // keep track of current queue pair

                try {
                    while (true) {
                        Thread.sleep(milliSeconds); // wait for the defined interval

                        // if the player is no longer in any queue, exit the thread
                        if (!isPlayerInAnyPair(player, game, rank)) break;

                        // toggle to the opposite queue pair (0 <-> 1)
                        int newPair = (currentPair + 1) % 2;

                        // remove the player from the current queue pair
                        Queue<Player>[] currentPairQueues = queuesPerGame.get(game).get(getPairKey(rank, currentPair));
                        currentPairQueues[0].remove(player);
                        currentPairQueues[1].remove(player);

                        // get the target pair queues and add the player to the less populated one
                        Queue<Player>[] targetPair = queuesPerGame.get(game).get(getPairKey(rank, newPair));
                        Queue<Player> queueA = targetPair[0];
                        Queue<Player> queueB = targetPair[1];
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

                        // log the hop for debugging purposes
                        System.out.println("Player " + player.getUsername() +
                                " hopped from Pair " + currentPair + " to Pair " + newPair +
                                " (Game: " + game + ", Rank: " + rank + ")");

                        currentPair = newPair;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start(); // start the thread immediately
        }

        // ------------------------------
        // ===== CHECK IF PLAYER IS QUEUED =====
        // ------------------------------

        /**
         * Checks if the given player is present in any of the two queue pairs for the specified rank.
         *
         * @param player The player to check.
         * @param game   The game type.
         * @param rank   The player's rank (1-based).
         * @return true if the player exists in any queue, false otherwise.
         */
        private boolean isPlayerInAnyPair(Player player, GameType game, int rank) {
            for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
                Queue<Player>[] pair = queuesPerGame.get(game).get(getPairKey(rank, pairIndex));
                if (pair != null && (pair[0].contains(player) || pair[1].contains(player))) {
                    return true;
                }
            }
            return false;
        }

        // ------------------------------
        // ===== DEBUG: PRINT QUEUE STATUS =====
        // ------------------------------

        /**
         * Prints the status of all queues for the specified game.
         * <p>
         * This helps visualize the number of players waiting in each queue pair.
         *
         * @param game The game type.
         */
        public void printQueueStatus(GameType game) {
            Map<Integer, Queue<Player>[]> gameQueues = queuesPerGame.get(game);
            System.out.println("Queue Status for " + game.name());
            for (int rank = 1; rank <= RANK_COUNT; rank++) {
                Queue<Player>[] pair0 = gameQueues.get(getPairKey(rank, 0));
                Queue<Player>[] pair1 = gameQueues.get(getPairKey(rank, 1));
                System.out.println("Rank " + rank + " - Pair 0 Queue A: " + pair0[0].size() +
                        " | Queue B: " + pair0[1].size());
                System.out.println("Rank " + rank + " - Pair 1 Queue A: " + pair1[0].size() +
                        " | Queue B: " + pair1[1].size());
            }
        }

        // ------------------------------
        // ===== GET UNIQUE KEY FOR QUEUE PAIR =====
        // ------------------------------

        /**
         * Computes a unique key for each queue pair based on the player's rank and pair index.
         * <p>
         * For ranks 1 to 7:
         * - Pair 0 maps directly to keys 1-7.
         * - Pair 1 maps to keys 8-14 (i.e., rank + 7).
         *
         * @param rank      the player's rank (1-based).
         * @param pairIndex 0 for the first queue pair, 1 for the second.
         * @return a unique integer key for that queue pair.
         */
        private static int getPairKey(int rank, int pairIndex) {
            return rank + (pairIndex * RANK_COUNT);
        }

        // ------------------------------
        // ===== GETTERS FOR TESTING =====
        // ------------------------------

        public Map<GameType, Map<Integer, Queue<Player>[]>> getQueuesPerGame() {
            return queuesPerGame;
        }
    }
