package example

import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {
  "The Hello object" should "say hello" in {
    Hello.greeting shouldEqual "hello"
  }
}

/*
class HelloSpec extends FlatSpec with Matchers {
  "The Example object" should "Hello Test" 
}
*/