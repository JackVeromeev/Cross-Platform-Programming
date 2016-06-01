package notation;

import game2048onJavaFX.GameInfo;

public class GameStep {
  public int[][] field;
  public int score;

  public GameStep(GameInfo info) {
    field = new int[info.getBoard().length][info.getBoard()[0].length];
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[i].length; j++) {
	field[i][j] = info.getBoard()[i][j];
      }
    }
    score = info.getScore();
  }

  private GameStep(String[] strs, int raws, int columns) {
    field = new int[raws][columns];
    int k = 0;
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[i].length; j++) {
	field[i][j] = Integer.parseInt(strs[k]);
	k++;
      }
    }
    score = Integer.parseInt(strs[k]);
  }

  public static GameStep createFromString(String str, int raws, int columns)
      throws Exception {
    String[] parsed = str.split(" ");
    if (parsed.length < raws * columns) {
      throw new Exception("Creating GameStep from bad string");
    }
    return new GameStep(parsed, raws, columns);
  }

  public String toString() {
    StringBuilder collector = new StringBuilder();
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[i].length; j++) {
        collector.append(Integer.toString(field[i][j]));
        collector.append(" ");
      }
    }
    collector.append(Integer.toString(score));
    return collector.toString();
  }
}