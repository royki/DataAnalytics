package example

object ParallelCollection extends App {
  val n = 1 to 10
  val arr = new Array[Int](10) 
  val a = Array(11,21,13,4,5,6,7,8,9,10)
  
  println(a.fold(0)(_ + _))
  println(a.foldLeft(0)((s,i) => s+i))
  println(a.foldRight(0)((s,i) => s+i))  
  println(a.foldLeft(0)(_ - _))
  println(a.foldRight(0)(_ - _))
  println(a.par.foldRight(0)(_ - _))
  println(a.aggregate(0)(_, _))
}