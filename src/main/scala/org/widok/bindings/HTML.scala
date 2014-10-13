package org.widok.bindings

import org.widok._
import org.scalajs.dom
import org.scalajs.dom.HTMLInputElement

object HTML {
  trait Cursor
  object Cursor {
    case object Wait extends Cursor { override def toString = "wait" }
    case object Pointer extends Cursor { override def toString = "pointer" }
    case object Move extends Cursor { override def toString = "move" }
  }

  object Heading {
    case class Level1(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("h1", contents: _*)
    }

    case class Level2(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("h2", contents: _*)
    }

    case class Level3(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("h3", contents: _*)
    }

    case class Level4(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("h4", contents: _*)
    }

    case class Level5(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("h5", contents: _*)
    }

    case class Level6(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("h6", contents: _*)
    }
  }

  case class Paragraph(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("p", contents: _*)
  }

  object Text {
    case class Bold(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("b", contents: _*)
    }

    case class Small(contents: Widget*) extends Widget {
      val rendered = DOM.createElement("small", contents: _*)
    }
  }

  case class Raw(html: String) extends Widget {
    val rendered = DOM.createElement("span")
    rendered.innerHTML = html
  }

  case class Image(source: String) extends Widget {
    val rendered = DOM.createElement("img")
    rendered.setAttribute("src", source)
  }

  case class LineBreak() extends Widget {
    val rendered = DOM.createElement("br")
  }

  case class Button(contents: Widget*) extends Widget.Button {
    val rendered = DOM.createElement("button", contents: _*)
  }

  case class Section(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("section", contents: _*)
  }

  case class Header(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("header", contents: _*)
  }

  case class Footer(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("footer", contents: _*)
  }

  case class Navigation(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("nav", contents: _*)
  }

  case class Anchor(contents: Widget*) extends Widget.Anchor {
    val rendered = DOM.createElement("a", contents: _*)

    def url(value: String) = {
      rendered.setAttribute("href", value)
      this
    }
  }

  case class Form(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("form", contents: _*)
  }

  case class Label(contents: Widget*) extends Widget {
    val rendered = DOM.createElement("label", contents: _*)

    def forId(value: String) = {
      rendered.setAttribute("for", value)
      this
    }
  }

  object Input {
    case class Text() extends Widget.Input.Text {
      val rendered = DOM.createElement("input")
        .asInstanceOf[HTMLInputElement]
      rendered.setAttribute("type", "text")

      def autofocus(value: Boolean) = {
        rendered.setAttribute("autofocus", "")
        this
      }

      def placeholder(value: String) = {
        rendered.setAttribute("placeholder", value)
        this
      }

      def autocomplete(value: Boolean) = {
        rendered.setAttribute("autocomplete", if (value) "on" else "off")
        this
      }
    }

    case class Checkbox() extends Widget.Input.Checkbox {
      val rendered = DOM.createElement("input")
        .asInstanceOf[HTMLInputElement]
      rendered.setAttribute("type", "checkbox")
    }

    case class Select(options: Seq[String], selected: Int = -1) extends Widget.Input.Select {
      val rendered = DOM.createElement("select")
      options.zipWithIndex.foreach { case (cur, idx) =>
        val elem = DOM.createElement("option")
        elem.appendChild(dom.document.createTextNode(cur))
        if (idx == selected) elem.setAttribute("selected", "")
        rendered.appendChild(elem)
      }
    }
  }

  case class HorizontalLine() extends Widget {
    val rendered = DOM.createElement("hr")
  }

  object List {
    case class Unordered(contents: List.Item*) extends Widget.List {
      val rendered = DOM.createElement("ul", contents: _*)
    }

    case class Ordered(contents: List.Item*) extends Widget.List {
      val rendered = DOM.createElement("ol", contents: _*)
    }

    case class Item(contents: Widget*) extends Widget.List.Item {
      val rendered = DOM.createElement("li", contents: _*)
    }
  }

  object Container {
    case class Generic(contents: Widget*) extends Widget.Container {
      val rendered = DOM.createElement("div", contents: _*)
    }

    case class Inline(contents: Widget*) extends Widget.Container {
      val rendered = DOM.createElement("span", contents: _*)
    }
  }
}