package csvparser

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SuperMarioCharactersParserTest extends AnyWordSpec with Matchers {
  "The SuperMarioCharactersParser" when {
    "calling getAllNames on valid powerness csv file" should {
      "return values correctly" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters = superMarioCharactersParser.readAllItems("test/resources/valid-super-mario-characters-power.csv")

        readCharacters.size shouldBe 3
        readCharacters shouldBe List(List("3 Musty Fears", "Super Mario RPG: Legend of the Seven Stars", "4.62114"), List("Admiral Bobbery", "Paper Mario: The Thousand-Year Door", "50.3991"), List("Aerodent", "Wario Land 4", "62.536"))
      }
    }

    "calling getAllNames on valid speed csv file" should {
      "return values correctly" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters = superMarioCharactersParser.readAllItems("test/resources/valid-super-mario-characters-speed.csv")

        readCharacters.size shouldBe 3
        readCharacters shouldBe List(List("3 Musty Fears", "37.999"), List("Admiral Bobbery", "65.1533"), List("Aerodent", "22.9676"))
      }
    }

    "calling getAllNames on empty powerness csv file" should {
      "return no entries" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters = superMarioCharactersParser.readAllItems("test/resources/empty-super-mario-characters-power.csv")

        readCharacters.size shouldBe 0
        readCharacters shouldBe List.empty
      }
    }

    "calling getAllNames on empty speed csv file" should {
      "return no entries" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters = superMarioCharactersParser.readAllItems("test/resources/empty-super-mario-characters-speed.csv")

        readCharacters.size shouldBe 0
        readCharacters shouldBe List.empty
      }
    }
  }
}
