package minesweeper;

public class Cell {
    boolean isMine = false;
    boolean isOpen = false;
    boolean isFlag = false;
    int vicinityMineNum = 0;
    int x;
    int y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
