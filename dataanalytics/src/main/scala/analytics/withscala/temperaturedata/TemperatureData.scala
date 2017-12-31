package analytics.withscala.temperaturedata

import scala.collection.Seq

case class TemperatureData(day: Int, doy: Int, month: Int,  year: Int, precip: Double, snow: Double, tave: Double,
  tmax: Double, tmin: Double)

object TemperatureData {

  def toDoubleOrNeg(s: String): Double = {
    try {
      s.toDouble
    } catch {
      case _: NumberFormatException => -1
    }
  }

  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromFile("resources/temperatureData/MN212142_9392.csv")
    val lines = source.getLines().drop(1)
    /*
    // val data = lines.map {line =>
   	val data = lines.filter(!_.contains(",.,")).map{line => //filterNot
    val p = line.split(",")
       TempData(p(0).toInt, p(1).toInt, p(2).toInt, p(3).toString(), p(4).toInt, toDoubleOrNeg(p(5)),
           toDoubleOrNeg(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble)
    }.toArray
    */
    val data = lines.flatMap { line => // flatMap requires Sequence of Data
      val p = line.split(",").map(_.trim)
      if (p(7) == "." || p(8) == "." || p(9) == ".")
        Seq.empty
      else
        Seq(TemperatureData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, toDoubleOrNeg(p(5)),
          toDoubleOrNeg(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }.toArray

    source.close()

    // println(data.length)
    data.take(5) foreach println
    
    println("=========[using Scala filter] ==========")
    // => filter
    val maxTemp = data.map(_.tmax).max // Inefficient way to handle collection as map has created entire new array of 42K
    val hotDays = data.filter(_.tmax == maxTemp)
    println(s"[using Scala filter] Hot days ${hotDays.mkString(", ")}")
    
    println("=========[using Scala maxBy] ==========")
    // => maxBy
    // one way to do maxBy
    val hotDay1 = data.maxBy(_.tmax)
    println(s"[using Scala maxBy] Hot day 1 is $hotDay1")
    
    println("=========[using Scala reduceLeft] ==========")
    // => reduceLeft
    // another way to do by fold / reduceLeft; reduceLeft only works with one data type of collection; has to be same type
    val hotDay2 = data.reduceLeft((d1, d2) => if(d1.tmax >= d2.tmax) d1 else d2)
    println(s"[using Scala reduceLeft] Hot day 2 is $hotDay2")    
    val rainyDay = data.count(_.precip >= 1.0)
    println(s"There are $rainyDay rainy days. There is ${rainyDay*100.0/data.length} percent")
    
    println("=========[using Scala foldLeft] ==========")
    // => foldLeft
    // what is the high temp on those rainy days
    // foldLeft or foldRight can use with different data type
    // val (rainySum, rainyCount) = data.foldLeft((0.0, 0)){
    val (rainySum, rainyCount) = data.foldLeft(0.0 -> 0) {
      case((sum, count), tempdata) => if(tempdata.precip < 1.0) (sum, count) else (sum+tempdata.tmax, count+1)
    }      
    println(s" total rain = ${rainySum}, total rainy days = ${rainyCount}")
    println(s"[using Scala foldLeft] Average Rainy day temperature is ${rainySum/rainyCount}")
    
    println("=========[using Scala aggregate] ==========")
    // => aggregate
    val (rainySumpar, rainyCountpar) = data.par.aggregate(0.0 -> 0) ({
      case((sum, count), tempdata) => if(tempdata.precip < 1.0) (sum, count) else (sum+tempdata.tmax, count+1)
    }, { 
      case ((s1, c1), (s2, c2)) => (s1+s2, c1+c2)        
    })   
    println(s" total rain = ${rainySumpar}, total rainy days = ${rainyCountpar}")
    println(s"[using Scala aggregator] Average Rainy day temperature is ${rainySumpar/rainyCountpar}")
    
    
    println("=========[using Scala flatMap] ==========")
    // => flatMap
    // flatMap (map and filter together) is map to apply a function to every element of collection and makes a collection of the result
    // when function produces another seq. here we ve array and from it, the function ll produce another array
    // then it ll have an array of array. Map and then flatMap. 
    // Advantage: can produce multiple values or one value or 0, can be emoty sequence
    val rainyTemps = data.flatMap(tempdata => if(tempdata.precip < 1.0) Seq.empty else Seq(tempdata.tmax))
    println(s"[using Scala flatMap] Average Rainy day temperature is ${rainyTemps.sum/rainyTemps.length}")
    
    println("=========[using Scala groupBy] ==========")
    // => groupBy
    // avg temp by month accross entire data set
    val monthGroups = data.groupBy(_.month)
    val monthlyTemp = monthGroups.map{ case(m, days) =>
      m -> days.foldLeft(0.0)((sum, tempdata) => sum+tempdata.tmax) / days.length
    }
    
    println(s"Monthly highest temp is ${monthlyTemp}")
    monthlyTemp.toSeq.sortBy(_._1) foreach println
  }
}