package nac.tbot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Keyboard {

  private Robot robot;
  private static final int delay1 = 65;
  private static final int delay2 = 50;

  public Keyboard() {
    try {
      robot = new Robot();
    } catch (AWTException ex) {
      Logger.getLogger(Keyboard.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void sendSpace() {
    robot.keyPress(KeyEvent.VK_SPACE);
    robot.delay(delay1);
    robot.keyRelease(KeyEvent.VK_SPACE);
    robot.delay(delay2);
  }

  public void sendLeft() {
    robot.keyPress(KeyEvent.VK_LEFT);
    robot.delay(delay1);
    robot.keyRelease(KeyEvent.VK_LEFT);
    robot.delay(delay2);
  }

  public void sendRight() {
    robot.keyPress(KeyEvent.VK_RIGHT);
    robot.delay(delay1);
    robot.keyRelease(KeyEvent.VK_RIGHT);
    robot.delay(delay2);
  }

  public void sendRotate() {
    robot.keyPress(KeyEvent.VK_UP);
    robot.delay(delay1);
    robot.keyRelease(KeyEvent.VK_UP);
    robot.delay(delay2);
  }

  public void sendShift() {
    robot.keyPress(KeyEvent.VK_SHIFT);
    robot.delay(delay1);
    robot.keyRelease(KeyEvent.VK_SHIFT);
    robot.delay(delay2);
  }
}
