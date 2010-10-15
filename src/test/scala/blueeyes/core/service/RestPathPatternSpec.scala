package blueeyes.core.service

import org.specs.Specification

class RestPathPatternSpec extends Specification{
  "matches correct path" in {
    (RestPath.Root + StringElement("foo")).isDefinedAt("foo") mustEqual(true)
  }
  "matches complex correct path" in {
    (RestPath.Root + StringElement("foo") + StringElement("bar") + SymbolElement('param)).isDefinedAt("foo/bar/value") mustEqual(true)
  }
  "does not match incorrect path" in {
    (RestPath.Root + StringElement("foo")).isDefinedAt("bar") mustEqual(false)
  }
  "create parameters" in {
    (RestPath.Root + SymbolElement('param)).apply("value") mustEqual(Map[Symbol, String]('param -> "value"))
  }
  "create parameters for complex path" in {
    (RestPath.Root + StringElement("foo") + StringElement("bar") + SymbolElement('param)).apply("foo/bar/value") mustEqual(Map[Symbol, String]('param -> "value"))
  }
}