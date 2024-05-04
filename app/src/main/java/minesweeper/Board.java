package minesweeper;

import java.util.ArrayList;

public class Board {
    private final Cell[][] board;
    private final int width;
    private final int height;

    Board(int width, int height) {
        this.width = width;
        this.height = height;
        board = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = new Cell(x, y);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        return board[y][x];
    }

    public Cell[] getVicinityCells(int x, int y) {
        ArrayList<Cell> result = new ArrayList<>();
        for (int r = y - 1; r <= y + 1; r++) {
            for (int c = x - 1; c <= x + 1; c++) {
                if ((r < 0) || (r >= height) || (c < 0) || (c >= width) || ((r == y) && (c == x))) continue;
                result.add(getCell(c, r));
            }
        }
        return result.toArray(new Cell[0]);
    }
}
