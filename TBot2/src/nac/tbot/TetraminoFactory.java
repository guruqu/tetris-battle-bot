/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * @author camomot
 */
public class TetraminoFactory {

  private static Tetramino[][] MINOS;
  private static final TetraminoFactory INSTANCE = new TetraminoFactory();
  private static final String MINOS_FILE = "tetraminos.json";
  private static final int lightBlue = -13451526; // new Color(50, 190,250).getRGB();
  private static final int purple = -2995027; // new Color(210, 76, 173).getRGB();
  private static final int yellow = -15835; // new Color(255, 194, 37).getRGB();
  private static final int blue = -12294935; // new Color(68, 100, 233).getRGB();
  private static final int orange = -33243; // new Color(255, 126, 37).getRGB();
  private static final int green = -8596444; // new Color(124, 212, 36).getRGB();
  private static final int red = -380326; // new Color(250, 50, 90).getRGB();
  private static int[] nextMap = new int[7];

  private TetraminoFactory() {
    Gson gson = new Gson();
    MINOS = gson.fromJson(new InputStreamReader(TetraminoFactory.class.getResourceAsStream(MINOS_FILE)), Tetramino[][].class);
    for (int i = 0; i < MINOS.length; i++) {
      Tetramino[] t = MINOS[i];
      for (int j = 0; j < t.length; j++) {
        t[j].translate();
      }
    }
  }

  public static Tetramino[] get(int type) {
    return MINOS[type];
  }

  public static int getIndexByRBG(int rgb) {
    switch (rgb) {
      case lightBlue:
        return 0;
      case purple:
        return 1;
      case yellow:
        return 2;
      case blue:
        return 3;
      case orange:
        return 4;
      case green:
        return 5;
      case red:
        return 6;
      default:
        return -1;
    }
  }

  public static void relate(int index, int rgb) {
    nextMap[index] = rgb;
  }

  public static int getNextIndex(int rgb) {
    for (int i = 0; i < nextMap.length; i++) {
      if (nextMap[i] == rgb) {
        return i;
      }
    }
    return -1;
  }
}
