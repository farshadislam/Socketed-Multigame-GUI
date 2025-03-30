package gamelogic;

public abstract class AIBot extends Player {

    public AIBot(String name, int accountID, int level, int plays, int score ) {
        super(name, accountID, level, plays, score);
    }

    public abstract boolean makeMove(Object board, Object move);


    public abstract Object nextMove(Object board);




}