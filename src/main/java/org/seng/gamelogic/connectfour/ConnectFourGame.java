package org.seng.gamelogic.connectfour;
import org.seng.gamelogic.Player;
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

    /* Since its connect 4 I am guessing a win is 4 in a row
    PLEASE NOTE: I haven't showed up to any meetings and I don't know if theres a certain way you guys want to do
    this so I've made it as clear and concise as possible, if its useless or wrong please feel free to delete */
    public Player checkWinner() {
        for (int i = 0; i < /* INSERT CONNECT-4 ROWSIZE */; i++) { //make sure i and j are within the borders of the board
            for (int j = 0; j < /* INSERT CONNECT-4 COLUMNSIZE */; j++) {
                Symbol winningSymbol = board.getSymbol(i, j);
                Player winningPlayer;
                if (/* INSERT FUNCTIONALITY TO FIND EVERY PLAYERS SYMBOLY */) { //can be done better, idk what you guys want to do
                    winningPlayer = /* INSERT THE PLAYER WITH THE SAME SYMBOL AS winningSymbol */
                }
                if (winningSymbol == null) continue; //in case its empty
                //this is just error protection, if teh board is a 4x4 for example i dont want to try to access a position 5 or something (probably a better way to do this)
                if (j + 3 < /* INSERT CONNECT-4 COLUMNSIZE */ &&
                        winningSymbol.equals(board.getSymbol(i, j + 1)) && winningSymbol.equals(board.getSymbol(i, j + 2)) && winningSymbol.equals(board.getSymbol(i, j + 3))) {
                    return winningPlayer;
                }

                // error checking for row
                if (i + 3 < /* INSERT CONNECT-4 ROWSIZE */ &&
                        winningSymbol.equals(board.getSymbol(i + 1, j)) && winningSymbol.equals(board.getSymbol(i + 2, j)) && winningSymbol.equals(board.getSymbol(i + 3, j))) {
                    return winningPlayer;
                }

                //error checking for diagonal (topleft-bottomright)
                if (i + 3 < /* INSERT CONNECT-4 ROWSIZE */ && j + 3 < /* INSERT CONNECT-4 COLUMNSIZE */ &&
                        winningSymbol.equals(board.getSymbol(i + 1, j + 1)) && winningSymbol.equals(board.getSymbol(i + 2, j + 2)) && winningSymbol.equals(board.getSymbol(i + 3, j + 3))) {
                    return winningPlayer;
                }

                // error checking for diagonal (topright-bottomleft)
                if (i + 3 < /* INSERT CONNECT-4 ROWSIZE */ && j - 3 >= 0 &&
                        winningSymbol.equals(board.getSymbol(i + 1, j - 1)) && winningSymbol.equals(board.getSymbol(i + 2, j - 2)) && winningSymbol.equals(board.getSymbol(i + 3, j - 3))) {
                    return winningSymbol;
                }
            }
        }
        return null; // no winner found
    }

}

