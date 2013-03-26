package nac.tbot;

public class BotImpl implements Bot {

  private final int BUILDUP = 1;
  private final int BREAKDOWN = 2;
  private int phase = BUILDUP;
  private int breakDownLimit = 5;
  private int buildUpLimit = 15;
  private int towerGap = 2;

  @Override
  public Move move(Board board, int currentMino, boolean lrc) {
    double best_evaluation = -100000;
    double evaluation;
    Move best_move1 = null;

    Tetramino[] cvar = TetraminoFactory.get(currentMino);

    int cvarlen = cvar.length;
    int columns = board.getColumns();

    if (phase == BUILDUP) {
      if (board.getBoardHeight() <= buildUpLimit) {
        columns -= towerGap;
      } else {
        phase = BREAKDOWN;
      }
    } else if (phase == BREAKDOWN && board.getBoardHeight() <= breakDownLimit) {
      phase = BUILDUP;
    }
    for (int i = 0; i < cvarlen; i++) {
      for (int c = 0; c < columns - cvar[i].getWidth() + 1; c++) {

        Move move1 = new Move(cvar[i], i, c);
        Board newBoard1 = board.apply(move1);
        if (!move1.isGameOver()) {

          evaluation = newBoard1.evaluate() + move1.evaluate();

          if (evaluation > best_evaluation) {
            best_evaluation = evaluation;
            best_move1 = move1;
          }
        }
      }
    }

    if (best_move1 != null) {
      best_move1.setScore(best_evaluation);
    }

    return best_move1;
  }

  @Override
  public Move[] move(Board board, int currentMino, int nextMino, boolean lrc) {
    double best_evaluation = -100000;
    double evaluation;
    Move best_move1 = null;
    Move best_move2 = null;

    Tetramino[] cvar1 = TetraminoFactory.get(currentMino);
    Tetramino[] cvar2 = TetraminoFactory.get(nextMino);

    int cvarlen1 = cvar1.length;
    int cvarlen2 = cvar2.length;
    int columns = board.getColumns();

    if (phase == BUILDUP) {
      if (board.getBoardHeight() <= buildUpLimit) {
        columns -= towerGap;
      } else {
        phase = BREAKDOWN;
      }
    } else if (phase == BREAKDOWN && board.getBoardHeight() <= breakDownLimit) {
      phase = BUILDUP;
    }

    for (int i1 = 0; i1 < cvarlen1; i1++) {
      for (int c1 = 0; c1 < columns - cvar1[i1].getWidth() + 1; c1++) {
        for (int i2 = 0; i2 < cvarlen2; i2++) {
          for (int c2 = 0; c2 < columns - cvar2[i2].getWidth() + 1; c2++) {

            Move move1 = new Move(cvar1[i1], i1, c1);
            Board newBoard1 = board.apply(move1);

            Move move2 = new Move(cvar2[i2], i2, c2);
            Board newBoard2 = newBoard1.apply(move2);

            if (!move1.isGameOver() && !move2.isGameOver()) {
              evaluation = newBoard2.evaluate() + move1.evaluate() + move2.evaluate();

              if (evaluation > best_evaluation) {
                best_evaluation = evaluation;
                best_move1 = move1;
                best_move2 = move2;
              }
            }

          }
        }
      }
    }

    if (best_move1 != null) {
      best_move1.setScore(best_evaluation);
    }
    if (best_move2 != null) {
      best_move2.setScore(best_evaluation);
    }
    return new Move[]{best_move1, best_move2};
  }

  public void setBreakDownLimit(int breakDownLimit) {
    this.breakDownLimit = breakDownLimit;
  }

  public void setBuildUpLimit(int buildUpLimit) {
    this.buildUpLimit = buildUpLimit;
  }

  public void setTowerGap(int towerGap) {
    this.towerGap = towerGap;
  }

  public void clear() {
    phase = BUILDUP;
  }
}
