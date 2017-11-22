package example

object Hello extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "say hello"
}

/*
object Example{
  def main(args:Array[String]): Unit = {
    println("Hello Test")
  }
}
*/