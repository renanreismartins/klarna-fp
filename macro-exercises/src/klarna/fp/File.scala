package klarna.fp

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

case class FileInfo(lineNumber: Int, fileName: String, fullPath: String)

object File {

  // Macros, what are they?
  //
  // Imagine building a library that you charge companies for and you allow
  // them the use of it for 1 year. How do you make sure that they cannot
  // use it after 1 year? Do you make it misbehave after a year? No, that's
  // pretty mean...
  //
  // Wouldn't it be great if things couldn't compile against the library after
  // the license expires?
  //
  // With macros in Scala, you gain access to what the compiler sees at compile
  // time. As such, it's actually like writing a small amount of code that is
  // executed by the compiler.
  //
  // We'll have a look at how to implement the above usecase:
  //
  //    object MyLib {
  //      def assertCompilable(): Unit = macro compilableImpl
  //
  //      def compilableImpl(c: Context) = {
  //        import c.universe._
  //        if (getCurrentDate() > Date(2018, 12, 31))
  //          c.abort(c.enclosingPosition, "your license has expired!")
  //        else q"()"
  //      }
  //    }
  //
  //
  // On the right hand side of `assertCompilable` you can see that the call to
  // the macro function looks a bit unusual. This is because `macro` is
  // actually a keyword that expects a macro function as its second argument.
  //
  // The macro function has to accept a `Context` as in its first parameter
  // list. The context gives you the context from which the function was
  // invoked from the perspective of the compiler. As such, you can use the
  // context to find out - where was this called? On what object? And so on.
  //
  // So, what does `compilableImpl` actually return? It returns a
  // `c.Expr[Unit]` in the case above. `Expr` is a compiler tree that is typed
  // `Unit`. The resulting tree will then be inlined into the call-site of the
  // macro (if compilation succeeds!).
  //
  // The string interpolator `q"..."` is called quasiquote. This string
  // interpolator, can take a string and turn it into a compiler tree. That
  // means that we don't have to actually write compiler code, we can write
  // something that sort of looks like regular Scala, and the string
  // interpolator will turn that into trees!
  //
  // Let's look at the expansion of `q"x < 10"`:
  //
  //    Apply(
  //      Select(Ident(TermName("x")), TermName("$less"),
  //      List(Literal(Constant(10))))
  //    )
  //
  // We're taking the term `x` and selecting to use the method `$less` and then
  // applying that method to a literal constant `10`. Hopefully you find the
  // quasiquote easier to read than manually constructing the tree!
  //
  // Macros can also take arguments, but more on that later!

  // 1) Implement the `line` method using a macro.
  //
  //    Tip: remember that `Context` contains what you need in order to find
  //    out where the macro was called from!

  /** Returns the current line number */
  def line: Int = ???

  // 2) Implement `info`

  /** Returns info about the current file */
  def info: FileInfo = ???


  // 3) Implement `enclosingMethod`

  /** Returns the nearest enclosing method */
  def enclosingMethod: String = ???

}
