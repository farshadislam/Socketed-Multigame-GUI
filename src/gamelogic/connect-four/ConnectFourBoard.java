package gamelogic;

public class ConnectFourBoard {

    public static final int ROW_COUNT = 6;
    public static final int COL_COUNT = 7;

    public enum Chip {
        EMPTY, BLUE, YELLOW
    }

    private Chip[][] grid;

    public ConnectFourBoard () {
        grid = new Chip[ROW_COUNT][COL_COUNT];
        initalizeBoard();
    }

    private void initalizeBoard() {
        for (int row = 0; row < ROW_COUNT; row ++){
            for (int col = 0; col < COL_COUNT; col ++){
                grid[row][col] = Chip.EMPTY;
            }
        }
    }

    public boolean dropPiece(int col, Player player) {
        return true;
    }

    public boolean columnFull(int col) {
        if (grid[0][col] == Chip.EMPTY) {
            return false;
        } else {
            return true;
        }
    }

    public void resetBoard() {
        initalizeBoard();
    }
}
