package gamelogic.tictactoe;
import gamelogic.TicTacToeBoard;
import gamelogic.TicTacToeBoard.Mark;

public class TicTacToeGame {

    private TicTacToeBoard board;
    private Mark currPlayer;
    private String status; // Can be "In Progress", "X Wins", "O Wins", "Draw"
