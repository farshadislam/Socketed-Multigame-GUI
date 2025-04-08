package org.seng.gamelogic.tictactoe;

import org.seng.gamelogic.AIBot;
import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtendedAIBotTicTacToe extends AIBot {
    private TicTacToeBoard board;
    private TicTacToeGame game;
    private Mark symbol;

    public ExtendedAIBotTicTacToe(Mark symbol, TicTacToeGame game, TicTacToeBoard board) {
        super("AIBot", 0, marktoChar(symbol), 0); // ✅ cast Mark to char safely
        this.board = board;
        this.game = game;
        this.symbol = symbol;
    }

    public boolean makeMove(TicTacToeBoard board, TicTacToeMove move) {
        if (this.board != board) return false;
        if (move == null || move.getSymbol() != marktoChar(this.symbol)) return false;
        return board.makeMove(move.getRow(), move.getCol(), this.symbol);
    }

    public int[] findBestMove(TicTacToeBoard board) {
        List<int[]> emptySpots = new ArrayList<>();
        for (int r = 0; r < TicTacToeBoard.SIZE; r++) {
            for (int c = 0; c < TicTacToeBoard.SIZE; c++) {
                if (board.validMove(r, c)) {
                    emptySpots.add(new int[]{r, c});
                }
            }
        }
        if (emptySpots.isEmpty()) return null;
        return emptySpots.get(new Random().nextInt(emptySpots.size()));
    }

    @Override
    public Object nextMove(Object board) {
        return board; // placeholder
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = chartoMark(symbol);
    }

    @Override
    public char getSymbol() {
        return marktoChar(this.symbol);
    }

    private static Mark chartoMark(char symbol) {
        if (symbol == 'X') return Mark.X;
        if (symbol == 'O') return Mark.O;
        return null;
    }

    private static char marktoChar(Mark mark) {
        if (mark == Mark.X) return 'X';
        if (mark == Mark.O) return 'O';
        return ' ';
    }
}
