package org.cloudio.morpheus.test

import java.util.concurrent.SynchronousQueue

import org.morpheus.Morpheus._
import org.morpheus.Morpher._
import org.morpheus._
import org.cloudio.morpheus.test.samples._
import org.junit.Assert._
import org.junit.Test
import org.morpheus.test.illTyped

/**
* Created by zslajchrt on 07/04/15.
*/
class AdvancedTests {

  @Test
  def testComplexPlaceholder(): Unit = {
    val k1 = singleton[A with B]
    // $[X with XW1 with $[A]] is translated to $[X] with $[XW1] with A
    val r1: &[$[X with XW1 with $[A]]] = k1
    val k2 = *(r1, single[X], single[XW1])
    val res = k2.~.onX("x")
    assertEquals("XXW1x", res)
  }


  @Test
  def testPlaceholderWrapsPlaceholder(): Unit = {
    val k1 = singleton[A with B]
    val r1: &[$[X] with $[XW1]] = k1
    val k2 = *(r1, single[X], single[XW1])
    val res = k2.~.onX("x")
    assertEquals("XXW1x", res)
  }

  @Test
  def testSelfKernelReference(): Unit = {
    val k1 = singleton[((A with D1) or (B with C with D2)) with WTotal]
    val r1: &[WTotal with (A or B)] = k1.~.getWTotalRef
    val k2 = *(r1)
  }

  @Test
  def testMoreSameFragmentsInSourceMorphType(): Unit = {
    val k1 = singleton[(A with D1) or (A with D2) or B]
    val a1 = asMorphOf[A with D1](k1.!)
    val a2 = asMorphOf[A with D2](k1.!)

    a1.onA(1)
    assertEquals(1, a1.xx)
    assertEquals(1, a2.xx)
    a2.onA(1)
    assertEquals(2, a1.xx)
    assertEquals(2, a2.xx)

    val r1: &[A or B] = k1
    val k2 = *(r1)
    select[A](k2.!) match {
      case None => fail()
      case Some(a3) =>
        assertEquals(2, a3.xx)
        a3.onA(1)
        assertEquals(3, a1.xx)
        assertEquals(3, a2.xx)
        assertEquals(3, a3.xx)

        val a = k1.fragments.select[FragmentHolder[A]].proxy
        assertEquals(3, a.xx)
    }


    //
  }

  @Test
  def testUnmasking(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)
    var lightBlocked: Option[Int] = None
    val s1 = unmask[Red or Yellow or Green](rootStrategy(tlModel), lightBlocked)
    var lightSel: Int = 0
    val s2 = promote[Red or Yellow or Green](s1, lightSel)
    val tlComp = singleton(tlModel, s2)

    assertTrue(select[Red](tlComp.~.remorph).isDefined)
    lightSel = 1
    assertTrue(select[Yellow](tlComp.~.remorph).isDefined)
    lightSel = 2
    assertTrue(select[Green](tlComp.~.remorph).isDefined)


    lightBlocked = Some(2) // block green

    lightSel = 0
    assertTrue(select[Red](tlComp.~.remorph).isDefined)
    lightSel = 1
    assertTrue(select[Yellow](tlComp.~.remorph).isDefined)
    lightSel = 2
    // Despite the green is promoted, the red is selected since the green is prohibited and the red is the first
    // available alternative in the model
    assertTrue(select[Red](tlComp.~.remorph).isDefined)
  }

  @Test
  def testMaskingStrategyInReference(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightSel: Int = 0
    val tlStrategy = mask[Red or Yellow or Green](lightSel)
    val tlComp = compose(tlModel, tlStrategy)
    val tlCompRef: &[Red or Green] = tlComp
    val tlComp2 = *(tlCompRef)

    select[Red](tlComp2.~) match {
      case None => fail()
      case Some(r) => // OK
    }

    lightSel = 2
    tlComp2.~.remorph

    select[Green](tlComp2.~) match {
      case None => fail()
      case Some(y) => // OK
    }

    // masking the unpaired source fragment
    lightSel = 1
    try {
      tlComp2.~.remorph
      fail()
    }
    catch {
      case e: AlternativeNotAvailableException =>
        // OK
    }
  }


  @Test
  def testPromotingStrategyDrivenByInternalState(): Unit = {
    val tlModel = parse[(Red or Yellow or Green) with Color](true)
    val strat = promote[Red or Yellow or Green](tlModel)({
      case None => None
      case Some(tl) => Some(tl.color)
    })

    val tlKernel = singleton(tlModel, strat)

    assertTrue(select[Red](tlKernel.~).isDefined)

    tlKernel.~.color = 1

    assertTrue(select[Yellow](tlKernel.~.remorph()).isDefined)

    tlKernel.~.color = 2

    assertTrue(select[Green](tlKernel.~.remorph()).isDefined)
  }

  @Test
  def testMaskingAndKernelReference(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightSel: Int = 1 // deliberately set to a non-RED, which is not present in the kernel ref model
    val tlStrategy = mask[Red or Yellow or Green](lightSel)
    val tlKernel = compose(tlModel, tlStrategy)

    val tlRef: &[/?[Red with $[RedEx]]] = tlKernel
    val tlKernel2 = *(tlRef, single[RedEx])

    val x1 = ()
    val x2 = ()

    select[RedEx](tlKernel2.~) match {
      case None => // OK
      case Some(r) => fail()
    }

    lightSel = 0 // set TL to RED

    // Now the underlying model masks RED so {Red with RedEx} alt can be selected in the slave model
    select[RedEx](tlKernel2.~.remorph) match {
      case None => fail()
      case Some(r) => // OK
    }
  }

  @Test
  def testUnmaskingAndPromotingStrategy(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightSel: Int = 0
    val s0 = promote[Red or Yellow or Green](lightSel)
    var redCtrl: Int = 1
    val s1 = unmask[/?[Red]](s0, redCtrl)
    var yellowCtrl: Int = 1
    val s2 = unmask[/?[Yellow]](s1, yellowCtrl)
    var greenCtrl: Int = 1
    val s3 = unmask[/?[Green]](s2, greenCtrl)
    val tlComp = compose(tlModel, s3)

    // no masking

    select[Red](tlComp.~) match {
      case None => fail()
      case Some(r) => // OK
    }

    lightSel = 1
    tlComp.~.remorph

    select[Yellow](tlComp.~) match {
      case None => fail()
      case Some(y) => // OK
    }

    lightSel = 2
    tlComp.~.remorph
    select[Green](tlComp.~) match {
      case None => fail()
      case Some(g) => // OK
    }

    redCtrl = 1
    yellowCtrl = 0
    greenCtrl = 0
    lightSel = 2 // green is promoted but forbidden
    // yellow and green are cleared, only red is available
    select[Red](tlComp.~.remorph) match {
      case None => fail()
      case Some(r) => // OK
    }

    // red is unmasked
    redCtrl = 1
    yellowCtrl = 1
    greenCtrl = 1

    // green should be now available
    select[Green](tlComp.~.remorph) match {
      case None => fail()
      case Some(r) => // OK
    }
  }

  @Test
  def testMaskingStrategy(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightSel: Int = 0
    val tlStrategy = mask[Red or Yellow or Green](lightSel)
    val tlComp = compose(tlModel, tlStrategy)

    select[Red](tlComp.~) match {
      case None => fail()
      case Some(r) => // OK
    }

    lightSel = 1
    tlComp.~.remorph

    select[Yellow](tlComp.~) match {
      case None => fail()
      case Some(y) => // OK
    }

    lightSel = 2
    tlComp.~.remorph
    select[Green](tlComp.~) match {
      case None => fail()
      case Some(g) => // OK
    }
  }

  @Test
  def testRatingStrategyWithImmutableMorph(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightSel = Set((0, 1), (1, 0), (2, 0))
    val tlStrategy = rate[Red or Yellow or Green](lightSel)
    val tlComp = compose(tlModel, tlStrategy)


    var tl = tlComp.!

    select[Red](tl) match {
      case None => fail()
      case Some(r) => // OK
    }

    lightSel = Set((0, 0), (1, 1), (2, 0))
    tl = tl.remorph

    select[Yellow](tl) match {
      case None => fail()
      case Some(y) => // OK
    }

    lightSel = Set((0, 0), (1, 0), (2, 1))
    tl = tl.remorph

    select[Green](tl) match {
      case None => fail()
      case Some(g) => // OK
    }
  }

  @Test
  def testRatingStrategy(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightSel = Set((0, 1), (1, 0), (2, 0))
    val tlStrategy = rate[Red or Yellow or Green](lightSel)
    val tlComp = compose(tlModel, tlStrategy)

    select[Red](tlComp.~) match {
      case None => fail()
      case Some(r) => // OK
    }

    lightSel = Set((0, 0), (1, 1), (2, 0))
    tlComp.~.remorph

    select[Yellow](tlComp.~) match {
      case None => fail()
      case Some(y) => // OK
    }

    lightSel = Set((0, 0), (1, 0), (2, 1))
    tlComp.~.remorph
    select[Green](tlComp.~) match {
      case None => fail()
      case Some(g) => // OK
    }
  }

  @Test
  def testPromotingStrategy(): Unit = {
    val tlModel = parse[Red or Yellow or Green](true)

    var lightNum = 0
    val tlStrategy = promote[Red or Yellow or Green](Some(lightNum))
    val tlComp = compose(tlModel, tlStrategy)

    select[Red](tlComp.~) match {
      case None => fail()
      case Some(r) => // OK
    }

    lightNum = 1
    tlComp.~.remorph
    select[Yellow](tlComp.~) match {
      case None => fail()
      case Some(y) => // OK
    }

    lightNum = 2
    tlComp.~.remorph
    select[Green](tlComp.~) match {
      case None => fail()
      case Some(g) => // OK
    }
  }

  @Test
  def testSlaveControlWithPlaceholder(): Unit = {
    val masterInst = {
      implicit val mutFrgCfg = mutableFragment()
      compose[(Red or Green) with MutableFragment]
    }

    // Slave

    val morphEvent = CompositeEvent("morphLight2", null, null)
    val activeLight2 = EventMonitor[Int]("activeLight2", morphEvent)
    val slaveRef: &[(Red or $[Yellow] or Green) with $[MutableFragment]] = masterInst
    val placehld = (single[Yellow], mutableFragment(activeLight2))
    val slaveRootStrategy = rootStrategy(slaveRef)
    val slaveStrategy = promote[Red or Yellow or Green](slaveRootStrategy, activeLight2)

    val slaveInst = *(slaveRef, slaveStrategy, placehld)
    val slave = slaveInst.~
    val isSlaveListening = slave.startListening(morphEvent.nameSelector)
    slave.fire("activeLight2", 0, null)


    select[Red](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    slave.fire("activeLight2", 2, null)

    select[Green](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    slave.fire("activeLight2", 1, null)

    select[Yellow](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

  }

  @Test
  def testSlaveControl(): Unit = {
    val morphEvent = CompositeEvent("morphLight", null, null)
    val activeLight = EventMonitor[Int]("activeLight", morphEvent)

    val masterModel = parse[(Red or Yellow or Green) with MutableFragment](true)
    val masterStrategy = promote[Red or Yellow or Green](rootStrategy(masterModel), activeLight)

    implicit val mutFrgCfg = mutableFragment(activeLight)
    val masterInst = compose(masterModel, masterStrategy)
    val master = masterInst.~
    master.startListening(morphEvent.nameSelector)

    master.fire("activeLight", 0, null)
    select[Red](master) match {
      case Some(s) => //OK
      case None => fail()
    }

    master.fire("activeLight", 1, null)
    select[Yellow](master) match {
      case Some(s) => //OK
      case None => fail()
    }

    master.fire("activeLight", 2, null)
    select[Green](master) match {
      case Some(s) => //OK
      case None => fail()
    }

    // Slave

    val slaveRef: &[Red or Green] = masterInst
    val slaveInst = *(slaveRef)
//    val newSlaveStrategy = new MorphingStrategy[slaveInst.Model] {
//
//      override def chooseAlternatives(instance: MorphKernel[slaveInst.Model])(owningMutableProxy: Option[instance.MutableLUB]): Alternatives[slaveInst.Model] = {
//        val origAlts: Alternatives[slaveInst.Model] = slaveInst.defaultStrategy.chooseAlternatives(instance)(None)
//        val origRatedAltList = origAlts.toList
//        val commonRating: Double = origRatedAltList.head._2
//
//        if (origRatedAltList.tail.forall(_._2 == commonRating)) {
//          val greenAlt = origRatedAltList.filter(_._1.contains(FragmentNode(1, false))).map(_._1).toSet
//          origAlts.promote(greenAlt)
////          // all alts have the same rating
////          val last = origRatedAltList.last
////          // promote Green
////          (last._1, commonRating + 1) :: origRatedAltList.dropRight(1)
//        } else {
//          origAlts
//        }
//      }
//    }
//    val slave = slaveInst.morph_~(newSlaveStrategy)
    val slave = slaveInst.~

    val isSlaveListening = slave.startListening()

    select[Green](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    master.fire("activeLight", 0, null)

    select[Red](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    master.fire("activeLight", 2, null)

    select[Green](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    // unknown light for the slave, but the default is Red
    master.fire("activeLight", 1, null)

    // Green is default overridden by the custom slave strategy
    select[Red](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    val isSlaveListeningStopped = slave.stopListening()

    master.fire("activeLight", 2, null)

    // No change
    select[Red](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

    // listen again
    val isSlaveListening2 = slave.startListening()

    master.fire("activeLight", 2, null)

    select[Green](slave) match {
      case Some(s) => //OK
      case None => fail()
    }

  }

  @Test
  def testFragmentConformanceLevel(): Unit = {
    // WPartial -> [A or B]

    illTyped(
      """
        val exist0 = compose[D1 with WPartial]
      """)

    illTyped(
      """
        val incl0 = compose[D1 with WTotal]
      """)

    val exist1 = compose[A with WPartial]

    illTyped(
      """
        val incl1 = compose[A with WTotal]
      """)

    illTyped(
      """
        val exist2 = compose[A with B with WPartial]
      """)

    illTyped(
      """
        val incl2 = compose[A with B with WTotal]
      """)

    val exist3 = compose[(A or B) with WPartial]
    val incl3 = compose[(A or B) with WTotal]
  }

  @Test
  def testPartialConformanceLevel(): Unit = {
    val c0 = compose[A or B]
    val existRef0: ~&[A or B] = c0
    val existInst0 = *(existRef0)
    assertEquals(Partial, existInst0.conformanceLevel)

    val c1 = compose[A with B]

    // Partial level ref

    illTyped(
      """
        val existRef: ~&[A or B] = c1
      """)

    illTyped(
      """
        val inclRef: &[A or B] = existInst0 // this should not compile: incompatible conformance levels
      """)
  }


  @Test
  def testTotalConformanceLevel(): Unit = {
    val c1 = compose[A or B]

    // Total level ref

    val inclRef: &[A or B] = c1
    val inclInst = *(inclRef)
    assertEquals(Total, inclInst.conformanceLevel)

    val existRef: ~&[A or B] = inclInst
    val inclRef2: &[A or B] = inclInst

  }

  @Test
  def testExclusiveConformanceLevel(): Unit = {
    val c1 = compose[A or B]

    // Total level ref

    val exclRef: &[A or B] = c1
    val exclInst = *(exclRef)
    assertEquals(Total, exclInst.conformanceLevel)

    val existRef: ~&[A or B] = exclInst
    val inclRef: &[A or B] = exclInst

  }

  @Test
  def testGlobalStrategy(): Unit = {

    val comp1 = compose[(D3 or D4 or D1) with FailoverD]

    val morph1 = MorphingStrategy.withGlobalStrategy((inst, alts) => {
      alts.last :: alts.dropRight(1) // (A, B, C) => (C, A, B)
    }) {
      comp1.~
    }

    assertTrue(select[D1](morph1).isDefined)

  }


  @Test
  def testFailover(): Unit = {
    val comp1 = compose[(D3 or D4 or D1) with FailoverD]
    val morph1 = comp1.~

    assertTrue(select[D3](morph1).isDefined)

    try {
      // it should produce DivisionByZero, however, the failover wrapper should switch to the other fragment D1
      morph1.onD(0)

      fail()
    }
    catch {
      case e: Exception =>

        assertTrue(select[D4](morph1).isDefined)

        try {
          morph1.onD(0)

          fail()
        }
        catch {
          case e2: Exception =>
            assertTrue(select[D1](morph1).isDefined)
            // now the operation should go well
            assertEquals(0, morph1.onD(0))
        }

    }

    // always failing composite
    val comp2 = compose[(D3 or D4) with FailoverD]
    val morph2 = comp2.~

    // the failing fragments should be swapping

    var cnt = 0
    while (cnt < 10)
      try {
        if (cnt % 2 == 0)
          assertTrue(select[D3](morph2).isDefined)
        else
          assertTrue(select[D4](morph2).isDefined)

        cnt += 1

        morph2.onD(0)

        fail()
      }
      catch {
        case e: Exception =>
      }
  }

  @Test
  def testRemorph(): Unit = {
    val comp1 = compose[(A or B) with U]
    val morph1 = comp1.~

    morph1.switch(init = true)
    assertTrue(select[A](morph1).isDefined)
    morph1.switch(init = false)
    assertTrue(select[B](morph1).isDefined)
    morph1.switch(init = false)
    assertTrue(select[A](morph1).isDefined)
  }


  @Test
  def testGenericDimensionWithManuallyCreatedSuperDelegator(): Unit = {
    val m1 = compose[SInt with SWrapper].~
    m1.setX(1)
    assertEquals(2, m1.getX)

    illTyped(
      """
        val mErr = compose[SBoolean with SWrapper].~
      """)


    val m2 = compose[TImpl with TWrapper].~
    m2.setX(1)
    assertEquals(2, m2.getX)
  }

  @Test
  def testMirror(): Unit = {
    val model1 = parse[(A or B) with R](true)
    import model1._

    var aActive = true
    //val strategy1 = lookupAlt(whenFragment[A](aActive))
    val strategy1 = RatingStrategy(RootStrategy[model1.Model](), hasFragment[A]((found, _) => if ((found && aActive) || (!found && !aActive)) 1 else 0))
    val comp1 = compose(model1, strategy1)
    val morph1 = comp1.~

    assertTrue(select[A](morph1).isDefined)
    aActive = false
    morph1.switch() // a method on R invoking 'remorph' on the mutable proxy obtained through the mirror method
    assertFalse(select[A](morph1).isDefined)
  }

  @Test
  def testAsynchronousSelect(): Unit = {
    val model1 = parse[A or (B with C)](true)
    import model1._

    val qIn = new SynchronousQueue[Int]()
    val qOut = new SynchronousQueue[Option[Int]]()

    var cActive = true
    //val strategy1 = lookupAlt(whenFragment[C](cActive))
    val strategy1 = RatingStrategy(RootStrategy[model1.Model](), hasFragment[C]((found, _) => if ((found && cActive) || (!found && !cActive)) 1 else 0))

    val comp1 = compose(model1, strategy1)

    // create a mutable morph
    val morph1 = comp1.~

    select[C](morph1) match {
      case None => fail()
      case Some(c) =>
        c.onC2(qIn, qOut)

        // the loop in C should detect fragment B and use it to calculate the reply
        qIn.put(1)
        val reply = qOut.take()
        assertEquals(Some(2), reply)
    }

    // switch off B with C and activate A
    cActive = false
    morph1.remorph

    select[C](morph1) match {
      case None =>
        // the loop in C should not detect fragment B and reply by None
        qIn.put(2)
        val reply = qOut.take()
        assertEquals(None, reply)

      case Some(c) =>
        fail()
    }

    // break the loop in C
    qIn.put(-1)

    // create an immutable morph
    cActive = true
    val morph2 = comp1.make
    //val morph2x: MorphMirror[_, _] = morph2

    select[C](morph2) match {
      case None => fail()
      case Some(c) =>
        c.onC2(qIn, qOut)

        // the loop in C should detect fragment B and use it to calculate the reply
        qIn.put(1)
        val reply = qOut.take()
        assertEquals(Some(2), reply)
    }

  }

  @Test
  def testOptionalDependencies(): Unit = {
    val comp1 = compose[A with B with M with N].~
    val res1 = comp1.onD(10)
    val comp2 = compose[A with M with N].~
    val res2 = comp2.onD(10)
    val comp3 = compose[B with M with N].~
    val res3 = comp3.onD(10)
    val comp4 = compose[M with N].~
    val res4 = comp4.onD(10)

    assertEquals(20, res1)
    assertEquals(10, res2)
    assertEquals(2, res3)
    assertEquals(0, res4)
  }

}
