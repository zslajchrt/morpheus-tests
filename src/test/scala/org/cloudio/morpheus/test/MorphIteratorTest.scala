package org.cloudio.morpheus.test

import org.junit.Test
import org.junit.Assert._

import org.morpheus._
import org.morpheus.Morpheus._

import org.cloudio.morpheus.test.samples._

/**
 * Created by zslajchrt on 21/07/15.
 */
class MorphIteratorTest {

  @Test
  def testIterator(): Unit = {

    val c = singleton[A or B or D1 or D2]
    val morphs = new c.MorphIterator().toList
    assertEquals(4, morphs.size)
    assertTrue(morphs(0).isInstanceOf[A])
    assertTrue(morphs(1).isInstanceOf[B])
    assertTrue(morphs(2).isInstanceOf[D1])
    assertTrue(morphs(3).isInstanceOf[D2])

  }

}
