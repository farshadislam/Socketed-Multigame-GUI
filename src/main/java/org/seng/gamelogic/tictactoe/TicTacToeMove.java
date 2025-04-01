package org.seng.gamelogic.tictactoe;

public class TicTacToeMove {
    public int row;
    public int col;
    public char symbol; // symbol is either 'X' or 'O' for tictactoe

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
    public void move(int row, int col, char symbol) {
        // place on board
    }

}
