package notation;

import java.util.ArrayList;

import game2048onJavaFX.Game;

public class GameHistory {
  int rawNumber;
  int columnNumber;
  int moves;
  int score;
  ArrayList<GameStep> history;
  int currentStep = 0;

  public GameHistory() {
    rawNumber = Game.settings.rawNumber;
    columnNumber = Game.settings.columnNumber;
    history = new ArrayList<GameStep>();
    moves = 0;
  }

  public GameStep getNextStep() {
    currentStep++;
    return history.get(currentStep - 1);
  }

  public boolean add(GameStep currentInfo) {
    moves++;
    score = currentInfo.score;
    return history.add(currentInfo);
  }
}
