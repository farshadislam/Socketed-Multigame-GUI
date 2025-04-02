package org.seng.gamelogic.tictactoe;
import org.seng.gamelogic.HumanPlayer;

public class TicTacToePlayer extends HumanPlayer {
    private String pieceColor;
    // Not really sure how the constructor is going to exactly work, but this is just a skeleton code so whatever
    // Everything under this is theoretical code however im not entirely sure this is correct
    public TicTacToePlayer(String name, int playerID, char symbol, int rank, String pieceColour) {
        super(name, playerID, symbol, rank, pieceColour);
        this.pieceColor = pieceColour;
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


    //this is a personal addition for the game however plase review if its correc (im using the class diagram
    public boolean makeMove(TicTacToeBoard board, TicTacToeMove move) {
        return board.placeMove(move.getRow(), move.getCol(), this.symbol);
    }
}
