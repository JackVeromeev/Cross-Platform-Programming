package game2048onJavaFX;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import notation.FileHandler;
import notation.GameHistory;
import notation.GameStep;

/**
 * Class that draws game board, controls player and bot actions and calls needed
 * methods in class GameInfo
 * 
 * @author Jack Veromeev
 */

class GameBoard {
  /**
   * Object with information of field in game Initialized for each game
   */
  GameInfo currentInfo;
  /**
   * Scene for game window
   */
  private Scene gameScene;
  /**
   * Table that is used for draw game field
   */
  private GridPane table;
  /**
   * Pane for draw game over information above the field
   */
  private StackPane root;
  /**
   * Variable that shows if game is over(false) or not
   */
  private boolean notOver;
  /**
   * Thread for control bot actions
   */
  private AnimationTimer botTimer;
  /**
   * Variable to store game history. Used in method replay
   */
  GameHistory history;

  /**
   * Main constructor
   * 
   * @param information
   *          - data for game field
   */
  public GameBoard() {
    table = new GridPane();
    table.setId("pane");
    root = new StackPane();
    root.getChildren().add(table);
    gameScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    gameScene.getStylesheets().
        addAll(this.getClass().getResource("game.css").toExternalForm());
  }

  /**
   * Method to start the game as player or bot
   */
  void init() {
    history = new GameHistory();
    currentInfo = new GameInfo();
    notOver = true;
    root.getChildren().clear();
    root.getChildren().add(table);
    Game.stage.setScene(gameScene);
    repaint();
    Game.stage.show();
    switch (Game.settings.currentGameType) {
    case PLAYER:
      playPlayer();
      break;
    case BOT:
      playBot();
      break;
    default:
      break;
    }

  }

  /**
   * Method to start replay
   * 
   * @param inFile
   *          file to read replay data
   */
  void replay(File inFile) {
    currentInfo = new GameInfo();
    root.getChildren().clear();
    root.getChildren().add(table);
    Game.stage.setScene(gameScene);
    history = FileHandler.loadGame(inFile);
    botTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        notOver = currentInfo.changeToStatement(history.getNextStep());
        repaint();
        if (notOver == false) {
          botTimer.stop();
          gameOver(true);
        }
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          System.out.println("The sleep of a bot was interrupted");
          e.printStackTrace();
        }
      }
    };
    // repaint();
    Game.stage.show();
    botTimer.start();
  }

  /**
   * Method for play game as a player
   */
  private void playPlayer() {
    gameScene.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        switch (event.getCharacter()) {
        case "w":
          notOver = currentInfo.moveToThe(GameInfo.Direction.UP);
          repaint();
          break;
        case "s":
          notOver = currentInfo.moveToThe(GameInfo.Direction.DOWN);
          repaint();
          break;
        case "a":
          notOver = currentInfo.moveToThe(GameInfo.Direction.LEFT);
          repaint();
          break;
        case "d":
          notOver = currentInfo.moveToThe(GameInfo.Direction.RIGHT);
          repaint();
          break;
        case "q":
          FileHandler.saveGame(history);
          Game.menu.mainMenu();
          break;
        default:
          System.out.println(event.getCharacter());
          break;
        }
        history.add(new GameStep(currentInfo));
        if (notOver == false) {
          gameScene.setOnKeyTyped(null);
          gameOver(false);
        }
      }
    });
  }

  /**
   * Method for play game as a computer
   */
  private void playBot() {
    Random rand = new Random();
    botTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        GameInfo.Direction currentDirection;
        switch (rand.nextInt(4)) {
        case 0:
          currentDirection = GameInfo.Direction.LEFT;
          break;
        case 1:
          currentDirection = GameInfo.Direction.UP;
          break;
        case 2:
          currentDirection = GameInfo.Direction.RIGHT;
          break;
        case 3:
          currentDirection = GameInfo.Direction.DOWN;
          break;
        default:
          currentDirection = GameInfo.Direction.LEFT;
          break;
        }
        notOver = currentInfo.moveToThe(currentDirection);
        repaint();
        history.add(new GameStep(currentInfo));
        if (notOver == false) {
          botTimer.stop();
          gameOver(false);
        }
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          System.out.println("The sleep of a bot was interrupted");
          e.printStackTrace();
        }
      }
    };
    botTimer.start();
  }

  /**
   * Method to change window appearance after changes in field data
   */
  private void repaint() {
    table.getChildren().clear();
    for (int i = 0; i < currentInfo.getRawNumber(); i++) {
      for (int j = 0; j < currentInfo.getColumnNumber(); j++) {
        Label l = new Label();
        if (currentInfo.getBoard()[i][j] != 0) {
          l.setText(Integer.toString(currentInfo.getBoard()[i][j]));
        }
        l.setId("c" + Integer.toString(currentInfo.getBoard()[i][j]));
        table.add(l, j, i);
      }
    }
    Label score = new Label("Score: " +
        Integer.toString(currentInfo.getScore()));
    score.setId("c1");
    table.add(score, 0, currentInfo.getRawNumber(), 4, 4);
  }

  /**
   * Method to show "game over" menu
   */
  private void gameOver(boolean replay) {
    Label l = new Label("Game Over. Score: " +
        Integer.toString(currentInfo.getScore()));
    l.setId("exit-label");
    String buttonName;
    if (replay) {
      buttonName = "Back to menu";
    } else {
      buttonName = "Autosave and back\n   to main menu";
    }
    Button bGO = new Button(buttonName);
    bGO.setId("exit-button");
    bGO.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (!replay) {
          FileHandler.saveGame(history);
        }
        Game.menu.mainMenu();
      }
    });
    VBox boxGO = new VBox();
    boxGO.setId("exit-pane");
    boxGO.setAlignment(Pos.CENTER);
    boxGO.getChildren().add(l);
    boxGO.getChildren().add(bGO);
    root.getChildren().add(boxGO);
  }
}
