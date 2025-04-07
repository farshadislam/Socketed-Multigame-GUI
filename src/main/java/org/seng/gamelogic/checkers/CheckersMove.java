package org.seng.gamelogic.checkers;
import org.seng.gamelogic.Player;


/** Determines a movie in a checkers game.
 *  This class is used to decide how a piece moves given the current location and future location.
 */
public class CheckersMove {

    private int row_start;
    private int col_start;
    private int row_end;
    private int col_end;
    private CheckersPlayer player;

    /**
     * Initializes the CheckersMove class
     * @param row_start The row a piece is starting in
     * @param col_start The column a piece is starting in
     * @param row_end The row a piece is ending in
     * @param col_end The column a piece is ending in
     * @param player The player who is moving the piece for their turn
     */
    public CheckersMove(int row_start, int col_start, int row_end, int col_end, CheckersPlayer player) {
        this.row_start = row_start;
        this.col_start = col_start;
        this.row_end = row_end;
        this.col_end = col_end;
        this.player = player;
    }

    /**
     * Getter and Setter methods needed for board operations
     * @return the row a piece will be moved to
     */
    public int getRowStart() {
        return row_start;
    }

    public int getColStart() {
        return col_start;
    }

    public int getRowEnd() {
        return row_end;
    }

    public int getColEnd() {
        return col_end;
    }
    public Player getPlayer() {
        return player;
    }

    public void setRowStart(int row) {
        row_start = row;
    }

    public void setColStart(int col) {
        col_start = col;
    }

    public void setRowEnd(int row) {
        row_end = row;
    }

    public void setColEnd(int col) {
        col_end = col;
    }
    public void setPlayer(CheckersPlayer change_player) {
        player = change_player;
    }

    /**
     * Gives the details of each player movement to help players keep track of the game
     * @return the String with the details of the last player movement
     */
    public String checkersMoveDetails() {
        String symbol_color;
        if (player.getSymbol() == 'b') {
            symbol_color = "black";
        } else {
            symbol_color = "white';";
        }
        String message = "Player " + player.getName() + "has moved a " + symbol_color + " piece from row " + row_start + " and column " + col_start + " to row " + row_end + " and column " + col_end + ".";
        return message;
    }
}
