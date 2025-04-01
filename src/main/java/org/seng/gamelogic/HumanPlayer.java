package main.java.org.seng.gamelogic;

public class HumanPlayer extends gamelogic.Player {

//constructor for HumanPlayer
    public HumanPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);
    }

    public boolean readyStart() {
        // check if all conditions are met this should be condition part
        return true;
    }

    @Override
    public char getSymbol() {
        //gets the symbol such as X and O for tic tac toe, likely?
        return symbol;
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
//some classes extend off of this