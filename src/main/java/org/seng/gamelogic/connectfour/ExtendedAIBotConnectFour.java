package org.seng.gamelogic.connectfour;

import org.seng.gamelogic.AIBot;

public class ExtendedAIBotConnectFour extends AIBot {
    private ConnectFourGame game;
    private ConnectFourBoard boardRef;

    public ExtendedAIBotConnectFour(String username, int accountID, char symbol, int rank, ConnectFourGame game) {
        super(username, accountID, symbol, rank);
        this.symbol = ' ';
        this.game = game;
        this.boardRef = (ConnectFourBoard) boardRef;
    }

    public boolean makeMove(Object board, Object move) {
        ConnectFourBoard connectFourBoard = (ConnectFourBoard) board;
        if (connectFourBoard != boardRef){
            System.out.println("Board mismatch");
            return false;
        }
        return true; // placeholder, needs a return statement
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

