package statistcs;

import java.util.ArrayList;
import java.util.Comparator;

import game2048onJavaFX.Game;
import notation.FileHandler;
import notation.GameStatisticsData;

public class SortTable implements Runnable{

  public int bestGameNumber, worstGameNumber;
  public boolean isLoading = false;
  public int loaded, maxLoading;
  public Thread thread = new Thread(this, "sort handler");
  public long statData[][] = new long[4][2];
  /**
   * false - sort, true - stat
   */
  public boolean sortOrStat = false;

  private ArrayList<GameStatisticsData> load(int max) {
    maxLoading = max;
    ArrayList<GameStatisticsData> list = new ArrayList<GameStatisticsData>();
    GameStatisticsData data;
    System.out.println("start loading "+max);
    isLoading = true;
    for (loaded = 0; loaded < max; loaded++) {
      data = FileHandler.loadStat(FileHandler.SAVE_GAMES_FOLDER + "/"
	  + FileHandler.SAVE_GAME_FILE_NAME + loaded
          + FileHandler.SAVE_GAME_FILE_EXTENSION);
      if (data.rawNumber != 0) {
	data.gameNumber = loaded;
	list.add(data);
      }
    }
    isLoading = false;
    return list;
  }
  public long[] coupleSort(int max) {
    ArrayList<GameStatisticsData> list = load(max);
    System.out.println("java sort started");
    GameStatisticsData arr[] = new GameStatisticsData[list.size()];
    list.toArray(arr);
    long javaTime = System.currentTimeMillis();
    list.sort(new Comparator<GameStatisticsData>() {
      @Override
      public int compare(GameStatisticsData arg0, GameStatisticsData arg1) {
	return arg1.score - arg0.score;
      }
    });
    javaTime = System.currentTimeMillis() - javaTime;
    System.out.println("java sort "+max+"= " + javaTime);

    long scalaTime = System.currentTimeMillis();
    arr = StatisticsHandler.sort(arr);
    scalaTime = System.currentTimeMillis() - scalaTime;
    System.out.println("scala sort "+max+"= " + scalaTime);

    System.out.println("best score = " + list.get(0).score);
    System.out.println("worst score = " + list.get(list.size() - 1).score);
    bestGameNumber = list.get(0).gameNumber;
    worstGameNumber = list.get(list.size() - 1).gameNumber;
    long mills[] = new long[2];
    mills[0] = javaTime;
    mills[1] = scalaTime;
    return mills;
  }

  @Override
  public void run() {
    if (!sortOrStat) {
      /*
       * sort
       */
      statData[0] = coupleSort(1000);
      statData[1] = coupleSort(10000);
      statData[2] = coupleSort(40000);
    } else {
      /*
       * count statistics
       */
      ArrayList<GameStatisticsData> list = load(Game.settings.autosaveNumber);
      GameStatisticsData arr[] = new GameStatisticsData
	  [Game.settings.autosaveNumber];
      list.toArray(arr);
      statData[0] = StatisticsHandler.collectMoves(arr);
      statData[1] = StatisticsHandler.collectScore(arr);
      statData[2] = StatisticsHandler.collectRaws(arr);
      statData[3] = StatisticsHandler.collectColumns(arr);
    }

  } 
}
