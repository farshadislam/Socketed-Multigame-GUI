package org.seng.gamelogic.tictactoe;

import java.util.ArrayList;
import java.util.List;

/**
 * represents an online two-player tic tac toe game.
 * each client instantiates its own game instance using an initial empty board.
 * moves are applied through network messages or local input.
 * turn switching is managed externally by the ui controller.
 */
public class OnlineTicTacToeGame {

    private TicTacToeBoard board;
    private TicTacToePlayer[] players; // players[0] = player 1 (x), players[1] = player 2 (o)
    private int gameID;
    private String status; // possible values: "in progress", "game over: x wins", "game over: o wins", "draw"
    private List<String> chatLog;

    /**
     * constructs an online tic tac toe game.
     *
     * @param board   the tic tac toe board.
     * @param players an array of two players.
     * @param gameID  a unique identifier for the game.
     * @throws IllegalArgumentException if the players array is null or does not contain exactly two players.
     */
    public OnlineTicTacToeGame(TicTacToeBoard board, TicTacToePlayer[] players, int gameID) {
        if (players == null || players.length != 2) {
            throw new IllegalArgumentException("there must be exactly 2 players.");
        }
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.chatLog = new ArrayList<>();
        this.status = "in progress";

        // assigns symbols based on player order.
        if (players[0] != null) {
            players[0].setSymbol('X');
        }
        if (players[1] != null) {
            players[1].setSymbol('O');
        }
    }

    /**
     * returns the current tic tac toe board.
     *
     * @return the board.
     */
    public TicTacToeBoard getBoard() {
        return board;
    }

    /**
     * returns the status of the game.
     *
     * @return the current game status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * returns the chat log for the game.
     *
     * @return a list of chat messages.
     */
    public List<String> getChatLog() {
        return chatLog;
    }

    /**
     * applies a move to the board without changing the turn.
     *
     * @param row    the row index (0 to 2).
     * @param col    the column index (0 to 2).
     * @param symbol the symbol to place ('X' or 'O').
     * @return true if the move was applied successfully, false otherwise.
     */
    public boolean applyMove(int row, int col, char symbol) {
        if (!status.equals("in progress")) {
            return false;
        }
        if (!board.validMove(row, col)) {
            return false;
        }
        TicTacToeBoard.Mark mark;
        if (symbol == 'X') {
            mark = TicTacToeBoard.Mark.X;
        } else if (symbol == 'O') {
            mark = TicTacToeBoard.Mark.O;
        } else {
            return false;
        }
        board.makeMove(row, col, mark);

        // checks if the current move results in a win.
        if (checkWinner(mark)) {
            status = "game over: " + symbol + " wins";
        }
        // checks for a draw if the board is full.
        else if (boardFull()) {
            status = "draw";
        }
        return true;
    }

    /**
     * checks whether the board is full.
     *
     * @return true if every cell of the board is not empty, false otherwise.
     */
    public boolean boardFull() {
        for (int r = 0; r < TicTacToeBoard.SIZE; r++) {
            for (int c = 0; c < TicTacToeBoard.SIZE; c++) {
                if (board.getMark(r, c) == TicTacToeBoard.Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks whether the specified mark has won the game.
     *
     * @param mark the mark to check (x or o).
     * @return true if the mark forms a winning combination (row, column, or diagonal), false otherwise.
     */
    public boolean checkWinner(TicTacToeBoard.Mark mark) {
        // checks rows for a win.
        for (int r = 0; r < TicTacToeBoard.SIZE; r++) {
            if (board.getMark(r, 0) == mark &&
                    board.getMark(r, 1) == mark &&
                    board.getMark(r, 2) == mark) {
                return true;
            }
        }
        // checks columns for a win.
        for (int c = 0; c < TicTacToeBoard.SIZE; c++) {
            if (board.getMark(0, c) == mark &&
                    board.getMark(1, c) == mark &&
                    board.getMark(2, c) == mark) {
                return true;
            }
        }
        // checks the first diagonal for a win.
        if (board.getMark(0, 0) == mark &&
                board.getMark(1, 1) == mark &&
                board.getMark(2, 2) == mark) {
            return true;
        }
        // checks the second diagonal for a win.
        if (board.getMark(0, 2) == mark &&
                board.getMark(1, 1) == mark &&
                board.getMark(2, 0) == mark) {
            return true;
        }
        return false;
    }

    /**
     * adds a chat message to the game's chat log if the message is valid.
     *
     * @param message the message to add.
     */
    public void addChatMessage(String message) {
        if (message != null && message.trim().length() > 0) {
            chatLog.add(message);
        }
    }
}
