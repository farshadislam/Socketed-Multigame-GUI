package main.java.org.seng.leaderboard_matchmaking;

import main.java.org.seng.authentication.Player;

import java.util.ArrayList;
import java.util.List;

public class Last5Matches {


    // Main 2D ArrayList holding last 5 matches, each match is another internal ArrayList holding gametype and player
    // Note, the oldest match is at index 0, and latest match is index 4, because we append/add new matches
    private final List<List<Object>> matchHistory;

    public Last5Matches() {
        this.matchHistory = new ArrayList<>();
    }

    /**
     * Adds a new match to the history.
     * If the list is full (5), it removes the oldest match before adding.
     *
     * @param gameType The type of the game played.
     * @param opponent The opponent Player object.
     */
    public void update(GameType gameType, Player opponent) {
        int MAX_SIZE = 5;
        if (matchHistory.size() == MAX_SIZE) {
            matchHistory.remove(0); // Remove oldest match
        }
        List<Object> newMatch = new ArrayList<>();
        newMatch.add(gameType);
        newMatch.add(opponent);
        matchHistory.add(newMatch);
    }

    /**
     * Gets the full match history as a List of List<Object>.
     * Each inner list contains [GameType, Player].
     *
     * @return The match history.
     */
    public List<List<Object>> getMatchHistory() {
        return new ArrayList<>(matchHistory); // Defensive copy
    }

    /**
     * Gets the match at a specific index.
     *
     * @param index Index from 0 to matchHistory.size() - 1
     * @return The match list at that index.
     */
    public List<Object> getMatchAt(int index) {
        if (index < 0 || index >= matchHistory.size()) {
            throw new IndexOutOfBoundsException("Invalid match index.");
        }
        return new ArrayList<>(matchHistory.get(index));
    }

    /**
     * Clears all stored matches.
     */
    public void clearHistory() {
        matchHistory.clear();
    }
}

