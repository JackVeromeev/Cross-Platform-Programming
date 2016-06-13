package notation;

import java.util.ArrayList;

import game2048onJavaFX.Game;

public class GameHistory {
  int rawNumber;
  int columnNumber;
  public int moves;
  int score;
  ArrayList<GameStep> history;
  public int currentStep = 0;

  public GameHistory() {
    rawNumber = Game.settings.rawNumber;
    columnNumber = Game.settings.columnNumber;
    history = new ArrayList<GameStep>();
    moves = 0;
  }

  public GameStep getNextStep() {
    if(currentStep < moves) {
      currentStep++;
    }
    /*
     * This if is needed in case if game loader late at loading next step
     * while replay bot wants to show it. In that case bot just passes
     * one iteration in replay
     */
    return history.get(currentStep - 1);
  }

  public boolean add(GameStep currentInfo) {
    moves++;
    score = currentInfo.score;
    return history.add(currentInfo);
  }
}
