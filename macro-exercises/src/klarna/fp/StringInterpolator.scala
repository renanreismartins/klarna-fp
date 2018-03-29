package klarna.fp

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object StringInterpolator {

  // Sometimes the function that we're implementing using a macro takes
  // parameters. The macro then needs to take exrpessions of the correct types
  // as shown below.
  //
  // Implement the macro `noInterpImpl` which should have the following
  // properties:
  //
  // - Don't allow any values to be passed as argument to `lg`
  // - Take the string content of the string interpolator and concatenate it so
  //   that it looks untouched from the original literal
  //
  // Hint: have a look at `c.prefix.tree`

  implicit class LogInterpolator(val sc: StringContext) {
    def lg(args: Any*): String = macro noInterpImpl
  }

  // 1) Implement the function:

  def noInterpImpl(c: Context)(args: c.Expr[Any]*) =
    c.abort(c.enclosingPosition, "Method not implemented in `StringInterpolator.scala`")
}
