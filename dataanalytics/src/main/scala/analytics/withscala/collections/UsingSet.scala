package analytics.withscala.collections

object UsingSets extends App {
	
	// Pick a year, find all names from states on that year. 
	// How many of those names were used in that year from each state
	
	val year = 2016
	val nationalData = {
		val source = scala.io.Source.fromFile(s"resources/Babynames/names/yob$year.txt")
		// names is iterator of String, split() gives the array
		//.toArray & toSeq is a slow process compare to .toSet  // .toSeq gives Stream Closed error
		val names = source.getLines().filter(_.nonEmpty).map(_.split(",")(0)).toSet
		source.close()		
		names
		// println(names.mkString(","))		
	}	
	println("Names " + nationalData)

	// run through every state
	/*for (stateFile <- new java.io.File("Babynames/namesbystate").list(); if stateFile.endsWith(".TXT")) {
		val stateData = {
			val source = io.Source.fromFile("Babynames/namesbystate/"+stateFile)
			val names = source.getLines().filter(_.nonEmpty).map(_.split(",")).
				filter(a => a(2).toInt == year).map(a => a(3)).toArray
		  val count = names.count(n => nationalData.contains(n))
			source.close()
			count
		}
		println(stateFile.take(2) + " "+ stateData.toDouble/nationalData.length)
	}*/
	// run through every state
	val info = for (stateFile <- new java.io.File("resources/Babynames/namesbystate").list(); if stateFile.endsWith(".TXT")) yield {
		val source = scala.io.Source.fromFile("resources/Babynames/namesbystate/" + stateFile)
		val names = source.getLines().filter(_.nonEmpty).map(_.split(",")).
			filter(a => a(2).toInt == year).map(a => a(3)).toArray // .toSet		
		source.close()		
		(stateFile.take(2), names)		
	}
	println("Total namses in " + year + " " +nationalData.size)	
	println(info(0)._2.size + " names from state "+ info(0)._1)
	println(info(1)._2.size + " names from state "+ info(1)._1)
	val start = System.nanoTime()
	for ((state, sname) <- info) {
		 println("State: " +state + " Coverage of name in "+ year+" "+ sname.count(n => nationalData.contains(n)).toDouble / nationalData.size) // Set doesn't have length method
	}
	 println((System.nanoTime() - start) / 1e9)
}