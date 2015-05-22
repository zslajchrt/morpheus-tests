//package org.cloudio.morpheus.samples2
//
//import org.morpheus.Morpheus._
//import org.morpheus.FragmentSelector._
//import org.morpheus.Morpher._
//import org.cloudio.morpheus.samples._
//
///**
// * Created by zslajchrt on 28/02/15.
// */
//object MyConfig3 {
//  type Model = Entity1 with FragmentA2 with /?[DimAWrapper2] with /?[DimAWrapper5] with FragmentB2 with FragmentB2Wrapper
//
//  val ent1: Entity1 = new Entity1(101)
//
//  object fragmentB2Config extends FragmentB2Config {
//    val mm: MongoPersistence = {
//      new MongoPersistenceImpl
//    }
//
//    def xx2: String = {
//      "abc"
//    }
//  }
//
//  var isFrag1 = true
//  var isFrag2 = true
//  var isFrag3 = true
//
//  implicit def ent = external(ent1)
//  implicit def faw = frag[DimAWrapper5, DimAWrapper5Config](new DimAWrapper5Config {
//    override val xyz: Int = 123
//  })
//  implicit def fb = frag[FragmentB2, FragmentB2Config](fragmentB2Config)
//  implicit def fbw = frag[FragmentB2Wrapper2, FragmentB2Wrapper2Config](new FragmentB2Wrapper2Config {
//    override val zumo: String = "ZUMO"
//  })
//  implicit def strategy = strategies[Model](
//    activator[Model](
//      ?[DimAWrapper2] { _ => isFrag1 } orElse
//        ?[DimAWrapper5] { _ => isFrag2 } orElse
//        ?[FragmentB2Wrapper2] { _ => isFrag3 }
//    ) orElse left[Model])
//
//}
