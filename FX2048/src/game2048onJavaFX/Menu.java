package game2048onJavaFX;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;


/**
 *
 * class that draws main and settings menu and creates game
 * @author Jack Veromeev
 */
class Menu {

  private final int BUTTON_DISTANCE = 25;
  private Scene mainMenuScene, settingsMenuScene;

  public Menu() {
    mainMenuScene = createMainMenuScene();
    settingsMenuScene = createSettingsMenuScene();
    mainMenu();
  }

  private Scene createMainMenuScene() {
    VBox root = new VBox();
    root.setId("pane");
    root.setAlignment(Pos.CENTER);
    root.setSpacing(BUTTON_DISTANCE);

    Label lTitle = new Label("2048 Fx");
    lTitle.setId("title-label");
    root.getChildren().add(lTitle);

    Button bNewGame = new Button("New game");
    bNewGame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        new GameBoard(new GameInfo());
      }
    });
    bNewGame.setId("c16");
    root.getChildren().add(bNewGame);

    Button bLoadGame = new Button("Load game");
    bLoadGame.setId("c256");
//    bLoadGame.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Document");
//        FileChooser.ExtensionFilter extFilter =
//            new FileChooser.ExtensionFilter("Saved 2048 games", "*.sg2048");
//        fileChooser.getExtensionFilters().add(extFilter);
//        File file = fileChooser.showOpenDialog(Game.stage);
//        if (file != null) {
//        } else {
//        }
//      }
//    });
    root.getChildren().add(bLoadGame);

    Button bSettings = new Button("Settings");
    bSettings.setId("c4096");
    bSettings.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        settingsMenu();
      }
    });
    root.getChildren().add(bSettings);

    Button bQuit = new Button("Quit");
    bQuit.setId("c1024");
    bQuit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Game.stage.close();
      }
    });
    root.getChildren().add(bQuit);
    Scene menuScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    menuScene.getStylesheets().
        addAll(this.getClass().getResource("menu.css").toExternalForm());
    return menuScene;
  }

  private Scene createSettingsMenuScene() {
    GridPane root = new GridPane();
    root.setId("pane");
    root.setVgap(BUTTON_DISTANCE);
    root.setHgap(BUTTON_DISTANCE / 3);
    Label lTitle = new Label("Settings");
    lTitle.setId("title-label");
    root.add(lTitle, 0, 0, 2, 1);

    ObservableList<String> rawOptions = 
        FXCollections.observableArrayList(
          "3", "4", "5", "6"
        );
    ComboBox<String> comboRawChoose = new ComboBox<String>(rawOptions);
    comboRawChoose.setValue(Integer.toString(Game.settings.rawNumber));
    Label lRawChoose = new Label("Raws:");
    root.add(lRawChoose, 0, 1);
    root.add(comboRawChoose, 1, 1);

    ObservableList<String> columnOptions = 
        FXCollections.observableArrayList(
          "3", "4", "5", "6"
        );
    ComboBox<String> comboColumnChoose = new ComboBox<String>(columnOptions);
    comboColumnChoose.setValue(Integer.toString(Game.settings.columnNumber));
    Label lColumnChoose = new Label("Columns:");
    root.add(lColumnChoose, 0, 2);
    root.add(comboColumnChoose, 1, 2);

    ObservableList<String> diffOptions = 
        FXCollections.observableArrayList(
          "Easy", "Hard"
        );
    ComboBox<String> comboDiffChoose = new ComboBox<String>(diffOptions);
    if (Game.settings.currentDifficulty == Settings.Difficulty.EASY) {
      comboDiffChoose.setValue("Easy");
    } else {
      comboDiffChoose.setValue("Hard");
    }
    Label lDiffChoose = new Label("Difficulty:");
    root.add(lDiffChoose, 0, 3);
    root.add(comboDiffChoose, 1, 3);

    ObservableList<String> botOptions = 
        FXCollections.observableArrayList(
          "Player", "Bot"
        );
    ComboBox<String> comboBotChoose = new ComboBox<String>(botOptions);
    if (Game.settings.currentGameType == Settings.GameType.PLAYER) {
      comboBotChoose.setValue("Player");
    } else {
      comboBotChoose.setValue("Bot");
    }
    Label lBotChoose = new Label("Who plays:");
    root.add(lBotChoose, 0, 4);
    root.add(comboBotChoose, 1, 4);

    Button bApply = new Button("Apply");
    bApply.setId("c16");
    bApply.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Game.settings.rawNumber = Integer.parseInt(comboRawChoose.getValue());
        Game.settings.columnNumber =
            Integer.parseInt(comboColumnChoose.getValue());
        if (comboDiffChoose.getValue().equals("Easy")) {
          Game.settings.currentDifficulty = Settings.Difficulty.EASY;
        } else {
          Game.settings.currentDifficulty = Settings.Difficulty.EASY;
        }
        if (comboBotChoose.getValue().equals("Bot")) {
          Game.settings.currentGameType =
              Settings.GameType.BOT;
        } else {
          Game.settings.currentGameType =
              Settings.GameType.PLAYER;
        }
        mainMenu();
      }
    });

    Button bCancel = new Button("Cancel");
    bCancel.setId("c4096");
    bCancel.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        mainMenu();
      }
    });
    root.add(bApply, 0, 5);
    root.add(bCancel, 1, 5);
    Scene settingsScene =
        new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    settingsScene.getStylesheets().
    addAll(this.getClass().getResource("menu.css").toExternalForm());
    return settingsScene;
  }

  public void mainMenu() {
    Game.stage.setScene(mainMenuScene);
    Game.stage.show();
  }

  public void settingsMenu() {
    settingsMenuScene = createSettingsMenuScene();
    Game.stage.setScene(settingsMenuScene);
    Game.stage.show();
  }
}
