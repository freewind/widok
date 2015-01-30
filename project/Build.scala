import sbt._
import sbt.Keys._
import org.typelevel.sbt.Developer
import org.typelevel.sbt.TypelevelPlugin._
import org.scalajs.sbtplugin._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.Import._

object Build extends sbt.Build {
  val buildOrganisation = "io.github.widok"
  val buildScalaVersion = "2.11.5"
  val buildScalaOptions = Seq(
    "-unchecked", "-deprecation",
    "-encoding", "utf8")

  def between(subject: String, left: String, right: String): String = {
    val ofs = subject.indexOf(left)
    val end = subject.indexOf(right, ofs + left.length + 1)
    assert(ofs != -1 && end != -1)
    subject.slice(ofs + left.length, end)
  }

  def generateFontAwesome(sourceGen: File, webJars: File): Seq[File] = {
    val file = sourceGen / "org" / "widok" / "bindings" / "FontAwesome" / "Icon.scala"

    def objectName(cssTag: String): String =
      cssTag.foldLeft("") { case (acc, cur) =>
        if (acc.length == 0) "" + cur.toUpper
        else if (acc.last == '-') acc.dropRight(1) + cur.toUpper
        else acc + cur
      }

    val objects = io.Source.fromFile(webJars / "lib" / "font-awesome" / "scss" / "_icons.scss")
      .getLines()
      .filter(_.startsWith(".#"))
      .map { line =>
        val icon = between(line, "}-", ":")
        val obj = objectName(icon)

        s"""
         case class $obj() extends Icon { val icon = "$icon" }
         """
      }
      .mkString("\n")

    IO.write(file,
      s"""
      package org.widok.bindings.FontAwesome
      $objects
      """
    )

    Seq(file)
  }

  val faTask = {
    val source = sourceManaged in Compile
    val zipped = source.zip(WebKeys.webJarsDirectory in Assets)
    zipped.map { case (src, web) => generateFontAwesome(src, web) }
  }.dependsOn(WebKeys.webJars in Assets)

  lazy val main = Project(id = "widok", base = file("."))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(SbtWeb)
    .settings(typelevelDefaultSettings: _*)
    .settings(
      TypelevelKeys.signArtifacts := true,
      TypelevelKeys.githubDevs += Developer("Tim Nieradzik", "tindzk"),
      TypelevelKeys.githubProject := ("widok", "widok"),
      homepage := Some(url("http://widok.github.io/")),
      licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.7.0",
        "org.monifu" %%% "minitest" % "0.10" % "test",
        "org.webjars" % "font-awesome" % "4.3.0-1"
      ),
      sourceGenerators in Compile <+= faTask,
      testFrameworks += new TestFramework("minitest.runner.Framework"),
      organization := buildOrganisation,
      scalaVersion := buildScalaVersion,
      scalacOptions := buildScalaOptions
    )
}
