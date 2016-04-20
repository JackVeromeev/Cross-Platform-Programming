package game2048onJavaFX;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class with entry point to application 
 * @author Jack Veromeev
 */
public class Game extends Application {

  static final int WINDOW_SIZE_X = 400;
  static final int WINDOW_SIZE_Y = 500;

  public static Stage stage;
  public static Menu menu;
  public static Settings settings;

  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    stage.setTitle("2048FX");
    stage.getIcons().add(new Image("file:resources/images/icon.png"));
    stage.setResizable(false);
    settings = new Settings();
    menu = new Menu();
  }

  public static void main(String[] args) {
    try {
      launch(args);
    } catch (IllegalStateException e) {
      System.out.println("Why do I launch application twice a session?");
      e.printStackTrace();
    }
  }
}