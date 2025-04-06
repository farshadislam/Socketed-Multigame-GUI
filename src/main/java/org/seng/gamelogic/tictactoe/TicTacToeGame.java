package org.seng.gamelogic.tictactoe;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;

import java.util.ArrayList;
import java.util.List;


public class TicTacToeGame {

    private TicTacToeBoard board;
    private Mark currPlayer;
    private String status;// Can be "In Progress", "X Wins", "O Wins", "Draw"
    private List<String> chatLog;
    public TicTacToeGame() {
        board = new TicTacToeBoard();
        currPlayer = Mark.X; // X starts
        status = "In Progress";
        chatLog = new ArrayList<>();
    }

    public boolean makeMove(int row, int col) {
        if (status.equals("In Progress") && board.validMove(row, col)) {
            board.makeMove(row, col, currPlayer);

            if (checkWinner(currPlayer)) {
                status = currPlayer + " Wins";
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
                return true;
            }
        }

        //check if there is a winner in a diagonal
        return (board.getMark(0, 0) == mark && board.getMark(1, 1) == mark && board.getMark(2, 2) == mark) ||
                (board.getMark(0, 2) == mark && board.getMark(1, 1) == mark && board.getMark(2, 0) == mark);
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
        currPlayer = Mark.O;
    }

    public TicTacToeBoard getBoard() {
        return board;
    }

    public Mark getCurrentPlayer() {
        return currPlayer;
    }

    public String getStatus() {
        return status;
    }

    public void resetGame() {
        board.resetBoard();
        currPlayer = Mark.X;
        status = "In Progress";
    }
    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            chatLog.add(currPlayer + ": " + message);
        }
    }
    public List<String> getChatLog() {
        return new ArrayList<>(chatLog);
    }
}
