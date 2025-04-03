package org.seng.authentication;
import org.seng.leaderboard.Rank;




public class PlayerStats {
    // defined instance variable
    private Player player;

    // created the constructor to initialize the player in the PlayerStats class
    public PlayerStats(Player player) {
        this.player = player;
    }

    // created Methods for Connect4 Game
    public int getWinsForConnect4(){
        return player.getStats("Connect4").getWins();}
    public int getLossesForConnect4() {
        return player.getStats("Connect4").getLosses();
    }
    public int getTiesForConnect4() {
        return player.getStats("Connect4").getTies();
    }
    public Rank getRankForConnect4() {
        return player.getStats("Connect4").getRank();
    }

    // created Methods for Tic-Tac-Toe game
    public int getWinsForTicTacToe(){
        return player.getStats("TicTacToe").getWins();
    }
    public int getLossesForTicTacToe() {
        return player.getStats("TicTacToe").getLosses();
    }
    public int getTiesForTicTacToe() {
        return player.getStats("TicTacToe").getTies();
    }
    public Rank getRankForTicTacToe() {
        return player.getStats("TicTacToe").getRank();
    }

    // created Methods for Checkers game
    public int getWinsForChecker(){
        return player.getStats("Checker").getWins();
    }
    public int getLossesForChecker() {
        return player.getStats("Checker").getLosses();
    }
    public int getTiesForChecker() {
        return player.getStats("Checker").getTies();
    }
    public Rank getRankForChecker(){
        return player.getStats("Checker").getRank();
    }
}
