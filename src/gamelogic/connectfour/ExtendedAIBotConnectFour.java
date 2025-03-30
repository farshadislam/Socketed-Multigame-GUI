package gamelogic.connectfour;
import gamelogic.AIBot;
import gamelogic.connectfour.ConnectFourBoard;
import gamelogic.connectfour.ConnectFourGame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtendedAIBotConnectFour extends AIBot {
    private ConnectFourBoard game;
    private char symbol;
    private ConnectFourBoard boardRef;

    public ExtendedAIBotConnectFour(String name, int accountID, ConnectFourBoard game) {
        super(name, accountID, 0, 0, 0);
        this.symbol = ' ';
        this.game = game;
        this.boardRef = (ConnectFourBoard) game;
    }
    @Override
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
