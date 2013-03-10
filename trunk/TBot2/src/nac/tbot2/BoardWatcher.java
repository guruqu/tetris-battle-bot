/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot2;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nathaniel
 */
public class BoardWatcher {

    private Rectangle nextAreaBounds;
    private Rectangle bounds;
    private int rows = 20;
    private int columns = 10;
    private Robot robot;
    private int gridWidth = 0;
    private int gridHeight = 10;
    private Timer timer;
    private BoardListener boardListener;
    private int[] boardColor;

    public BoardWatcher() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(BoardWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                watch();
            }
        }, 0, 100);
    }

    public void setBoardListener(BoardListener boardListener) {
        this.boardListener = boardListener;
    }

    public BoardListener getBoardListener() {
        return boardListener;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
        gridWidth = bounds.width / columns;
        gridHeight = bounds.height / rows;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getNextAreaBounds() {
        return nextAreaBounds;
    }

    public void setNextAreaBounds(Rectangle nextAreaBounds) {
        this.nextAreaBounds = nextAreaBounds;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void watch() {

        if (bounds == null) {
            return;
        }

        if (robot == null) {
            return;
        }

        BufferedImage image = robot.createScreenCapture(bounds);
        int[] board = new int[rows];
        int[] b = new int[rows];
//        System.out.println("BOARD");
        for (int i = 0; i < rows; i++) {
            String rowStr = "";
            for (int j = 0; j < columns; j++) {
                if (image != null) {
                    int px = j * gridWidth + (gridWidth / 2);
                    int py = i * gridHeight + (gridHeight / 2);

                    int rgb = image.getRGB(px, py);
                    b[i] += rgb;
                    Tetramino[] piece = TetraminoFactory.get(rgb);
                    if (piece == null) {
                        rowStr += "0";
                    } else {
                        rowStr += "1";
                    }
                }

            }
//            System.out.println(rows - i + " " + rowStr);
            board[rows - i - 1] = Integer.reverse(Integer.parseInt(rowStr, 2));
        }
        if (boardColor != null && b[0] != boardColor[0]) {
            System.out.println("CHANGE");
        }
        boardColor = b;//        System.out.println("BOARD");
    }
}
