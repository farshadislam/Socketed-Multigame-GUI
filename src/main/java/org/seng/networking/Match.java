package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;
import java.util.*;
import org.seng.authentication.Player;

import java.io.*;
/**
 * this class represents one single match between 2 players
 * doesn't actually run the game logic anymore — that's handled somewhere else (like the GUI/game logic package)
 * this just stores match info: who's playing, what game, and if the match is usable
 */
public class Match {

    private final String matchID;           // unique id for this match (so we can reference it if needed)
    private final Player player1;           // player 1 — the one who joined or got matched first
    private final Player player2;           // player 2 — the second one added in
    private final GameType gameType;        // what game is being played in this match
    private boolean isReady;                // signals if this match is ready to be used (like hasn't already been consumed)

    // this is our global pool of matches — like a waiting bin
    private static final Queue<Match> availableMatches = new LinkedList<>();

    // when we create a new match, we give it 2 players and a game type, and it gets an auto ID
    public Match(Player player1, Player player2, GameType gameType) {
        this.matchID = UUID.randomUUID().toString(); // randomly generate a match id
        this.player1 = player1;
        this.player2 = player2;
        this.gameType = gameType;
        this.isReady = true; // by default, a new match is ready unless we mark it not ready
    }

    // these are basic getters just to grab info from this match
    public String getMatchID() { return matchID; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public GameType getGameType() { return gameType; }
    public boolean isReady() { return isReady; }

    // we can manually flip this to not ready if it already got used or we don't wanna reuse it again
    public void markAsNotReady() { this.isReady = false; }

    // this adds a match into the global match pool — which will be grabbed by the next person who joins
    public static void addAvailableMatch(Match match) {
        availableMatches.offer(match); // this adds to the end of the queue
        System.out.println("Match added to the queue. Match ID: " + match.getMatchID());
    }
}
