//package org.cloudio.morpheus.samples2
//
//import org.morpheus.{Space}
//import org.morpheus.{fragment, dimension, wrapper}
//import org.cloudio.morpheus.samples._
//import scala.language.reflectiveCalls
//
///**
// * Created by zslajchrt on 26/01/15.
// */
//class UseCases {
//
//}
//
//
//
//@wrapper
//@dimension
//trait MyDimAWrapper extends DimA {
//  abstract override def convertToJson: String = {
//    super.convertToJson
//  }
//
//  abstract override def convertFromJson(json: String): Unit = {
//    super.convertFromJson(json)
//  }
//}
//
//
//
//@wrapper
//@fragment
//trait MyFragB2AWrapper extends FragmentB2 {
//  this: Entity1 with DimA =>
//
//  override def readFromMongo(entityId: Long): String = super.readFromMongo(entityId)
//}
//
//
//@wrapper
//@fragment
//trait MyFragA1AWrapper extends FragmentA1 {
//  this: Entity1 =>
//
//  override def convertToJson: String = super.convertToJson
//}
//
//object UseCases {
//
//  //  def compose(f1: Any, f2: Any) = null
//  //
//  //  trait Shell[F]
//  //
//  //  class C1[F1](f1: Shell[F1]) {
//  //    def make: F1 = null.asInstanceOf[F1]
//  //  }
//  //
//  //  class C2[F1, F2](f1: Shell[F1]) {
//  //    def With(f2: Shell[F2]): C1[F1 with F2] = new C1[F1 with F2](compose(f1, f2).asInstanceOf[Shell[F1 with F2]])
//  //  }
//  //
//  //
//  //  class C3[F1, F2, F3](f1: Shell[F1]) {
//  //    def With(f2: Shell[F2]): C2[F1 with F2, F3] = new C2[F1 with F2, F3](compose(f1, f2).asInstanceOf[Shell[F1 with F2]])
//  //  }
//  //
//  //  class M[F1, F2, F3] {
//  //    def New(f1: Shell[F1]) = new C3[F1, F2, F3](f1)
//  //
//  //    def apply(f1: Shell[F1], f2: Shell[F2], f3: Shell[F3]) = new C3[F1, F2, F3](f1).With(f2).With(f3).make
//  //  }
//
//
//
//  class MyFragmentB2Config extends FragmentB2Config {
//    val mm: MongoPersistence = {
//      new MongoPersistenceImpl
//    }
//    def xx2: String = {
//      "abc"
//    }
//  }
//
//
//  private val fragmentB2Config = new MyFragmentB2Config
//
//  def main(args: Array[String]) {
////
////    {
////      import Space._
////      val space = newSpace[Entity1 with DimA]
////      import space._
////
////      val f1 = new Entity1()
////      val f2 = newFragment[FragmentA1]
////
////      val composite = compose(f1, f2)
////      val json: String = composite.convertToJson
////      composite.convertFromJson(json)
////
////      val v1: (Shell[Entity1], Shell[FragmentA1]) = composite.fragments
////      val (e, a): (Shell[Entity1], Shell[FragmentA1]) = composite.fragments
////      val (e2, a2) = composite.fragments
////
////    }
////
////
//    {
//      val space = Space.newSpace[Entity1 with DimA with DimB]
//      import space._
//
//      val f1 = new Entity1()
//      val f2: Shell[FragmentA1] = newFragment[FragmentA1]
//      val f3: Shell[FragmentB2] = newFragment[FragmentB2](fragmentB2Config)
//      //val f3 = newFragment[FragmentB2](fragmentB2Config)
//      //val w = newWrapper[FragmentB2Wrapper]
//
//
//      //val composite = compose(new Shell(new Entity1()), f2, w.wrap(f3))
//      val composite = compose(new Shell(new Entity1()), f2, f3)
//
//      composite.load()
//      val json: String = composite.convertToJson
//      composite.convertFromJson(json)
//      composite.store()
//
//      //val f1shell: Shell[Entity1] = composite.as[Entity1]
//
//      //val (ff1, ff2, ff3): (Shell[Entity1], Shell[FragmentA1], Shell[FragmentB2]) = composite
//
////      val f1shell: Shell[FragmentA1] = composite
////      val (f1shell, f2shell): (Shell[FragmentA1], Shell[FragmentB2]) = composite
//
//      composite match {
//        case c: Entity1 =>
//          println("c as Entity1")
//      }
//
//      composite match {
//        case c: Entity1 with FragmentA1 =>
//          println("c as Entity1 with FragmentA1")
//      }
//
//      composite match {
//        case c: Entity1 with FragmentA1 with FragmentB2 =>
//          println("c as Entity1 with FragmentA1 with FragmentB2")
//      }
//
//      composite match {
//        case c: FragmentA1 with FragmentB2 =>
//          println("c as FragmentA1 with FragmentB2")
//      }
//    }
//
//    // swap entities
//    //val cc = s2.space.compose(s3.composite.fragments._1, s2.composite.fragments._2)
//    //cc.convertFromJson("{i=1}")
//
//  }
//}
//
