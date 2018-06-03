package analytics.withscala.collections

import scala.io.StdIn._

object UsingMap extends App {


	// Read in every state and have the ability for the user to enter a name and then tell them what year that name was
	// most popular in the state they selected

	case class nameData(gender: Char, year: Int, name: String, count: Int)
  

		val stateInfo =  for (stateData <- new java.io.File("resources/Babynames/namesbystate/").list(); if stateData.endsWith(".TXT")) yield {
		val source = scala.io.Source.fromFile("resources/Babynames/namesbystate/" + stateData)
		val nameInfo = source.getLines().filter(_.nonEmpty).map { line =>
			val p = line.split(",")
			nameData(p(1)(0), p(2).toInt, p(3), p(4).toInt)
		}.toArray.groupBy(_.name)
		source.close()
		(stateData.take(2), nameInfo)
	}
	
	var input = ""
	while (input != "quit") {
		println("Enter name ")
		val name = readLine()
		for ((state, info) <- stateInfo) {
			println(state + " " + info(name).maxBy(_.count))
		}
	}

}