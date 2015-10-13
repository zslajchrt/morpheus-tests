package org.cloudio.morpheus.test

import org.morpheus._
import org.morpheus.Morpheus._
import org.cloudio.morpheus.test.samples._
import org.junit.Test
import org.junit.Assert._
import org.morpheus.test.illTyped

/**
 *
 * Created by zslajchrt on 20/04/15.
 */
class AdditionalTests {

    @Test
    def testModelWithDimension(): Unit = {
      implicit val dFrag = external[D](null)
      val c = compose[D]
    }

    @Test
    def testCompleteOneAlternative(): Unit = {
      val ref1: &[C with D] = compose[A with B with C with D1 with I]
      val ci1 = *(ref1)
      val pxs = proxies(ci1)
      import pxs._

      implicitly[C] // explicit fragment in ref1
      implicitly[B] // this is the C's dependency
      implicitly[D] // explicit fragment in ref1
    }

    @Test
    def testCompleteMoreAlternatives(): Unit = {
      val ref1: &[C or D] = compose[A with ((B with C) or D1)]
      val ci1 = *(ref1)

      val pxs = proxies(ci1)
      import pxs._

      implicitly[C]
      implicitly[B]
      implicitly[D]
    }


  @Test
  def testSingletonRecompositionByProxies(): Unit = {
    //val ref1: &[C] = singleton[B with C]
    val ref1: &[B or (B with C)] = singleton[B or (B with C)]
    val ref1p: &[$[B] or ($[B] with C)] = singleton[B or (B with C)]
    val ci1 = *(ref1)

//    val pxs = proxies(ci1)
//    import pxs._
//
//    val ci2 = glean[B with C]
//    val ref2: &[B with C] = ci2
//    ci1.~.x = 2
//    val x1 = ci1.~.x
//    val x2 = ci2.~.x
//    assertEquals(2, x1)
//    assertEquals(2, x2)

//    val ci3 = glean[B or (B with C)]
//    val ref3: &[B or (B with C)] = ci3
    //switch[B or (B with C)](RootStrategy[ci3.Model](), () => None)
    //ci3.morph_~()
//    val y1 = ci1.~.
//    val y2 = ci2.~.x
//    assertEquals(2, x1)
//    assertEquals(2, x2)
  }

  @Test
  def testNonSingletonRecompositionByProxies(): Unit = {
    val ref1: &[C] = compose[B with C]
    val ci1 = *(ref1)

    val ref2a: &[$[A] with C] = ci1

    val pxs = proxies(ci1)
    import pxs._

    val ci2 = glean[B with C]
    ci1.~.x = 2
    val x1 = ci1.~.x
    val x2 = ci2.~.x
    assertEquals(2, x1)
    assertEquals(0, x2)

  }

  @Test
  def testTupledRef(): Unit = {
    val ref1: &[A with B] = compose[A with B]
    val tupledRef1: (Frag[A, Unit] => A, Frag[B, Unit] => B) = tupled(ref1)

    val ref2: &[$[A] with $[B] with D] = compose[D1]
    // use tupledRef1 as placeholders
    val comp2 = *(ref2, tupledRef1)
    val inst2 = comp2.~

    assertEquals(1, inst2.onA(1))
    assertEquals("x", inst2.onB("x"))
    assertEquals(6, inst2.onD(3))
  }

  val pingPong1 = {
    implicit val player = single[RealPingMan, RealPingManConfig](RealPingManCfg(Player("Jacob", "China")))
    implicit val court = single[Court, CourtConfig](CourtConfig("Beijing"))
    val ref: &[PingMan with PongMan] = compose[RealPingMan with DummyPongMan with Court]
    *(ref)
  }

  val pingPong2 = {
    implicit val player = single[RealPongMan, RealPongManConfig](RealPongManCfg(Player("Jakub", "Czech")))
    implicit val court = single[Court, CourtConfig](CourtConfig("Prague"))
    val ref: &[PingMan with PongMan] = compose[DummyPingMan with RealPongMan with Court]
    *(ref)
  }

  val pingPong3 = {
    implicit val player = single[RealPingMan, RealPingManConfig](RealPingManCfg(Player("Jacob2", "China")))
    implicit val court = single[Court, CourtConfig](CourtConfig("Beijing"))
    val ref: &[PingMan with PongMan] = compose[RealPingMan with DummyPongMan with Court]
    *(ref)
  }

  val pingPong4 = {
    implicit val player = single[RealPongMan, RealPongManConfig](RealPongManCfg(Player("Jakub2", "Czech")))
    implicit val court = single[Court, CourtConfig](CourtConfig("Prague"))
    val ref: &[PingMan with PongMan] = compose[DummyPingMan with RealPongMan with Court]
    *(ref)
  }

//  @Test
//  def testCompositeFork(): Unit = {
//    pingPong1.~.ping()
//
//    val forkPingPong1 = \/(pingPong1, pingPong2)
//    val forkPlay = forkPingPong1.~
//    forkPlay.ping()
//
//    val forkPingPong2 = \/(pingPong3, pingPong4)
//    //val forkPlay2 = forkPingPong2.~
//
//    val forkPingPong3 = \/(forkPingPong1, forkPingPong2)
//    val forkPlay3 = forkPingPong3.~
//
//    val refTest: &[PingMan] = forkPingPong3
//    val refInstTest = *(refTest)
//    val pingMan: PingMan = refInstTest.~
//
//    pingMan.ping()
//
//  }

//  @Test
//  def testComplementaryReferences(): Unit = {
//    val ref1: &[PingMan with $[PongMan]] = pingPong1
//    val ref2: &[$[PingMan] with PongMan] = pingPong2
//
//    val cmp1 = *(ref1, tupled(ref2))
//    val cmp2 = *(ref2, tupled(ref1))
//
//    val cmp3 = \/(cmp1, cmp2)
//    //
//    //    // the game is played in either context depending on the number of the played games
//    //    // ctx1
//    cmp3.~.ping()
//    //    // ctx2
//    cmp3.~.ping()
//
//  }


}
