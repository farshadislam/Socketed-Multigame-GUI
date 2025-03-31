package gamelogic.tictactoe;

public class TicTacToePlayer extends HumanPlayer {

    // Not really sure how the constructor is going to exactly work, but this is just a skeleton code so whatever
    // Everything under this is theoretical code however im not entirely sure this is correct
    public TicTacToePlayer(String playerName, int playerID, int playerRank, char symbol, String pieceColour) {
        super(name, accountID, symbol, rank);
    }

    @Override
    public boolean readyStart() {
        return true;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    // play the chip
    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean makeMove(Board board, Move move) {
        return board.placeMove(move.getRow(), move.getCol(), this.symbol);
    }
}
