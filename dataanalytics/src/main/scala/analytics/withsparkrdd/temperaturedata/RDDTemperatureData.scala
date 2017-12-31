package analytics.withsparkrdd.temperaturedata

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

case class TemperatureData(day: Int, doy: Int, month: Int, year: Int, precip: Double, snow: Double, tave: Double,
  tmax: Double, tmin: Double)

object RDDTemperatureData {

  def toDoubleOrNeg(s: String): Double = {
    try {
      s.toDouble
    } catch {
      case _: NumberFormatException => -1
    }
  }

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("TemperatureData").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("resources/temperatureData/MN212142_9392.csv").filter(!_.contains("Day"))

    val data = lines.flatMap { line =>
      val p = line.split(",")
      if (p(7) == "." || p(8) == "." || p(9) == ".")
        Seq.empty
      else
        Seq(TemperatureData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, toDoubleOrNeg(p(5)),
          toDoubleOrNeg(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }
    data.take(5) foreach println
    println(data.count())
    
    // lines.take(5) foreach println
  }
}