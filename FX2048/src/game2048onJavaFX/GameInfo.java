package game2048onJavaFX;

import java.util.ArrayList;
import java.util.Random;

import notation.GameStep;

/**
 * 
 * Class with main arithmetics of the game contains and processes game field
 * 
 * @author Jack Veromeev
 */

public class GameInfo {
  /**
   * Amount of the columns
   */
  private int columnNumber;
  /**
   * Amount of the raws
   */
  private int rawNumber;
  /**
   * Probability of appearance of 4 on the field
   */
  private int probabilityOfFour;
  /**
   * Data of the game field
   */
  private int board[][];
  /**
   * Score of the game
   */
  private int score;

  /**
   * Direction of the movement
   * 
   * @author Jack Veromeev
   *
   */
  enum Direction {
    LEFT, RIGHT, UP, DOWN
  };

  /**
   * Class that contains raw and column replacement of the cell
   * 
   * @author Jack Veromeev
   *
   */
  private class CellIndexies {
    public int rawIndex;
    public int columnIndex;

    public CellIndexies(int raw, int column) {
      rawIndex = raw;
      columnIndex = column;
    }
  }

  GameInfo() {
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

  /**
   * Adds new number (2 or 4) to the field, if it is possible.
   */
  public void addNewNumber() {
    ArrayList<CellIndexies> emptyCells = new ArrayList<CellIndexies>();
    for (int rawIndex = 0; rawIndex < rawNumber; rawIndex++) {
      for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
        if (board[rawIndex][columnIndex] == 0) {
          emptyCells.add(new CellIndexies(rawIndex, columnIndex));
        }
      }
    }
    if (emptyCells.isEmpty()) {
      return;
    }
    Random randomNumber = new Random();
    CellIndexies newCell =
        emptyCells.get(randomNumber.nextInt(emptyCells.size()));
    if (randomNumber.nextInt(100) <= probabilityOfFour) {
      board[newCell.rawIndex][newCell.columnIndex] = 4;
    } else {
      board[newCell.rawIndex][newCell.columnIndex] = 2;
    }
  }

  /**
   * @return current score of the game
   */
  public int getScore() {
    return score;
  }

  /**
   * @return table of game field
   */
  public int[][] getBoard() {
    return board;
  }

  /**
   * @return amount of raws
   */
  public int getRawNumber() {
    return rawNumber;
  }

  /**
   * @return amount of columns
   */
  public int getColumnNumber() {
    return columnNumber;
  }

  /**
   * Checks for game over. It happens if all field have no empty cells and no
   * cells can be merged.
   * 
   * @return true if game is over, else false
   */
  private boolean isGameOver() {
    for (int raw = 0; raw < rawNumber; raw++) {
      for (int column = 0; column < columnNumber; column++) {
        /*
         * first we check for empty cells. If at least one is empty, game is not
         * over and method returns false.
         */
        if (board[raw][column] == 0) {
          return false;
        } else {
          /*
           * second we check for "mergeable" cells (to the right and under the
           * cell). If at least one pair found, game is not over. We should
           * check an existence of the next raw or column before call a cell
           * there. That's why we use embedded "if" blocks
           */
          if (raw + 1 < rawNumber) {
            if (board[raw][column] == board[raw + 1][column]) {
              return false;
            }
          }
          if (column + 1 < columnNumber) {
            if (board[raw][column] == board[raw][column + 1]) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Main method in class that processes the moves of the field, changes game
   * data and checks for game over
   * 
   * @param direction
   *          Where to move
   * @return true if game is not over, else false
   */
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
      if (movement) {
        addNewNumber();
      }
      if (isGameOver()) {
        return false;
      }
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Moves to the left
   * 
   * @return true if field changed, else false
   */
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

  /**
   * Moves to the right
   * 
   * @return true if field changed, else false
   */
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

  /**
   * Moves up
   * 
   * @return true if field changed, else false
   */
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

  /**
   * Moves down
   * 
   * @return true if field changed, else false
   */
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
   * A method that processes a column or a raw. A processed array looks like
   * elements were swiped to the left.
   * 
   * @param array
   *          original raw or column to process
   * @return true if array was changed (some elements merged and(or) moved)
   */
  private boolean processArray(int array[]) {
    int i, j;
    boolean someMoved = false;
    /*
     * finds and merge pairs
     */
    for (i = 0; i < array.length - 1; i++) {
      if (array[i] != 0) {
        for (j = i + 1; j < array.length && array[j] == 0; j++)
          ;
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
     * than moves all not-zeroes to the left
     */
    for (i = 0; i < array.length; i++) {
      if (array[i] != 0) {
        for (j = 0; j < array.length && array[j] != 0; j++)
          ;
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

  /**
   * 
   * @param newInfo
   *          information of the step of game
   * @return true if game is NOT over, otherwise false
   */
  boolean changeToStatement(GameStep newInfo) {
    rawNumber = newInfo.field.length;
    columnNumber = newInfo.field[0].length;
    board = new int[newInfo.field.length][newInfo.field[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = newInfo.field[i][j];
      }
    }
    score = newInfo.score;
    return !isGameOver();
  }
}
