package minesweeper;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.core.rCore;
import com.raylib.java.core.input.Mouse.MouseButton;
import com.raylib.java.shapes.Rectangle;
import com.raylib.java.shapes.rShapes;

public class App {
    static Raylib rlj = new Raylib();

    public static void main(String[] args) {
        int padding = 20;
        int cellSize = 50;
        int fontSize = 35;

        int col = 9;
        int row = 7;
        int mine = 10;

        rlj.core.InitWindow(padding * 2 + col * cellSize, padding * 2 + row * cellSize, "Minesweeper Raylib-J");

        Game game = new Game(col, row, mine);

        while (!rlj.core.WindowShouldClose()) {
            int screenWidth = rCore.GetScreenWidth();
            int screenHeight = rCore.GetScreenHeight();

            rlj.core.BeginDrawing();
            rlj.core.ClearBackground(Color.SKYBLUE);

            for (int y = 0; y < game.board.getHeight(); y++) {
                for (int x = 0; x < game.board.getWidth(); x++) {
                    Cell cell = game.board.getCell(x, y);
                    Rectangle cellRec = new Rectangle(padding + x * cellSize, padding + y * cellSize, cellSize,
                            cellSize);

                    Color cellColor;
                    if (cell.isOpen)
                        cellColor = Color.GRAY;
                    else if ((game.isPlay())
                            && rlj.shapes.CheckCollisionPointRec(rCore.GetMousePosition(), cellRec))
                        // プレイ中 & ホバー中
                        cellColor = Color.SKYBLUE;
                    else if (((x + y) % 2) == 0)
                        cellColor = Color.BLUE;
                    else
                        cellColor = Color.DARKBLUE;

                    rShapes.DrawRectangleRec(cellRec, cellColor);

                    if (cell.isOpen) {
                        if (cell.isMine) {
                            drawTextCenter("B", cellRec, fontSize, Color.DARKBROWN);
                        } else if (!(cell.vicinityMineNum == 0)) {
                            drawTextCenter(String.valueOf(cell.vicinityMineNum), cellRec, fontSize, Color.BLACK);
                        }
                    } else if (cell.isFlag) {
                        drawTextCenter("F", cellRec, fontSize, Color.RED);
                    }

                    if (game.isPlay()) {
                        if (rlj.shapes.CheckCollisionPointRec(rCore.GetMousePosition(), cellRec)) {
                            if (rlj.core.IsMouseButtonPressed(MouseButton.MOUSE_BUTTON_LEFT)) {
                                game.open(x, y);
                            } else if (rlj.core.IsMouseButtonPressed(MouseButton.MOUSE_BUTTON_RIGHT)) {
                                game.toggleFlag(x, y);
                            } else if (rlj.core.IsMouseButtonPressed(MouseButton.MOUSE_BUTTON_MIDDLE)) {
                                game.openVicinity(x, y);
                            }
                        }
                    }
                }
            }

            if (game.isWin() || game.isLose()) {
                Rectangle screenRec = new Rectangle(0, 0, screenWidth, screenHeight);
                rShapes.DrawRectangleRec(screenRec, new Color(255, 255, 255, 200));
                String labelText = game.isWin() ? "Game Clear" : "Game Over";
                int labelFontSize = 60;
                int labelTextWidth = rlj.text.MeasureText(labelText, labelFontSize);
                int posX = ((screenWidth / 2) - (labelTextWidth / 2));
                int posY = ((screenHeight / 3) - (labelFontSize / 2));
                rlj.text.DrawText(labelText, posX, posY, labelFontSize, Color.BLACK);

                String buttonText = "Retry";
                int buttonFontSize = 40;
                int buttonTextWidth = rlj.text.MeasureText(buttonText, buttonFontSize);
                float buttonWidth = buttonTextWidth + 60;
                float buttonHeight = (float) (buttonFontSize * 1.5);
                Rectangle buttonRec = rectFromAnchor(screenRec, Anchor.Center, 0, 60, buttonWidth, buttonHeight);
                boolean isHover = rlj.shapes.CheckCollisionPointRec(rCore.GetMousePosition(), buttonRec);
                Color buttonColor = isHover ? new Color(190, 198, 213, 255) : new Color(156, 163, 175, 255);
                rShapes.DrawRectangleRec(buttonRec, buttonColor);
                drawTextCenter(buttonText, buttonRec, buttonFontSize, Color.BLACK);
                if (isHover && rlj.core.IsMouseButtonReleased(MouseButton.MOUSE_BUTTON_LEFT))
                    game = new Game(col, row, mine);
            }

            rlj.core.EndDrawing();
        }
    }

    private static void drawTextCenter(String text, Rectangle rec, int fontSize, Color color) {
        int textWidth = rlj.text.MeasureText(text, fontSize);
        int posX = (int) (rec.x + (rec.width / 2) - (textWidth / 2));
        int posY = (int) (rec.y + (rec.height / 2) - (fontSize / 2));
        rlj.text.DrawText(text, posX, posY, fontSize, color);
    }

    private static Rectangle rectFromAnchor(Rectangle parentRec, Anchor anchor, float offsetX, float offsetY,
            float width, float height) {
        float x = 0, y = 0;

        switch (anchor) {
            case Center:
                x = parentRec.x + (parentRec.width / 2 - width / 2);
                y = parentRec.y + (parentRec.height / 2 - height / 2);
                break;
            case TopLeft:
                x = parentRec.x;
                y = parentRec.y;
                break;
            case TopRight:
                x = parentRec.x + (parentRec.width - width);
                y = parentRec.y;
                break;
            case BottomLeft:
                x = parentRec.x;
                y = parentRec.y + (parentRec.height - height);
                break;
            case BottomRight:
                x = parentRec.x + (parentRec.width - width);
                y = parentRec.y + (parentRec.height - height);
                break;
        }

        x += offsetX;
        y += offsetY;

        return new Rectangle(x, y, width, height);
    }
}
