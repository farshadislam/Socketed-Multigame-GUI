package org.seng.gamelogic.connectfour;

import org.seng.gamelogic.Player;

import java.util.ArrayList;

// Do we need to extend or implement from GameGUI class?
public class ConnectFourGame extends ConnectFourBoard {

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
    public boolean checkWinner(Chip chip) {
        for (int i = 0; i < ConnectFourBoard.ROW_COUNT; i++) { //make sure i and j are within the borders of the board
            for (int j = 0; j < ConnectFourBoard.COL_COUNT; j++) {
                if (chip == Chip.EMPTY) continue; //in case its empty

                //this is just error protection, if teh board is a 4x4 for example i dont want to try to access a position 5 or something (probably a better way to do this)
                if (j + 3 < ConnectFourBoard.COL_COUNT && chip == board.getChip(i, j) &&
                        chip == board.getChip(i, j + 1) && chip == board.getChip(i, j + 2) &&  chip == board.getChip(i, j + 3)) {
                    return true;
                }

                // error checking for row
                if (i + 3 < ConnectFourBoard.ROW_COUNT &&
                        chip == board.getChip(i, j) && chip == board.getChip(i + 1, j) &&  chip == board.getChip(i + 2, j) && chip == board.getChip(i + 3, j)) {
                    return true;
                }

                //error checking for diagonal (topleft-bottomright)
                if (i + 3 < ConnectFourBoard.ROW_COUNT && j + 3 < ConnectFourBoard.COL_COUNT &&
                        chip == board.getChip(i, j) && chip == board.getChip(i + 1, j + 1) && chip == board.getChip(i + 2, j + 2) && chip == board.getChip(i + 3, j + 3)) {
                    return true;
                }

                // error checking for diagonal (topright-bottomleft)
                if (i + 3 < ConnectFourBoard.ROW_COUNT && j - 3 >= 0  &&
                        chip == board.getChip(i, j) && chip == board.getChip(i + 1, j - 1) &&  chip == board.getChip(i + 2, j - 2) && chip == board.getChip(i + 3, j - 3)) {
                    return true;
                }
            }
        }
        return false; // no winner found
    }


}

