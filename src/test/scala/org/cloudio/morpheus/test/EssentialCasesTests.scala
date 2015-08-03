package org.cloudio.morpheus.test

import org.morpheus.Morpheus._
import org.morpheus.Morpher._
import org.morpheus._
import org.cloudio.morpheus.test.samples._
import org.junit.Assert._
import org.junit.Test
import org.morpheus.test.illTyped

/**
* Created by zslajchrt on 01/03/15.
*/

class EssentialCasesTests {

//  @Test
//  def testEntityOnlyImplicitCreation() {
//
//    val morph: EntityA = compose[EntityA].make
//
//    assertEquals(0, morph.id)
//    assertEquals(1, morph.methodX(1))
//
//  }

  @Test
  def testEntityOnlyExplicitCreation() {

    implicit val ent = EntityA(100)
    val morph: EntityA = glean[EntityA].make

    assertEquals(100, morph.id)
    assertEquals(101, morph.methodX(1))

  }

//  @Test
//  def testOneWrapperOnEntity() {
//    val model = compose[EntityA with EntityAValidator]
//    val morph: EntityA with EntityAValidator = model.make
//
//    // todo: identify name clashes in fragment variables, no two fragments can have an equally named non-private variable
//    // todo: check that a fragment wrapper directly extends the fragment. This limitation may be removed in the future
//    // todo: check that a fragment does not call any method from the context during the initialization
//
//    assertEquals(0, morph.id)
//    assertEquals(0, morph.counterValidatorX)
//    assertEquals(1, morph.methodX(1))
//    assertEquals(1, morph.counterValidatorX)
//
//  }

  @Test
  def testOverridingTemplateMethodOnEntity() {
    // todo: Warn that entity wrapper methods are not called when invoked from within the entity. Entity wrappers provide just a limited functionality.
    // todo: In general, using entities is considered as a legacy workaround. Incorporating a legacy entity can be done in two ways:
    // todo:  1) Wrapping the entity to a fragment trait. Since the entity is isolated from the other fragments, there is no limitation on the resulting composite.
    // todo:  2) Using the entity as a 'lame' fragment. Such a fragment has limited functionality in terms of overriding its methods by fragment wrappers.

    // todo: Specify exactly what is supported and what is not in terms of legacy entities

    //    val model = compose[EntityA with EntityAHelper]
    //    val morph: EntityA = model.morph
    //
    //    val r: Int = morph.methodR(1)
    //    println(r)

  }

//  @Test
//  def testMoreWrappersOnEntity() {
//    val model = compose[EntityA with EntityAValidator with EntityALogger]
//    val morph: EntityA with EntityAValidator with EntityALogger = model.make
//
//    assertEquals(0, morph.id)
//    assertEquals(0, morph.counterValidatorX)
//    assertEquals(0, morph.counterLoggerX)
//    assertEquals(1, morph.methodX(1))
//    assertEquals(1, morph.counterValidatorX)
//    assertEquals(1, morph.counterLoggerX)
//    assertEquals(1, morph.logLevel)
//
//  }

//  @Test
//  def testOneOptionalWrapperOnEntity() {
//
//    def assertLoggerStatus(expCounter: Int, morph: EntityA with EntityALogger): Unit = {
//      assertEquals(0, morph.id)
//      assertEquals(expCounter - 1, morph.counterLoggerX)
//      assertEquals(1, morph.methodX(1))
//      assertEquals(expCounter, morph.counterLoggerX)
//      assertEquals(1, morph.logLevel)
//    }
//
//    // /? operator is ON by default
//    val model = singleton[EntityA with /?[EntityALogger]]
//    import model._
//
//    model.make match {
//      case morph: EntityA with EntityALogger =>
//        assertLoggerStatus(1, morph)
//      case _ => fail()
//    }
//
//    // introducing the morph strategy
//
//    var logFlag = false // logger is OFF
//
//    implicit val morphStrategy = activator(
//      ?[EntityALogger] { _ => logFlag}
//    )
//
//    // the morpher rules out the logger from the resulting composite
//
//    model.morph match {
//      case morph: EntityA with EntityALogger =>
//        fail()
//      case _ =>
//    }
//
//    logFlag = true // logger is ON
//
//    // the morpher includes the logger from the resulting composite
//
//    model.morph match {
//      case morph: EntityA with EntityALogger =>
//        assertLoggerStatus(2, morph)
//      case _ => fail()
//    }
//
//  }

//  @Test
//  def testMoreOptionalWrappersOnEntity() {
//
//    // /? operator is ON by default
//    val model = singleton[EntityA with /?[EntityAValidator] with /?[EntityALogger]]
//    import model._
//
//    val validator = fragments.select[FragmentHolder[EntityAValidator]].proxy
//    val logger = fragments.select[FragmentHolder[EntityALogger]].proxy
//
//    def assertCounters(counterValidatorX: Int, counterValidatorY: Int, counterLoggerX: Int, counterLoggerZ: Int) = {
//      assertEquals(counterValidatorX, validator.counterValidatorX)
//      assertEquals(counterValidatorY, validator.counterValidatorY)
//      assertEquals(counterLoggerX, logger.counterLoggerX)
//      assertEquals(counterLoggerZ, logger.counterLoggerZ)
//    }
//
//    model.make match {
//      case morph: EntityA with EntityAValidator with EntityALogger =>
//        assertCounters(0, 0, 0, 0)
//        morph.methodX(0)
//        assertCounters(1, 0, 1, 0)
//        morph.methodY(0)
//        assertCounters(1, 1, 1, 0)
//        morph.methodZ(0)
//        assertCounters(1, 1, 1, 1)
//
//      case _ => fail()
//    }
//
//    var validateFlag = false // validator is OFF
//    var logFlag = false // logger is OFF
//
//    implicit val morphStrategy = activator(
//      ?[EntityAValidator] { _ => validateFlag} orElse
//        ?[EntityALogger] { _ => logFlag}
//    )
//
//    model.morph match {
//      case morph: EntityA with EntityAValidator with EntityALogger =>
//        fail()
//      case morph: EntityA with EntityAValidator =>
//        fail()
//      case morph: EntityA with EntityALogger =>
//        fail()
//      case morph: EntityA =>
//        // no change in counters
//        morph.methodX(0)
//        assertCounters(1, 1, 1, 1)
//        morph.methodY(0)
//        assertCounters(1, 1, 1, 1)
//        morph.methodZ(0)
//        assertCounters(1, 1, 1, 1)
//      // OK
//      case _ =>
//        fail()
//    }
//
//    validateFlag = true
//    logFlag = true
//
//    model.morph match {
//      case morph: EntityA with EntityAValidator with EntityALogger =>
//        morph.methodX(0)
//        assertCounters(2, 1, 2, 1)
//        morph.methodY(0)
//        assertCounters(2, 2, 2, 1)
//        morph.methodZ(0)
//        assertCounters(2, 2, 2, 2)
//      case _ =>
//        fail()
//    }
//
//    validateFlag = true
//    logFlag = false
//
//    model.morph match {
//      case morph: EntityA with EntityAValidator with EntityALogger =>
//        fail()
//      case morph: EntityA with EntityAValidator =>
//        morph.methodX(0)
//        assertCounters(3, 2, 2, 2)
//        morph.methodY(0)
//        assertCounters(3, 3, 2, 2)
//        morph.methodZ(0)
//        assertCounters(3, 3, 2, 2) // no change in the 4-th counter from the logger, which is OFF
//      case morph: EntityA with EntityALogger =>
//        fail()
//      case morph: EntityA =>
//        fail()
//      case _ =>
//        fail()
//    }
//
//    validateFlag = false
//    logFlag = true
//
//    model.morph match {
//      case morph: EntityA with EntityAValidator with EntityALogger =>
//        fail()
//      case morph: EntityA with EntityAValidator =>
//        fail()
//      case morph: EntityA with EntityALogger =>
//        morph.methodX(0)
//        assertCounters(3, 3, 3, 2)
//        morph.methodY(0)
//        assertCounters(3, 3, 3, 2) // no change in the 3-rd counter from the validator, which is OFF
//        morph.methodZ(0)
//        assertCounters(3, 3, 3, 3)
//      case morph: EntityA =>
//        fail()
//      case _ =>
//        fail()
//    }
//  }

  @Test
  def testOneWrapperOnFragment(): Unit = {
    //    val cr = new ClassReader("org/cloudio/morpheus/test/PingLogger$class")
    //    ClassPrinter.printClass(cr)

    implicit val pongConfig = PongConfig.cfg
    val pingPong: Ping with PingLogger with Pong = compose[Ping with PingLogger with Pong].make

    pingPong.ping(0)

    assertEquals(11, pingPong.pingLoggerCounter)
  }

  @Test
  def testMoreWrappersOnFragment(): Unit = {
    implicit val pongConfig = PongConfig.cfg
    val pingPong: Ping with PingValidator with PingLogger with Pong = compose[Ping with PingValidator with PingLogger with Pong].make

    pingPong.ping(0)

    assertEquals(11, pingPong.pingLoggerCounter)
    assertEquals(11, pingPong.pingValidatorCounter)
  }

  @Test
  def testMoreOptionalWrappersOnFragment() {

    // /? operator is ON by default

    implicit val pongConfig = PongConfig.cfg
    val model = singleton[Ping with /?[PingValidator] with /?[PingLogger] with Pong]
    import model._

    val ping = fragments.select[FragmentHolder[Ping]].proxy
    val validator = fragments.select[FragmentHolder[PingValidator]].proxy
    val logger = fragments.select[FragmentHolder[PingLogger]].proxy

    def assertCounters(counterPingX: Int, counterPingY: Int, counterPingZ: Int, counterValidatorX: Int, counterValidatorY: Int, counterLoggerX: Int, counterLoggerZ: Int) = {
      assertEquals(counterPingX, ping.methodXCounterInPing)
      assertEquals(counterPingY, ping.methodYCounterInPing)
      assertEquals(counterPingZ, ping.methodZCounterInPing)
      assertEquals(counterValidatorX, validator.methodXCounterInValidator)
      assertEquals(counterValidatorY, validator.methodYCounterInValidator)
      assertEquals(counterLoggerX, logger.methodXCounterInLogger)
      assertEquals(counterLoggerZ, logger.methodZCounterInLogger)
    }

    model.make match {
      case morph: Ping with PingValidator with PingLogger with Pong =>
        assertCounters(0, 0, 0, 0, 0, 0, 0)
        morph.methodX(0)
        assertCounters(1, 0, 0, 1, 0, 1, 0)
        morph.methodY(0)
        assertCounters(1, 1, 0, 1, 1, 1, 0)
        morph.methodZ(0)
        assertCounters(1, 1, 1, 1, 1, 1, 1)

      case _ => fail()
    }

    var validateFlag = false // validator is OFF
    var logFlag = false // logger is OFF

    implicit val morphStrategy = activator(
      ?[PingValidator] { _ => validateFlag} orElse
        ?[PingLogger] { _ => logFlag}
    )

    model.morph match {
      case morph: Ping with PingValidator with PingLogger with Pong =>
        fail()
      case morph: Ping with PingValidator with Pong =>
        fail()
      case morph: Ping with PingLogger with Pong =>
        fail()
      case morph: Ping with Pong =>
        // no change in counters
        morph.methodX(0)
        assertCounters(2, 1, 1, 1, 1, 1, 1)
        morph.methodY(0)
        assertCounters(2, 2, 1, 1, 1, 1, 1)
        morph.methodZ(0)
        assertCounters(2, 2, 2, 1, 1, 1, 1)
      // OK
      case _ =>
        fail()
    }

    validateFlag = true
    logFlag = true

    model.morph match {
      case morph: Ping with PingValidator with PingLogger with Pong =>
        morph.methodX(0)
        assertCounters(3, 2, 2, 2, 1, 2, 1)
        morph.methodY(0)
        assertCounters(3, 3, 2, 2, 2, 2, 1)
        morph.methodZ(0)
        assertCounters(3, 3, 3, 2, 2, 2, 2)
      case _ =>
        fail()
    }

    validateFlag = true
    logFlag = false

    model.morph match {
      case morph: Ping with PingValidator with PingLogger with Pong =>
        fail()
      case morph: Ping with PingValidator with Pong =>
        morph.methodX(0)
        assertCounters(4, 3, 3, 3, 2, 2, 2)
        morph.methodY(0)
        assertCounters(4, 4, 3, 3, 3, 2, 2)
        morph.methodZ(0)
        assertCounters(4, 4, 4, 3, 3, 2, 2) // no change in the 4-th counter from the logger, which is OFF
      case morph: Ping with PingLogger with Pong =>
        fail()
      case morph: Ping with Pong =>
        fail()
      case _ =>
        fail()
    }

    validateFlag = false
    logFlag = true

    model.morph match {
      case morph: Ping with PingValidator with PingLogger with Pong =>
        fail()
      case morph: Ping with PingValidator with Pong =>
        fail()
      case morph: Ping with PingLogger with Pong =>
        morph.methodX(0)
        assertCounters(5, 4, 4, 3, 3, 3, 2)
        morph.methodY(0)
        assertCounters(5, 5, 4, 3, 3, 3, 2) // no change in the 3-rd counter from the validator, which is OFF
        morph.methodZ(0)
        assertCounters(5, 5, 5, 3, 3, 3, 3)
      case _ =>
        fail()
    }
  }


//  @Test
//  def testMutableProxy() {
//    val model = singleton[EntityA with \?[EntityALogger]]
//    import model._
//
//    // retrieve the fragment instance of the logger
//    val logger = fragments.select[FragmentHolder[EntityALogger]].proxy
//
//    var logFlag = false // logger is OFF
//
//    implicit val morphStrategy = activator(
//      ?[EntityALogger] { _ => logFlag}
//    )
//
//    val proxy = model.morph_~
//
//    assertEquals(0, logger.counterLoggerX)
//    proxy.methodX(1)
//    assertEquals(0, logger.counterLoggerX) // no change, the logger is OFF
//
//    logFlag = true
//    proxy.remorph()
//
//    proxy.methodX(2)
//    assertEquals(1, logger.counterLoggerX) // the counter incremented, the logger is ON
//
//    logFlag = false
//    proxy.remorph()
//
//    proxy.methodX(3)
//    assertEquals(1, logger.counterLoggerX) // no change, the logger is OFF
//
//  }
//
//  @Test
//  def testAlternativeFragmentsDependentOnEntity(): Unit = {
//
//    implicit val ent = external(EntityA(100))
//    val model = singleton[EntityA with (EntityAJSONPrinter or EntityACSVPrinter)]
//    import model._
//
//    var printFmt = 'json
//
//    implicit val morphStrategy = activator(
//      ?[EntityAJSONPrinter] { _ => printFmt == 'json} orElse
//        ?[EntityACSVPrinter] { _ => printFmt == 'csv}
//    )
//
//    val morph = model.morph_~
//
//    var out = morph.print()
//    assertEquals("{'id': 100}", out)
//
//    printFmt = 'csv
//    morph.remorph()
//
//    out = morph.print()
//    assertEquals("100", out)
//  }
//
//  @Test
//  def testSharingFragments() {
//    //val model = compose[EntityA with \?[EntityAValidator] with \?[EntityALogger]]
//    val model1 = {
//      singleton[EntityA with EntityAValidator with EntityALogger]
//    }
//
//    val outerLogger = model1.fragments.select[FragmentHolder[EntityALogger]]
//
//    val model2 = {
//      implicit val logger = external[EntityALogger](outerLogger.proxy)
//      singleton[EntityA with EntityAValidator with EntityALogger]
//    }
//
//    val proxy1: EntityA with EntityAValidator with EntityALogger = model1.make
//    val proxy2: EntityA with EntityAValidator with EntityALogger = model2.make
//
//    proxy1.methodX(1)
//    assertEquals(1, proxy1.counterLoggerX)
//    assertEquals(1, proxy2.counterLoggerX)
//    assertEquals(1, proxy1.counterValidatorX)
//    assertEquals(0, proxy2.counterValidatorX)
//    proxy2.methodX(2)
//    assertEquals(2, proxy1.counterLoggerX)
//    assertEquals(2, proxy2.counterLoggerX)
//    assertEquals(1, proxy1.counterValidatorX)
//    assertEquals(1, proxy2.counterValidatorX)
//  }


  @Test
  def testMutuallyDependentFragments() {
    // todo: a negative test for unsatisfied dependencies of PingDimA or PongDimA

    implicit val pongConfig = single[Pong, PongConfig](new PongConfig {
      val maxReturns: Int = 10
    })
    val model = singleton[Ping with Pong]
    import model._

    val ping = fragments.select[FragmentHolder[Ping]].proxy
    val pong = fragments.select[FragmentHolder[Pong]].proxy

    val pingPong: Ping with Pong = model.make
    val res = pingPong.ping(0)

    assertEquals(10, res)
    assertEquals(11, ping.pingCounter)
    assertEquals(11, pong.pongCounter)

  }

  @Test
  def testProxyImplicits(): Unit = {
    implicit val pongConfig = frag[Pong, PongConfig](new PongConfig {
      val maxReturns: Int = 10
    })
    val pingPong = singleton[Ping with Pong]
    import pingPong.proxies._

    // it's ok if it compiles
    val ping = implicitly[Ping]
    val pong = implicitly[Pong]
  }

  @Test
  def testGlean() {
    // todo: when composing an abstract composite the composite model type should be flat

    val ping = singleton_?[Ping]
    import ping.proxies._

    val pong = {
      implicit val pongConfig = frag[Pong, PongConfig](new PongConfig {
        val maxReturns: Int = 10
      })
      singleton_?[Pong]
    }
    import pong.proxies._

    val trainers = singleton_?[PingTrainer with PongTrainer]
    import trainers.proxies._

    val team = glean[PingTrainer with PongTrainer with Ping with Pong]

    val teamMorph: PingTrainer with PongTrainer = team.make
    assertTrue(teamMorph.pingReady())
    assertTrue(teamMorph.pongReady())

    val pingPongMorph: Ping with Pong = glean[Ping with Pong].make
    pingPongMorph.ping(0)

    val pingReady = teamMorph.pingReady()
    val pongReady = teamMorph.pingReady()
    assertFalse(pingReady)
    assertFalse(pongReady)

  }

  @Test
  def testVolatilePolymorphism(): Unit = {
    implicit val mutConfig = single[MutableFragment, MutableFragmentConfig](MutableFragmentConfig())
    val animal: Animal = compose[Animal with MutableFragment].make
    val c = animal.carnivore
    val likableFood = animal.craveFor

    //assertTrue(likableFood.delegate.isInstanceOf[Apple])
    assertEquals("Apple", likableFood.toString)

    // make the food preference change
    animal.carnivore = true
    //assertTrue(likableFood.delegate.isInstanceOf[Fish])
    assertEquals("Fish", likableFood.toString)

    // make the food preference change again
    animal.carnivore = false
    //assertTrue(likableFood.delegate.isInstanceOf[Apple])
    assertEquals("Apple", likableFood.toString)
  }

  @Test
  def testVolatilePolymorphismCollection(): Unit = {

    def movingAnimal(a: MutableMorphMirror[_]): Option[MovingAnimal] = inspect(a) {
      case m: MovingAnimal => Some(m)
      case _ => None
    }

    val herd: Herd = compose[Herd].make
    val a1 = herd.add()
    val a2 = herd.add()

    for (a <- herd.members()) {
      assertTrue(movingAnimal(a).isDefined)
      assertTrue(a.delegate.isInstanceOf[MovingAnimal])
      a.kill()
      assertFalse(movingAnimal(a).isDefined)
      assertFalse(a.delegate.isInstanceOf[MovingAnimal])
    }

    herd.members()(0).craveFor.delegate.isInstanceOf[Apple]
    inspect(herd.members()(0).craveFor) {
      case a: Apple => Some(a)
      case _ => None
    }

    // todo


  }

  @Test
  def testCompose(): Unit = {
    val model = compose_?[Ping]

    val ping1 = model.fragments.select[FragmentHolder[Ping]].proxy
    val ping2 = model.fragments.select[FragmentHolder[Ping]].proxy

    assertNotSame(ping1, ping2)
  }

  @Test
  def testSingleton(): Unit = {
    val model = singleton_?[Ping]

    val ping1 = model.fragments.select[FragmentHolder[Ping]].proxy
    val ping2 = model.fragments.select[FragmentHolder[Ping]].proxy

    assertSame(ping1, ping2)
  }

  @Test
  def testComposeWithSingletonFragment(): Unit = {
    implicit val pongCfg = single[Pong, PongConfig](PongConfig)
    val model = compose[Ping with Pong]

    val ping1 = model.fragments.select[FragmentHolder[Ping]].proxy
    val ping2 = model.fragments.select[FragmentHolder[Ping]].proxy

    assertNotSame(ping1, ping2)

    val pong1 = model.fragments.select[FragmentHolder[Pong]].proxy
    val pong2 = model.fragments.select[FragmentHolder[Pong]].proxy

    assertSame(pong1, pong2)
  }

  @Test
  def testSingletonWithNonSingletonFragment(): Unit = {
    implicit val pongCfg = frag[Pong, PongConfig](PongConfig)
    val model = singleton[Ping with Pong]

    val ping1 = model.fragments.select[FragmentHolder[Ping]].proxy
    val ping2 = model.fragments.select[FragmentHolder[Ping]].proxy

    assertSame(ping1, ping2)

    val pong1 = model.fragments.select[FragmentHolder[Pong]].proxy
    val pong2 = model.fragments.select[FragmentHolder[Pong]].proxy

    assertNotSame(pong1, pong2)
  }


  @Test
  def testComplexStructure(): Unit = {

    val editor = AlbumEditor()
    editor.loadPhotos(List(1l, 2l, 3l))

    val photos = editor.photos
    println(photos)
    for (photo <- photos) {
      photo.resize(2, 2)
      if (photo.width < photo.height)
        photo.rotate(Math.PI / 2)
    }
    println(photos)

    for (photo <- photos) {
      inspect(photo) {
        case bp: BigPhoto =>
          println(s"Before iconized: $bp")
          bp.iconize()
          println(s"After iconized: $bp")
        case _ =>
      }
    }

    for (photo <- photos) {
      inspect(photo) {
        case icon: IconPhoto =>
          println(s"Icon: $icon")
        case _ =>
      }
    }
  }

  @Test
  def testSelect(): Unit = {
    val composite = compose[Jin or Jang]
    import composite._

    implicit val strategy = AlternatingMorphingStrategy(left, right)
    val mutPx = morph_~

    select[Jin](mutPx) match {
      case Some(jin) =>
        assertTrue(jin.isInstanceOf[Jin])
      // OK
      case None =>
        fail()
    }

    select[Jang](mutPx) match {
      case Some(jangEnt) =>
        fail()
      case None =>
      // OK
    }

    // Switch the strategy to 'right' and re-morph the composite by notifying the proxy
    strategy.switch(1)
    mutPx.remorph()

    select[Jang](mutPx) match {
      case Some(jang) =>
        assertTrue(jang.isInstanceOf[Jang])
      // OK
      case None =>
        fail()
    }

    select[Jin](mutPx) match {
      case Some(jinEnt) =>
        fail()
      case None =>
      // OK
    }

    //must not compile
    //    select[Ping](mutPx) match {
    //      case Some(_) =>
    //      case None =>
    //    }

  }

  @Test
  def testSelectHiddenFragment(): Unit = {
    val kernel = compose[D1 with F]
    val morph = kernel.!
    val morphNarrowed = asMorphOf[F](morph)
    // selecting the visible fragment F
    select[F](morphNarrowed) match {
      case None => fail()
      case Some(f) => // OK
    }
    // selecting the invisible fragment D1
    select[D1](morphNarrowed) match {
      case None => fail()
      case Some(d1) => // OK
    }
  }



  @Test
  def testSelectWithEntity(): Unit = {
    implicit val cfg1 = external[EntityA](new EntityA(1))
    val composite = compose[EntityA with (Jin or Jang)]
    import composite._
    implicit val strategy = AlternatingMorphingStrategy(left, right)
    val mutPx = morph_~

    select[EntityA with Jin](mutPx) match {
      case Some(jinEnt) =>
        assertTrue(jinEnt.isInstanceOf[EntityA with Jin])
      // OK
      case None =>
        fail()
    }

    select[EntityA with Jang](mutPx) match {
      case Some(jangEnt) =>
        fail()
      case None =>
      // OK
    }

    // Switch the strategy to 'right' and re-morph the composite by notifying the proxy
    strategy.switch(1)
    mutPx.remorph()

    select[EntityA with Jang](mutPx) match {
      case Some(jangEnt) =>
        assertTrue(jangEnt.isInstanceOf[EntityA with Jang])
      // OK
      case None =>
        fail()
    }

    select[EntityA with Jin](mutPx) match {
      case Some(jinEnt) =>
        fail()
      case None =>
      // OK
    }

    //must not compile
    //    select[Ping](mutPx) match {
    //      case Some(_) =>
    //      case None =>
    //    }

  }

  @Test
  def testDimensionWrapper(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with XMLRenderer]
    import composite._

    val px = make
    assertEquals("<0/>", px.render)
    assertEquals(1, px.xmlRendererCounter)

  }

  @Test
  def testDimensionWrapperCrossSuperCall(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with XMLRenderer]
    import composite._

    val px = make
    // super.methodX is called from XMLRenderer.methodU
    px.methodU(1)

    assertEquals(1, px.methodUCounterInXMLRenderer)
    assertEquals(1, px.methodXCounterInRenderer)
    assertEquals(0, px.methodUCounterInRenderer)

  }

  @Test
  def testFragmentWrapperCrossSuperCall(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with EntityBRendererDecorator]
    import composite._

    val px = make
    // super.methodU is called from EntityBRendererDecorator.methodV
    px.methodV(1)

    assertEquals(1, px.methodVCounterInDecorator)
    assertEquals(1, px.methodUCounterInRenderer)
    assertEquals(0, px.methodVCounterInRenderer)

  }

  @Test
  def testDimensionAndFragmentWrapperCrossSuperCall(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator]
    import composite._

    val px = make
    // super.methodU is called from EntityBRendererDecorator.methodV
    // super.methodX is called from XMLRenderer.methodU
    px.methodV(1)

    assertEquals(1, px.methodVCounterInDecorator)
    assertEquals(1, px.methodUCounterInXMLRenderer)
    assertEquals(1, px.methodXCounterInRenderer)

  }

  @Test
  def testOptionalDimensionWrapper(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with /?[XMLRenderer]]
    import composite._

    implicit val strategy = AlternatingMorphingStrategy(left, right)
    val px = morph_~

    assertEquals("<0/>", px.render)

    // Switch off the XMLRenderer
    strategy.switch(1)
    px.remorph()

    assertEquals("0", px.render)

    //println(px.rendererLoggerCounter)

  }

  @Test
  def testDimensionAndFragmentWrapper(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator]
    import composite._

    val px = make

    assertEquals("{<0/>}", px.render)
    assertEquals(1, px.xmlRendererCounter)
    assertEquals(1, px.entityBRendererDecoratorCounter)

  }

  @Test
  def testFragmentAndDimensionWrapper(): Unit = {

    val composite = compose[EntityB with EntityBRenderer with EntityBRendererDecorator with XMLRenderer]
    import composite._

    val px = make

    assertEquals("<{0}/>", px.render)
    assertEquals(1, px.xmlRendererCounter)
    assertEquals(1, px.entityBRendererDecoratorCounter)
  }

  @Test
  def testOptionalDimensionAndFragmentWrappersOnFragment() {

    // /? operator is ON by default

    val model = singleton[EntityB with EntityBRenderer  with /?[XMLRenderer] with /?[EntityBRendererDecorator]]
    import model._

    val fragment = fragments.select[FragmentHolder[EntityBRenderer]].proxy
    val dimensionWrapper = fragments.select[FragmentHolder[XMLRenderer]].proxy
    val fragmentWrapper = fragments.select[FragmentHolder[EntityBRendererDecorator]].proxy

    def assertCounters(fragmentCounterX: Int, fragmentCounterY: Int, fragmentCounterZ: Int, dimensionWrapperCounterX: Int, dimensionWrapperCounterY: Int, fragmentWrapperCounterX: Int, fragmentWrapperCounterZ: Int) = {
      assertEquals(fragmentCounterX, fragment.methodXCounterInRenderer)
      assertEquals(fragmentCounterY, fragment.methodYCounterInRenderer)
      assertEquals(fragmentCounterZ, fragment.methodZCounterInRenderer)
      assertEquals(dimensionWrapperCounterX, dimensionWrapper.methodXCounterInXMLRenderer)
      assertEquals(dimensionWrapperCounterY, dimensionWrapper.methodYCounterInXMLRenderer)
      assertEquals(fragmentWrapperCounterX, fragmentWrapper.methodXCounterInDecorator)
      assertEquals(fragmentWrapperCounterZ, fragmentWrapper.methodZCounterInDecorator)
    }

    model.make match {
      case morph: EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator =>
        assertCounters(0, 0, 0, 0, 0, 0, 0)
        morph.methodX(0)
        assertCounters(1, 0, 0, 1, 0, 1, 0)
        morph.methodY(0)
        assertCounters(1, 1, 0, 1, 1, 1, 0)
        morph.methodZ(0)
        assertCounters(1, 1, 1, 1, 1, 1, 1)

      case _ => fail()
    }

    var dimWrapperFlag = false
    var fragWrapperFlag = false

    implicit val morphStrategy = activator(
      ?[XMLRenderer] { _ => dimWrapperFlag} orElse
        ?[EntityBRendererDecorator] { _ => fragWrapperFlag}
    )

    model.morph match {
      case morph: EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator =>
        fail()
      case morph: EntityB with EntityBRenderer with XMLRenderer =>
        fail()
      case morph: EntityB with EntityBRenderer with EntityBRendererDecorator =>
        fail()
      case morph: EntityB with EntityBRenderer =>
        // no change in counters
        morph.methodX(0)
        assertCounters(2, 1, 1, 1, 1, 1, 1)
        morph.methodY(0)
        assertCounters(2, 2, 1, 1, 1, 1, 1)
        morph.methodZ(0)
        assertCounters(2, 2, 2, 1, 1, 1, 1)
      // OK
      case _ =>
        fail()
    }

    dimWrapperFlag = true
    fragWrapperFlag = true

    model.morph match {
      case morph: EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator =>
        morph.methodX(0)
        assertCounters(3, 2, 2, 2, 1, 2, 1)
        morph.methodY(0)
        assertCounters(3, 3, 2, 2, 2, 2, 1)
        morph.methodZ(0)
        assertCounters(3, 3, 3, 2, 2, 2, 2)
      case _ =>
        fail()
    }

    dimWrapperFlag = true
    fragWrapperFlag = false

    model.morph match {
      case morph: EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator =>
        fail()
      case morph: EntityB with EntityBRenderer with XMLRenderer =>
        morph.methodX(0)
        assertCounters(4, 3, 3, 3, 2, 2, 2)
        morph.methodY(0)
        assertCounters(4, 4, 3, 3, 3, 2, 2)
        morph.methodZ(0)
        assertCounters(4, 4, 4, 3, 3, 2, 2) // no change in the 4-th counter from the dimensionWrapper, which is OFF
      case _ =>
        fail()
    }

    dimWrapperFlag = false
    fragWrapperFlag = true

    model.morph match {
      case morph: EntityB with EntityBRenderer with XMLRenderer with EntityBRendererDecorator =>
        fail()
      case morph: EntityB with EntityBRenderer with EntityBRendererDecorator =>
        morph.methodX(0)
        assertCounters(5, 4, 4, 3, 3, 3, 2)
        morph.methodY(0)
        assertCounters(5, 5, 4, 3, 3, 3, 2) // no change in the 3-rd counter from the validator, which is OFF
        morph.methodZ(0)
        assertCounters(5, 5, 5, 3, 3, 3, 3)
      case _ =>
        fail()
    }
  }

  @Test
  def testRatingStrategy(): Unit = {
    val composite = compose[EntityB with EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
    //val composite = compose[EntityB with /?[XMLRenderer]]
    import composite._

    //var m = morph_~(lookupAlt(isFragment[XMLRenderer], isFragment[EntityBRendererDecorator]))
    var m = morph_~(RatingStrategy(defaultStrategy, hasFragment[XMLRenderer](IncRating), hasFragment[EntityBRendererDecorator](IncRating)))

    select[XMLRenderer with EntityBRendererDecorator](m) match {
      case None =>
        fail()
      case Some(f) =>
      // OK
    }

    //m = morph_~(lookupAlt(isFragment[XMLRenderer], isNotFragment[EntityBRendererDecorator]))
    m = morph_~(RatingStrategy(defaultStrategy, hasFragment[XMLRenderer](IncRating), hasFragment[EntityBRendererDecorator](DecRating)))

    select[EntityBRendererDecorator](m) match {
      case None =>
      // OK
      case Some(f) =>
        fail()
    }

    select[XMLRenderer with EntityBRendererDecorator](m) match {
      case None =>
      // OK
      case Some(f) =>
        fail()
    }

    select[XMLRenderer](m) match {
      case None =>
        fail()
      case Some(f) =>
      // OK
    }

    //m = morph_~(lookupAlt(isNotFragment[XMLRenderer], isNotFragment[EntityBRendererDecorator]))
    m = morph_~(RatingStrategy(defaultStrategy, hasFragment[XMLRenderer](DecRating), hasFragment[EntityBRendererDecorator](DecRating)))

    select[EntityBRendererDecorator](m) match {
      case None =>
      // OK
      case Some(f) =>
        fail()
    }

    select[XMLRenderer with EntityBRendererDecorator](m) match {
      case None =>
      // OK
      case Some(f) =>
        fail()
    }

    select[XMLRenderer](m) match {
      case None =>
      // OK
      case Some(f) =>
        fail()
    }

    select[EntityB with EntityBRenderer](m) match {
      case None =>
        fail()
      case Some(f) =>
      // OK
    }

  }

  @Test
  def testInclusiveReference(): Unit = {
    val compositeNoAlts = compose[EntityB with EntityBRenderer with EntityBRendererDecorator with XMLRenderer]
    assertTrue(A.inclRefTest(compositeNoAlts))

    val compositeWithAlts = compose[EntityB with EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
    assertTrue(A.inclRefTest(compositeWithAlts))

    // this should not compile since the missing EntityBRenderer
    val incompatibleComp = compose[EntityB]
    illTyped(
      """
        A.inclRefTest(incompatibleComp)
      """)

    val incompleteComp = compose_?[EntityBRenderer with EntityBRendererDecorator with XMLRenderer]

//    // this should not compile since the reference requires the deps check
//    val depsCheckCompRef: &[EntityBRenderer with EntityBRendererDecorator with XMLRenderer] = incompleteComp

//    // this should compile since the reference does not require the deps check
//    val nodepsCheckCompRef: &?[EntityBRenderer with EntityBRendererDecorator with XMLRenderer] = incompleteComp
  }

//  @Test
//  def testExclusiveReference(): Unit = {
//    val c = compose[(A with B) or D1]
//    val cr: &[A or D] = c
//    val m = *(cr).~
//    m.remorph(RatingStrategy(m.strategy, hasFragment[A](IncRating)))
//    assertTrue(select[A](m).isDefined)
//    m.remorph(RatingStrategy(m.strategy, hasFragment[A](DecRating), hasFragment[D](IncRating)))
//    assertFalse(select[A](m).isDefined)
//    assertTrue(select[D](m).isDefined)
//
//    // this should not compile
//    //    val c2 = compose[(A with B) or D1]
//    //    val cr2: &[A or B] = c2
//
//    //    val compositeNoAlts = compose[EntityB with EntityBRenderer with EntityBRendererDecorator with XMLRenderer]
//    //    // this should not compile since the missing optional fragments EntityBRendererDecorator and XMLRenderer
//    //    A.exclRefTest(compositeNoAlts)
//
//    val compositeWithCompleteAlts = compose[EntityB with EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
//    assertTrue(A.exclRefTest(compositeWithCompleteAlts))
//
//    //    val compositeWithMutuallyExclAlts = compose[EntityB with EntityBRenderer with (EntityBRendererDecorator or XMLRenderer)]
//    //    //this should not compile since it fails during the inclusive model validation (which precedes the exclusivity check) the source model cannot yield [EntityB with EntityBRenderer with EntityBRendererDecorator with XMLRenderer] alternative
//    //    assertTrue(A.exclRefTest(compositeWithMutuallyExclAlts))
//
//    //    val compositeWithAlts = compose[EntityB with EntityBRenderer with EntityBRendererDecorator with /?[XMLRenderer]]
//    //    //this should not compile since the missing optional fragment EntityBRendererDecorator
//    //    assertTrue(A.exclRefTest(compositeWithAlts))
//
//    //
//    //    // this should not compile since the missing EntityBRenderer
//    //    val incompatibleComp = compose[EntityB]
//    //    assertTrue(A.inclRefTest(incompatibleComp))
//
//    val incompleteComp = compose_?[EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
//
//    // this should not compile since the reference requires the deps check
//    //val depsCheckCompRef: &[EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]] = incompleteComp
//
//    // this should compile since the reference does not require the deps check
//    val nodepsCheckCompRef: &?[EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]] = incompleteComp
//
//  }

  @Test
  def testExistentialReference(): Unit = {
    // this composite has the minimum components to be accepted by the existential reference
    val comp = compose[EntityB with EntityBRenderer]
    assertTrue(A.existRefTest(comp))

    // This should not compile since there is no source alternative for any target one
    illTyped(
      """
        val comp2 = compose[EntityB]
        assertTrue(A.existRefTest(comp2))
      """)
  }

  @Test
  def testDefaultStrategy(): Unit = {

    val model = parse[EntityB with EntityBRenderer with /?[XMLRenderer]](true)
    val defStrat = AlternatingMorphingStrategy(model.left, model.right)
    val composite = build(model, false, FactoryProvider, defStrat, Total) // the equivalent to "compose"
    import composite._

    // We do not call morph_~ since we control the structure by the default strategy set to the composite instance
    val px = make_~

    assertEquals("<0/>", px.render)

    // Switch off the XMLRenderer
    defStrat.switch(1)
    px.remorph()

    assertEquals("0", px.render)

    //println(px.rendererLoggerCounter)

  }


  //  @Test
  //  def testConcat(): Unit = {
  //    val compositeNoAlts = compose[EntityB with EntityBRenderer with EntityBRendererDecorator with XMLRenderer]
  //    assertTrue(A.concatTestIncl(compositeNoAlts))
  //
  //    val compositeWithAlts = compose[EntityB with EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
  //    assertTrue(A.concatTestExcl(compositeWithAlts))
  //  }

  def testComplexDependencyCheck(): Unit = {
    // 1. Ping and Pong depend on each other
    // 2. EntityBRenderer depends on EntityB, however they are both optional
    implicit val pongConfig = PongConfig.cfg
    compose[Ping with Pong with (Unit or (EntityB with EntityBRenderer))]

    // compose[Unit] //todo

    // should not compile
    illTyped(
      """
        compose[Ping with (Unit or (EntityB with EntityBRenderer))]
      """)

    // should not compile
    illTyped(
      """
        compose[Ping with Pong with /?[EntityB] with /?[EntityBRenderer]]
      """)
  }

  @Test
  def testUsingRefToSpecializeComposite(): Unit = {
    val compositeWithAlts = compose[EntityB with EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
    val specCompRef: &[EntityB with XMLRenderer] = compositeWithAlts
    // The LUB of the specialized composite is the same as its model type, since the composite type has only one alternative
    // Also, the specialized type needn't be complete since no deps check is made, since it is assumed that the source
    // composite instance is complete.
    val deref = *(specCompRef)
    val specComp: EntityB with XMLRenderer = deref.make
    val specCompMut: EntityB with XMLRenderer = *(specCompRef).make_~

    // just try to invoke some methods on the proxies
    specComp.methodX(1)
    specCompMut.methodX(1)
  }

  /**
   * This is a more concise version (using the asMorphOf macro) of testUsingRefToSpecializeComposite
   */
  @Test
  def testAsCompositeOf(): Unit = {
    val compositeWithAlts = compose[EntityB with EntityBRenderer with /?[EntityBRendererDecorator] with /?[XMLRenderer]]
    val specComp = asMorphOf[EntityB with XMLRenderer](compositeWithAlts)
    // just try to invoke some methods on the proxies
    specComp.methodX(1)

    val specCompMut = asMorphOf_~[EntityB with XMLRenderer](compositeWithAlts)
    // just try to invoke some methods on the proxies
    specCompMut.methodX(1)
    specCompMut.remorph

    // Should not compile because of the unknown fragment PingLogger
    illTyped(
      """
        asMorphOf[EntityB with PingLogger](compositeWithAlts)
      """)

    // Should not compile because of the LUB is not same as the composite type (there are two alternatives)
    //asMorphOf[EntityB with /?[XMLRenderer]](compositeWithAlts)
  }

  @Test
  def testPlaceholder(): Unit = {
    val model: MorphModel[$[EntityA]] = parse[$[EntityA]](false)

    model.rootNode match {
      case fn@FragmentNode(_, true) =>
        model.fragmentDescriptor(fn) match {
          case None =>
            fail()
          case Some(fd) =>
            import scala.reflect.runtime.universe._
            assertTrue(fd.fragTag.tpe =:= implicitly[WeakTypeTag[EntityA]].tpe)
        }
      // OK
      case _ =>
        fail()
    }
  }


  @Test
  def testAltMappingsPseudoCode(): Unit = {
    type Comp = EntityB with /?[EntityBRenderer]
    val inst = compose[Comp]
    val instRef: &[Comp] = inst

    // Target alternative   Corresponding original alternatives
    // (0, 1)   ->          (0, 1)
    // (0)      ->          (0), (0, 1)

    instRef.altMappings.newAltToOrigAlt.get(List(0, 1)) match {
      case None =>
        fail()
      case Some(origAlts) =>
        assertEquals(List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))),
          origAlts)
    }
    instRef.altMappings.newAltToOrigAlt.get(List(0)) match {
      case None =>
        fail()
      case Some(origAlts) =>
        assertEquals(List(
          OrigAlt(List(0),List(OriginalInstanceSource(FragmentNode(0,false)))),
          OrigAlt(List(0, 1),List(OriginalInstanceSource(FragmentNode(0,false)), OriginalInstanceSource(FragmentNode(1,false))))),
          origAlts)
    }

    // Original alternative Alternative's template
    // (0, 1)   ->          (Original(0), Original(1))
    // (0)   ->             (Original(0))

    //    instRef.altMappings.origAltToTemplateAlt.get(List(0, 1)) match {
    //      case None =>
    //        fail()
    //      case Some(origAlts) =>
    //        assertEquals(List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))), origAlts)
    //    }
    //
    //    instRef.altMappings.origAltToTemplateAlt.get(List(0)) match {
    //      case None =>
    //        fail()
    //      case Some(origAlts) =>
    //        assertEquals(List(OriginalInstanceSource(FragmentNode(0, false))), origAlts)
    //    }
  }

  @Test
  def testMirrorTrait(): Unit = {
    type Comp = EntityB with /?[EntityBRenderer]
    val inst = compose[Comp]

    // MorphMirror
    val m = inst.make
    assertEquals(2, m.myAlternative.size)
    assertSame(inst, m.kernel)

    // MutableMorphMirror
    val mm = inst.make_~
    assertEquals(2, mm.myAlternative.size)
    assertSame(inst, mm.kernel)
    assertNotNull(mm.delegate)
    mm.remorph // test it just by calling it
  }

  @Test
  def testMutableFragment(): Unit = {
    val model = parse[(StatefulX or StatefulY) with MutableFragment](true)
    import model._

    val statusMonitor = EventMonitor[String]("status")

    val strategy = activator(
      ?[StatefulX] { _ => statusMonitor("x", true)} orElse
        ?[StatefulY] { _ => statusMonitor("y", false)}
    )

    implicit val mutableFragConfig = single[MutableFragment, MutableFragmentConfig](MutableFragmentConfig(statusMonitor))

    val inst = compose(model, strategy)

    val m = inst.~
    println(m.isInstanceOf[MutableFragment])
    val isListening = m.startListening({
      case CompositeEvent("status", _, _) => true
    })

    select[StatefulX](m) match {
      case Some(s) => //OK
      case None => fail()
    }

    m.switch()

    select[StatefulY](m) match {
      case Some(s) => //OK
      case None => fail()
    }

    m.switch()

    select[StatefulX](m) match {
      case Some(s) => //OK
      case None => fail()
    }

    m.stopListening()

    m.switch()
    // no change since we stopped listening
    select[StatefulX](m) match {
      case Some(s) => //OK
      case None => fail()
    }

  }
}