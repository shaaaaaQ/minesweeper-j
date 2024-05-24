package minesweeper;

import java.util.Random;

public class Game {
    Board board;
    private GameState state;
    private final int mineNum;
    private boolean isFirst;
    private int flagNum;
    private int openedCellNum;

    Game(int width, int height, int mine) {
        board = new Board(width, height);
        state = GameState.Play;
        mineNum = mine;
        flagNum = 0;
        isFirst = true;
        openedCellNum = 0;
    }

    public boolean isPlay() {
        return state == GameState.Play;
    }

    public boolean isWin() {
        return state == GameState.Win;
    }

    public boolean isLose() {
        return state == GameState.Lose;
    }

    public void open(int x, int y) {
        if (isFirst) {
            isFirst = false;
            initMine(x, y);
        }

        Cell cell = board.getCell(x, y);

        if (!cell.isOpen && !cell.isFlag) {
            cell.isOpen = true;
            openedCellNum++;
            if (cell.isMine)
                state = GameState.Lose;
            else if ((board.getWidth() * board.getHeight()) - mineNum == openedCellNum)
                state = GameState.Win;
            else if (cell.vicinityMineNum == 0) {
                Cell[] vc = board.getVicinityCells(cell.x, cell.y);
                for (Cell c : vc) {
                    if (c.isFlag)
                        toggleFlag(c.x, c.y);
                    open(c.x, c.y);
                }
            }
        }
    }

    public void openVicinity(int x, int y) {
        Cell cell = board.getCell(x, y);
        Cell[] vc = board.getVicinityCells(x, y);
        int vf = 0;
        for (Cell c : vc)
            if (c.isFlag)
                vf++;
        if (cell.isOpen && (cell.vicinityMineNum == vf)) {
            for (Cell c : vc)
                open(c.x, c.y);
        }
    }

    public void toggleFlag(int x, int y) {
        Cell cell = board.getCell(x, y);
        if (!cell.isOpen) {
            flagNum += cell.isFlag ? -1 : 1;
            cell.isFlag = !cell.isFlag;
        }
        System.out.println(flagNum);
    }

    private void initMine(int fx, int fy) {
        int count = 0;

        while (count < mineNum) {
            int x = new Random().nextInt(board.getWidth());
            int y = new Random().nextInt(board.getHeight());

            if ((x <= fx + 1) && (x >= fx - 1) && (y <= fy + 1) && (y >= fy - 1))
                continue;

            Cell cell = board.getCell(x, y);

            if (cell.isMine)
                continue;

            cell.isMine = true;

            for (Cell vc : board.getVicinityCells(x, y))
                vc.vicinityMineNum++;

            count++;
        }
    }
}
