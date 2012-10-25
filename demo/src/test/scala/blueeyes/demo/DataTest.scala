// These tests were auto-generated by Lift Json XSchema - do not edit
package blueeyes.demo {

  import blueeyes.json.JParser._
  import blueeyes.json._
  
  import blueeyes.json.serialization.DefaultSerialization._
  
  import blueeyes.demo.Serialization._
  import blueeyes.demo.Constants._
import org.specs2.mutable.Specification

object ExampleProductData {
    lazy val ExampleContact: blueeyes.demo.Contact = JObject(Nil).deserialize[blueeyes.demo.Contact]
  }

  object DataProductSerializationExamples extends Specification {
    "Deserialization of Contact succeeds even when information is missing" in {
      ExampleProductData.ExampleContact.isInstanceOf[blueeyes.demo.Contact] must be_==(true)
    }

    "Serialization of Contact has non-zero information content" in {
      ExampleProductData.ExampleContact.serialize must_!=(JObject(Nil))
    }
  }
}
