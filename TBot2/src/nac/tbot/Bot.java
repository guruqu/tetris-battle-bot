package nac.tbot;

public class Bot {

  public Move[] move(Board board, int currentMino, int nextMino) {
    double best_evaluation = -100000;
    double evaluation = 0;
    Move best_move1 = null;
    Move best_move2 = null;

    Tetramino[] cvar = TetraminoFactory.get(currentMino);
    Tetramino[] nvar = TetraminoFactory.get(nextMino);

    for (int i = 0; i < cvar.length; i++) {
      for (int j = 0; j < nvar.length; j++) {
        for (int c = 0; c < board.getColumns() - cvar[i].getWidth() + 1; c++) {
          for (int n = 0; n < board.getColumns() - nvar[j].getWidth() + 1; n++) {

            Move move1 = new Move(cvar[i], i, c);
            Move move2 = new Move(nvar[j], j, n);
           
            Board newBoard1 = board.apply(move1);
            if (!move1.isGameOver()) {

              Board newBoard2 = newBoard1.apply(move2);
              if (move2.isGameOver()) {
                evaluation = newBoard1.evaluate() + move1.evaluate();
//                System.out.println("e:" + evaluation);
                if (evaluation > best_evaluation) {
//                  System.out.println("best e:" + evaluation);
                  best_evaluation = evaluation;
                  best_move1 = move1;
                }
              } else {
                evaluation = newBoard2.evaluate() + move1.evaluate() + move2.evaluate();
//                System.out.println("e:" + evaluation);
                if (evaluation > best_evaluation) {
//                  System.out.println("best e:" + evaluation);
                  best_evaluation = evaluation;
                  best_move1 = move1;
                  best_move2 = move2;
                }
              }
            }
//             System.out.println("test " + move1 + "=====" + move2 + " score:" + evaluation);
          }
        }
      }
    }
//    System.out.println("FINAL : " + best_evaluation);
    return new Move[]{best_move1, best_move2};
  }

  public Move move(Board board, int currentMino) {
    double best_evaluation = -100000;
    double evaluation;
    Move best_move1 = null;

    Tetramino[] cvar = TetraminoFactory.get(currentMino);

    for (int i = 0; i < cvar.length; i++) {
      for (int c = 0; c < board.getColumns() - cvar[i].getWidth() + 1; c++) {

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

    best_move1.setScore(best_evaluation);
    return best_move1;
  }
}
