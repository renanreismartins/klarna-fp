package klarna.fp

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

case class FileInfo(lineNumber: Int, fileName: String, fullPath: String)

object File {

  // 1) Implement the `line` method using a macro.
  //
  //    Tip: remember that `Context` contains what you need in order to find
  //    out where the macro was called from!

  /** Returns the current line number */
  def line: Int = macro lineNumberImpl

  def lineNumberImpl(c: Context) = {
    import c.universe._
    val lineNumber = Constant(c.enclosingPosition.line)
    q"$lineNumber"
  }

  // 2) Implement `info`

  /** Returns info about the current file */
  def info: FileInfo = macro infoImpl

  def infoImpl(c: Context) = {
    import c.universe._
    val fileName = Constant(c.enclosingPosition.source.file.name)
    val fullPath = Constant(c.enclosingPosition.source.file.file.getCanonicalPath)
    q"FileInfo(${lineNumberImpl(c)}, $fileName, $fullPath)"
  }

  // 3) Implement `enclosingMethod`

  /** Returns the nearest enclosing method */
  def enclosingMethod: String = macro encMethImpl

  def encMethImpl(c: Context) = {
    import c.universe._

    val owner = c.internal.enclosingOwner match {
      case t if t.isMethod => Constant(t.name.toString)
      case x =>
        c.abort(c.enclosingPosition, s"not wrapped in def, wrapped in: $x")
    }

    q"$owner"
  }
}
