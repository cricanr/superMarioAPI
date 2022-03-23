package query

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class QueryParametersTest extends AnyWordSpec with Matchers {
  "The QueryParameters" when {
    "calling apply on valid empty request parameters" should {
      "return nothing" in {
        val result = QueryParameters.apply(Map.empty)
        result shouldBe None
      }
    }

    "calling apply on valid request parameters with sort given" should {
      "return the sortOrder param" in {
        val result = QueryParameters.apply(Map("sortOrder" -> Seq("asc")))
        result shouldBe Some("asc")
      }
    }

    "calling apply on valid request parameters with sort given and other params" should {
      "return the sortOrder param" in {
        val result = QueryParameters.apply(
          Map("sortOrder" -> Seq("desc"), "filterBy" -> Seq("name"))
        )
        result shouldBe Some("desc")
      }
    }

    "calling apply on valid request parameters without sort given and other params" should {
      "return nothing" in {
        val result = QueryParameters.apply(Map("filterBy" -> Seq("name")))
        result shouldBe None
      }
    }
  }
}
