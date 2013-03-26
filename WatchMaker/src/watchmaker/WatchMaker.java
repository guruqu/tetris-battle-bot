/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package watchmaker;

import nac.tbot.Board;
import nac.tbot.Bot;
import nac.tbot.BotImpl;
import nac.tbot.Move;
import org.uncommons.maths.random.MersenneTwisterRNG;

/**
 *
 * @author Administrator
 */
public class WatchMaker {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    int highestScore = 0;
    for (int tg = 0; tg < 5; tg++) {
      for (int bul = 0; bul < 18; bul++) {
        for (int bdl = 0; bdl < 18; bdl++) {
          BotImpl bot = new BotImpl();
          bot.setBreakDownLimit(bdl);
          bot.setBuildUpLimit(bul);
          bot.setTowerGap(tg);
          System.out.println("---TEST---");
          System.out.println("BDL:" + bdl + ", BUL:" + bul + ", TG:" + tg);
          int score = testBot2(bot);
          System.out.println("SCORE:" + score);
          if (score > highestScore) {
            highestScore = score;
            System.out.println("HIGH-SCORE:" + highestScore);
          }
        }
      }
    }

  }

  private static int testBot(Bot bot) {
    int testSize = 20;
    int score = 0;
    MersenneTwisterRNG rng = new MersenneTwisterRNG();
    for (int t = 0; t < testSize; t++) {
      Board board = new Board(20, 10);
      int points = 0;
      int comboCount = 0;
      for (int i = 0; i < 400; i++) {
        int piece = rng.nextInt(7);
        Move move = bot.move(board, piece, true);
        if (move != null) {
          int rowsRemoved = move.getRowsRemoved();
          if (rowsRemoved == 4) {
            points += 4;
          } else if (rowsRemoved > 0) {
            points += (rowsRemoved - 1);
          }
          if (comboCount >= 7) {
            points += 4;
          } else if (comboCount > 1) {
            points += Math.ceil((comboCount - 1) / 2);
          }

          if (rowsRemoved == 0) {
            comboCount = 0;
          } else {
            comboCount++;
          }
          if (move.isGameOver()) {
            break;
          } else {
            board = board.apply(move);
          }
        }

      }
      score += points;
    }
    return score / testSize;
  }
  
  private static int testBot2(Bot bot) {
    int testSize = 20;
    int score = 0;
    MersenneTwisterRNG rng = new MersenneTwisterRNG();
    for (int t = 0; t < testSize; t++) {
      
      Board board = new Board(20, 10);
      int points = 0;
      int comboCount = 0;
      
      for (int i = 0; i < 200; i++) {
        int piece1 = rng.nextInt(7);
        int piece2 = rng.nextInt(7);
        
        Move[] moves = bot.move(board, piece1, piece2, true); 
        if (moves[0] != null) {
          int rowsRemoved = moves[0].getRowsRemoved();
          if (rowsRemoved == 4) {
            points += 4;
          } else if (rowsRemoved > 0) {
            points += (rowsRemoved - 1);
          }
          if (comboCount >= 7) {
            points += 4;
          } else if (comboCount > 1) {
            points += Math.ceil((comboCount - 1) / 2);
          }

          if (rowsRemoved == 0) {
            comboCount = 0;
          } else {
            comboCount++;
          }
          if (moves[0].isGameOver()) {
            break;
          } else {
            board = board.apply(moves[0]);
          }
        }
        
        if (moves[1] != null) {
          int rowsRemoved = moves[1].getRowsRemoved();
          if (rowsRemoved == 4) {
            points += 4;
          } else if (rowsRemoved > 0) {
            points += (rowsRemoved - 1);
          }
          if (comboCount >= 7) {
            points += 4;
          } else if (comboCount > 1) {
            points += Math.ceil((comboCount - 1) / 2);
          }

          if (rowsRemoved == 0) {
            comboCount = 0;
          } else {
            comboCount++;
          }
          if (moves[1].isGameOver()) {
            break;
          } else {
            board = board.apply(moves[1]);
          }
        }
      }
      score += points;
    }
    return score / testSize;
  }
}
