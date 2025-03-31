package gamelogic;

public class HumanPlayer extends Player {

//constructor for HumanPlayer
    public HumanPlayer(String name, int playerID, char symbol, int rank) {
        super(name, playerID, symbol, rank);
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