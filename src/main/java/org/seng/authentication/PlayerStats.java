package org.seng.authentication;
import org.seng.leaderboard.GeneralStats;
import org.seng.leaderboard.checkersStats;
import org.seng.leaderboard.connect4Stats;
import org.seng.leaderboard.ticTacToeStats;
import org.seng.leaderboard.Rank;

import org.seng.leaderboard_matchmaking.Last5Matches;

import java.util.List;

public class PlayerStats {
    // defined instance variable
    private Player player;

    // created the constructor to initialize the player in the PlayerStats class
    public PlayerStats(Player player) {
        this.player = player;
    }

    public int getGamesPlayedForConnect4() {
        connect4Stats connectStats = player.getConnect4Stats();
        if(connectStats != null){
            return connectStats.getGamesPlayed();
        }
        return -1;
    }

    // created Methods for Connect4 Game
    public int getWinsForConnect4() {
        connect4Stats connectStats = player.getConnect4Stats();
        if(connectStats != null){
            return connectStats.get_wins();
        }
        return -1;
    }

    public int getLossesForConnect4() {
        connect4Stats connectStats = player.getConnect4Stats();
        if(connectStats != null){
            return connectStats.get_losses();
        }
        return -1;
    }

    public int getTiesForConnect4() {
        connect4Stats connectStats = player.getConnect4Stats();
        if(connectStats != null){
            return connectStats.get_ties();
        }
        return -1;
    }
    public Rank getRankForConnect4() {
        connect4Stats connectStats = player.getConnect4Stats();
        if(connectStats != null){
            return connectStats.getRank();
        }
        return null;
    }

    public int getGamesPlayedForTicTacToe(){
        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
        if(TicTacToeStats != null){
            return TicTacToeStats.getGamesPlayed();
        }
        return -1;
    }
    // created Methods for Tic-Tac-Toe game
    public int getWinsForTicTacToe(){
        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
        if(TicTacToeStats != null){
            return TicTacToeStats.get_wins();
        }
        return -1;
    }

    public int getLossesForTicTacToe() {
        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
        if(TicTacToeStats != null){
            return TicTacToeStats.get_losses();
        }
        return -1;
    }

    public int getTiesForTicTacToe() {
        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
        if(TicTacToeStats != null){
            return TicTacToeStats.get_ties();
        }
        return -1;
    }

    public Rank getRankForTicTacToe() {
        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
        if(TicTacToeStats != null){
            return TicTacToeStats.getRank();
        }
        return null;
    }

    public int getGamesPlayedForChecker(){
        checkersStats CheckerStats = player.getCheckersStats();
        if(CheckerStats != null){
            return CheckerStats.getGamesPlayed();
        }
        return -1;
    }

    // created Methods for Checkers game
    public int getWinsForChecker(){
        checkersStats CheckerStats = player.getCheckersStats();
        if(CheckerStats != null){
            return CheckerStats.get_wins();
        }
        return -1;
    }
    public int getLossesForChecker() {
        checkersStats CheckerStats = player.getCheckersStats();
        if(CheckerStats != null){
            return CheckerStats.get_losses();
        }
        return -1;
    }
    public int getTiesForChecker() {
        checkersStats CheckerStats = player.getCheckersStats();
        if(CheckerStats != null){
            return CheckerStats.get_ties();
        }
        return -1;
    }
    public Rank getRankForChecker(){
        checkersStats CheckerStats = player.getCheckersStats();
        if(CheckerStats != null){
            return CheckerStats.getRank();
        }
        return null;
    }

    //need to fix the merging issue with the leaderboard_matchmaking and go over the code again

    //returning the last 5 matches
    public List<List<Object>> getLast5Matches() {
        Last5Matches history = player.getLast5Matches();
        if (history != null) {
            return history.getMatchHistory();
        }
        return null;
    }

    //returning a match at a given index
    public List<Object> getMatchAtIndex(int index) {
        Last5Matches history = player.getLast5Matches();
        if (history != null) {
            return history.getMatchAt(index);
        }
        return null;
    }

    //clearing the match history
    public void clearLast5Matches() {
        Last5Matches history = player.getLast5Matches();
        if (history != null) {
            history.clearHistory();
        }
    }


//    Commenting this since its going to be deleted


//    public int getTotalGamesPlayed() {
//        connect4Stats Connect4Stats = player.getConnect4Stats();
//        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
//        checkersStats CheckersStats = player.getCheckersStats();
//        return Connect4Stats.getGamesPlayed() + TicTacToeStats.getGamesPlayed() + CheckersStats.getGamesPlayed();
//    }
//
//    public int getTotalWins() {
//        connect4Stats Connect4Stats = player.getConnect4Stats();
//        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
//        checkersStats CheckersStats = player.getCheckersStats();
//        return Connect4Stats.get_wins() + TicTacToeStats.get_wins() + CheckersStats.get_wins();
//    }
//
//    public int getTotalLosses() {
//        connect4Stats Connect4Stats = player.getConnect4Stats();
//        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
//        checkersStats CheckersStats = player.getCheckersStats();
//        return Connect4Stats.get_losses() + TicTacToeStats.get_losses() + CheckersStats.get_losses();
//    }
//
//    public int getTotalTies() {
//        connect4Stats Connect4Stats = player.getConnect4Stats();
//        ticTacToeStats TicTacToeStats = player.getTicTacToeStats();
//        checkersStats CheckersStats = player.getCheckersStats();
//        return Connect4Stats.get_ties() + TicTacToeStats.get_ties() + CheckersStats.get_ties();
//    }
}
