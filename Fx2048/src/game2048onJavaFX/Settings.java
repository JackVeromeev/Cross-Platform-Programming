package game2048onJavaFX;

/**
 * Class to store game settings
 * 
 * @author Jack Veromeev
 *
 */
public class Settings {

  /**
   * An enumeration to show type of the game
   * 
   * @author Jack Veromeev
   */
  public enum GameType {
    PLAYER, BOT
  }

  /**
   * An enumeration to show difficulty of the game
   * 
   * @author Jack Vermeev
   *
   */
  public enum Difficulty {
    EASY, HARD
  }

  /**
   * Probability of appearance of cell "4" on the game field if difficulty is
   * easy
   */
  static final int PROBABILITY_OF_FOUR_EASY = 10;
  /**
   * Probability of appearance of cell "4" on the game field if difficulty is
   * hard
   */
  static final int PROBABILITY_OF_FOUR_HARD = 60;

  /**
   * A variable to store type of the current game
   */
  public GameType currentGameType;
  /**
   * A variable to store type of the current game
   */
  public Difficulty currentDifficulty;
  /**
   * A variable to store amount of raws in game field
   */
  public int rawNumber;
  /**
   * A variable to store amount of columns in game field
   */
  public int columnNumber;
  /**
   * A variable to store a number of next autosaved game to autogenerate file
   * name with saved game
   */
  public int autosaveNumber;

  /**
   * A variable to show, if game is replay or not
   */
  public Settings() {
    useDefaultParams();
  }

  Settings(Settings s) {
    rawNumber = s.rawNumber;
    columnNumber = s.columnNumber;
    currentDifficulty = s.currentDifficulty;
    currentGameType = s.currentGameType;
  }

  void useDefaultParams() {
    currentDifficulty = Difficulty.EASY;
    currentGameType = GameType.PLAYER;
    rawNumber = 4;
    columnNumber = 4;
    autosaveNumber = 0;
  }
}
