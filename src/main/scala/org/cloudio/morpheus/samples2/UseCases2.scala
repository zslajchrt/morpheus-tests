//package org.cloudio.morpheus.samples2
//
//
//import org.morpheus.Morpheus._
//import org.morpheus.FragmentSelector._
//import org.morpheus.Morpher._
//import org.cloudio.morpheus.samples._
//
///**
// * Created by zslajchrt on 29/01/15.
// *
// */
//
//object MyConfig2 {
//
//  type Model = Entity1 with FragmentA2 with /?[DimAWrapper2] with /?[DimAWrapper5] with (FragmentB1 or FragmentB2 with FragmentB2Wrapper)
//
//  val ent1: Entity1 = new Entity1(101)
//
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
//
//  implicit def ent = external(ent1)
//
//  implicit def faw = frag[DimAWrapper5, DimAWrapper5Config](new DimAWrapper5Config {
//    override val xyz: Int = 123
//  })
//
//  implicit def fb = frag[FragmentB2, FragmentB2Config](fragmentB2Config)
//
//  implicit def fbw = frag[FragmentB2Wrapper2, FragmentB2Wrapper2Config](new FragmentB2Wrapper2Config {
//    override val zumo: String = "ZUMO"
//  })
//
//  implicit def strategy = strategies[Model](
//    activator[Model](
//      ?[DimAWrapper2] { _ => isFrag1} orElse
//        ?[DimAWrapper5] { _ => isFrag2} orElse
//        ?[FragmentB2Wrapper2] { _ => isFrag3}
//    ) orElse left[Model])
//
//
//}
//
//
//object UseCases2 {
//
//  class MyFragmentB2Config extends FragmentB2Config {
//    val mm: MongoPersistence = {
//      new MongoPersistenceImpl
//    }
//
//    def xx2: String = {
//      "abc"
//    }
//  }
//
//  private val fragmentB2Config = new MyFragmentB2Config
//
//  def testComposite(comp: Entity1 with FragmentA2 with DimB, testName: String): Unit = {
//    println("***** " + testName)
//
//    //val comp: Entity1 with FragmentA2 with DimAWrapper2 with DimAWrapper5 with FragmentB2 with FragmentB2Wrapper2 = Morpheus.morph(model)
//    comp.load()
//    val x = comp.convertToJson // it will compile
//    comp.convertFromJson(x)
//    comp.store()
//  }
//
//  def main(args: Array[String]): Unit = {
//
//    val ent1: Entity1 = new Entity1(101)
//
//    //type Model = Entity1
//    //type Model = Entity1 with FragmentA2 with (DimAWrapper2 or DimAWrapper5) with FragmentB2 with (Unit or FragmentB2Wrapper2)
//    //type alt[F] = or[Unit, F]
//    //type Model = Entity1 with FragmentA2 with (Unit or DimAWrapper2) with (Unit or DimAWrapper5) with FragmentB2 with (Unit or FragmentB2Wrapper2)
//    //type Model = Entity1 with FragmentA2 with /?[DimAWrapper2] with /?[DimAWrapper5] with FragmentB2 with FragmentB2Wrapper
//    //type Model = Entity1 with FragmentA2 with /?[DimAWrapper2] with /?[DimAWrapper5] with (FragmentB1 or (FragmentB2 with FragmentB2Wrapper))
//    //type Model = Entity1 with FragmentA2 with DimAWrapper2 with DimAWrapper5 with FragmentB2 with FragmentB2Wrapper2
//    //type Model = Entity1 with FragmentA2 with FragmentB2 with FragmentB2Wrapper2
//    //type Model = Entity1 with FragmentA2 with FragmentB2
//    //type Model = Entity1 with FragmentA2 with DimAWrapper2 with FragmentB2
//    //type Model = Entity1 with (FragmentA2 with DimAWrapper2 or FragmentA1) with FragmentB2 with FragmentB2Wrapper2
//    //type Model = Entity1 with (FragmentA2 or FragmentA1) with (FragmentB2 with FragmentB2Wrapper2 or FragmentB1)
//    //type Model = Entity1 with FragmentA2 with FragmentB2
//    //type Model = Entity1 with FragmentA2 with FragmentB2
//    //type Model = Entity1 with FragmentA2 with FragmentB2 with Unit
//    //type Model = Entity1 with FragmentA2 with (Unit or FragmentB2)
//
//    object MyConfig {
//
//      type Model = Entity1 with FragmentA2 with /?[DimAWrapper2] with /?[DimAWrapper5] with FragmentB2 with FragmentB2Wrapper
//
//      // fragment configuration and instantiation
//
//      implicit def ent = external(ent1)
//
//      implicit def faw = frag[DimAWrapper5, DimAWrapper5Config](new DimAWrapper5Config {
//        override val xyz: Int = 123
//      })
//
//      implicit def fb = frag[FragmentB2, FragmentB2Config](fragmentB2Config)
//
//      implicit def fbw = frag[FragmentB2Wrapper2, FragmentB2Wrapper2Config](new FragmentB2Wrapper2Config {
//        override val zumo: String = "ZUMO"
//      })
//
//      // morphing strategy configuration
//
//
//      var isFrag1 = true
//      var isFrag2 = true
//      var isFrag3 = true
//
//      implicit def strategy = strategies[Model](
//        activator[Model](
//          ?[DimAWrapper2] { _ => isFrag1} orElse
//            ?[DimAWrapper5] { _ => isFrag2} orElse
//            ?[FragmentB2Wrapper2] { _ => isFrag3}
//        ) orElse left[Model])
//
//    }
//
//    import MyConfig2._
//    val model = compose[Model]
//
//
//    // Explicit (immutable) morphing
//
//    {
//
//      isFrag1 = false
//      isFrag2 = false
//      isFrag3 = false
//      testComposite(model.morph, "Test1")
//
//      isFrag1 = true
//      isFrag2 = false
//      isFrag3 = false
//      testComposite(model.morph, "Test2")
//
//      isFrag1 = true
//      isFrag2 = true
//      isFrag3 = false
//      testComposite(model.morph, "Test3")
//
//      isFrag1 = true
//      isFrag2 = true
//      isFrag3 = true
//      testComposite(model.morph, "Test4")
//    }
//
//
//    // Implicit (mutable) morphing
//
//    val composite = model.mutableProxy
//
//    {
//
//      isFrag1 = false
//      isFrag2 = false
//      isFrag3 = false
//      composite.remorph()
//
//      testComposite(composite, "Test1")
//
//      isFrag1 = true
//      isFrag2 = false
//      isFrag3 = false
//      composite.remorph()
//
//      testComposite(composite, "Test2")
//
//      isFrag1 = true
//      isFrag2 = true
//      isFrag3 = false
//      composite.remorph()
//
//      testComposite(composite, "Test3")
//
//      isFrag1 = true
//      isFrag2 = true
//      isFrag3 = true
//      composite.remorph()
//
//      testComposite(composite, "Test4")
//    }
//
//    println(model)
//
//  }
//
//}
