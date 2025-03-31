package gamelogic.connectfour;
import gamelogic.HumanPlayer;

public class ConnectFourPlayer extends HumanPlayer {

    // constructor
    public ConnectFourPlayer(String name, int accountID, int rank, char symbol) {
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
    {

    }
}
