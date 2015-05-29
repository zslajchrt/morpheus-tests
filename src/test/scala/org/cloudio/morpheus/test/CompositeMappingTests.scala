package org.cloudio.morpheus.test

import org.morpheus.{fragment, wrapper, dimension}
import org.morpheus._
import org.morpheus.Morpheus._
import org.cloudio.morpheus.test.samples._
import org.junit.Test
import org.junit.Assert._

/**
*
* Created by zslajchrt on 27/03/15.
*/
class CompositeMappingTests {

  @Test
  def testPartialToPartial(): Unit = {
    //    A -> A
    //    \?[A] -> \?[A]
    //    \?[A] -> \?[B]
    //    A -> A or B
    //    A or B -> A or B
    //    A or B -> \?[A]
    //    A with B -> A
    //    (A or B) with (C or D) -> A or B
    //    A or B -> A or $X


    val c1: ~&[A] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Partial)
    val c2: ~&[\?[A]] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Partial)
    // Here, there are two source alternatives {} and {B} both mapping to {} in the target. Thus the condition of right-totality is satisfied.
    val c3: ~&[\?[B]] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Partial)
    val c4: ~&[A or B] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Partial)
    val c5: ~&[\?[A]] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Partial)
    val c6: ~&[\?[A]] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Partial)
    val c7: ~&[A or B] = build(parse[(A or B) with (D1 or D2)](true), true, SingletonProvider, RootStrategy(), Partial)
    val c8: ~&[A or $[P]] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Partial)

    // Invalid ones
    //    \?[A] -> A
    //    A or B -> A
    //    A or B -> A with B

    //val e1: ~&[A] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e2: ~&[A] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e3: ~&[A or B] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Partial)

  }


  @Test
  def testTotalToPartial(): Unit = {
    //    A -> A
    //    \?[A] -> A
    //    A -> \?[A]
    //    \?[A] -> \?[A]
    //    A with B -> A
    //    A or B -> A
    //    A or B -> A or B
    val c1: ~&[A] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Total)
    val c2: ~&[A] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Total)
    // It works since \?[A] is [A or Unit], and Unit is a friend with any fragment.
    val c3: ~&[\?[A]] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Total)
    val c4: ~&[\?[A]] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Total)
    val c5: ~&[A] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Total)
    val c6: ~&[A] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Total)
    val c7: ~&[A or B] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Total)

    // Invalid ones
    //    A or B -> A with B
    //val e2: ~&[A with B] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Total)
    //val e3: ~&[\?[B]] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Total)

  }

  @Test
  def testPartialToTotal(): Unit = {
    //    A -> A
    //    A -> \?[A]
    //    A with B -> A
    //    A with B with \?[C] -> A with \?[B] // A and B are NOT antagonists

    val c1: &[A] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Partial)
    val c2: &[\?[A]] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Partial)
    val c3: &[A] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Partial)
    val c4: &[A with \?[B]] = build(parse[A with B with \?[C]](true), true, SingletonProvider, RootStrategy(), Partial)
    val c5: &[\?[D1 with F]] = build(parse[\?[D1 with F]](true), true, SingletonProvider, RootStrategy(), Total)

    // Invalid ones
    //    \?[A] -> A
    //    \?[A] -> \?[A]
    //    A -> A or B
    //    A with B -> A or B
    //    A or B -> A
    //    A or B -> A or B
    //    (A or B) with (C or D) -> A or B

    //val e1: &[A] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e2: &[\?[A]] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e3: &[A or B] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e4: &[A or B] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e5: &[A] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e6: &[A or B] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Partial)
    //val e7: &[A or B] = build(parse[(A or B) with (D1 or D2)](true), true, SingletonProvider, RootStrategy(), Partial)

  }

  @Test
  def testTotalToTotal(): Unit = {
    //    A -> A
    //    A with B -> A
    //    \?[A] -> \?[A]
    //    A or B -> A or B
    //    A or B -> A
    //    A with B -> \?[A] with \?[B]

    val c1: &[A] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Total)
    val c2: &[A] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Total)
    val c3: &[\?[A]] = build(parse[\?[A]](true), true, SingletonProvider, RootStrategy(), Total)
    val c4: &[A or B] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Total)
    val c5: &[A] = build(parse[A or B](true), true, SingletonProvider, RootStrategy(), Total)
    val c6: &[\?[A] with \?[B]] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Total)


    // Invalid ones
    //    A with B -> A or B
    //    A -> A or B

    //val e1: &[A or B] = build(parse[A with B](true), true, SingletonProvider, RootStrategy(), Total)
    //val e2: &[A or B] = build(parse[A](true), true, SingletonProvider, RootStrategy(), Total)

  }

  @Test
  def testBasicNonOptionalCases(): Unit = {
    val c1: &[A] = compose[A]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))),
      c1.altMappings)

    val c2: &[A] = compose[A with B]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c2.altMappings)

    val c3: &[A with B] = compose[A with B]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c3.altMappings)

    // should not compile
    //val c4: &[A with B] = compose[A]


    // test dimension in the ref
    val c4: &[A with D] = compose[A with D1]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c4.altMappings)

  }

  @Test
  def testBasicOptionalFragments(): Unit = {
    val instance = compose[\?[A]]
    val c1: &[A] = instance
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))),
      c1.altMappings)

    val c2: &[\?[A]] = compose[A]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(
        List() -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))),
      c2.altMappings)

    val c3: &[\?[A]] = compose[\?[A]]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(
        List() -> List(
          OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false)))),
          OrigAlt(List(), List())),
        List(0) -> List(
          OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))),
      c3.altMappings)

    val c4: &[A] = compose[A or B]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))),
      c4.altMappings)

    val c5: &[A or B] = compose[A or B]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(
        List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(1) -> List(OrigAlt(List(1), List(OriginalInstanceSource(FragmentNode(1, false))))))),
      c5.altMappings)

    val c6: &[A or B] = compose[\?[A] with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(
        List(0) -> List(
          OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(1) -> List(
          OrigAlt(List(1), List(OriginalInstanceSource(FragmentNode(1, false))))))),
      c6.altMappings)

    val c7: &[\?[A] with \?[B]] = compose[\?[A] with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List() -> List(
        OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
        OrigAlt(List(1), List(OriginalInstanceSource(FragmentNode(1, false)))),
        OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false)))),
        OrigAlt(List(), List())),
        List(0) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
          OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(1) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
          OrigAlt(List(1), List(OriginalInstanceSource(FragmentNode(1, false))))),
        List(0, 1) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c7.altMappings)

    val c8: &[A] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(
        OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
        OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))),
      c8.altMappings)

    val c9: &[B] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 1),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c9.altMappings)

    val c10: &[\?[B]] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 1),
      Map(List() -> List(
        OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
        OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(0) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c10.altMappings)

    val c11: &[A with B] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c11.altMappings)

    val c12: &[A with \?[B]] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0) -> List(
        OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
        OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c12.altMappings)

    val c13: &[\?[A] with \?[B]] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List() -> List(
        OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
        OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(0) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)))),
          OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))),
        List(1) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))),
        List(0, 1) -> List(
          OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c13.altMappings)

    val c14: &[\?[A] with B] = compose[A with \?[B]]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(
        List(1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))),
        List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c14.altMappings)

  }

  @Test
  def testBasicNonOptionalWithPlaceholder(): Unit = {
    // should not compile
    //val err1: &[A with $[E]] = compose[A]

    val b = singleton[B].!
    b.y = 1
    val c0: &[$[B]] = b.kernel
    val b2kernel = *(c0, single[B])
    val b2 = b2kernel.!
    assertEquals(0, b2.y) // the new B instance has y = 0

    // B is a free placeholder, i.e. no interference with source composite
    val c1: &[A with $[B]] = compose[A]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0, 1) -> List(OrigAlt(List(0), List(PlaceholderSource(FragmentNode(1, true)), OriginalInstanceSource(FragmentNode(0, false))))))),
      c1.altMappings)

    //should not compile - C is neither a placeholder nor a fragment in the source composite
    //val n1: &[A with $[B] with C] = compose[A]

    // B is a swap placeholder since it has the exact counterpart in the source composite, also there are no dependent fragments on B
    val c2: &[A with $[B]] = compose[A with B]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(1, true))))))),
      c2.altMappings)

    // D2 is a swap placeholder replacing D1 in the source composite, since D1 and D2 implement the same dimension D and there is no fragment depending on D1
    val c3: &[A with $[D2]] = compose[A with D1]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(1, true))))))),
      c3.altMappings)

    // B is a swap placeholder since it has the exact counterpart in the source composite, also there are is fragment C dependent on B
    val c4: &[A with $[B]] = compose[A with B with C]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1, 2), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(1, true)), OriginalInstanceSource(FragmentNode(2, false))))))),
      c4.altMappings)

    // D2 is a swap placeholder replacing D1 in the source composite, since D1 and D2 implement the same dimension D and E depends on dimension D (the sets of dependees of D1 and D2 are same)
    val c5: &[A with $[D2]] = compose[A with D1 with E]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1, 2), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(1, true)), OriginalInstanceSource(FragmentNode(2, false))))))),
      c5.altMappings)

    // D2 is a swap placeholder replacing D1 in the source composite, since D1 and D2 implement the same dimension D while E and G depends on dimension D (the sets of dependees of D1 and D2 are same)
    val c6: &[A with $[D2]] = compose[A with D1 with E with G]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1, 2, 3), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(1, true)), OriginalInstanceSource(FragmentNode(2, false)), OriginalInstanceSource(FragmentNode(3, false))))))),
      c6.altMappings)

    // should not compile - D1 violates the dependencies graph in the source composite, since F depends on D1. Replacing D1 for D2 would violate that dependency.
    //val n7: &[A with $[D2]] = compose[A with D1 with F]

    // should not compile - D1 violates the dependencies graph in the source composite, since F depends on D1. Replacing D1 for D2 would violate that dependency.
    //val n8: &[A with $[D2]] = compose[A with D1 with E with F with G]
  }


  @Test
  def testBasicOptionalPlaceholders(): Unit = {
    // should not compile
    //val err1: &[A with /?[$[E]]] = compose[A]

    val c1: &[/?[$[A]]] = compose[/?[A]]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(
        // if A is chosen in the target, there are two alts in the source that become equivalent after the placeholder is substituted
        List(0) -> List(
          OrigAlt(List(), List(PlaceholderSource(FragmentNode(0, true)))),
          OrigAlt(List(0), List(PlaceholderSource(FragmentNode(0, true))))),
        // if A is not chosen, there are two alternative in the source that comply this choice:
        List() -> List(
          OrigAlt(List(), List()), // the empty source alt complies with the empty target alt
          OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false))))))), // the source alt with the original A fragment also complies (the placeholder is not applied here)
      c1.altMappings)
  }


  @Test
  def testBasicNonOptionalWithPlaceholderWithWrapper(): Unit = {
    // B is a free placeholder, i.e. no interference with source composite
    val c1: &[A with $[B]] = compose[A with H]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(PlaceholderSource(FragmentNode(1, true)), OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c1.altMappings)

    // B is a swap placeholder since it has the exact counterpart in the source composite, also there are no dependent fragments on B
    val c2: &[A with $[B]] = compose[A with H with B]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 2),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1, 2), List(OriginalInstanceSource(FragmentNode(0, false)), OriginalInstanceSource(FragmentNode(1, false)), PlaceholderSource(FragmentNode(1, true))))))),
      c2.altMappings)

    // A is swap placeholder, H is a fragment wrapper of A, so there is no violation of dependency
    val c3: &[$[A]] = compose[A with H]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c3.altMappings)

    // D1 is swap placeholder, I is a dimension wrapper of D, there is no violation of dependency since D1 implements D
    val c4: &[$[D1]] = compose[D1 with I]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c4.altMappings)


    // D2 is swap placeholder, I is a dimension wrapper of D, there is no violation of dependency since D1 implements D
    val c5: &[$[D2]] = compose[D1 with I]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c5.altMappings)

    // D1 is swap placeholder, I is a dimension wrapper wrapping D, F is a wrapper fragment wrapping D1
    val c6: &[$[D1]] = compose[D1 with I with F]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0, 1, 2), List(PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false)), OriginalInstanceSource(FragmentNode(2, false))))))),
      c6.altMappings)

    // D1 is swap placeholder, I is a dimension wrapper wrapping D, F is a wrapper fragment wrapping D1
    val c7: &[$[D1]] = compose[D1 with F with I]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0, 1, 2), List(PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false)), OriginalInstanceSource(FragmentNode(2, false))))))),
      c7.altMappings)

    // H frapper (fragment wrapper) is a simple swap
    val c8: &[$[H]] = compose[A with H]
    assertEquals(AltMappings(
      Map(0 -> 1),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true))))))),
      c8.altMappings)

    // I dipper (dimension wrapper) is a simple swap
    val c9: &[$[I]] = compose[D1 with I]
    assertEquals(AltMappings(
      Map(0 -> 1),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true))))))),
      c9.altMappings)

    // I dipper becomes A decorator
    val c10: &[$[I]] = compose[D1]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true))))))),
      c10.altMappings)

    // H frapper becomes A decorator
    val c11: &[$[H]] = compose[A]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true))))))),
      c11.altMappings)

    // K frapper is appended right after A
    val c12: &[$[K]] = compose[A with H]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c12.altMappings)

    // J dipper is appended right after D1
    val c13: &[$[J]] = compose[D1 with I]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c13.altMappings)

    // J dipper is appended right after  D1
    val c14: &[$[J]] = compose[D1 with L]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c14.altMappings)

    // L frapper is appended right after D1
    val c15: &[$[L]] = compose[D1 with J]
    assertEquals(AltMappings(
      Map(),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c15.altMappings)
  }

  @Test
  def testDimensionAsPlaceholder(): Unit = {
    // the composite does not guarantee the satisfaction of dependencies of a dimension implementation
    val c1: &[$[D]] = compose[D1 with G]
    assertEquals(AltMappings(
      Map(0 -> 0),
      Map(List(0) -> List(OrigAlt(List(0, 1), List(PlaceholderSource(FragmentNode(0, true)), OriginalInstanceSource(FragmentNode(1, false))))))),
      c1.altMappings)

    // should not compile, L frapper wraps D1 and replacing D1 for any D could break the dependency
    //val n1: &[$[D]] = compose[D1 with L]


    val c2: &[A with $[D]] = compose[A with D1]
    assertEquals(AltMappings(
      Map(0 -> 0, 1 -> 1),
      Map(List(0, 1) -> List(OrigAlt(List(0, 1), List(OriginalInstanceSource(FragmentNode(0, false)), PlaceholderSource(FragmentNode(1, true))))))),
      c2.altMappings)

    // should not compile since the placeholder is a dimension which would replace D1 referenced by F
    //val n1: &[A with $[D]] = compose[A with D1 with F] // F -> D1

  }


  //  @Test
  //  def testGarbageCollectionInMapping(): Unit = {
  //    // todo: ???
  //  }


  @Test
  def testDeref(): Unit = {
    val c0: &[A] = compose[A with B]
    val d0 = *(c0)
    val d0_x: MorphKernel[A] = d0
    val p0: A = d0.make
    assertEquals(1, p0.onA(1))
    assertEquals(1, d0.~ onA 1)

    val c1: &[A with $[B]] = compose[A]
    val d1 = *(c1, single[B])
    val d1_x: MorphKernel[A with B] = d1
    val p1: A with B = d1.make
    assertEquals(1, p1.onA(1))
    assertEquals("A", p1.onB("A"))

    val c2: &[A or $[B]] = compose[A]
    val d2 = *(c2, single[B])
    val d2_x: MorphKernel[A or B] = d2
    val p2 = d2.make
    assertEquals(1, asMorphOf[A](d2).onA(1))
    assertEquals("A", asMorphOf[B](d2).onB("A"))

    val c3: &[A or (B with $[C])] = compose[A or B]
    val d3 = *(c3, single[C])
    val d3_x: MorphKernel[A or (B with C)] = *(c3, single[C])
    val p3 = d3.make
    assertEquals(1, asMorphOf[A](d3).onA(1))
    val bc: B with C = asMorphOf[B with C](d3)
    assertEquals("A", bc.onB("A"))
    assertEquals("AA", bc.onC("A"))

    val c4: &[A or (B with $[C]) or $[D1]] = compose[A or B]
    val d4 = *(c4, single[C], single[D1])
    val d4_x: MorphKernel[A or (B with C) or D1] = *(c4, single[C], single[D1])
    val p4 = d4.make
    assertEquals(1, asMorphOf[A](d4).onA(1))
    assertEquals("AA", asMorphOf[B with C](d4).onC("A"))
    assertEquals(6, asMorphOf[D1](d4).onD(3))

    // it's a kind of generic ...
    //val c5: &[A with ({type x <: D})#x] = compose[A with D1]
    val c5: &[A with $[D]] = compose[A with D1]
    val d5 = *(c5, single[D2])
    val d5_x: MorphKernel[A with D2] = *(c5, single[D2])
    val p5 = d5.make
    assertEquals(9, p5.onD(3)) // there is D2 producing 3 * 3

    val c6: &[A with D] = compose[A with D1]
    val d6 = *(c6)
    val d6_x: MorphKernel[A with D] = *(c6)
    val p6 = d6.make
    assertEquals(6, p6.onD(3)) // there is D1 producing 3 + 3

    val c7: &[A with $[D]] = compose[A with D1 with I]
    val d7 = *(c7, single[D2])
    val d7_x: MorphKernel[A with D2] = *(c7, single[D2])
    val p7 = d7.make
    assertEquals(36, p7.onD(3)) // there is D2.onD is producing 3 * 3, but I.onD changes it to (2*3) * (2*3)) = 36

  }

  @Test
  def testMultipleDeref(): Unit = {
    val c0: &[C with I] = compose[A with H with B with C with D1 with I]
    val d0 = *(c0)
    val p0 = d0.make
    assertEquals("aaaaaa", p0.onC("aaa"))
    assertEquals(24, p0.onI(3))

    val c1: &[C] = d0
    val d1 = *(c1)
    val p1 = d1.make
    assertEquals("aaaaaa", p1.onC("aaa"))

    // The alternatives must be same. The only difference is in the visibility of the fragments.
    assertEquals(p0.myAlternative.map(_.fragment), p1.myAlternative.map(_.fragment))
  }

  @Test
  def testAnnotatedFragmentInReference(): Unit = {
    // todo: annotated fragment reference: the annotation is used to check if the referenced fragment has the annotation
    // todo: annotated fragment placeholder: the annotation tells that the placeholder is going to have the annotation

    val c0: &[({type i = I@dimension @wrapper})#i] = compose[A with H with B with C with D1 with I]
    val d0 = *(c0)
    val p0 = d0.make
    assertEquals(24, p0.onI(3))

    val c1: &[$[({type wd = D@dimension})#wd]] = compose[A with H with B with C with D1 with I]
    val d1 = *(c1, single[D2])
    val p1 = d1.make
    assertEquals(36, p1.onD(3)) // there is D1 producing 3 + 3

    val c2: &[$[({type wd = A@fragment})#wd]] = compose[A with H with B with C with D1 with I]
    val d2 = *(c2, single[A])
    val p2 = d2.make
    assertEquals(36, p1.onD(3)) // there is D1 producing 3 + 3

  }

  @Test
  def testAbstractDimensionWrapperPlaceholder(): Unit = {
    val c0: &[$[({type wd = D@dimension @wrapper})#wd]] = compose[A with H with B with C with D1 with I]
    val d0 = *(c0, single[J])
    val p0 = d0.make
    assertEquals(120, p0.onD(3))
    assertEquals(1200, p0.onJ(3))
  }

  @Test
  def testAbstractDimensionPlaceholders(): Unit = {
    val c0: &[$[({type d = D@dimension})#d]] = compose[A with H with B with C with D1 with I]
    val d0 = *(c0, single[D2])
    val p0 = d0.make
    assertEquals(36, p0.onD(3))
    //assertEquals(1200, p0.onI(3))
  }

  @Test
  def testAbstractDimensionAndDimensionWrapperPlaceholders(): Unit = {
    val c0: &[$[({type d = D@dimension})#d] with $[({type wd = D@dimension @wrapper})#wd]] = compose[A with H with B with C with D1 with I]
    val d0 = *(c0, single[D2], single[J])
    val p0 = d0.make
    assertEquals(3600, p0.onD(3))
    assertEquals(360000, p0.onJ(3))
  }

  @Test
  def testMoreDimensionWrapperPlaceholders(): Unit = {
//    val c0: &[$[$[({type wd = D@dimension @wrapper})#wd]]] = compose[A with H with B with C with D1]
//    val d0 = *(c0, single[D2], single[I], single[J])
//    val p0 = d0.make
//    println(p0.myAlternative)
//    assertEquals(120, p0.onD(3))
//    assertEquals(1200, p0.onJ(3))
  }

  def testAbstractFragmentWrapperPlaceholder(): Unit = {
    // todo
    //val c0: &[A with $[A]] = compose[A with H with B with C with D1 with I]
  }
}
