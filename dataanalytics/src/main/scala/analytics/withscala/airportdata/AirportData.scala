package analytics.withscala.airportdata

case class AirportData(id:Int, ident:String, name:String, typeAirport:String, latitude_deg:Double,
    longitude_deg:Double, elevation_ft:Double, continent:String, iso_country:String, iso_region:String,
    municipality:String)

object AirportData extends App {

  def toDoubleOrNeg(s: String): Double = {
    try {
      s.toDouble
    } catch {
      case _: NumberFormatException => -1 
    }
  }
  
  val source = scala.io.Source.fromFile("resources/airportData/airports.csv")
  val lines = source.getLines().drop(1)
  val data = lines.flatMap { line =>
    val p = line.split(",")
      Seq(AirportData(p(0).toInt, p(1).toString, p(2).toString, p(3).toString, toDoubleOrNeg(p(4)), toDoubleOrNeg(p(5)), 
          toDoubleOrNeg(p(6)), p(7).toString, p(8).toString, p(9).toString, p(10).toString))                       
  }.toArray   
  source.close()
  println(data.length)
  data.take(10) foreach println
}