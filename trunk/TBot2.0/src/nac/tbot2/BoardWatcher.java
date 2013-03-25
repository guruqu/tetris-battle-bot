/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot2;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author nathaniel
 */
public class BoardWatcher {

    private Robot robot;
    private BoardListener boardListener;
    private Timer timer;
    //
    private Rectangle bounds;
    private int rows = 20;
    private int columns = 10;
    private int gridWidth = 0;
    private int gridHeight = 0;
    private int[] boardColor;
    //
    private Rectangle nextAreaBounds;
    private int nextAreaRows = 2;
    private int nextAreaColumns = 4;
    private int nextAreaGridWidth = 0;
    private int nextAreaGridHeight = 0;
    private int[] nextAreaColor;
    //
    private Container pane;
    private boolean optionAsking = false;
    private List<String> options = Arrays.asList("I", "T", "O", "J", "L", "S", "Z");
    
    public BoardWatcher(Container pane) {
        this.pane = pane;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(BoardWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        stopCalibration();
        System.out.println("start");
        if (timer == null) {
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    watch();
                }
            }, 0, 100);
        }
    }

    public void stop() {
        System.out.println("stop");
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = null;
    }

    public void startCalibration() {
        stop();
        System.out.println("start calibration");
        if (timer == null) {
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    calibrate();
                }
            }, 0, 100);
        }
        TetraminoFactory.clear();
    }

    public void stopCalibration() {
        System.out.println("stop calibration");
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = null;
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
        nextAreaGridWidth = nextAreaBounds.width / columns;
        nextAreaGridHeight = nextAreaBounds.height / rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    private void watch() {

        if (bounds == null || nextAreaBounds == null || robot == null) {
            return;
        }

        int[] board = new int[rows];
        int[] b = new int[rows];
        int[] n = new int[nextAreaRows];

        BufferedImage nextAreaImage = robot.createScreenCapture(nextAreaBounds);
        for (int i = 0; i < nextAreaRows; i++) {
            for (int j = 0; j < nextAreaColumns; j++) {
                if (nextAreaImage != null) {
                    int px = j * nextAreaGridWidth + (nextAreaGridWidth / 2);
                    int py = i * nextAreaGridHeight + (nextAreaGridHeight / 2);

                    int rgb = nextAreaImage.getRGB(px, py);
                    n[i] += rgb;
                }

            }
        }

        BufferedImage boardImage = robot.createScreenCapture(bounds);
        for (int i = 0; i < rows; i++) {
            String rowStr = "";
            for (int j = 0; j < columns; j++) {
                if (boardImage != null) {
                    int px = j * gridWidth + (gridWidth / 2);
                    int py = i * gridHeight + (gridHeight / 2);

                    int rgb = boardImage.getRGB(px, py);
                    b[i] += rgb;
                    Tetramino[] piece = TetraminoFactory.get(rgb);
                    if (piece == null) {
                        rowStr += "0";
                    } else {
                        rowStr += "1";
                    }
                }

            }
            board[rows - i - 1] = Integer.reverse(Integer.parseInt(rowStr, 2));
        }
        if (nextAreaColor != null && !TBotUtils.arrayEquals(n, nextAreaColor)) {
            if (boardListener != null) {
                boardListener.onNextAreaChange(TetraminoFactory.get(TBotUtils.sumArray(n)));
            }
        }

        if (boardColor != null && b[0] != boardColor[0]) {
            if (boardListener != null) {
                boardListener.onPieceEntered();
            }
        }

        nextAreaColor = n;
        boardColor = b;
    }

    public void calibrate() {
        if (optionAsking) {
            return;
        }
        if (nextAreaBounds == null || robot == null) {
            return;
        }

        int[] n = new int[nextAreaRows];
        BufferedImage nextAreaImage = robot.createScreenCapture(nextAreaBounds);
        for (int i = 0; i < nextAreaRows; i++) {
            for (int j = 0; j < nextAreaColumns; j++) {
                if (nextAreaImage != null) {
                    int px = j * nextAreaGridWidth + (nextAreaGridWidth / 2);
                    int py = i * nextAreaGridHeight + (nextAreaGridHeight / 2);

                    int rgb = nextAreaImage.getRGB(px, py);
                    n[i] += rgb;
                }

            }
        }
        System.out.println("calibrate" + Arrays.toString(n));
        if (nextAreaColor != null && !TBotUtils.arrayEquals(n, nextAreaColor)) {
            optionAsking = true;
            int o = JOptionPane.showOptionDialog(pane, "Test", "Test", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options.toArray() , null);
            optionAsking = false;
            if (o != -1) {
                options.set(o, "-" + options.get(o) + "-");
                TetraminoFactory.set(o, TBotUtils.sumArray(n));
            }
        }
        nextAreaColor = n;
    }
}
