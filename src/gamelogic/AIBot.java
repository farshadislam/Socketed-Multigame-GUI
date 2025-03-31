package gamelogic;

public abstract class AIBot extends Player {

    public AIBot(String name, int playerID, char symbol, int rank) {
        super(name, playerID, symbol, rank);
    }

    public Object nextMove(Object board) {
        return board;
    }
}