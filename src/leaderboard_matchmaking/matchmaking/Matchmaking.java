package leaderboard_matchmaking.matchmaking;
import java.util.*;
import leaderboard_matchmaking.*;
import leaderboard_matchmaking.Rank;


public class Matchmaking {

    private static final int RANK_COUNT = 7;
    private static final int QUEUE_PAIRS_PER_RANK = 2;

    // Maps each game to its own set of rank queues
    private final Map<GameType, Map<Integer, Queue<Player>[]>> queuesPerGame;
    private final Random random;

    public Matchmaking() {
        queuesPerGame = new HashMap<>();
        random = new Random();

        // Initialize queues for each game type

        // 3 Games
        for (GameType game : GameType.values()) {
            Map<Integer, Queue<Player>[]> gameQueues = new HashMap<>();

            // 7 Ranks
            for (int rank = 1; rank <= RANK_COUNT; rank++) {

                // 2 Pairs
                Queue<Player>[] pair = new Queue[QUEUE_PAIRS_PER_RANK];

                for (int i = 0; i < QUEUE_PAIRS_PER_RANK; i++) {
                    pair[i] = new Queue<>();
                }
                gameQueues.put(rank, pair);
            }

            queuesPerGame.put(game, gameQueues);
        }
    }

    /**
     * Adds a player to the proper queue for the given game.
     * Uses the player's rank in that game, chooses a random pair,
     * and places the player in the less populated queue within that pair.
     */
    public void addPlayerToQueue(Player player, GameType game) {

        int rank = player.getRank(game).ordinal() + 1; // Convert Enum to 1-based rank
        Queue<Player>[] pair = queuesPerGame.get(game).get(rank);

        int pairIndex = random.nextInt(2); // Pick a pair randomly
        Queue<Player> queueA = pair[pairIndex];
        Queue<Player> queueB = pair[(pairIndex + 1) % 2];

        // Pick the queue with fewer players or random if equal
        // A is Smaller
        if (queueA.size() < queueB.size()) {
            queueA.enqueue(player);
        }
        // B is Smaller
        else if (queueB.size() < queueA.size()) {
            queueB.enqueue(player);
        }
        // Same size, randomly choose
        else {
            if (random.nextBoolean()) {
                queueA.enqueue(player);
            }
            else {
                queueB.enqueue(player);
            }
        }
    }

    /**
     * Removes a player from the correct queue based on game and rank.
     */
    public void removePlayer(Player player, GameType game) {
        int rank = player.getRank(game).ordinal() + 1;
        Queue<Player>[] pair = queuesPerGame.get(game).get(rank);
        for (Queue<Player> q : pair) {
            q.remove(player);
        }
    }

// GOING TO ADD IN MATCHMAKING, CONNECTING PLAYERS, STARTING MATCH, ETC, AND ALSO MOVING QUEUES AFTER WAIT TOMORROW

    /**
     * Prints the number of players in each queue of the specified game.
     */
    public void printQueueStatus(GameType game) {
        Map<Integer, Queue<Player>[]> gameQueues = queuesPerGame.get(game);

        System.out.println("Queue Status for " + game.name());
        for (int rank = 1; rank <= RANK_COUNT; rank++) {
            Queue<Player>[] pair = gameQueues.get(rank);
            System.out.println("Rank " + rank + " Queue A: " + pair[0].size() + " players");
            System.out.println("Rank " + rank + " Queue B: " + pair[1].size() + " players");
        }
    }
}