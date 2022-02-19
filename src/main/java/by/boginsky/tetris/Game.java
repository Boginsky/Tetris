package by.boginsky.tetris;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import static by.boginsky.tetris.Constants.BLOCK_SIZE;
import static by.boginsky.tetris.Constants.DELAY_ANIMATION;
import static by.boginsky.tetris.Constants.DOWN;
import static by.boginsky.tetris.Constants.FIELD_DX;
import static by.boginsky.tetris.Constants.FIELD_DY;
import static by.boginsky.tetris.Constants.FIELD_HEIGHT;
import static by.boginsky.tetris.Constants.FIELD_WIDTH;
import static by.boginsky.tetris.Constants.GAME_OVER_MSG;
import static by.boginsky.tetris.Constants.LEFT;
import static by.boginsky.tetris.Constants.RIGHT;
import static by.boginsky.tetris.Constants.SCORES;
import static by.boginsky.tetris.Constants.START_LOCATION;
import static by.boginsky.tetris.Constants.TITLE_OF_PROGRAM;
import static by.boginsky.tetris.Constants.UP;
import static by.boginsky.tetris.Constants.gameOver;
import static by.boginsky.tetris.Constants.mine;

public class Game extends JFrame {

    private final Canvas canvasPanel = new Canvas();
    private transient Figure figure = new Figure();
    private int gameScore = 0;

    public static void main(String[] args) {
        new Game().start();
    }

    Game() {
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, FIELD_WIDTH * BLOCK_SIZE + FIELD_DX, FIELD_HEIGHT * BLOCK_SIZE + FIELD_DY);
        setResizable(false);
        canvasPanel.setBackground(Color.black);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == DOWN) figure.drop();
                    if (e.getKeyCode() == UP) figure.rotate();
                    if (e.getKeyCode() == LEFT || e.getKeyCode() == RIGHT) figure.move(e.getKeyCode());
                }
                canvasPanel.repaint();
            }
        });
        add(BorderLayout.CENTER, canvasPanel);
        setVisible(true);
        Arrays.fill(mine[FIELD_HEIGHT], 1);
    }

    void start() {
        while (!gameOver) {
            try {
                Thread.sleep(DELAY_ANIMATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvasPanel.repaint();
            checkFilling();
            if (figure.isTouchGround()) {
                figure.leaveOnTheGround();
                figure = new Figure();
                gameOver = figure.isCrossGround();
            } else
                figure.stepDown();
        }
    }

    void checkFilling() {
        int row = FIELD_HEIGHT - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < FIELD_WIDTH; col++)
                filled *= Integer.signum(mine[row][col]);
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) System.arraycopy(mine[i - 1], 0, mine[i], 0, FIELD_WIDTH);
            } else
                row--;
        }
        if (countFillRows > 0) {
            gameScore += SCORES[countFillRows - 1];
            setTitle(TITLE_OF_PROGRAM + " : " + gameScore);
        }
    }

    class Canvas extends JPanel {

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < FIELD_WIDTH; x++)
                for (int y = 0; y < FIELD_HEIGHT; y++) {
                    if (x < FIELD_WIDTH - 1 && y < FIELD_HEIGHT - 1) {
                        g.setColor(Color.lightGray);
                        g.drawLine((x + 1) * BLOCK_SIZE - 2, (y + 1) * BLOCK_SIZE, (x + 1) * BLOCK_SIZE + 2, (y + 1) * BLOCK_SIZE);
                        g.drawLine((x + 1) * BLOCK_SIZE, (y + 1) * BLOCK_SIZE - 2, (x + 1) * BLOCK_SIZE, (y + 1) * BLOCK_SIZE + 2);
                    }
                    if (mine[y][x] > 0) {
                        g.setColor(new Color(mine[y][x]));
                        g.fill3DRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 1, BLOCK_SIZE - 1, true);
                    }
                }
            if (gameOver) {
                g.setColor(Color.white);
                for (int y = 0; y < GAME_OVER_MSG.length; y++)
                    for (int x = 0; x < GAME_OVER_MSG[y].length; x++)
                        if (GAME_OVER_MSG[y][x] == 1) g.fill3DRect(x * 11 + 18, y * 11 + 160, 10, 10, true);
            } else
                figure.paint(g);
        }
    }
}
