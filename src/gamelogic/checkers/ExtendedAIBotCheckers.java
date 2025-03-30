package gamelogic.checkers;

import gamelogic.AIBot;
import gamelogic.CheckersBoard;
import gamelogic.connectfour.ConnectFourBoard;

public class ExtendedAIBotCheckers extends AIBot {
    private char symbol;
    private CheckersGame game;
    private CheckersBoard boardRef;

    public ExtendedAIBotCheckers(String name, int accountID, CheckersGame game) {
        super(name, accountID, 0, 0, 0);
        this.symbol = ' ';
        this.game = game;
        this.boardRef = (CheckersBoard) boardRef;
    }
    @Override
    public boolean makeMove(Object board, Object move){

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
