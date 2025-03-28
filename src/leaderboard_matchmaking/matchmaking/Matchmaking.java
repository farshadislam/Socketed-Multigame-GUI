package leaderboard_matchmaking.matchmaking;

import java.util.*;

public class Matchmaking {

    // Total number of ranks (e.g., BRONZE to GRANDMASTER)
    private static final int RANK_COUNT = 7;

    // Number of queue pairs per rank (2 per rank, each with 2 queues => 28 total queues)
    private static final int QUEUE_PAIRS_PER_RANK = 2;

    // Map of queues for each rank (1-based rank index)
    // Each rank contains 2 queues stored in an array: queues.get(rank)[0] and queues.get(rank)[1]
    private final Map<Integer, Queue<Player>[]> queues;

    // Random generator for pair/queue selection
    private final Random random;

    // Constructor initializes queues for each rank
    public Matchmaking() {
        queues = new HashMap<>();
        random = new Random();

        // Create 2 queues for each rank
        for (int rank = 1; rank <= RANK_COUNT; rank++) {
            Queue<Player>[] pair = new Queue[QUEUE_PAIRS_PER_RANK];
            for (int i = 0; i < QUEUE_PAIRS_PER_RANK; i++) {
                pair[i] = new Queue<>();
            }
            queues.put(rank, pair);
        }
    }

}
