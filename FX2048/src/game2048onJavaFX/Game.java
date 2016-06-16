package game2048onJavaFX;

import generator.GeneratorBoard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import notation.FileHandler;
/**
 * Main class with entry point to application
 * 
 * @author Jack Veromeev
 */
public class Game extends Application {
  /**
   * Horizontal window size
   */
  public static final int WINDOW_SIZE_X = 400;
  /**
   * Vertical window size
   */
  public static final int WINDOW_SIZE_Y = 500;

  /**
   * Main game window
   */
  public static Stage stage;
  /**
   * Game menu
   */
  public static Menu menu;
  /**
   * Game settings
   */
  public static Settings settings;
  /**
   * Game board
   */
  public static GameBoard board;
  /**
   * generator of new savegames
   */
  public static GeneratorBoard generator;
  /**
   * statistics menu
   */
  public static StatisticsBoard statBoard;

  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    stage.setTitle("2048FX");
    stage.getIcons().add(new Image("file:resources/images/icon.png"));
    stage.setResizable(false);
    generator = new GeneratorBoard();
    statBoard = new StatisticsBoard();
    board = new GameBoard();
    settings = FileHandler.loadSettings();
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        FileHandler.saveSettings(Game.settings);
      }
    });
    menu = new Menu();
  }

  /**
   * Entry point to application.
   * 
   * @param args
   *          Command line arguments
   */
  public static void main(String[] args) {
    try {
      launch(args);
    } catch (IllegalStateException e) {
      System.out.println("Why do I launch application twice a session?");
      e.printStackTrace();
    }
  }
}