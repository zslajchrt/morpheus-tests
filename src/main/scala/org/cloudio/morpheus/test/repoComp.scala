package org.cloudio.morpheus.test

import org.morpheus.Morpher._
import org.morpheus.Morpheus._
import org.morpheus._

/**
 *
 * Created by zslajchrt on 06/03/15.
 */
class repoComp {

}


object A {
  // todo: the composite type used as argument needn't be complete, i.e. the dependencies needn't be satisfied completely.
  // Here, EntityBRenderer requires EntityB, e.g.
  type MyCompType1 = EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]

  // Incompatible composite type
  type MyCompType2 = Ping with /?[PingLogger] with Pong

  def inclRefTest(cmpRef: &[MyCompType1]): Boolean = {
    val cmp = *(cmpRef)
    import cmp._

    // this is the test corresponding to the definition of the inclusive composite reference
    //val m = cmp.morph_~(lookupAlt(isFragment[XMLRenderer], isFragment[EntityBRendererDecorator]))
    val m = cmp.morph_~(RatingStrategy(defaultStrategy, hasFragment[XMLRenderer](IncRating), hasFragment[EntityBRendererDecorator](IncRating)))

    select[XMLRenderer with EntityBRendererDecorator](m).isDefined
  }

  def existRefTest(cmpRef: ~&[MyCompType1]): Boolean = {
    val cmp = *(cmpRef)
    val m = cmp.~

    select[EntityBRenderer](m).isDefined
  }

  def exclRefTest(cmpRef: &[MyCompType1]): Boolean = {
    val cmp = *(cmpRef)
    import cmp._

    // this is the test corresponding to the definition of the exclusive composite reference

    // the inclusivity check
    //val m = cmp.morph_~(lookupAlt(isFragment[XMLRenderer], isFragment[EntityBRendererDecorator]))
    val m = cmp.morph_~(RatingStrategy(defaultStrategy, hasFragment[XMLRenderer](IncRating), hasFragment[EntityBRendererDecorator](IncRating)))

    if (select[XMLRenderer with EntityBRendererDecorator](m).isDefined) {

      // the exclusivity check, we must be able to turn off the fragments
      //val m = cmp.morph_~(lookupAlt(isNotFragment[XMLRenderer], isNotFragment[EntityBRendererDecorator]))
      val m = cmp.morph_~(RatingStrategy(defaultStrategy, hasFragment[XMLRenderer](DecRating), hasFragment[EntityBRendererDecorator](DecRating)))

      !select[XMLRenderer](m).isDefined && !select[EntityBRendererDecorator](m).isDefined &&
        select[EntityBRenderer](m).isDefined

    } else {
      false
    }
  }


  implicit val pongConfig = PongConfig.cfg
  val myCmp = compose[MyCompType2]

//  def concatTestIncl(cmpRef: &?[MyCompType1]): Boolean = {
//    val myCmpRef: &[MyCompType2] = myCmp
//    val concated = concat(cmpRef, myCmpRef)
//    import concated._
//
//
//    val m = concated.morph_~(lookupAlt(isFragment[PingLogger], isFragment[XMLRenderer], isFragment[EntityBRendererDecorator]))
//
//    select[Ping with PingLogger with Pong with XMLRenderer with EntityBRendererDecorator](m).isDefined
//  }

//
//
//  def concatTestExcl(cmpRef: &?[MyCompType1]): Boolean = {
//    // EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer] WITH Ping with /?[PingLogger] with Pong
//    val myCmpRef: &[MyCompType2] = myCmp
//    val concated = blend[MyCompType1 with MyCompType2](cmpRef, myCmpRef)
//    //val concated = concat(cmpRef, &(myCmp))
//    import concated._
//
//    val m = concated.morph_~(lookupAlt(isFragment[PingLogger], isFragment[XMLRenderer], isFragment[EntityBRendererDecorator]))
//
//    if (select[Ping with PingLogger with Pong with XMLRenderer with EntityBRendererDecorator](m).isDefined) {
//
//      val m = concated.morph_~(lookupAlt(isNotFragment[PingLogger], isNotFragment[XMLRenderer], isNotFragment[EntityBRendererDecorator]))
//
//      !select[PingLogger](m).isDefined && !select[XMLRenderer](m).isDefined && !select[EntityBRendererDecorator](m).isDefined &&
//        select[Ping with Pong with EntityBRenderer](m).isDefined
//    } else {
//      false
//    }
//  }

  // Composing a two-fragment composite from another one, which MAY contain the second fragment
  def fffHidden(pingRef: &?[Ping]): Unit = {
//    val pingCmp = *(pingRef)
//
//    implicit val pongFrag: (Frag[Pong, PongConfig] => Pong) = pingCmp.fragmentHolder[Pong] match {
//      case None => frag[Pong, PongConfig](PongConfig)
//      case Some(pongHolder) => pongHolder
//    }
//    val pingPongCmp = compose[Ping with Pong]
  }


}


//trait MorphTrans[S[_], T[_]] {
//
//  def aaa[X, Y](): Unit = {
//    val s: S[X] = null.asInstanceOf[S[X]]
//    val t: T[X with Y] = null.asInstanceOf[T[X with Y]]
//  }
//}
//
//trait Semaphore[R, Y, G] {
//  this: R or Y or G =>
//
//  def start(): Unit = {
//    new Thread() {
//      // ...
//    }
//  }
//
//  def red(): Unit = {
//    for (m <- mirror(this);
//         mm <- m.owningMutableProxy) {
//      val ci = m.toMorphKernel
//      mm.remorph(RatingStrategy(RootStrategy[ci.Model](), hasFragment[R](IncRating)))
//    }
//  }
//
//  def yellow(): Unit = {
//    for (m <- mirror(this);
//         mm <- m.owningMutableProxy) {
//      val ci = m.toMorphKernel
//      mm.remorph(RatingStrategy(RootStrategy[ci.Model](), hasFragment[Y](IncRating)))
//    }
//  }
//
//  def green(): Unit = {
//    for (m <- mirror(this);
//         mm <- m.owningMutableProxy) {
//      val ci = m.toMorphKernel
//      mm.remorph(RatingStrategy(RootStrategy[ci.Model](), hasFragment[G](IncRating)))
//    }
//  }
//}
//
