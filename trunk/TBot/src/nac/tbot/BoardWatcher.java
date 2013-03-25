package nac.tbot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class BoardWatcher {

  private final int lightBlack = new Color(47, 47, 47).getRGB();
  private final int lightestBlack = new Color(77, 77, 77).getRGB();
  private final int darkBlack = new Color(43, 43, 43).getRGB();
  private final Dimension dimension = new Dimension(10 * 18, 20 * 18);
  private Robot robot = null;
  private Rectangle rect;
  private TState state = TState.STANDBY;
  private Keyboard keyboard = new Keyboard();
  private BotImpl bot = new BotImpl();
  private int newPieceRGB;
  private int hold = -1;
  private int current = -1;
  private boolean pressedShift = false;
  private int lastRowsRemoved = 0;
  private boolean run = true;

  public BoardWatcher() {
    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  public void setRun(boolean run) {
    this.run = run;
  }

  public void doUpdate() {
    if (run && rect != null) {
      
      BufferedImage image = robot.createScreenCapture(rect);

      int gridHeight = 18;
      int gridWidth = 18;
      int blankcount = 0;

      int[] board = new int[19];

      for (int i = 1, x = 19; i < 21; i++, x--) {
        String rowStr = "";
        for (int j = 1; j < 11; j++) {
          if (image != null) {
            int px = j * gridWidth - 9;
            int py = i * gridHeight - 9;
            int rgb = image.getRGB(px, py);
            if (rgb == lightBlack || rgb == darkBlack
                    || rgb == lightestBlack) {
              blankcount++;
              rowStr += "0";
            } else {
              rowStr += "1";
              if (i == 1 && (state == TState.STARTED || state == TState.WAIT_FOR_PIECE)) {
                state = TState.SENDING_PIECE;
                newPieceRGB = rgb;
              }
            }
          }

        }
        if (!rowStr.isEmpty()) {
          if (x != 19) {
            board[x] = Integer.parseInt(new StringBuffer(rowStr).reverse().toString(), 2);
          }
        }

      }

      if (blankcount == 200) {
        state = TState.STARTED;
        hold = -1;
        pressedShift = false;
        bot.clear();
      }
      
      if (state == TState.SENDING_PIECE) {
        current = TetraminoFactory.getIndexByRBG(newPieceRGB);

        if (current != -1) {

          if (hold == -1) {
            keyboard.sendShift();
            hold = current;

            pressedShift = true;
          } else {
            Move move1;
            Move move2;
            Move finalMove;
            boolean lrc = lastRowsRemoved > 0;
            if (pressedShift == false) {

              move1 = bot.move(new Board(board, 19, 10), current, lrc);
              move2 = bot.move(new Board(board, 19, 10), hold, lrc);

              if (move1 != null && move2 != null && move1.getScore() >= move2.getScore()) {
                finalMove = move1;
              } else {

                keyboard.sendShift();
                hold = current;

                pressedShift = true;
                finalMove = move2;
              }
            } else {
              finalMove = bot.move(new Board(board, 19, 10), current, lrc);
            }

            if (finalMove != null) {
              lastRowsRemoved = finalMove.getRowsRemoved();
              int rotation = finalMove.getRotation();
              
              if(rotation == 3){
                keyboard.sendRotateCounter();
              }else if(rotation == 2){
                keyboard.sendRotateClock();
                keyboard.sendRotateClock();
              }else if(rotation == 1){
                keyboard.sendRotateClock();
              }

              int column = finalMove.getColumn();
              int offset = column - finalMove.getTetramino().getOffset();

              if (offset > 0) {
                while (offset > 0) {
                  keyboard.sendRight();
                  offset--;
                }
              } else if (offset < 0) {
                while (offset < 0) {
                  keyboard.sendLeft();
                  offset++;
                }
              }
              pressedShift = false;
              keyboard.sendSpace();
            }
          }
          state = TState.WAIT_FOR_PIECE;
        } else {
          state = TState.STARTED;
          hold = -1;
          pressedShift = false;
          bot.clear();
        }
      }
    }
  }

  public void setLoc(Point p) {
    rect = new Rectangle(p, dimension);
  }

  public BotImpl getBot() {
    return bot;
  }

  public Keyboard getKeyboard() {
    return keyboard;
  }
}
