package by.boginsky.tetris;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import static by.boginsky.tetris.Constants.FIELD_WIDTH;
import static by.boginsky.tetris.Constants.LEFT;
import static by.boginsky.tetris.Constants.RIGHT;
import static by.boginsky.tetris.Constants.SHAPES;
import static by.boginsky.tetris.Constants.mine;

public class Figure {
    private final ArrayList<Block> typeOfFigure = new ArrayList<>();
    private final int[][] shape = new int[4][4];
    private final int size;
    private final int color;
    private int x = 3;
    private int y = 0;
    Random random = new Random();

    Figure() {
        int type = random.nextInt(SHAPES.length);
        size = SHAPES[type][4][0];
        color = SHAPES[type][4][1];
        if (size == 4) y = -1;
        for (int i = 0; i < size; i++)
            System.arraycopy(SHAPES[type][i], 0, shape[i], 0, SHAPES[type][i].length);
        createFromShape();
    }

    void createFromShape() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) typeOfFigure.add(new Block(x + this.x, y + this.y));
    }

    boolean isTouchGround() {
        for (Block block : typeOfFigure) if (mine[block.getY() + 1][block.getX()] > 0) return true;
        return false;
    }

    boolean isCrossGround() {
        for (Block block : typeOfFigure) if (mine[block.getY()][block.getX()] > 0) return true;
        return false;
    }

    void leaveOnTheGround() {
        for (Block block : typeOfFigure) mine[block.getY()][block.getX()] = color;
    }

    boolean isTouchWall(int direction) {
        for (Block block : typeOfFigure) {
            if (direction == LEFT && (block.getX() == 0 || mine[block.getY()][block.getX() - 1] > 0)) return true;
            if (direction == RIGHT && (block.getX() == FIELD_WIDTH - 1 || mine[block.getY()][block.getX() + 1] > 0))
                return true;
        }
        return false;
    }

    void move(int direction) {
        if (!isTouchWall(direction)) {
            int dx = direction - 38;
            for (Block block : typeOfFigure) block.setX(block.getX() + dx);
            x += dx;
        }
    }

    void stepDown() {
        for (Block block : typeOfFigure) block.setY(block.getY() + 1);
        y++;
    }

    void drop() {
        while (!isTouchGround()) stepDown();
    }

    boolean isWrongPosition() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) {
                    if (y + this.y < 0) return true;
                    if (x + this.x < 0 || x + this.x > FIELD_WIDTH - 1) return true;
                    if (mine[y + this.y][x + this.x] > 0) return true;
                }
        return false;
    }

    void rotateShape(int direction) {
        for (int i = 0; i < size / 2; i++)
            for (int j = i; j < size - 1 - i; j++)
                if (direction == RIGHT) {
                    int tmp = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[i][j];
                    shape[i][j] = tmp;
                } else { // counterclockwise
                    int tmp = shape[i][j];
                    shape[i][j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = tmp;
                }
    }

    void rotate() {
        rotateShape(RIGHT);
        if (!isWrongPosition()) {
            typeOfFigure.clear();
            createFromShape();
        } else
            rotateShape(LEFT);
    }

    void paint(Graphics g) {
        for (Block block : typeOfFigure) block.paint(g, color);
    }
}
