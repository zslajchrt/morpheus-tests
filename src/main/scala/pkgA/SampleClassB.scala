package pkgA


/**
 * Created by zslajchrt on 21/01/15.
 */
class SampleClassB {

  //def a(): Int = 1

  def s(): String = "spring"

}


object SampleClassBApp {

  def main(args: Array[String]) {

    val g = new Runnable {
      override def run(): Unit = {}

    }
    g.run()




//    class NestedX {
//      def h = 1
//    }
//
//    val n = new NestedX

//    val map = MorpheusMacros.map(classOf[SampleClassB])
//    //map.asInstanceOf[StringBuilder].append("Hello")
//    println(map)
//
  }

}

