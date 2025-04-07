package org.seng.gamelogic.tictactoe;
import org.seng.gamelogic.Player;
import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToeGame {

    private TicTacToeBoard board;
    private Mark currMark;
    private String status;// Can be "In Progress", "X Wins", "O Wins", "Draw"
    private List<String> chatLog;
    public TicTacToePlayer[] players;  // 2 players
    public int gameID;

    public TicTacToeGame(TicTacToeBoard board, TicTacToePlayer[] players, int gameID) {
        this.players = players;
        this.gameID = gameID;
        this.board = board;
        currMark = Mark.X; // X starts
        status = "In Progress";
        chatLog = new ArrayList<>();
    }

    public boolean makeMove(int row, int col) {
        if (status.equals("In Progress") && board.validMove(row, col)) {
            board.makeMove(row, col, currMark);

            if (checkWinner(currMark)) {
                status = currMark + " Wins";
            } else if (boardFull()) {
                status = "Draw";
            } else {
                switchTurn();
            }
            return true;
        }
        return false;
    }

    public boolean checkWinner(Mark mark) {
        for (int i = 0; i < TicTacToeBoard.SIZE; i++) {
            //check if there is a winner in rows and columns
            if ((board.getMark(i, 0) == mark && board.getMark(i, 1) == mark && board.getMark(i, 2) == mark) ||
                    (board.getMark(0, i) == mark && board.getMark(1, i) == mark && board.getMark(2, i) == mark)) {
                char symbolCheck;
                if (mark == Mark.X) {
                    symbolCheck = 'X';
                } else {
                    symbolCheck = 'O';
                }
                if (players[0].getSymbol() == symbolCheck) {
                    players[0].incrementWins();
                    players[1].incrementLosses();
                } else {
                    players[1].incrementWins();
                    players[0].incrementLosses();
                }
                return true;
            }
        }

        //check if there is a winner in a diagonal
        if ((board.getMark(0, 0) == mark && board.getMark(1, 1) == mark && board.getMark(2, 2) == mark) ||
                (board.getMark(0, 2) == mark && board.getMark(1, 1) == mark && board.getMark(2, 0) == mark)) {
            char symbolCheck;
            if (mark == Mark.X) {
                symbolCheck = 'X';
            } else {
                symbolCheck = 'O';
            }
            if (players[0].getSymbol() == symbolCheck) {
                players[0].incrementWins();
                players[1].incrementLosses();
            } else {
                players[1].incrementWins();
                players[0].incrementLosses();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean boardFull() {
        //checks for a draw
        for (int row = 0; row < TicTacToeBoard.SIZE; row++) {
            for (int col = 0; col < TicTacToeBoard.SIZE; col++) {
                if (board.getMark(row, col) == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void switchTurn() {
        if (currMark == Mark.X) {
            currMark = Mark.O;
        } else {
            currMark = Mark.X;
        }
    }

    public TicTacToeBoard getBoard() {
        return board;
    }

    public Mark getCurrentMark() {
        return currMark;
    }

    public String getStatus() {
        return status;
    }

    public void resetGame() {
        board.resetBoard();
        currMark = Mark.X;
        status = "In Progress";
    }

    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            chatLog.add(currMark + ": " + message);
        }
    }

    public List<String> getChatLog() {
        return new ArrayList<>(chatLog);
    }

    private void initializePlayerSymbols() {
        players[0].symbol = 'X';
        players[1].symbol = 'O';
    }

    //Starts the game loop for console-based play Handles player input, move validation, win/draw checks and output.
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
}
