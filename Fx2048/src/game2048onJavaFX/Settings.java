package game2048onJavaFX;
/**
 * Class to store game settings
 * @author JackVeromeev
 *
 */
public class Settings {

  enum GameType {
    PLAYER, BOT, REPLAY
  }

  enum Difficulty {
    EASY, HARD
  }

  static final int PROBABILITY_OF_FOUR_EASY = 10;
  static final int PROBABILITY_OF_FOUR_HARD = 60;

  GameType currentGameType;
  Difficulty currentDifficulty;
  int rawNumber;
  int columnNumber;

  public Settings() {
    currentDifficulty = Difficulty.EASY;
    currentGameType = GameType.PLAYER;
    rawNumber = 4;
    columnNumber = 4;
  }
}
