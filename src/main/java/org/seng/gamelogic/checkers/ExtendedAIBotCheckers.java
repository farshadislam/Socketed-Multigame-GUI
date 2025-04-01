package gamelogic.checkers;

import gamelogic.AIBot;
import gamelogic.checkers.CheckersBoard;

public class ExtendedAIBotCheckers extends AIBot {
    private CheckersGame game;
    private CheckersBoard boardRef;

    public ExtendedAIBotCheckers(String username, int playerID, char symbol, int rank, CheckersGame game) {
        super(username, playerID, symbol, rank);
        this.game = game;
        this.boardRef = (CheckersBoard) boardRef;
    }

    public boolean makeMove(Object board, Object move){
        return true;
    }

    @Override
    public Object nextMove(Object board) {
        return board; // placeholder
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public char getSymbol() {
        return this.symbol;
    }

}
