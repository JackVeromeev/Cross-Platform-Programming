package game2048onJavaFX;

import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import statistcs.SortTable;

public class StatisticsBoard {
  private AnimationTimer painter;

  private Scene createStatScene() {
    VBox root = new VBox();
    root.setId("pane");
    root.setAlignment(Pos.CENTER);
    root.setSpacing(Game.menu.BUTTON_DISTANCE);
    Label lTitle = new Label("Also");
    lTitle.setId("title-label");
    root.getChildren().add(lTitle);
    /*
     * gengerator of savings
     */
    TextField field = new TextField();
    root.getChildren().add(field);
    Button bGenerateSaves = new Button("Generate games");
    bGenerateSaves.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
	int amount;
	try {
	  amount = Integer.parseInt(field.getText());
	} catch(Exception e) {
	  amount = 0;
	}
	if (amount >0) {
	  Game.generator.init(amount);
	}
      }
    });
    bGenerateSaves.setId("c16");
    root.getChildren().add(bGenerateSaves);
    /*
     * Sorts Menu
     */
    Button bWatchSorts = new Button("Watch sorts");
    bWatchSorts.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
	sortMenu();
      }
    });
    bWatchSorts.setId("c256");
    root.getChildren().add(bWatchSorts);
    /*
     * statistics
     */
    Button bStatistics = new Button("Statistics");
    bStatistics.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
	statShow();
      }
    });
    bStatistics.setId("c1024");
    root.getChildren().add(bStatistics);
    /*
     * Back to main menu
     */
    Button bBack = new Button("Back");
    bBack.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
	Game.menu.mainMenu();
      }
    });
    bBack.setId("c4096");
    root.getChildren().add(bBack);
    Scene statScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    statScene.getStylesheets().
        addAll(this.getClass().getResource("menu.css").toExternalForm());
    return statScene;
  }

  public void statMenu() {
    Game.stage.setScene(createStatScene());
    Game.stage.show();
  }

  public void statShow() {
    VBox root = new VBox();
    root.setId("pane");
    root.setAlignment(Pos.CENTER);
    root.setSpacing(Game.menu.BUTTON_DISTANCE);
    Label lTitle = new Label("Sorts");
    lTitle.setId("title-label");
    root.getChildren().add(lTitle);
    /*
     * sort compare table
     */
    GridPane sortTable = new GridPane();
    sortTable.setVgap(Game.menu.BUTTON_DISTANCE/2);
    sortTable.setHgap(Game.menu.BUTTON_DISTANCE/2);
    sortTable.setAlignment(Pos.CENTER);
    sortTable.setBorder(new Border(new BorderStroke(
	null, null, null, new BorderWidths(2))));

    root.getChildren().add(sortTable);
    /*
     * back button
     */
    Button bBack = new Button("Back");
    bBack.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
	painter.stop();
	statMenu();
      }
    });
    bBack.setId("c4096");
    root.getChildren().add(bBack);
    Scene sortScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    sortScene.getStylesheets().
        addAll(this.getClass().getResource("menu.css").toExternalForm());
    Game.stage.setScene(sortScene);
    Game.stage.show();
    SortTable sortThread = new SortTable();
    painter = new AnimationTimer() {
      @Override
      public void handle(long now) {
	sortTable.getChildren().clear();
	sortTable.add(new Label("In all games"), 1, 0);
	sortTable.add(new Label("In average"), 2, 0);
	sortTable.add(new Label("Moves"), 0, 1);
	sortTable.add(new Label("Score"), 0, 2);
	sortTable.add(new Label("Raws"), 0, 3);
	sortTable.add(new Label("Columns"), 0, 4);
	sortTable.add(new Label(sortThread.statData[0][0] + ""), 1, 1);
	sortTable.add(new Label(sortThread.statData[0][1] + ""), 2, 1);
	sortTable.add(new Label(sortThread.statData[1][0] + ""), 1, 2);
	sortTable.add(new Label(sortThread.statData[1][1] + ""), 2, 2);
	sortTable.add(new Label(sortThread.statData[2][0] + ""), 1, 3);
	sortTable.add(new Label(sortThread.statData[2][1] + ""), 2, 3);
	sortTable.add(new Label(sortThread.statData[3][0] + ""), 1, 4);
	sortTable.add(new Label(sortThread.statData[3][1] + ""), 2, 4);
	if (sortThread.statData[1][1] != 0) {
	  sortTable.add(new Label("in average score = " +
	      sortThread.statData[1][1] / sortThread.statData[0][1]),
	      0, 5, 3, 1);
	}
	if (sortThread.isLoading) {
	  sortTable.add(new Label("loaded: " + sortThread.loaded + "/" +
	      sortThread.maxLoading), 0, 6, 3, 1);
	} else {
	  sortTable.add(new Label(""), 0, 6, 3, 1);
	}
	Game.stage.show();
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          System.err.println("The sleep of a painter bot was interrupted");
          e.printStackTrace();
        }
      }
    };
    painter.start();
    sortThread.sortOrStat = true;
    sortThread.thread.start();
  }
  
  
  public void sortMenu() {
    VBox root = new VBox();
    root.setId("pane");
    root.setAlignment(Pos.CENTER);
    root.setSpacing(Game.menu.BUTTON_DISTANCE);
    Label lTitle = new Label("Sorts");
    lTitle.setId("title-label");
    root.getChildren().add(lTitle);
    /*
     * sort compare table
     */
    GridPane sortTable = new GridPane();
    sortTable.setVgap(Game.menu.BUTTON_DISTANCE/2);
    sortTable.setHgap(Game.menu.BUTTON_DISTANCE/2);
    sortTable.setAlignment(Pos.CENTER);
    sortTable.setBorder(new Border(new BorderStroke(
	null, null, null, new BorderWidths(2))));

    root.getChildren().add(sortTable);
    /*
     * back button
     */
    Button bBack = new Button("Back");
    bBack.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
	painter.stop();
	statMenu();
      }
    });
    bBack.setId("c4096");
    root.getChildren().add(bBack);
    Scene sortScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    sortScene.getStylesheets().
        addAll(this.getClass().getResource("menu.css").toExternalForm());
    Game.stage.setScene(sortScene);
    Game.stage.show();
    SortTable sortThread = new SortTable();
    painter = new AnimationTimer() {
      @Override
      public void handle(long now) {
	sortTable.getChildren().clear();
	sortTable.add(new Label("Java"), 1, 0);
	sortTable.add(new Label("Scala"), 2, 0);
	sortTable.add(new Label("1000"), 0, 1);
	sortTable.add(new Label("10000"), 0, 2);
	sortTable.add(new Label("40000"), 0, 3);
	sortTable.add(new Label(sortThread.statData[0][0] + ""), 1, 1);
	sortTable.add(new Label(sortThread.statData[0][1] + ""), 2, 1);
	sortTable.add(new Label(sortThread.statData[1][0] + ""), 1, 2);
	sortTable.add(new Label(sortThread.statData[1][1] + ""), 2, 2);
	sortTable.add(new Label(sortThread.statData[2][0] + ""), 1, 3);
	sortTable.add(new Label(sortThread.statData[2][1] + ""), 2, 3);
	if (sortThread.isLoading) {
	  sortTable.add(new Label("loaded: " + sortThread.loaded + "/" +
	      sortThread.maxLoading), 0, 4, 3, 1);
	} else {
	  sortTable.add(new Label(""), 0, 4, 3, 1);
	}
	Game.stage.show();
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          System.err.println("The sleep of a painter bot was interrupted");
          e.printStackTrace();
        }
      }
    };
    painter.start();
    sortThread.thread.start();
  }
}
