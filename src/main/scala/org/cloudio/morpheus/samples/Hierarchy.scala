//package org.cloudio.morpheus.samples
//
///**
// * Created by zslajchrt on 13/01/15.
// *
// */
//class Hierarchy {
//
//}
//
//
//class BaseClass1 extends Sample1 {
//
//  def getX = "X"
//
//  def printText(s: String): Unit = {
//    println(s)
//  }
//}
//
//class BaseClass2 extends BaseClass1 {
//
//  def getY = "Y"
//
//  override def printText(s: String): Unit = {
//    super.printText(s + "1")
//  }
//}
//
//trait Trait1 extends BaseClass2 {
//
//  def ww: Int = 1
//
//  override def printText(s: String): Unit = {
//    super.printText(s + "2")
//  }
//}
//
//trait TraitY {
//  def doSomething(): String
//}
//
//trait TraitX extends BaseClass2 with TraitY {
//
//  def doSomething(): String = {
//    "X"
//  }
//
//}
//
//trait Trait2 extends TraitX {
//  def getZ = "Z"
//  override def printText(s: String): Unit = {
//    super.printText(s + "3" + uu(2) + doSomething())
//  }
//
//  override def doSomething() = "Y" + super.doSomething()
//
//  def uu(i: Int): Int
//}
//abstract class Trait2fragment extends Trait2
//
//trait Trait3 extends Trait2 {
//
//  this: Trait1 =>
//
//  private[this] var k: Int = 0
//  private[this] lazy val tt: Int = 0
//  //override def getZ: String = super.getZ
//
//  override def printText(s: String): Unit = {
//    k += 1
//    val s2 = s + getX + getY + getZ + u + tt + ww
//    super.printText(s + "4")
//  }
//}
//abstract class Trait3fragment extends Trait3 {
//  this: Trait1 =>
//}
//
//trait BaseClass2Interceptor1 extends BaseClass2 {
//  override def getX: String = super.getX + "!"
//}
//
//trait TraitYInterceptor1 extends TraitY {
//  abstract override def doSomething(): String = super.doSomething()
//}
//
//class BBB extends TraitX with TraitYInterceptor1 {
//}
//
//
//abstract class DerivedClass extends BaseClass2 with Trait1 with Trait3 with BaseClass2Interceptor1 {
//  override def printText(s: String): Unit = super.printText(s + "5")
//}