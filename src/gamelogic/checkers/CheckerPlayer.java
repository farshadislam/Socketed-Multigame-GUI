package gamelogic.checkers;

import gamelogic.HumanPlayer;
import gamelogic.Move;

public class CheckersPlayer extends HumanPlayer {

    // Constructor
    public CheckersPlayer(String name, int playerID, char symbol, int rank) {
        super(name, playerID, symbol, rank);

    }

    public boolean makeMove(Move move) {
        return false;
    }

    @Override
    public boolean readyStart() {
        return true;
    }

    //just copy pasted this from TicTacToePlayer please review
    public boolean makeMove(Board board, Move move) {
        return board.placeMove(move.getRow(), move.getCol(), this.symbol);
    }
}
