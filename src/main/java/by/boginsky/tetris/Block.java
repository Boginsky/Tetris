package by.boginsky.tetris;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.Color;
import java.awt.Graphics;

import static by.boginsky.tetris.Constants.ARC_RADIUS;
import static by.boginsky.tetris.Constants.BLOCK_SIZE;

@Data
@AllArgsConstructor
public class Block {

    private int x;
    private int y;

    void paint(Graphics g, int color) {
        g.setColor(new Color(color));
        g.drawRoundRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2, ARC_RADIUS, ARC_RADIUS);
    }
}
