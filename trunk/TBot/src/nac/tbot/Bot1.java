package nac.tbot;

public class Bot1 implements Bot{

  public Move[] move(Board board, int currentMino, int nextMino) {
    double best_evaluation = -100000;
    double evaluation = 0;
    Move best_move1 = null;
    Move best_move2 = null;

    Tetramino[] cvar = TetraminoFactory.get(currentMino);
    Tetramino[] nvar = TetraminoFactory.get(currentMino);

    for (int i = 0; i < cvar.length; i++) {
      for (int j = 0; j < nvar.length; j++) {
        for (int c = 0; c < board.getColumns() - cvar[i].getWidth() + 1; c++) {
          for (int n = 0; n < board.getColumns() - nvar[j].getWidth() + 1; n++) {
            evaluation = 0;

            Move move1 = new Move(cvar[i], i, c);
            Move move2 = new Move(nvar[j], j, n);

            Board newBoard1 = board.apply(move1);
            if (!move1.isGameOver()) {
              evaluation += newBoard1.evaluate();

              Board newBoard2 = board.apply(move2);
              if (move2.isGameOver()) {
                if (evaluation > best_evaluation) {
                  best_move1 = move1;
                }
              } else {
                evaluation += newBoard2.evaluate();

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
    }
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
         
          if(newBoard1.getBoardHeight() < 7 && move1.getRowsRemoved() == 1){
              evaluation = -9999999;
          }else if(newBoard1.getBoardHeight() < 5 && move1.getRowsRemoved() == 2){
              evaluation = -9999999;
          }else{
             evaluation = newBoard1.evaluate() + move1.evaluate();
          }
          
          if (evaluation > best_evaluation) {
            best_evaluation = evaluation;
            best_move1 = move1;
          }
        }
      }
    }
    
    if(best_move1 != null){
        best_move1.setScore(best_evaluation);
    }
  
    return best_move1;
  }
}
