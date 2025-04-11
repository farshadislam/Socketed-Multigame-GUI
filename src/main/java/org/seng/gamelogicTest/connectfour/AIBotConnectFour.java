package org.seng.gamelogicTest.connectfour;

import org.seng.gamelogicTest.connectfour.ConnectFourBoard.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIBotConnectFour extends ConnectFourPlayer {
    private ConnectFourGame game;
    private ConnectFourBoard board;
    private char symbol;
    private Random random;

    public AIBotConnectFour(char symbol, ConnectFourGame game, ConnectFourBoard board) {
        super("AIBot", String.valueOf(0), String.valueOf(symbol)); // passes username, player ID, symbol as char, rank
        this.game = game;
        this.board = board;
        this.random = new Random();
    }

    public boolean makeMove(ConnectFourBoard board, ConnectFourGame game) {
        if (this.board != board) {
            System.out.println("Board mismatch");
            return false;
        }
        if (game == null || game.currentPlayer != this) {
            return false;
        }

        Integer column = findNextMove(board);
        if (column != null) {
            if (board.dropPiece(column, this)) {
                return true;
            }
        }
        return false;
    }

    public Integer findNextMove(ConnectFourBoard board) {
        List<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < ConnectFourBoard.COL_COUNT; col++) {
            if (!board.columnFull(col)) {
                availableColumns.add(col);
            }
        }

        if (availableColumns.isEmpty()) {
            return null;
        }

        return availableColumns.get(random.nextInt(availableColumns.size()));
    }

    public ConnectFourMove getMove(ConnectFourBoard board) {
        if (game.currentPlayer != this) {
            return null;
        }

        Integer column = findNextMove(board);
        if (column == null) {
            return null;
        }

        boolean row = board.isColumn(column); // assuming this returns the next open row
        return new ConnectFourMove(this, column, row);
    }

    public static Chip charToChip(char symbol) {
        if (symbol == 'b') {
            return Chip.BLUE;
        } else if (symbol == 'y') {
            return Chip.YELLOW;
        }
        return Chip.EMPTY;
    }

    public static char chipToChar(Chip chip) {
        if (chip == Chip.BLUE) {
            return 'b';
        } else if (chip == Chip.YELLOW) {
            return 'y';
        }
        return ' ';
    }

    // === Inner class starts here ===
    public static class ConnectFourMove {
        private final ConnectFourPlayer player;
        private final int column;
        private final boolean row;

        public ConnectFourMove(ConnectFourPlayer player, int column, boolean row) {
            this.player = player;
            this.column = column;
            this.row = row;
        }

        public ConnectFourPlayer getPlayer() {
            return player;
        }

        public int getColumn() {
            return column;
        }

        public boolean getRow() {
            return row;
        }

        @Override
        public String toString() {
            return "Move[player=" + player.getUsername() + ", column=" + column + ", row=" + row + "]";
        }
    }
}
