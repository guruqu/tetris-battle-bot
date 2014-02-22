/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DTIT
 */
public class TetrisBattleBoard {

  private static final TetrisBattleBoard INSTANCE = new TetrisBattleBoard();
  private Point initialPoint = null;
  private Rectangle boardRect = null;
  private Robot robot;

  public static TetrisBattleBoard instance() {
    return INSTANCE;
  }

  private TetrisBattleBoard() {
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTFocusListener(), AWTEvent.MOUSE_EVENT_MASK |AWTEvent.FOCUS_EVENT_MASK);
    try {
      robot = new Robot();
    } catch (AWTException ex) {
      Logger.getLogger(TetrisBattleBoard.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private class AWTFocusListener implements AWTEventListener {

    @Override
    public void eventDispatched(AWTEvent event) {
      System.out.println(event.getID());
      if (event.getID() == FocusEvent.FOCUS_LAST) {
        initialPoint = MouseInfo.getPointerInfo().getLocation();
        findBorders();
      }
    }
  }

  private void findBorders() {
    int x = initialPoint.x;
    int y = initialPoint.y;
    Color color;
    int blackCount = 0;

    while (x > 0) {
      
      color = robot.getPixelColor(x, y);
      
      if (color.equals(Color.BLACK)) {
        blackCount++;
      }else{
        blackCount = 0;
      }
      if(blackCount == 6){
        robot.mouseMove(x, y);
        System.out.println(x +  " " + y);
        return;
      }else{
        
      }
      x--;
    }

  }
}
