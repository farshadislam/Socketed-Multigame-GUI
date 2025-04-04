package org.seng.authentication;
import org.seng.leaderboard.GeneralStats;
import org.seng.leaderboard.checkersStats;
import org.seng.leaderboard.connect4Stats;
import org.seng.leaderboard.ticTacToeStats;
import org.seng.leaderboard.Rank;

public class PlayerStats {
    // defined instance variable
    private Player player;

    // created the constructor to initialize the player in the PlayerStats class
    public PlayerStats(Player player) {
        this.player = player;
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
}
