package org.seng.gamelogic.tictactoe;

import org.seng.gamelogic.HumanPlayer;
import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;

public class TicTacToePlayer extends HumanPlayer {
    private String pieceColor;
    private char symbol;

    public TicTacToePlayer(String name, int playerID, char symbol, int rank, String pieceColor) {
        super(name, playerID, symbol, rank);
        this.symbol = symbol;
        this.pieceColor = pieceColor;
    }

    @Override
    public boolean readyStart() {
        return true;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean makeMove(TicTacToeBoard board, TicTacToeMove move) {
        // Convert char symbol to Mark
        Mark mark = (symbol == 'X') ? Mark.X : Mark.O;
        return board.makeMove(move.getRow(), move.getCol(), mark);
    }
}
