package generator;

import java.util.Random;

import game2048onJavaFX.*;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import notation.*;

/**
 * Class to generate and show process
 * @author Jack Veromeev
 *
 */
public class GeneratorBoard {

  private int rawNumberToSave;
  private int columnNumberToSave;

  private int saveAmount;
  private int generatedThreads;
  private int finishedThreads;
  
  private GameInfo currentInfo;
  private GameHistory history;
  
  private Scene gameScene;
  private GridPane table;
  private StackPane root;
  
  private boolean notOver;
  private boolean enoughGamesGenerated;
  private AnimationTimer gameTimer;
  private AnimationTimer generatorTimer;
  private Random random;
  
  public GeneratorBoard() {
    gameTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        GameInfo.Direction currentDirection;
        switch (random.nextInt(4)) {
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
        history.add(new GameStep(currentInfo));
        if (notOver == false) {
          gameTimer.stop();
          FileHandler.saveGame(history);
          finishedThreads++;
          if(!enoughGamesGenerated)  {
            System.out.println("enough games");
            generatorTimer.start();
          }
        }
      }
    };
    generatorTimer = new AnimationTimer() {
      @Override
      public void handle(long arg0) {
        if(finishedThreads == saveAmount) {
          enoughGamesGenerated = true;
          gameTimer.stop();
          generatorTimer.stop();
          gameOver();
        }
        System.out.println(finishedThreads + "-fin  " );
        if(generatedThreads == 0 && !enoughGamesGenerated) {
          System.out.println("genered = 0 & not enough");
          createThread();
          repaint();
          generatorTimer.stop();
        }
        if (generatedThreads == finishedThreads && !enoughGamesGenerated) {
          System.out.println("genered = finished & not enough");
          createThread();
	  repaint();
	  generatorTimer.stop();
	}
      }
    };
    enoughGamesGenerated = false;
    random = new Random();
    table = new GridPane();
    table.setId("pane");
    root = new StackPane();
    root.getChildren().add(table);
    table.add(new Label("Generated: " + generatedThreads), 0, 0);
    table.add(new Label("Finished: " + finishedThreads), 0, 1);
    table.add(new Label("Total: " + saveAmount), 0, 2);
    gameScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    gameScene.getStylesheets().
        addAll(this.getClass().getResource("game.css").toExternalForm());
  }

  private void repaint() {
    table.getChildren().clear();
    table.add(new Label("Generated: " + generatedThreads), 0, 0);
    table.add(new Label("Finished: " + finishedThreads), 0, 1);
    table.add(new Label("Total: " + saveAmount), 0, 2);
  }

  public void init(int amount) {
    root.getChildren().clear();
    root.getChildren().add(table);
    rawNumberToSave = Game.settings.rawNumber;
    columnNumberToSave = Game.settings.columnNumber;
    saveAmount = amount;
    generatedThreads = 0;
    finishedThreads = 0;
    repaint();
    generatorTimer.start();
    System.out.println("generator started");
    Game.stage.setScene(gameScene);
    Game.stage.show();
  }

  private void createThread() {
    if (random.nextInt(10) > 7) {
    Game.settings.rawNumber = random.nextInt(4) + 3;
    Game.settings.columnNumber = random.nextInt(4) + 3;
    } else {
      Game.settings.rawNumber = random.nextInt(2) + 3;
      Game.settings.columnNumber = random.nextInt(2) + 3;
    }
    currentInfo = new GameInfo();
    history = new GameHistory();
    history.add(new GameStep(currentInfo));
    notOver = true;
    generatedThreads++;
    gameTimer.start();
  }

  private void gameOver() {
    repaint();
    Game.settings.rawNumber = rawNumberToSave;
    Game.settings.columnNumber = columnNumberToSave;
    Label l = new Label(finishedThreads + "\ngames generated");
    l.setId("exit-label");
    String buttonName = "Back to menu";
    Button bGO = new Button(buttonName);
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
