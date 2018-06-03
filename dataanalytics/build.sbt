import Dependencies._

lazy val root = (project in file(".")).
  settings(
  	name := "DataAnalytics",
    inThisBuild(List(
      organization := "com.royex",
      scalaVersion := "2.11.11",
      version      := "0.1.0-SNAPSHOT"
    )),    
    libraryDependencies += scalaTest % Test,    
	libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0",
	libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.2.0",
	libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.2.0",
	libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "2.2.0"
	
	// libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,    
	// libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.3.0",
	// libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.3.0",
	// libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.3.0",
	// libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "2.3.0"
	
  )
