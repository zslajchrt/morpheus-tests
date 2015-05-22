package org.cloudio.morpheus.test.samples.FragmentsForCompletionTest

import org.morpheus.fragment
import org.morpheus.Morpheus.or

/**
* Created by zslajchrt on 27/04/15.
*/

@fragment
trait A {

}

@fragment
trait B {

}

@fragment
trait C {

}

@fragment
trait D {

}

@fragment
trait E {
  this: A or B =>
}

@fragment
trait F {
  this: C or D =>
}

@fragment
trait G {
  this: E or F =>
}


@fragment
trait X {
  this: Y or Z =>
}

@fragment
trait Y {
  this: X or Z =>
}

@fragment
trait Z {
  this: Y or X =>
}