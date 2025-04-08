package org.seng.gamelogic.tictactoe;

import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a game of Tic-Tac-Toe.
 */
public class TicTacToeGame {

    private TicTacToeBoard board;
    private Mark currMark;
    private String status; // Can be "In Progress", "X Wins", "O Wins", "Draw"
    private List<String> chatLog;

    public TicTacToePlayer[] players; // Array to store two players
    public TicTacToePlayer currentPlayer;
    public int gameID;

    /**
     * Constructs a Tic-Tac-Toe game.
     *
     * @param board  The Tic-Tac-Toe board.
     * @param players Array of two players participating in the game.
     * @param gameID Unique identifier for the game.
     */
    public TicTacToeGame(TicTacToeBoard board, TicTacToePlayer[] players, int gameID) {
        this.players = players;
        this.gameID = gameID;
        this.board = board;
        currMark = Mark.X; // X starts first
        currentPlayer = players[0];
        status = "In Progress";
        chatLog = new ArrayList<>();
    }

    /**
     * Attempts to make a move at the specified row and column.
     *
     * @param row The row index (0-2).
     * @param col The column index (0-2).
     * @return true if the move was successful, false otherwise.
     */
    public boolean makeMove(int row, int col) {
        if (status.equals("In Progress") && board.validMove(row, col)) {
            board.makeMove(row, col, currMark);

            if (checkWinner(currMark)) {
                status = currMark + " Wins";
                // Logic: Player 1 is always X, Player 2 is always O
                if (currMark == Mark.X) { // Player 1 wins
                    players[0].getTicTacToeStats().win();
                    players[1].getTicTacToeStats().lose();
                }
                else { // Player 2 wins
                    players[0].getTicTacToeStats().lose();
                    players[1].getTicTacToeStats().win();
                }
            } else if (boardFull()) {
                status = "Draw";
                players[0].getTicTacToeStats().tie();
                players[0].getTicTacToeStats().tie();
            } else {
                switchTurn();
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the given mark (X or O) has won the game.
     *
     * @param mark The mark to check.
     * @return true if the mark has won, false otherwise.
     */
    public boolean checkWinner(Mark mark) {
        for (int i = 0; i < TicTacToeBoard.SIZE; i++) {
            // Check rows and columns for a win
            if ((board.getMark(i, 0) == mark && board.getMark(i, 1) == mark && board.getMark(i, 2) == mark) ||
                    (board.getMark(0, i) == mark && board.getMark(1, i) == mark && board.getMark(2, i) == mark)) {
                return true;
            }
        }
        // Check diagonals for a win
        return (board.getMark(0, 0) == mark && board.getMark(1, 1) == mark && board.getMark(2, 2) == mark) ||
                (board.getMark(0, 2) == mark && board.getMark(1, 1) == mark && board.getMark(2, 0) == mark);
    }

    /**
     * Checks if the board is full, indicating a draw.
     *
     * @return true if the board is full, false otherwise.
     */
    public boolean boardFull() {
        for (int row = 0; row < TicTacToeBoard.SIZE; row++) {
            for (int col = 0; col < TicTacToeBoard.SIZE; col++) {
                if (board.getMark(row, col) == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * This method changes the turn to the other player.
     */
    public void switchTurn() {
        if (currMark == Mark.X) {
            currMark = Mark.O;
        } else {
            currMark = Mark.X;
        }
    }
    /**
     * This method returns the whole board
     */
    public TicTacToeBoard getBoard() {
        return board;
    }

    /**
     * This method returns the mark(O or X) of the current player
     */
    public Mark getCurrentMark() {
        return currMark;
    }

    /**
     * This method returns the status of the current game
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method resets the game by reseting the board
     */
    // Do not think we need this method because we did not discuss a 'reset' feature
    public void resetGame() {
        board.resetBoard();
        currMark = Mark.X;
        status = "In Progress";
    }

    /**
     * This method adds a message to the chatlog if it is valid
     */

    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            chatLog.add(currMark + ": " + message);
        }
    }

    /**
     * This method returns the chatlog
     */
    public List<String> getChatLog() {
        return new ArrayList<>(chatLog);
    }

    /**
     * This method initializes the different symbols for the player
     */
    private void initializePlayerSymbols() {
        players[0].setSymbol('X');
        players[1].setSymbol('O');
    }

    /**
     * Starts the game loop for console-based play Handles player input, move validation, win/draw checks and output.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        initializePlayerSymbols(); // assign X and O to players

        while (status.equals("In Progress")) {
            board.display(); // requires a display() method in TicTacToeBoard

            System.out.println("Current player: " + currMark);
            System.out.print("Enter row (0-2): ");
            int row = scanner.nextInt();
            System.out.print("Enter column (0-2): ");
            int col = scanner.nextInt();

            boolean moveMade = makeMove(row, col);

            if (!moveMade) {
                System.out.println("Invalid move! Try again.");
            }
        }

        board.display();
        System.out.println("Game Over! Status: " + status);
        scanner.close();
    }

    /**
     * Changes the symbolCheck, currentPlayer and message/status based on the parameter row and column values.
     */
    public void setStatus(int row, int column) {
        char symbolCheck;
        if (currMark == Mark.X) {
            symbolCheck = 'X';
        } else {
            symbolCheck = 'O';
        }
        if (players[0].getSymbol() == symbolCheck) {
            currentPlayer = players[0];
        } else {
            currentPlayer = players[1];
        }
        String message = "Player " + currentPlayer.getUsername() + " has drawn an " + currentPlayer.getSymbol() + " in row " + row + " and column " + column;
        status = message;
    }

}
