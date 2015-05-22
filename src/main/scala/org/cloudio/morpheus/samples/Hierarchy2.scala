//package org.cloudio.morpheus.samples
//
//import org.morpheus._
//import org.morpheus.{SuperBase, Super}
//
//import scala.util.DynamicVariable
//
///**
// * Created by zslajchrt on 14/01/15.
// *
// */
//object Hierarchy2 {
//
//
//  def testComposite(name: String, composite: Entity1 with DimA with DimB): Unit = {
//    println(s"************ $name *************")
//
//    composite.load()
//    val json: String = composite.convertToJson
//    composite.convertFromJson(json)
//
//    composite.store()
//  }
//
//
//
//
//  def main(args: Array[String]): Unit = {
////    val f1 = MorpheusMacros.map[FragmentB2].apply(new FragmentB2Config {
////      override def xx2: String = ""
////
////      override val mm: MongoPersistence = new MongoPersistenceImpl
////    })
//    //val f1 = MorpheusMacros.map[Entity1]
//
//
//
//    //FragmentB2Wrapper$fragment
//    val fragB2Frag: Class[_] = Class.forName("org.cloudio.morpheus.samples.FragmentB2$fragment")
//    println(fragB2Frag)
//    println(fragB2Frag.getAnnotation(classOf[fragmentClass]))
//
//    val dimAWrapper5Frag: Class[_] = Class.forName("org.cloudio.morpheus.samples.DimAWrapper5$wrapper")
//    println(dimAWrapper5Frag)
//    val d = dimAWrapper5Frag.getConstructors()(0).newInstance(new DimAWrapper5Config {
//      override val xyz: Int = 123
//    })
//    println(d)
//
//    //    val dimAwrapperCls: Class[_] = Class.forName("org.cloudio.morpheus.samples.DimAWrapper2$wrapper")
////    val dimAwrapper = dimAwrapperCls.newInstance().asInstanceOf[Super[DimA] with DimAWrapper2]
////
////
////    val dimAImpl1: DimA with Object {def convertToJson: String; def convertFromJson(json: String): Unit} = new DimA {
////      override def convertToJson: String = "xyz"
////
////      override def convertFromJson(json: String): Unit = {
////        println(json)
////      }
////    }
////
////
////    val dimAImpl2: DimA with Object {def convertToJson: String; def convertFromJson(json: String): Unit} = new DimA {
////      override def convertToJson: String = "xyz2"
////
////      override def convertFromJson(json: String): Unit = {
////        println(json + "***")
////      }
////    }
////
////    val dimBImpl1 = new DimB {
////      override def load(): Unit = println("loading")
////
////      override def store(): Unit = println("storing")
////    }
////
////
////    val dimBImpl2 = new DimB {
////      override def load(): Unit = println("loading2")
////
////      override def store(): Unit = println("storing2")
////    }
////
////    val dimAdimCls: Class[_] = Class.forName("org.cloudio.morpheus.samples.DimA$dimension")
////    val dimBdimCls: Class[_] = Class.forName("org.cloudio.morpheus.samples.DimB$dimension")
////
////    val dimA: Super[DimA] with DimA = dimAdimCls.newInstance().asInstanceOf[Super[DimA] with DimA]
////    val dimB: Super[DimB] with DimB = dimBdimCls.newInstance().asInstanceOf[Super[DimB] with DimB]
////
////    dimA.$$super$.withValue(dimAImpl1) {
////      dimB.$$super$.withValue(dimBImpl1) {
////        dimB.load()
////        dimA.convertFromJson(dimA.convertToJson)
////        dimB.store()
////      }
////    }
////
////    dimA.$$super$.withValue(dimAImpl2) {
////      dimB.$$super$.withValue(dimBImpl2) {
////        dimB.load()
////        dimA.convertFromJson(dimA.convertToJson)
////        dimB.store()
////      }
////    }
//
////    println("Composite 1")
////
////    class CompositeX extends Entity1(1) with FragmentA1 with FragmentB2 {
////      override val mm: MongoPersistence = new MongoPersistenceImpl
////    }
////
////    val x: CompositeX = new CompositeX()
////    testComposite(x)
////
////
////    println("Composite 2")
////
////    val y = new Entity1(2) with FragmentA2 with FragmentB2 {
////      override val mm: MongoPersistence = new MongoPersistenceImpl
////    }
////    testComposite(y)
//
//  }
//
//}
//
//
//class Entity1(val id: Long) {
//
//  def this() = this(0)
//
//  var x = 1
//
//  def calculate(i: Int): Unit = {
//    x = x * i
//  }
//
//  override def toString: String = "Entity1 id:" + id + " x:" + x
//}
//
//
//@dimension
//trait DimA {
//  def convertToJson: String
//
//  def convertFromJson(json: String)
//
//}
//
//
//// generated
//abstract class DimA$base extends DimA with Super[DimA] {
//
//  val $$super$: DynamicVariable[DimA] = new DynamicVariable[DimA](null)
//
//  override def convertToJson: String = $$super$.value.convertToJson
//
//  override def convertFromJson(json: String): Unit = $$super$.value.convertFromJson(json)
//}
//
//// pretty print
//@fragment
//trait FragmentA1 extends DimA {
//  this: Entity1 =>
//  def convertToJson: String = "{x=" + x + "}"
//
//  def convertFromJson(json: String) {} // todo
//}
//
//class FragmentA1_fragment extends FragmentA1 {
//  this: Entity1 =>
//}
//
//
//
//// basic print
//@fragment
//trait FragmentA2 extends DimA {
//  this: Entity1 =>
//  def convertToJson: String = {
//    println("Converting to JSON on entity " + id)
//    "{\n\tx=" + x + "\n}"
//  }
//
//  def convertFromJson(json: String): Unit = {
//    // todo
//    println("Converting from JSON on entity " + id + " => " + json)
//  }
//}
//
//class FragmentA2_fragment extends FragmentA2 {
//  this: Entity1 =>
//}
//
//
//@dimension
//trait DimB {
//
//  def load()
//
//  def store()
//
//  override def toString: String = "DIM_B"
//
//}
//
//// generated
//abstract class DimB$base extends DimB with Super[DimB] {
//  val $$super$: DynamicVariable[DimB] = new DynamicVariable[DimB](null)
//
//  override def load(): Unit = $$super$.value.load()
//
//  override def store(): Unit = $$super$.value.store()
//
//}
//
////abstract class DimB$base23
//
//
//
////@dimension
//abstract class DimB$base2 extends SuperBase[DimB] with DimB {
//  private def _$_ = $super$
//}
//
//
////object DimB$base2App {
////  def main(args: Array[String]) {
////    val dim2 = new DimB$base2
////    dim2.$$super$.withValue(new DimB {
////      override def store(): Unit = {
////        println("storing")
////      }
////
////      override def load(): Unit = {
////        println("loading")
////      }
////    }) {
////      dim2.load()
////      dim2.$$super$.withValue(new DimB {
////        override def store(): Unit = {
////          println("storing2")
////        }
////
////        override def load(): Unit = {
////          println("loading2")
////        }
////      }) {
////        dim2.load()
////        dim2.store()
////      }
////      dim2.store()
////
////    }
////  }
////}
//
//
//// JPA persistence
//@fragment
//trait FragmentB1 extends DimB {
//  this: Entity1 =>
//  def load(): Unit = {
//    val entity = this
//    // call entityManager.refresh(entity)
//  }
//
//  def store(): Unit = {
//    val entity = this
//    // call entityManager.merge(entity)
//  }
//}
//
//class FragmentB1_fragment extends FragmentB1 {
//  this: Entity1 =>
//}
//
//
//// Mongo persistence
//trait FragmentB2Base extends DimB {
//  protected var t = "T"
////  protected var tt: String
//  protected val xx: String
//
//}
//
//trait FragmentB2Config {
//  val mm: MongoPersistence
//  def xx2: String
//}
//
//
//@fragment
//trait FragmentB2 extends FragmentB2Base with FragmentB2Config {
//  this: Entity1 with DimA =>
////  val mm: MongoPersistence
//
////  // try to access the entity
////  println(id)
//
//  override val xx: String = xx2
//
//  protected var s = "S" + t
//  //private var s = "S" + id
//  //private var s = "S" + convertToJson
//
//  def load(): Unit = {
//    val entityAsJson = mm.load(id)
//    convertFromJson(entityAsJson)
//    println("Loaded entity " + id + " xx:" + xx + " s:" + s)
//  }
//
//  def store(): Unit = {
//    val entityAsJson = convertToJson
//    mm.store(id, entityAsJson)
//    println("Stored entity " + id)
//  }
//
//  def readFromMongo(entityId: Long) = ""
//
//  override def toString: String = {
//    "FragmentB:" + super.toString + "as JSON: " + convertToJson
//    //"FragmentB:"
//  }
//
//}
//
//
//trait MongoPersistence {
//  def load(id: Long): String
//
//  def store(id: Long, json: String)
//}
//
//
//
//class MongoPersistenceImpl extends MongoPersistence {
//  override def load(id: Long): String = ""
//
//  override def store(id: Long, json: String) {}
//}
//
//
//
////@fragment
//class FragmentB2_fragment($config$: FragmentB2Config) extends FragmentB2 {
//  this: Entity1 with DimA =>
//  val mm: MongoPersistence = $config$.mm
//  val xx2: String = $config$.xx2
//}
//
////class FragmentB2_fragment(mm$param: MongoPersistence, xx$param: String) extends FragmentB2 {
////  this: Entity1 with DimA =>
//////  override protected var tt: String = ""
////  val mm: MongoPersistence = mm$param
////  protected val xx: String = xx$param
////}
//
//
//// concrete overriding
//
//@wrapper @fragment
//trait FragmentB2Wrapper extends FragmentB2 {
//  this: Entity1 with DimA =>
//
//
//  @MyAnnot
//  def ggg() = 1
//
//  override def load(): Unit = {
//    println("Loading in FragmentB2Wrapper")
//    super.load()
//  }
//
//  override def store(): Unit = {
//    println("Storing in FragmentB2Wrapper")
//    super.store()
//  }
//}
//
//
//
//// generated
//@fragmentClass(deps = Array(classOf[org.cloudio.morpheus.samples.Entity1], classOf[org.cloudio.morpheus.samples.DimA]))
//abstract class FragmentB2Wrapper_fragment extends FragmentB2Wrapper {
//  this: Entity1 with DimA =>
//}
//
//trait FragmentB2Wrapper2Config {
//  val zumo: String
//}
//
//
//@wrapper @fragment
//trait FragmentB2Wrapper2 extends FragmentB2 with FragmentB2Wrapper2Config {
//  this: Entity1 with DimA =>
//
//  var g = "jjj"
//
//  override def load(): Unit = {
//    println("Loading in FragmentB2Wrapper2 using " + zumo + " and an inherited field: " + xx + " and own field: " + g)
//    g = g + g
//    s = s + "!"
//    super.load()
//  }
//
//
//  override def store(): Unit = {
//    println("Storing in FragmentB2Wrapper2 using " + zumo)
//    super.store()
//  }
//
//  def abc(i: Int) = g + i + xx
//}
//
//
//// abstract overriding
//
//trait DimAWrapper1 extends DimA {
//
//  val r: (String) => String
//
//  private def compact(s: String): String = r(s)
//
//  abstract override def convertFromJson(json: String): Unit = {
//    println("DimAWrapper1:convertFromJson")
//    super.convertFromJson(compact(json))
//  }
//}
//
//
//abstract class DimAWrapper1Wrapper_fragment(val r: (String) => String) extends DimA$base with DimAWrapper1
//
//class X(val r: (String) => String) extends FragmentA1 with DimAWrapper1 {
//  this: Entity1 =>
//}
//
//trait DimAWrapper11 extends DimAWrapper1 {
//  abstract override def convertFromJson(json: String): Unit = {
//    super.convertFromJson(json + "!")
//
//  }
//}
//
//
//abstract class DimAWrapper11Wrapper_fragment(val r: (String) => String) extends DimA$base with DimAWrapper11
//
//@wrapper @dimension
//trait DimAWrapper2 extends DimA {
//  abstract override def convertToJson: String = {
//    super.convertToJson + "***"
//  }
//
//  def exH(i: Int) = convertToJson + "i"
//}
//
//class DimAWrapper2Wrapper_fragment extends DimA$base with DimAWrapper2
//
//trait DimAWrapper3 extends DimA {
//  abstract override def convertToJson: String = super.convertToJson + "%%%"
//
//  abstract override def convertFromJson(json: String): Unit = super.convertFromJson(json + "@@@")
//
//}
//
//abstract class DimAWrapper3Wrapper_fragment extends DimA$base with DimAWrapper3
//
//trait DimAWrapper5Config {
//  val xyz: Int
//}
//
//@wrapper @dimension
//trait DimAWrapper5 extends DimA with DimAWrapper5Config {
//  abstract override def convertToJson: String = {
//    super.convertToJson + "%%%" + xyz
//  }
//
//  abstract override def convertFromJson(json: String): Unit = {
//    super.convertFromJson(json + "@@@" + xyz)
//  }
//
//  def exM(i: Int, j: Int) = convertToJson + "i" + i + j
//
//}
//
////// generated
////class DimAWrapper5Wrapper_fragment(@autoproxy val $config$: DimAWrapper5Config) extends SuperBase[DimA] with DimA with DimAWrapper5 {
////
////  //override val xyz: Int = $config$.xyz
////  @autoproxy override val $$super$: DynamicVariable[DimA] = super.$$super$
////}
//
//
