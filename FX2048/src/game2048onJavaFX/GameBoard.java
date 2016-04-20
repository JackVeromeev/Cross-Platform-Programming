package game2048onJavaFX;

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

/**
 * Class that draws game board and controls player and bot actions
 * and calls needed methods in class GameInfo
 * @author Jack Veromeev
 *
 */

class GameBoard {
  GameInfo currentInfo;
  private Scene gameScene;
  private GridPane table;
  private StackPane root;
  private boolean notOver = true;
  private AnimationTimer botTimer;

  public GameBoard(GameInfo information) {
    currentInfo = information;
    table = new GridPane();
    table.setId("pane");
    root = new StackPane();
    root.getChildren().add(table);
    gameScene = new Scene(root, Game.WINDOW_SIZE_X,
        Game.WINDOW_SIZE_Y);
    gameScene.getStylesheets().addAll(this.getClass().
        getResource("game.css").toExternalForm());
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
    case REPLAY:
      break;
    default:
      break;
    }
  }

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
          Game.menu.mainMenu();
          break;
        default:
          System.out.println(event.getCharacter());
          break;
        }
        if(notOver == false) {
          gameOver();
        }
      }
    });
  }

  private void playBot() {
    Random rand = new Random();
    botTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        boolean notOver = true;
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
        if (notOver == false) {
          gameOver();
        }
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    botTimer.start();
  }

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
    if(notOver == false) {
      gameOver();
    }
  }

  private void gameOver() {
    switch (Game.settings.currentGameType) {
    case PLAYER:
      gameScene.setOnKeyTyped(null);
      break;
    case BOT:
      botTimer.stop();
      break;
    case REPLAY:
      break;
    default:
      break;
    }
    Label l = new Label("Game Over. Score: " +
      Integer.toString(currentInfo.getScore()));
    l.setId("exit-label");
    Button bGO = new Button("Back to menu");
    bGO.setId("exit-button");
    bGO.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
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
