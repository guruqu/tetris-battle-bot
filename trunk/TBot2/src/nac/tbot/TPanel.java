package nac.tbot;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class TPanel extends JPanel {

  public final Dimension d = new Dimension(10 * 18, 20 * 18);
  private final int lightBlack = new Color(47, 47, 47).getRGB();
  private final int lightestBlack = new Color(77, 77, 77).getRGB();
  private final int darkBlack = new Color(43, 43, 43).getRGB();
  private final int partida1 = new Color(56, 56, 56).getRGB();
  private final int partida2 = new Color(79, 79, 79).getRGB();
  private Robot robot;
  private Rectangle rect;
  private Point nextPoint;
  private TState state = TState.STANDBY;
  private Keyboard keyboard = new Keyboard();
  private Bot bot = new Bot();
  private int currentPieceRGB;
  private int lastNextPieceRGB;
  private int hold = -1;
  private int current = -1;
  private int next = -1;
  private boolean pressedShift = false;
  private int lastRowsRemoved = 0;

  public TPanel() {
    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    BufferedImage image = null;
    int height = getHeight();
    int width = getWidth();

    if (rect != null) {
      image = robot.createScreenCapture(rect);
    }

    g2d.setColor(Color.red);
    g2d.setStroke(new BasicStroke(2F));

    int gridHeight = 18;
    int gridWidth = 18;

    for (int i = 1; i < 10; i++) {
      g2d.drawLine(i * gridWidth, 0, i * gridWidth, height);
    }

    for (int i = 1; i < 20; i++) {
      g2d.drawLine(0, i * gridHeight, width, i * gridHeight);
    }

    g2d.setStroke(new BasicStroke(1F));
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
            g2d.setColor(Color.white);
            int rx = j * gridWidth - 13;
            int ry = i * gridHeight - 13;
            g2d.fillRect(rx, ry, 8, 8);
            blankcount++;
            rowStr += "0";
          } else {
            g2d.setColor(new Color(rgb));
            int rx = j * gridWidth - 13;
            int ry = i * gridHeight - 13;
            g2d.fillRect(rx, ry, 8, 8);
            rowStr += "1";

            if (i == 1 && (state == TState.STARTED || state == TState.WAIT_FOR_PIECE)) {
              state = TState.SENDING_PIECE;
              currentPieceRGB = rgb;
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
    }
    if (state == TState.SENDING_PIECE) {
      current = TetraminoFactory.getIndexByRBG(currentPieceRGB);

      if (current != -1 && lastNextPieceRGB != -1 && TetraminoFactory.getNextIndex(lastNextPieceRGB) == -1) {
        TetraminoFactory.relate(current, lastNextPieceRGB);
      }

      lastNextPieceRGB = robot.getPixelColor(nextPoint.x, nextPoint.y).getRGB();
      next = TetraminoFactory.getNextIndex(lastNextPieceRGB);

      if (current != -1) {

//        if (hold == -1) {
//          keyboard.sendShift();
//          hold = current;
//
//          pressedShift = true;
//        } else {
        Move move1;
        Move move2;
        Move finalMove = null;
        Move[] finalMoves = null;

//          boolean lrc = lastRowsRemoved > 0;
        if (false) {

          move1 = bot.move(new Board(board, 19, 10), current);
          move2 = bot.move(new Board(board, 19, 10), hold);

          if (move1.getScore() >= move2.getScore()) {
            finalMove = move1;
          } else {

            keyboard.sendShift();
            hold = current;

            pressedShift = true;
            finalMove = move2;
          }
        } else {

          if (next == -1) {
            finalMove = bot.move(new Board(board, 19, 10), current);
          } else {
//              System.out.println("current: " + current + ", next" + next);
            finalMoves = bot.move(new Board(board, 19, 10), current, next);
          }
        }

        if (finalMove != null) {
          lastRowsRemoved = finalMove.getRowsRemoved();
          int rotation = finalMove.getRotation();

          while (rotation > 0) {
            keyboard.sendRotate();
            rotation--;
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
        } else {
          Move finalMoves1 = finalMoves[0];
          Move finalMoves2 = finalMoves[1];
//            System.out.println("FINAL " + finalMoves1 + "=====" + finalMoves2);
          if (finalMoves1 != null) {
            int rotation = finalMoves1.getRotation();

            while (rotation > 0) {
              keyboard.sendRotate();
              rotation--;
            }

            int column = finalMoves1.getColumn();
            int offset = column - finalMoves1.getTetramino().getOffset();

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
            keyboard.sendSpace();

          }
          if (finalMoves2 != null) {
            int rotation = finalMoves2.getRotation();

            while (rotation > 0) {
              keyboard.sendRotate();
              rotation--;
            }

            int column = finalMoves2.getColumn();
            int offset = column - finalMoves2.getTetramino().getOffset();

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
            keyboard.sendSpace();
          }
          pressedShift = false;
        }

//        }
        state = TState.WAIT_FOR_PIECE;
      } else {
        state = TState.STARTED;
      }

    }
//        System.out.println(state);
  }

  @Override
  public Dimension getPreferredSize() {
    return d;
  }

  public void setLoc() {
    hold = -1;
    current = -1;
    next = -1;
    pressedShift = false;
    lastRowsRemoved = 0;
    Point location = getLocationOnScreen();
    rect = new Rectangle(location, d);
    nextPoint = new Point(location.x + 220, location.y + 48);
  }

  public void setLoc(Point p) {
    hold = -1;
    current = -1;
    next = -1;
    pressedShift = false;
    lastRowsRemoved = 0;
    rect = new Rectangle(p, d);
    nextPoint = new Point(p.x + 220, p.y + 48);
  }

  public void resetLoc() {
    hold = -1;
    current = -1;
    next = -1;
    pressedShift = false;
    lastRowsRemoved = 0;
    rect = null;
    nextPoint = null;
    state = TState.STANDBY;
  }

  public boolean isSetLoc() {
    return (rect == null) ? false : true;
  }
}
