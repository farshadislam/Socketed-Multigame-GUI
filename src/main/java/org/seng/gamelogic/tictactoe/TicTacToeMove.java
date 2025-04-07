package org.seng.gamelogic.tictactoe;

import org.seng.gamelogic.tictactoe.TicTacToeGame;

public class TicTacToeMove {
    public int row;
    public int col;
    public char symbol; // symbol is either 'X' or 'O' for tictactoe

    // will want to change order of column/row/symbol argument input for all 3 move classes to avoid future confusion
    public TicTacToeMove(int column, int row, char symbol) {
        this.row = row;
        this.col = column;
        this.symbol = symbol;
    }

    // getter methods
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getSymbol() {
        return symbol;
    }


    // set Move
    public String getMoveDetails(TicTacToeGame game) {
        return "hi";
    }

}
