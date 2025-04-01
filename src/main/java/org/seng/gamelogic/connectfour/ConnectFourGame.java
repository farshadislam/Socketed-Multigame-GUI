package org.seng.gamelogic.connectfour;
import main.java.org.seng.gamelogic.Player;
import java.util.ArrayList;

// Do we need to extend or implement from GameGUI class?
public class ConnectFourGame {

    public ConnectFourBoard board;
    public Player[] players;  // 2 players
    public Player currentPlayer;
    public int gameID;
    public String status;
    public ArrayList<String> chatLog;

    public ConnectFourGame(ConnectFourBoard board, Player[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "In Progress"; // placeholder for status
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // first player starts, this may be changed to implement a RNG decision?
    }

    // Manage player networking
    public boolean connectPlayers() {
        return true;
    }

    public boolean disconnectPlayers() {
        return true;
    }

    // Manage chat log (
    public void sendMessage(String message) {
    }

    // need to integrate with GUI team (start/exit button)
    public void startGame() {

    }

    public void exitGame() {

    }

    // restrict movements to column choice only. Returns true if move is made
    public boolean makeMove(int col) {
        // update board here so that the Connect Four piece is dropped in the correct column
        // error handling: make sure the integer passed into this method is 0 <= col <= 6 (7 columns)
        return true;
    }

    // made after move is made. We can also add a "skip turn" functionality if we have time, work with GUI/integration.
    public void switchTurn() {

    }

    // called at end of game? We can add a visual display for this (GUI).
    public Player checkWinner() {
        Player winner = players[0]; // placeholder. Can be Player[0] or Player[1]
        return winner;
    }


}

