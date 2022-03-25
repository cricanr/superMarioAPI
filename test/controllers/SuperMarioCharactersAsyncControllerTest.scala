package controllers

import mocks.SuperMarioCharactersServiceMock
import services.ISuperMarioCharactersService
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.Future

class SuperMarioCharactersAsyncControllerTest
    extends AnyWordSpec
    with Matchers
    with MockitoSugar
    with GuiceOneAppPerSuite {
  override def fakeApplication(): Application = {
    new GuiceApplicationBuilder()
      .overrides(
        bind[ISuperMarioCharactersService].to[SuperMarioCharactersServiceMock]
      )
      .build()
  }

  "The SuperMarioCharactersAsyncController" when {
    "doing a successful GET on /names" should {
      "should return a http 200 success with the response JSON of names" in {
        val controller: SuperMarioCharactersAsyncController =
          app.injector.instanceOf[SuperMarioCharactersAsyncController]
        val result: Future[Result] = controller.getAllNames.apply(
          FakeRequest(GET, "/names").withHeaders(
            Helpers.CONTENT_TYPE -> "application/json"
          )
        )

        println(result)
        val content = contentAsString(result)
        status(result) shouldBe OK
        content shouldBe ("""["3 Musty Fears","Admiral Bobbery","Aerodent"]""".stripMargin)
      }
    }

    "doing a successful GET on /charactersSorted?sortOrder=asc" should {
      "should return a http 200 success with the response JSON of names sorted ascending" in {
        val controller: SuperMarioCharactersAsyncController =
          app.injector.instanceOf[SuperMarioCharactersAsyncController]
        val result: Future[Result] = controller.getAllCharactersSorted.apply(
          FakeRequest(GET, "/charactersSorted?sortOrder=asc").withHeaders(
            Helpers.CONTENT_TYPE -> "application/json"
          )
        )

        println(result)
        val content = contentAsString(result)
        status(result) shouldBe OK
        content shouldBe ("""[{"name":"3 Musty Fears","firstGame":"Super Mario RPG: Legend of the Seven Stars","power":12.161214768809705,"speed":37.999},{"name":"Admiral Bobbery","firstGame":"Paper Mario: The Thousand-Year Door","power":77.35463898221579,"speed":65.1533},{"name":"Aerodent","firstGame":"Wario Land 4","power":272.2792107142235,"speed":22.9676}]""".stripMargin)
      }
    }

    "doing a successful GET on /charactersSorted?sortOrder=desc" should {
      "should return a http 200 success with the response JSON of names sorted descending" in {
        val controller: SuperMarioCharactersAsyncController =
          app.injector.instanceOf[SuperMarioCharactersAsyncController]
        val result: Future[Result] = controller.getAllCharactersSorted.apply(
          FakeRequest(GET, "/charactersSorted?sortOrder=desc").withHeaders(
            Helpers.CONTENT_TYPE -> "application/json"
          )
        )

        println(result)
        val content = contentAsString(result)
        status(result) shouldBe OK
        content shouldBe ("""[{"name":"3 Musty Fears","firstGame":"Super Mario RPG: Legend of the Seven Stars","power":12.161214768809705,"speed":37.999},{"name":"Admiral Bobbery","firstGame":"Paper Mario: The Thousand-Year Door","power":77.35463898221579,"speed":65.1533},{"name":"Aerodent","firstGame":"Wario Land 4","power":272.2792107142235,"speed":22.9676}]""".stripMargin)
      }
    }

    "doing a successful GET on /charactersSorted" should {
      "should return a http 200 success with the response JSON of names with default sort" in {
        val controller: SuperMarioCharactersAsyncController =
          app.injector.instanceOf[SuperMarioCharactersAsyncController]
        val result: Future[Result] = controller.getAllCharactersSorted.apply(
          FakeRequest(GET, "/charactersSorted").withHeaders(
            Helpers.CONTENT_TYPE -> "application/json"
          )
        )

        println(result)
        val content = contentAsString(result)
        status(result) shouldBe OK
        content shouldBe ("""[{"name":"3 Musty Fears","firstGame":"Super Mario RPG: Legend of the Seven Stars","power":12.161214768809705,"speed":37.999},{"name":"Admiral Bobbery","firstGame":"Paper Mario: The Thousand-Year Door","power":77.35463898221579,"speed":65.1533},{"name":"Aerodent","firstGame":"Wario Land 4","power":272.2792107142235,"speed":22.9676}]""".stripMargin)
      }
    }
  }
}
