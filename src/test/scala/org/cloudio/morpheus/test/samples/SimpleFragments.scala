package org.cloudio.morpheus.test.samples

import java.util.concurrent.{BlockingQueue, SynchronousQueue, CountDownLatch}

import org.morpheus.Morpher._
import org.morpheus.Morpheus._
import org.morpheus._


/**
 * Created by zslajchrt on 27/03/15.
 */
class SimpleFragments {

}

@fragment
trait A {
  var xx = 0

  def onA(x: Int) = {
    xx += x
    x
  }
}

@fragment
trait B {
  var y: Int = 0

  def onB(s: String) = s

  def onB2(y: Int) = y + y
}


@fragment
trait C {
  this: B =>

  var x: Int = 0

  def onC(s: String) = onB(s + s)

  def onC2(queueIn: BlockingQueue[Int], queueOut: BlockingQueue[Option[Int]]): Unit = {

    new Thread() {
      override def run(): Unit = {

        var eof = false
        while (!eof) {

          val x = queueIn.take()
          if (x == -1) {
            eof = true
          } else {
            select[B](C.this) match {
              case Some(b) => queueOut.put(Some(b.onB2(x)))
              case None => queueOut.put(None)
            }
          }
        }


      }
    }.start()

  }

}


@dimension
trait D {
  def onD(l: Long): Long
}

@fragment
trait D1 extends D {
  override def onD(l: Long): Long = l + l

  def onD1(l: Long): Long = l + l + l
}

@fragment
trait D2 extends D {
  override def onD(l: Long): Long = l * l
}

@fragment
trait D3 extends D {
  override def onD(l: Long): Long = 1 / l
}

@fragment
trait D4 extends D {
  override def onD(l: Long): Long = 1 / l / l
}

@dimension
@wrapper
trait FailoverD extends D {

  abstract override def onD(l: Long): Long = {
    try {
      super.onD(l)
    }
    catch {
      case t: Throwable =>
        //for (m <- mirror(this); mm <- m.owningMutableProxy) mm.switchToOtherThan[D]
        for (m <- mirror(this); mm <- m.owningMutableProxy) {
          val ci = m.kernel
          mm.findFragmentHolder[D] match {
            case Some(dHolder) =>
              // we do not want the fragment that caused this problem
              val defaultStrategy = FixedStrategy[m.Model](m.alternatives)
              mm.remorph(RatingStrategy(defaultStrategy, FindFragment(dHolder.fragment.fragTag.tpe, DecRating)))
            case None => sys.error("should not be here")
          }
        }

        throw t
    }
  }
}

@fragment
trait E {
  this: D =>
}


@fragment
trait F {
  this: D1 =>

  def onF(l: Long) = onD1(l)
}

@fragment
trait FF {
  this: F =>

  def onFF(l: Long) = onF(l)
}

@fragment
trait G {
  this: D =>
}

@fragment
@wrapper
trait H extends A


@dimension
@wrapper
trait I extends D {
  abstract override def onD(l: Long): Long = super.onD(2 * l)

  def onI(l: Long) = onD(2 * l)
}

@dimension
@wrapper
trait J extends D {

  abstract override def onD(l: Long): Long = super.onD(10 * l)

  def onJ(l: Long) = onD(10 * l)

}

@fragment
@wrapper
trait K extends A

@fragment
@wrapper
trait L extends D1

@fragment
trait M {
  def onM(i: Int): Int = i
}

@fragment
trait N extends D {

  //this: M with (A or B or Unit) =>
  this: M with \?[A] with \?[B] =>

  override def onD(l: Long): Long = {
    val maybeA: Option[Int] = for (a <- select[A](this))
      yield a.onA(l.toInt)

    val maybeB: Option[Int] = for (b <- select[B](this))
      yield b.onB2(maybeA.getOrElse(1))

    //require(maybeA.isDefined || maybeB.isDefined )

    val res: Int = maybeB.getOrElse(maybeA.getOrElse(0))
    res
  }
}

@fragment
trait O {
  def onO(i: Int): Int = i
}

@fragment
trait P {
  def onP(i: Int): Int = i
}

@fragment
trait R {
  this: (A or B) =>

  def switch() {
    for (m <- mirror(this);
         mm <- m.owningMutableProxy) {
      mm.remorph()
    }
  }
}


@ignore
@dimension
trait S[X] {

  def getX: X

  def setX(x: X): Unit

}

@fragment
trait SInt extends S[Int] {
  private var x_ : Int = _

  override def getX: Int = x_

  override def setX(x: Int): Unit = x_ = x
}

@fragment
trait SBoolean extends S[Boolean] {
  private var x_ : Boolean = _

  override def getX: Boolean = x_

  override def setX(x: Boolean): Unit = x_ = x
}

trait S$dimension[X] extends SuperBase[S[X]] with S[X] {

  def getX: X = $super$.getX

  def setX(x: X): Unit = $super$.setX(x)
}

@dimension
@wrapper
trait SWrapper extends S[Int] {
  abstract override def getX = 2 * super.getX
}

@ignore
@dimension
trait T {

  type X

  def getX: X

  def setX(x: X): Unit

}

@fragment
trait TImpl extends T {

  private var x_ : Int = _

  override type X = Int

  override def getX: Int = x_

  override def setX(x: X): Unit = x_ = x
}

trait T$dimension extends SuperBase[T] with T {
  outer =>

  private def sup = $super$.asInstanceOf[T {type X = outer.X}]

  def getX: X = sup.getX

  def setX(x: X): Unit = sup.setX(x)
}

@dimension
@wrapper
trait TWrapper extends T {

  override type X = Int

  abstract override def getX: Int = 2 * super.getX
}

@fragment
trait U {
  this: (A or B) =>

  def switch(init: Boolean) {
    for (m <- mirror(this);
         mm <- m.owningMutableProxy) {
      val i = m.kernel

      if (init) {
        //mm.remorph(i.lookupAlt(i.isFragment[A]))
        mm.remorph(Normalizer(RatingStrategy(FixedStrategy(mm.alternatives), hasFragment[A](IncRating)), 0, 0.5))
      } else select[A](mm) match {
        case None =>
          //mm.remorph(i.lookupAlt(i.isFragment[A]))
          mm.remorph(Normalizer(RatingStrategy(FixedStrategy(mm.alternatives), hasFragment[A](IncRating)), 0, 0.5))
        case Some(_) =>
          //mm.remorph(i.lookupAlt(i.isFragment[B]))
          mm.remorph(Normalizer(RatingStrategy(FixedStrategy(mm.alternatives), hasFragment[B](IncRating)), 0, 0.5))
      }

    }
  }
}


@fragment(confLevel = "partial")
trait WPartial {
  this: (A or B) =>

  def checkIt(): Unit = {
    //asMorphOf[A](mirror(this).get.kernel)
  }
}

@fragment(confLevel = "total")
trait WTotal {
  this: (A or B) =>

  def getWTotalRef: &[WTotal with (A or B)] = {
    &&(this)
    //    import org.morpheus._
    //    import org.morpheus.Morpheus._
    //    val ref = mirror(this) match {
    //      case None => None
    //      case Some(m) =>
    //        val fh = m.kernel.fragmentHolder[WTotal]
    //        val depsMaps = fh.get.fragment.depsMappings.get
    //        Some(new &(m.kernel.asInstanceOf[MorphKernel[Any]], depsMaps))
    //    }
    //    null
  }
}

@fragment
trait X {
  this: B =>

  def onX(a: String) = onB("X" + a)
}

@fragment
@wrapper
trait XW1 extends X {
  this: B =>

  override def onX(a: String) = super.onX("XW1" + a)
}

@fragment
trait Red {
}

@fragment
trait Yellow {
}


@fragment
trait Green {
}

@fragment
trait RedEx {
  this: Red =>

}


@fragment
trait Color {
  var color: Int = 0
}


case class Player(name: String, origin: String)

@dimension
trait PingMan {
  val pingPlayer: Player

  def ping(): Unit
}

@fragment
trait DummyPingMan extends PingMan {
  this: PongMan =>

  val pingPlayer: Player = Player("Eskymo", "North Pole")

  def ping(): Unit = pong()
}

trait RealPingManConfig {
  val pingPlayer: Player

}

case class RealPingManCfg(pingPlayer: Player) extends RealPingManConfig

@fragment
trait RealPingMan extends PingMan with RealPingManConfig {
  this: PongMan with Court =>

  def ping(): Unit = pong()

}

@dimension
trait PongMan {
  val pongPlayer: Player

  var pongWin = 0

  def pong() = {
    pongWin += 1
  }
}

@fragment
trait DummyPongMan extends PongMan {
  this: PingMan =>

  val pongPlayer: Player = Player("Yetti", "Himalayas")
}

trait RealPongManConfig {
  val pongPlayer: Player
}

case class RealPongManCfg(pongPlayer: Player) extends RealPongManConfig

@fragment
trait RealPongMan extends PongMan with RealPongManConfig {
  this: PingMan with Court =>
}

trait CourtConfig {
  val location: String
}

object CourtConfig {
  def apply(loc: String) = new CourtConfig {
    override val location: String = loc
  }
}

@fragment
trait Court extends CourtConfig {
  var playedGames: Int = 0
}

@dimension
trait X1 {
  var v1: Int

  def onX1(i: Int): Unit
}

@fragment
trait X1Imp extends X1 {

  override var v1: Int = _

  def onX1(i: Int): Unit = {
    v1 = i
  }
}

@dimension
trait X2 {
  def onX2(s: String): Unit
}


@fragment
trait X2Imp extends X2 {
  this: X1 =>

  def onX2(s: String): Unit = {
    onX1(s.length)
  }
}

@dimension
trait X3 {
  def onX3(s: String, i: Int): Unit
}

@fragment
trait X3Imp extends X3 {
  this: X2 =>

  def onX3(s: String, i: Int): Unit = {
    onX2(s"$s$i")
  }

}

@dimension
trait X4 {
  def onX4(s: String, i: Int, b: Boolean): Unit
}

@fragment
trait X4Imp extends X4 {
  this: X3 =>

  def onX4(s: String, i: Int, b: Boolean): Unit = {
    onX3(s, 2 * i)
  }
}

@dimension
trait X5 {
  def onX5(s: String): Unit
}

@fragment
trait X5Imp extends X5 {
  this: X4 =>

  def onX5(s: String): Unit = {
    onX4(s, s.length, s.isEmpty)
  }
}