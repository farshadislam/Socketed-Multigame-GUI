package gamelogic.checkers;

import gamelogic.HumanPlayer;
import gamelogic.Move;
import gamelogic.Board;
public class CheckersPlayer extends HumanPlayer {

    // Constructor
    public CheckersPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);

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
