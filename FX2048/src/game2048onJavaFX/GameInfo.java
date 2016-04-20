package game2048onJavaFX;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * Class with main arithmetics of the game
 * contains and processes game field
 *  @author Jack Veromeev
 */

public class GameInfo {
  private int columnNumber;
  private int rawNumber;
  private int probabilityOfFour;
  private int board[][];
  private int score;

  enum Direction {
    LEFT, RIGHT, UP, DOWN
  };

  private class CellIndexies {
    public int rawIndex;
    public int columnIndex;

    public CellIndexies(int raw, int column) {
      rawIndex = raw;
      columnIndex = column;
    }
  }

  public GameInfo() {
    columnNumber = Game.settings.columnNumber;
    rawNumber = Game.settings.rawNumber;
    if (Game.settings.currentDifficulty == Settings.Difficulty.EASY) {
      probabilityOfFour = Settings.PROBABILITY_OF_FOUR_EASY;
    } else {
      probabilityOfFour = Settings.PROBABILITY_OF_FOUR_HARD;
    }

    board = new int[rawNumber][columnNumber];
    score = 0;
    addNewNumber();
    addNewNumber();
  }

  public void addNewNumber() {
    ArrayList<CellIndexies> emptyCells = new ArrayList<CellIndexies>();
    for (int rawIndex = 0; rawIndex < rawNumber; rawIndex++) {
      for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
        if (board[rawIndex][columnIndex] == 0) {
          emptyCells.add(new CellIndexies(rawIndex, columnIndex));
        }
      }
    }
    Random randomCell = new Random();
    CellIndexies newCell =
        emptyCells.get(randomCell.nextInt(emptyCells.size()));
    if (randomCell.nextInt(100) <= probabilityOfFour) {
      board[newCell.rawIndex][newCell.columnIndex] = 4;
    } else {
      board[newCell.rawIndex][newCell.columnIndex] = 2;
    }
  }

  public int getScore() {
    return score;
  }

  public int[][] getBoard() {
    return board;
  }

  public int getRawNumber() {
    return rawNumber;
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  private boolean isWhole() {
    for (int i = 0; i < rawNumber; i++) {
      for (int j = 0; j < columnNumber; j++) {
        if (board[i][j] == 0) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean moveToThe(Direction direction) {
    boolean movement = false;
    try {
      switch (direction) {
      case LEFT:
        movement = moveLeft();
        break;
      case RIGHT:
        movement = moveRight();
        break;
      case UP:
        movement = moveUp();
        break;
      case DOWN:
        movement = moveDown();
        break;
      }
      if (isWhole()) {
        return false;
      } else {
        if (movement) {
          addNewNumber();
        }
        return true;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  private boolean moveLeft() {
    int currentRaw[] = new int[columnNumber];
    boolean movement = false;
    for (int rawIndex = 0; rawIndex < rawNumber; rawIndex++) {
      for (int i = 0; i < columnNumber; i++) {
        currentRaw[i] = board[rawIndex][i];
      }
      movement |= processArray(currentRaw);
      for (int i = 0; i < columnNumber; i++) {
        board[rawIndex][i] = currentRaw[i];
      }
    }
    return movement;
  }

  private boolean moveRight() {
    int currentRaw[] = new int[columnNumber];
    boolean movement = false;
    for (int rawIndex = 0; rawIndex < rawNumber; rawIndex++) {
      for (int i = 0; i < columnNumber; i++) {
        currentRaw[columnNumber - 1 - i] = board[rawIndex][i];
      }
      movement |= processArray(currentRaw);
      for (int i = 0; i < columnNumber; i++) {
        board[rawIndex][i] = currentRaw[columnNumber - 1 - i];
      }
    }
    return movement;
  }

  private boolean moveUp() {
    int currentColumn[] = new int[rawNumber];
    boolean movement = false;
    for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
      for (int i = 0; i < rawNumber; i++) {
        currentColumn[i] = board[i][columnIndex];
      }
      movement |= processArray(currentColumn);
      for (int i = 0; i < rawNumber; i++) {
        board[i][columnIndex] = currentColumn[i];
      }
    }
    return movement;
  }

  private boolean moveDown() {
    int currentColumn[] = new int[rawNumber];
    boolean movement = false;
    for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
      for (int i = 0; i < rawNumber; i++) {
        currentColumn[rawNumber - 1 - i] = board[i][columnIndex];
      }
      movement |= processArray(currentColumn);
      for (int i = 0; i < rawNumber; i++) {
        board[i][columnIndex] = currentColumn[rawNumber - 1 - i];
      }
    }
    return movement;
  }

  /**
   * a method that processes a column or a raw
   */
  private boolean processArray(int array[]) {
    int i, j;
    boolean someMoved = false;
    /*
     * find and merge pairs
     */
    for (i = 0; i < array.length - 1; i++) {
      if (array[i] != 0) {
        for (j = i + 1; j < array.length && array[j] == 0; j++);
        if (j != array.length) {
          if (array[i] == array[j]) {
            array[i] *= 2;
            score += array[i];
            array[j] = 0;
            i = j - 1;
            someMoved = true;
          }
        }
      }
    }
    /*
     * move all not-zeroes to the left
     */
    for (i = 0; i < array.length; i++) {
      if (array[i] != 0) {
        for (j = 0; j < array.length && array[j] != 0; j++);
        if (j == array.length) {
          break;
        }
        if (j < i) {
          array[j] = array[i];
          array[i] = 0;
          someMoved = true;
        }
      }
    }
    return someMoved;
  }
}
