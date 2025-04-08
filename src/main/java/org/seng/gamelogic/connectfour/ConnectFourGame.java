package org.seng.gamelogic.connectfour;

import org.seng.gamelogic.checkers.ExtendedAIBotCheckers;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of connect four following basic game logic.
 */
public class ConnectFourGame {

    public ConnectFourBoard board;
    public ConnectFourPlayer[] players;  // 2 players
    public ConnectFourPlayer currentPlayer;
    public int gameID;
    public String status;
    public ArrayList<String> chatLog;

    public ExtendedAIBotConnectFour AIBot;
    public char AISymbol;

    public ConnectFourGame(ConnectFourBoard board, ConnectFourPlayer[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "Initialized";
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // first player starts, this may be changed to implement a RNG decision?
    }

    // Manage chat log
    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            chatLog.add(message);
            System.out.println("Chat Message: " + message);
        }
    }

    public List<String> getChatLog() {
        return new ArrayList<>(chatLog);
    }

    /**
     * Starts the game and handles turn-based gameplay.
     * Connects with GUI through a START button
     */
    public void startGame() {
        initializePlayerSymbols(); // assign symbols to players
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        // once startGame() is called, status updates to "In Progress"
        status = "In Progress";
        System.out.println("Welcome to Console Checkers!");
        initializePlayerSymbols();
        if (players[0] instanceof ExtendedAIBotConnectFour) {
            AIBot = (ExtendedAIBotConnectFour) players[0];
            AISymbol = 'b';
        } else if (players[1] instanceof ExtendedAIBotConnectFour) {
            AIBot = (ExtendedAIBotConnectFour) players[1];
            AISymbol = 'y';
        } else {
            AIBot = null;
            AISymbol = 'n';
        }
        // game loop continues as long as there has been no win, draw, or exit
        while (!status.endsWith("Wins") && !status.equals("Draw") && !status.equals("Exiting Game")) {
            board.display(); // display board (requires display() method in ConnectFourBoard)
            System.out.println("Current Player: " + currentPlayer.getUsername() + " (" + currentPlayer.getSymbol() + ")");
            if (currentPlayer.getSymbol() == 'b' && AISymbol == 'b') {
                AIBot.makeMove(board, this);
                continue;
            } else if (currentPlayer.getSymbol() == 'y' && AISymbol == 'y'){
                AIBot.makeMove(board, this);
                continue;
            }
            System.out.print("Enter a column (0-6): ");

            int column = scanner.nextInt();

            // input validation
            if (column < 0 || column >= ConnectFourBoard.COL_COUNT) {
                System.out.println("Invalid column. Please choose a column between 0 and 6.");
                continue;
            }

            if (!board.validMove(column)) {
                System.out.println("Column is full. Try a different one.");
                continue;
            }

            // make the move
            makeMove(column);

            // update status method
            setStatus(currentPlayer, column);
        }

        board.display(); // final board state

        // 3 cases to ending the game: win, draw, exit
        if (status.endsWith("Wins")) {
            System.out.println("Game Over! Status: " + status);
        }
        else if (status.equals("Draw")) {
            System.out.println("Game Over! Draw.");
        }
        else if (status.equals("Exiting Game")) {
            System.out.println("Game has been exited.");
        }

        scanner.close();
    }

    // Connects with GUI through an exit button
    public void exitGame() {
        // status updates to something that is NOT "In Progress" such that game loop in startGame() breaks
        status = "Exiting Game";
    }

    // restrict movements to column choice only. Returns true if move is made
    public boolean makeMove(int col) {
        if (status.equals("In Progress")) {
            board.dropPiece(col, currentPlayer);
            char symbol = currentPlayer.getSymbol();
            ConnectFourBoard.Chip chip;
            if (symbol == 'b') {
                chip = ConnectFourBoard.Chip.BLUE;
            } else {
                chip = ConnectFourBoard.Chip.YELLOW;
            }
            // Case 1: A Player wins
            if (checkWinner(chip)) {
                status = currentPlayer.toString() + " Wins";
                currentPlayer.getConnect4Stats().win(); // Player wins
                if (currentPlayer == players[0]) { // Other player loses
                    players[1].getConnect4Stats().lose();
                }
                else {
                    players[0].getConnect4Stats().lose();
                }
            }
            // Case 2: Players tie
            else if (boardFull()) {
                status = "Draw";
                // update both players stats to a tie
                players[0].getConnect4Stats().tie();
                players[1].getConnect4Stats().tie();
            }
            // Case 3: No one has won yet, switch turns
            else {
                switchTurn();
            }
            return true;
        }
        return false;
        // update board here so that the Connect Four piece is dropped in the correct column
        // error handling: make sure the integer passed into this method is 0 <= col <= 6 (7 columns)
    }

    // made after move is made. We can also add a "skip turn" functionality if we have time, work with GUI/integration.
    public void switchTurn() {
        if (players[0] == currentPlayer) {
            currentPlayer = players[1];
        } else {
            currentPlayer = players[0];
        }
    }

    public boolean boardFull() {
        boolean full = true;
        for (int colCounter = 0; colCounter < 7; colCounter ++) {
            boolean eachCol = board.columnFull(colCounter);
            if (!eachCol) {
                full = false;
            }
        }
        return full;
    }

    // called at end of game? We can add a visual display for this (GUI).

    /* Since its connect 4 I am guessing a win is 4 in a row
    PLEASE NOTE: I haven't showed up to any meetings and I don't know if theres a certain way you guys want to do
    this so I've made it as clear and concise as possible, if its useless or wrong please feel free to delete */
    public boolean checkWinner(ConnectFourBoard.Chip chip) {
        for (int i = 0; i < ConnectFourBoard.ROW_COUNT; i++) { //make sure i and j are within the borders of the board
            for (int j = 0; j < ConnectFourBoard.COL_COUNT; j++) {
                if (chip == ConnectFourBoard.Chip.EMPTY) continue; //in case its empty

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

    private void initializePlayerSymbols() {
        if (players[0] != null) players[0].setSymbol('b');
        if (players[1] != null) players[1].setSymbol('y');
    }

    public void setStatus(ConnectFourPlayer gamePlayer, int column) {
        String message = "Player " + gamePlayer.getUsername() + " has dropped a piece in column " + column;
        status = message;
    }
}
