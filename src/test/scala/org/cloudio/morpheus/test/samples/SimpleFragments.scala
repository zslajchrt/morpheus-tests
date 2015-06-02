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
  def onA(x: Int) = x
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


//@dimension
trait S[X] {

  def getX: X

  def setX(x: X): Unit

}

@fragment
trait SImpl extends S[Int] {
  private var x_ : Int = _

  override def getX: Int = x_

  override def setX(x: Int): Unit = x_ = x
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