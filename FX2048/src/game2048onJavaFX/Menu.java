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
import javafx.stage.FileChooser;
import notation.FileHandler;

/**
 *
 * class that draws main and settings menu and creates game
 * 
 * @author Jack Veromeev
 */
public class Menu {

  /**
   * Distance between buttons in pixels
   */
  public final int BUTTON_DISTANCE = 25;
  /**
   * Scene for main menu
   */
  private Scene mainMenuScene;

  /**
   * Constructor for class Menu. Sets main menu in stage Game.stage
   */
  public Menu() {
    mainMenuScene = createMainMenuScene();
    mainMenu();
  }

  /**
   * Creates scene for main menu window
   * 
   * @return scene to set in stage to show main menu
   */
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
        Game.board.init();
      }
    });
    bNewGame.setId("c16");
    root.getChildren().add(bNewGame);

    Button bLoadGame = new Button("Load game");
    bLoadGame.setId("c256");
    bLoadGame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load saving");
        File directory = new File("./savegames");
        if (!directory.exists()) {
          directory = new File("./");
        }
        fileChooser.setInitialDirectory(directory);
        FileChooser.ExtensionFilter extFilter =
            new FileChooser.ExtensionFilter("Saved 2048 games", "*.saving");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Game.stage);
        if (file != null) {
          Game.board.replay(file);
        }
      }
    });
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

    Button bGenerate = new Button("Also");
    bGenerate.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
	Game.statBoard.statMenu();
      }
    });
    bGenerate.setId("c1024");
    root.getChildren().add(bGenerate);

    Button bQuit = new Button("Quit");
    bQuit.setId("c65536");
    bQuit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FileHandler.saveSettings(Game.settings);
        Game.stage.close();
      }
    });
    root.getChildren().add(bQuit);
    Scene menuScene = new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    menuScene.getStylesheets().
        addAll(this.getClass().getResource("menu.css").toExternalForm());
    return menuScene;
  }

  /**
   * Creates scene for settings menu window
   * 
   * @return scene to set in stage to show settings menu
   */
  private Scene createSettingsMenuScene() {
    /*
     * Grid pane
     */
    GridPane root = new GridPane();
    root.setId("pane");
    root.setVgap(BUTTON_DISTANCE);
    root.setHgap(BUTTON_DISTANCE / 3);
    /*
     * title label
     */
    Label lTitle = new Label("Settings");
    lTitle.setId("title-label");
    root.add(lTitle, 0, 0, 2, 1);
    /*
     * Raw amount selection
     */
    ObservableList<String> rawOptions =
        FXCollections.observableArrayList("3", "4", "5", "6");
    ComboBox<String> comboRawChoose = new ComboBox<String>(rawOptions);
    comboRawChoose.setValue(Integer.toString(Game.settings.rawNumber));
    Label lRawChoose = new Label("Raws:");
    root.add(lRawChoose, 0, 1);
    root.add(comboRawChoose, 1, 1);
    /*
     * Column amount selection
     */
    ObservableList<String> columnOptions =
        FXCollections.observableArrayList("3", "4", "5", "6");
    ComboBox<String> comboColumnChoose = new ComboBox<String>(columnOptions);
    comboColumnChoose.setValue(Integer.toString(Game.settings.columnNumber));
    Label lColumnChoose = new Label("Columns:");
    root.add(lColumnChoose, 0, 2);
    root.add(comboColumnChoose, 1, 2);
    /*
     * Difficulty selection
     */
    ObservableList<String> diffOptions =
        FXCollections.observableArrayList("Easy", "Hard");
    ComboBox<String> comboDiffChoose = new ComboBox<String>(diffOptions);
    if (Game.settings.currentDifficulty == Settings.Difficulty.EASY) {
      comboDiffChoose.setValue("Easy");
    } else {
      comboDiffChoose.setValue("Hard");
    }
    Label lDiffChoose = new Label("Difficulty:");
    root.add(lDiffChoose, 0, 3);
    root.add(comboDiffChoose, 1, 3);
    /*
     * Player/bot selection
     */
    ObservableList<String> botOptions =
        FXCollections.observableArrayList("Player", "Bot");
    ComboBox<String> comboBotChoose = new ComboBox<String>(botOptions);
    if (Game.settings.currentGameType == Settings.GameType.PLAYER) {
      comboBotChoose.setValue("Player");
    } else {
      comboBotChoose.setValue("Bot");
    }
    Label lBotChoose = new Label("Who plays:");
    root.add(lBotChoose, 0, 4);
    root.add(comboBotChoose, 1, 4);
    /*
     * Button "Apply"
     */
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
          Game.settings.currentGameType = Settings.GameType.BOT;
        } else {
          Game.settings.currentGameType = Settings.GameType.PLAYER;
        }
        mainMenu();
      }
    });
    /*
     * Button "Cancel"
     */
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
    /*
     * Creating a scene with all these settings
     */
    Scene settingsScene =
        new Scene(root, Game.WINDOW_SIZE_X, Game.WINDOW_SIZE_Y);
    settingsScene.getStylesheets().
        addAll(this.getClass().getResource("menu.css").toExternalForm());
    return settingsScene;
  }

  /**
   * Shows main menu in the window
   */
  public void mainMenu() {
    Game.stage.setScene(mainMenuScene);
    Game.stage.show();
  }

  /**
   * Shows settings menu in the window
   */
  public void settingsMenu() {
    /*
     * we need to create new settings scene because settings may be changed and
     * we need to show those changes
     */
    Game.stage.setScene(createSettingsMenuScene());
    Game.stage.show();
  }
}
