package org.cloudio.morpheus.test

import org.morpheus._
import org.morpheus.Morpheus._
import org.cloudio.morpheus.test.samples._
import org.junit.Assert._
import org.junit.Test
import org.morpheus.test.illTyped

/**
 * Created by zslajchrt on 02/05/15.
 */
class TemporaryTests {

//  trait Functor[F[_], RA[_], RB[_]] { self =>
//    def map[A, B](fa: F[A])(f: RA[A] => RB[B]): F[B]
//  }
//
//  trait SymRefFunctor[F[_], R[_]] extends Functor[F, R, R]

  import func._
  import func.Functor._

  trait Finder[T] {
    def find(id: Long): &![T]
  }

  trait Updater[T] {
    def update(t: &[T]): Unit
  }

  @Test
  def testServiceChain(): Unit = {

    implicit val f = new CovFunctor[Finder] {

      def map[A, B](fa: Finder[A])(f: (&![A]) => &![B]): Finder[B] = {
        new Finder[B] {
          override def find(id: Long): &![B] = {
            f(fa.find(id))
          }
        }
      }
    }

    implicit val u = new ConFunctor[Updater] {

      def map[A, B](fa: Updater[A])(f: (&[B]) => &[A]): Updater[B] = {
        new Updater[B] {
          override def update(t: &[B]): Unit = {
            fa.update(f(t))
          }
        }
      }
    }

    implicit object Finder1 extends Finder[X1Imp] {

      def find(id: Long): &![X1Imp] = {
        singleton[X1Imp]
      }

    }

    implicit object Updater1 extends Updater[X1Imp] {
      override def update(t: &[X1Imp]): Unit = {
        val x1 = *(t)
        assertEquals(4, x1.!.v1)
      }
    }


//    implicit object Finder2$ extends Finder[X2Imp with X1Imp] {
//
//      val s1 = Finder1$
//
//      def find(id: Long): &![X2Imp with X1Imp] = {
//        val x12: &![$[X2Imp] with X1Imp] = *(s1.find(id))
//        *(x12, single[X2Imp])
//      }
//
//
//      def update(x2: &[X2Imp with X1Imp]): Unit = {
//        s1.update(*(x2))
//      }
//    }

//    implicit object Service2 extends Service[X2] {
//
//      def find(id: Long): &[X2] = {
//        // ... todo
//        null
//      }
//
//      def update(x2: &[X2]): Unit = {
//        serialize(x2)
//      }
//    }
//
//    implicit object Finder3$ extends Finder[X3Imp with X2Imp with X1Imp] {
//      val s2 = Finder2$
//
//      def find(id: Long): &![X3Imp with X2Imp with X1Imp] = {
//        val x23: &![$[X3Imp] with X2Imp with X1Imp] = *(s2.find(id))
//        *(x23, single[X3Imp])
//      }
//
//      def update(x3: &[X3Imp with X2Imp with X1Imp]): Unit = {
//        s2.update(*(x3))
//      }
//    }

//    val s3 = Repository3

//    val s2 = f.map(Finder1)(aRef => {
//      val x12: &![$[X2Imp] with X1Imp] = *(aRef)
//      val bRef: &![X2Imp with X1Imp] = *(x12, single[X2Imp])
//      bRef
//    })

    val s2 = f.map(Finder1)(augment[X2Imp](_))
    val s3 = f.map(s2)(augment[X3Imp](_))
//    val u2b = f.map(Updater1)(shrink[X2Imp](_))
//    val u3b = f.map(u2b)(shrink[X3Imp](_))

    implicitly[CovFunctor[Finder]]

//    val s3 = f.map(s2)(aRef => {
//      val x23: &![$[X3Imp] with X2Imp with X1Imp] = *(aRef)
//      val bRef: &![X3Imp with X2Imp with X1Imp] = *(x23, single[X3Imp])
//      bRef
//    })

    val x3 = *(s3.find(0))
    x3.!.onX3("abc", 1)

    val u2 = u.map(Updater1)((bRef: &[X2Imp with X1Imp]) => {
      val aRef: &[X1Imp] = *(bRef)
      aRef
    })

    val u3 = u.map(u2)((bRef: &[X3Imp with X2Imp with X1Imp]) => {
      val aRef: &[X2Imp with X1Imp] = *(bRef)
      aRef
    })

    u3.update(x3)
  }

}
