package klarna.fp
package basics

import java.text.SimpleDateFormat
import java.util.Calendar

import scala.util.Try

object Extractor {

  /** In functional programming and Scala - pattern matching is very important.
    *
    * But how does pattern-matching actually work? The compiler follows the
    * following conventions:
    *
    * {{{
    *  val x = (1: Any) match {
    *    case x: Int =>
    *      // the compiler will do an `x.isInstanceOf[Int]` test and go into
    *      // this case if that yields true
    *      "was int!"
    *
    *    case "str" =>
    *      // the compiler will do an isInstanceOf check as well as an equality
    *      // check. If those are both true, it'll go into this case
    *      "was 'str'"
    *
    *    case Some(x) =>
    *      // The compiler will use the extractor pattern here. Essentially this
    *      // means, that the compiler will look for a method named `unapply` in
    *      // the companion object of the `Some` class
    *      ???
    *  }
    * }}}
    *
    * Let's take a look at how to write this `unapply` method:
    *
    * {{{
    *  object LargeInt {
    *    def unapply(i: Int): Option[Int] =
    *      if (i > 100) Some((i)) else None
    *  }
    * }}}
    *
    * This can now be used like:
    *
    * {{{
    *  (1000: Any) match {
    *    case LargeInt(i) =>
    *      println(s"got a large number: $i")
    *    case i =>
    *      println(s"got a small number: $i")
    *  }
    * }}}
    *
    * The compiler now knows that it can call `LargeInt.unapply(i)` and then do
    * a `.isDefined` check on the `Option` that is returned. If it is true,
    * then that means that the extractor managed to extract a tuple from the
    * argument.
    *
    * In this case, we have a tuple with just a single element - so no need to
    * put it in a real tuple.
    *
    * If we wanted to be able to extract more arguments - we simply return a
    * tuple with more elements. Eg:
    *
    * {{{
    *  object SumAndAverage {
    *    def unapply(xs: List[Int]): Option[(Int, Double)] = Option {
    *      if (xs.isEmpty) None
    *      else {
    *        val sum = xs.sum
    *        val avg = sum.toDouble / xs.length
    *        (sum, avg)
    *      }
    *    }
    *  }
    *
    *  // Which can be used as:
    *  List(1, 2, 3) match {
    *    case SumAndAverage(sum, avg) =>
    *      println(s"The sum: $sum, the average: $avg")
    *    case _ =>
    *      println("Couldn't handle empty list!")
    *  }
    * }}}
    */

  case class Birthday(day: Int, month: Int, year: Int)

  object Birthday {
    type ErrorMessage = String

    // 1) Implement valid birthday unapply:
    object valid {
      /** Should validate YYYY-MM-DD */
      def unapply(input: String): Option[Birthday] = Try {
        val formatter = new SimpleDateFormat("YYYY-MM-DD")
        val calendar = Calendar.getInstance()
        calendar.setTime(formatter.parse(input))

        if (calendar.after(Calendar.getInstance())) {
          throw new RuntimeException("invalid birthday")
        }

        Birthday(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
      }.toOption
    }

    // 2) Implement extractor for invalid birthday:
    object invalid {
      def unapply(input: String): Option[ErrorMessage] = {
        val a: Option[Birthday] = Birthday.valid.unapply(input)
        if (!a.isDefined) {
          Some("it does not work")
        } else {
          None
        }
      }
    }

  }

}
