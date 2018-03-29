package klarna.fp
package macros

import java.io.{File => JFile}

import org.scalatest.{FlatSpec, Matchers}

class FileSpec extends FlatSpec with Matchers {

  it should "get the correct line number for `File.line`" in {
    val line = File.line

    assert(11 === line, "wrong line number")
  }

  it should "get the correct file info for `File.info`" in {
    val info = File.info

    val currentFile = new JFile("exercises/test/macros/FileSpec.scala").getCanonicalPath

    assert(info.lineNumber === 17, "wrong line number")
    assert(info.fileName === "FileSpec.scala")
    assert(info.fullPath === currentFile)
  }

  it should "get the enclosing method via `File.enclosingMethod`" in {
    def enclosingMethod1 =
      File.enclosingMethod

    def enclosingMethod2 =
      File.enclosingMethod

    def enclosingMethodNested  = {
      def enclosingMethod3 = File.enclosingMethod
      enclosingMethod3
    }

    assert(enclosingMethod1 === "enclosingMethod1")
    assert(enclosingMethod2 === "enclosingMethod2")
    assert(enclosingMethodNested  === "enclosingMethod3")
  }

}
