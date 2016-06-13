package notation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author Jack Veromeev
 *
 */
public class GameFileLoader implements Runnable {

  public Thread thread;
  private File inFile;
  private GameHistory targetHistory;
  public GameFileLoader(File incomingFile, GameHistory history) {
    inFile = incomingFile;
    targetHistory = history;
    thread = new Thread(this, "game file loader");
  }

  public void run() {
    if (inFile.exists()) {
      try {
        BufferedReader in =
            new BufferedReader(new FileReader(inFile.getAbsoluteFile()));
        try {
          String[] initialParams = in.readLine().split(" ");
          targetHistory.rawNumber = Integer.parseInt(initialParams[0]);
          targetHistory.columnNumber = Integer.parseInt(initialParams[1]);
          for (int i = 0; i < Integer.parseInt(initialParams[2]); i++) {
            targetHistory.add(GameStep.createFromString(in.readLine(),
        	targetHistory.rawNumber, targetHistory.columnNumber));
          }
        } finally {
          in.close();
        }
      } catch (Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
      }
    }
  }

}
