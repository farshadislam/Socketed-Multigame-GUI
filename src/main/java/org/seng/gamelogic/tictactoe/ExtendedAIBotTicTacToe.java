package org.seng.gamelogic.tictactoe;
import main.java.org.seng.gamelogic.AIBot;
import main.java.org.seng.gamelogic.Player;
import main.java.org.seng.gamelogic.tictactoe.TicTacToeMove;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExtendedAIBotTicTacToe extends AIBot {
    private TicTacToeBoard boardRef;
    private TicTacToeGame game;

    public ExtendedAIBotTicTacToe(String name, int accountID, char symbol, int rank, TicTacToeGame game) {
        super(name, accountID, 0, 0);
        this.boardRef = (TicTacToeBoard) boardRef;
        this.game = game;
        this.symbol = ' ';

    }

    @Override
    public boolean makeMove(Object board, Object move) {
        TicTacToeBoard ticTacToeBoard = (TicTacToeBoard) board;
        if (ticTacToeBoard != boardRef) {
            return false;
        }
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
