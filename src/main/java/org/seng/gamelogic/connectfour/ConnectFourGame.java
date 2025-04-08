package org.seng.gamelogic.connectfour;
import org.seng.gamelogic.Player;
import java.util.ArrayList;
import java.util.List;

// Do we need to extend or implement from GameGUI class?
public class ConnectFourGame {

    public ConnectFourBoard board;
    public ConnectFourPlayer[] players;  // 2 players
    public ConnectFourPlayer currentPlayer;
    public int gameID;
    public String status;
    public ArrayList<String> chatLog;

    public ConnectFourGame(ConnectFourBoard board, ConnectFourPlayer[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "In Progress"; // placeholder for status
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // first player starts, this may be changed to implement a RNG decision?
    }

    // Manage chat log (
    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            chatLog.add(message);
            System.out.println("Chat Message: " + message);
        }
    }

    public List<String> getChatLog() {
        return new ArrayList<>(chatLog);
    }

    // need to integrate with GUI team (start/exit button)
    public void startGame() {
        initializePlayerSymbols(); // assign symbols to players
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (status.equals("In Progress")) {
            board.display(); // display board (requires display() method in ConnectFourBoard)
            System.out.println("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
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
        System.out.println("Game Over! Status: " + status);
        scanner.close();
    }

    public void exitGame() {
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
            if (checkWinner(chip)) {
                status = currentPlayer.toString() + " Wins";
            } else if (boardFull()) {
                status = "Draw";
            } else {
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
        if (players[0] != null) players[0].symbol = 'b';
        if (players[1] != null) players[1].symbol = 'y';
    }

    public void setStatus(ConnectFourPlayer gamePlayer, int column) {
        String message = "Player " + gamePlayer.getName() + " has dropped a piece in column " + column;
        status = message;
    }
}
