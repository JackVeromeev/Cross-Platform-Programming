package notation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import game2048onJavaFX.Game;
import game2048onJavaFX.Settings;
import game2048onJavaFX.Settings.*;

/**
 * Class with static methods to load and save games and settings
 * 
 * @author Jack Veromeev
 *
 */
public class FileHandler {

  /**
   * Settings file name
   */
  final static String SETTINGS_FILE_NAME = "Settings.cfg";
  final static String SAVE_GAMES_FOLDER = "./savegames";
  final static String SAVE_GAME_FILE_NAME = "save";
  final static String SAVE_GAME_FILE_EXTENSION = ".saving";

  public static Settings loadSettings() {
    Settings result = new Settings();
    File settingsFile = new File(SETTINGS_FILE_NAME);
    String[] params;
    if (settingsFile.exists()) {
      try {
        BufferedReader in = new BufferedReader(new FileReader(settingsFile.getAbsoluteFile()));
        try {
          params = in.readLine().split(" ");
          if (params[0].equals("Easy")) {
            result.currentDifficulty = Difficulty.EASY;
          } else {
            result.currentDifficulty = Difficulty.HARD;
          }
          if (params[1].equals("Player")) {
            result.currentGameType = GameType.PLAYER;
          } else {
            result.currentGameType = GameType.BOT;
          }
          result.rawNumber = Integer.parseInt(params[2]);
          result.columnNumber = Integer.parseInt(params[3]);
          result.autosaveNumber = Integer.parseInt(params[4]);
        } finally {
          in.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static void saveSettings(Settings arg) {
    /*
     * We create string array with our settings to save them in file
     */
    String params[] = new String[5];
    if (arg.currentDifficulty == Difficulty.EASY) {
      params[0] = "Easy";
    } else {
      params[0] = "Hard";
    }
    if (arg.currentGameType == GameType.PLAYER) {
      params[1] = "Player";
    } else {
      params[1] = "Bot";
    }
    params[2] = Integer.toString(arg.rawNumber);
    params[3] = Integer.toString(arg.columnNumber);
    params[4] = Integer.toString(arg.autosaveNumber);
    /*
     * And save them
     */
    File outFile = new File(SETTINGS_FILE_NAME);
    try {
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      PrintWriter out = new PrintWriter(outFile.getAbsoluteFile());
      try {
        out.println(params[0] + " " + params[1] + " " + params[2] + " " + params[3] + " " + params[4]);
      } finally {
        out.close();
      }
    } catch (IOException ioe) {
      System.err.println("settings file not saved!!!");
      ioe.printStackTrace();
    }
  }

  public static void saveGame(GameHistory his) {
    try {
      File savesFolder = new File(SAVE_GAMES_FOLDER);
      if(!savesFolder.exists()) {
	savesFolder.mkdir();
      }
      File outFile = new File(SAVE_GAMES_FOLDER + "/" + SAVE_GAME_FILE_NAME +
	  Game.settings.autosaveNumber + SAVE_GAME_FILE_EXTENSION);
      outFile.createNewFile();
      PrintWriter out = new PrintWriter(outFile.getAbsoluteFile());
      try {
        out.println(Game.settings.rawNumber + " " +
            Game.settings.columnNumber + " " + his.moves + " " + his.score);
        for (GameStep step : his.history) {
          out.println(step.toString());
        }
      } finally {
        out.close();
      }
    } catch (Exception e) {
      System.err.println("Game not saved!");
      e.printStackTrace();
    }
    Game.settings.autosaveNumber++;
  }

//  public static GameHistory loadGame(File inFile) {
//    GameHistory targetHistory = new GameHistory();
//    if (inFile.exists()) {
//      try {
//        BufferedReader in =
//            new BufferedReader(new FileReader(inFile.getAbsoluteFile()));
//        try {
//          String[] initialParams = in.readLine().split(" ");
//          targetHistory.rawNumber = Integer.parseInt(initialParams[0]);
//          targetHistory.columnNumber = Integer.parseInt(initialParams[1]);
//          for (int i = 0; i < Integer.parseInt(initialParams[2]); i++) {
//            targetHistory.add(GameStep.createFromString(in.readLine(),
//        	targetHistory.rawNumber, targetHistory.columnNumber));
//          }
//        } finally {
//          in.close();
//        }
//      } catch (Exception e) {
//        System.err.println(e.getMessage());
//        e.printStackTrace();
//      }
//    }
//    return targetHistory;
//  }
}