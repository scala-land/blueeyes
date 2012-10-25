// This code was auto-generated by Lift Json XSchema - do not edit
package blueeyes.demo {
  import blueeyes.json.JParser._
  import blueeyes.json._
  import blueeyes.json.serialization.{SerializationImplicits, Extractor, Decomposer}
  
  
  trait Orderings {
    implicit val ContactOrdering: Ordering[blueeyes.demo.Contact] = new Ordering[blueeyes.demo.Contact] {
      def compare(v1: blueeyes.demo.Contact, v2: blueeyes.demo.Contact): Int = {
        import Stream.{cons, empty}
        
        return if (v1 == v2) 0 else {      
          val comparisons = empty
          
          comparisons.dropWhile(_ == 0).append(0 :: Nil).head
        }
      }
    }
  }
  object Orderings extends Orderings
  
  case class Contact(name: String, email: Option[String], country: Option[String], city: Option[String], address: Option[String]) extends Ordered[blueeyes.demo.Contact] {
    def compare(that: blueeyes.demo.Contact): Int = blueeyes.demo.Orderings.ContactOrdering.compare(this, that)
    
  }
  
  trait Extractors {
    protected def extractField[T](jvalue: JValue, name: String, default: JValue, e: Extractor[T]): T = {
      try {
        e.extract((jvalue \ name) match { case JUndefined => default; case jvalue => jvalue })
      }
      catch {
        case _ => e.extract(default)
      }
    }
    
    implicit val ContactExtractor: Extractor[blueeyes.demo.Contact] = new Extractor[blueeyes.demo.Contact] {
      override def extract(jvalue: JValue): blueeyes.demo.Contact = {
        Contact(
          extractField[String](jvalue, "name", JString(""), blueeyes.json.serialization.DefaultExtractors.StringExtractor),
          extractField[Option[String]](jvalue, "email", JString(""), blueeyes.json.serialization.DefaultExtractors.OptionExtractor(blueeyes.json.serialization.DefaultExtractors.StringExtractor)),
          extractField[Option[String]](jvalue, "country", JString(""), blueeyes.json.serialization.DefaultExtractors.OptionExtractor(blueeyes.json.serialization.DefaultExtractors.StringExtractor)),
          extractField[Option[String]](jvalue, "city", JString(""), blueeyes.json.serialization.DefaultExtractors.OptionExtractor(blueeyes.json.serialization.DefaultExtractors.StringExtractor)),
          extractField[Option[String]](jvalue, "address", JString(""), blueeyes.json.serialization.DefaultExtractors.OptionExtractor(blueeyes.json.serialization.DefaultExtractors.StringExtractor))
        )
      }
    }
  }
  object Extractors extends Extractors
  
  trait Decomposers {
    implicit val ContactDecomposer: Decomposer[blueeyes.demo.Contact] = new Decomposer[blueeyes.demo.Contact] {
      def decompose(tvalue: blueeyes.demo.Contact): JValue = {
        JObject(
          JField("name", blueeyes.json.serialization.DefaultDecomposers.StringDecomposer.decompose(tvalue.name)) ::
          JField("email", blueeyes.json.serialization.DefaultDecomposers.OptionDecomposer(blueeyes.json.serialization.DefaultDecomposers.StringDecomposer).decompose(tvalue.email)) ::
          JField("country", blueeyes.json.serialization.DefaultDecomposers.OptionDecomposer(blueeyes.json.serialization.DefaultDecomposers.StringDecomposer).decompose(tvalue.country)) ::
          JField("city", blueeyes.json.serialization.DefaultDecomposers.OptionDecomposer(blueeyes.json.serialization.DefaultDecomposers.StringDecomposer).decompose(tvalue.city)) ::
          JField("address", blueeyes.json.serialization.DefaultDecomposers.OptionDecomposer(blueeyes.json.serialization.DefaultDecomposers.StringDecomposer).decompose(tvalue.address)) :: Nil
        )
      }
    }
  }
  object Decomposers extends Decomposers
  
  object Serialization extends Decomposers with Extractors with SerializationImplicits {
    
  }
  
  object Constants {
    import Serialization._
    
    
  }
}
