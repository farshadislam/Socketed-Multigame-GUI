package org.seng.gamelogic.connectfour;
import org.seng.gamelogic.Player;
import org.seng.gamelogic.connectfour.ConnectFourBoard.Chip;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtendedAIBotConnectFour extends ConnectFourPlayer {
    private ConnectFourGame game;
    private ConnectFourBoard board;
    private char symbol;
    private Random random;

    public ExtendedAIBotConnectFour(char symbol, ConnectFourGame game, ConnectFourBoard board) {
        super("AIBot", 0, symbol, 0); // passes username, player ID, symbol as char, rank)
        this.game = game;
        this.board = board;
        this.random = new Random();
    }

    /**
     * @param board ConnectFourBoard for AIBot.
     * @param move  ConnectFourMove that have the column
     * @return true if the move was successful, if not false.
     */

    public boolean makeMove(ConnectFourBoard board, ConnectFourMove move) {
        if (this.board != board) {
            System.out.println("Board mismatch");
            return false;
        }
        // ensure its not null, and player matches ai
        if (move == null || move.player != this) {
            return false;
        }

        if (board.dropPiece(move.column, this)) {
            return true;
        }
        return false;
    }

    /**
     * @param board ConnectFourBoard to find a move
     * @return an int representing the column for the next move, or null otherwise
     */
    public Integer findNextMove(ConnectFourBoard board) {
        // collect all empty columns
        List<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < ConnectFourBoard.COL_COUNT; col++) {
            if (!board.columnFull(col)) {
                availableColumns.add(col);
            }
        }

        if (availableColumns.isEmpty()) {
            return null; // valid moves are not available
        }

        return availableColumns.get(random.nextInt(availableColumns.size()));
    }

    /**
     * @param board ConnectFourBoard to find a move
     * @return ConnectFourMove object showing the next move, or null if there is no moves available
     */
    public ConnectFourMove nextMove(ConnectFourBoard board) {
        // checking if its ai's turn
        if (game.currentPlayer != this) {
            return null; // other player's turn
        }

        Integer column = findNextMove(board);
        if (column == null) {
            return null; //no valid moves are available
        }
        // set row to -1 right now, will be changed when the move is made
        return new ConnectFourMove(this, column, -1);

    }

    /**
     * @param symbol The char symbol change
     * @return Chip.BLUE, Chip.YELLOW or Chip.EMPTY
     */
    private static Chip charToChip(char symbol) {
        if (symbol == 'b') {
            return Chip.BLUE;
        } else if (symbol == 'y') {
            return Chip.YELLOW;
        }
        return Chip.EMPTY;
    }

    /**
     * @param chip The chip to change
     * @return b, y, or ' '
     */
    private static char chipToChar(Chip chip) {
        if (chip == Chip.BLUE) {
            return 'b';
        } else if (chip == Chip.YELLOW) {
            return 'y';
        }
        return ' ';
    }


    /**
     * @param symbol The symbol set b or y
     */
    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }


    /**
     * @return AI's symbol b or y
     * */
    @Override
    public char getSymbol() {
        return this.symbol;
    }

}

