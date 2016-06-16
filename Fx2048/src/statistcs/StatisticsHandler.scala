package statistcs

import notation.GameStatisticsData;

object StatisticsHandler {
  def scoreFilter(l: GameStatisticsData, r: GameStatisticsData): Boolean = {
    r.score < l.score
  }
  def sort(xs: Array[GameStatisticsData]) = xs.sortWith(scoreFilter)
  def collectMoves(xs: Array[GameStatisticsData]) : Array[Long] = {
    val total = xs.foldLeft(0)((l: Int, r: GameStatisticsData) => l + r.moves)
    Array(total, total/xs.length)
  }
  def collectScore(xs: Array[GameStatisticsData]) : Array[Long] = {
    val total = xs.foldLeft(0)((l: Int, r: GameStatisticsData) => l + r.score)
    Array(total, total/xs.length)
  }
  def collectRaws(xs: Array[GameStatisticsData]): Array[Long] = {
    val total = xs.foldLeft(0)((l: Int, r: GameStatisticsData) =>
      l + r.rawNumber)
    Array(total, total/xs.length)
  }
  def collectColumns(xs: Array[GameStatisticsData]): Array[Long] = {
    val total = xs.foldLeft(0)((l: Int, r: GameStatisticsData) =>
      l + r.columnNumber)
    Array(total, total/xs.length)
  }
}