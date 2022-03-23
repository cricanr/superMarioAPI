package csvparser

import models.{
  SuperMarioCharacter,
  SuperMarioCharacterPowerModel,
  SuperMarioCharacterSpeedModel,
  SuperMarioCharactersPower,
  SuperMarioCharactersSpeed
}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.File

class SuperMarioCharactersParserTest
    extends AnyWordSpec
    with Matchers
    with BeforeAndAfterEach {
  private val testSpeedFilePath =
    "test/resources/test-super-mario-characters-speed.csv"
  private val testPowerFilePath =
    "test/resources/test-super-mario-characters-power.csv"
  private val testUpdatePowerFilePath =
    "test/resources/test-update-super-mario-characters-power.csv"
  private val testUpdateSpeedFilePath =
    "test/resources/test-update-super-mario-characters-speed.csv"
  private val validPowerFilePath =
    "test/resources/valid-super-mario-characters-power.csv"
  private val validSpeedFilePath =
    "test/resources/valid-super-mario-characters-speed.csv"
  private val emptySpeedFilePath =
    "test/resources/empty-super-mario-characters-speed.csv"
  private val emptyPowerFilePath =
    "test/resources/empty-super-mario-characters-power.csv"

  override def afterEach(): Unit = {
    new File(testSpeedFilePath).delete()
    new File(testPowerFilePath).delete()
    new File(testUpdatePowerFilePath).delete()
    new File(testUpdateSpeedFilePath).delete()
  }

  "The SuperMarioCharactersParser" when {
    "calling getAllNames on valid powerness csv file" should {
      "return values correctly" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters =
          superMarioCharactersParser.readAllItems(validPowerFilePath)

        readCharacters.size shouldBe 3
        readCharacters shouldBe List(
          List(
            "3 Musty Fears",
            "Super Mario RPG: Legend of the Seven Stars",
            "4.62114"
          ),
          List(
            "Admiral Bobbery",
            "Paper Mario: The Thousand-Year Door",
            "50.3991"
          ),
          List("Aerodent", "Wario Land 4", "62.536")
        )
      }
    }

    "calling getAllNames on valid speed csv file" should {
      "return values correctly" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters =
          superMarioCharactersParser.readAllItems(validSpeedFilePath)

        readCharacters.size shouldBe 3
        readCharacters shouldBe List(
          List("3 Musty Fears", "37.999"),
          List("Admiral Bobbery", "65.1533"),
          List("Aerodent", "22.9676")
        )
      }
    }

    "calling getAllNames on empty powerness csv file" should {
      "return no entries" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters =
          superMarioCharactersParser.readAllItems(emptyPowerFilePath)

        readCharacters.size shouldBe 0
        readCharacters shouldBe List.empty
      }
    }

    "calling getAllNames on empty speed csv file" should {
      "return no entries" in {
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val readCharacters =
          superMarioCharactersParser.readAllItems(emptySpeedFilePath)

        readCharacters.size shouldBe 0
        readCharacters shouldBe List.empty
      }
    }
  }

  "calling getSuperMarioCharactersSpeedFromCsvLineMap on a valid csv speed line" should {
    "return the SuperMarioCharacterSpeedModel entry" in {
      val character =
        SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLineMap(
          List("cat", "23.1")
        )
      character shouldBe ("cat" -> SuperMarioCharacterSpeedModel(23.1))
    }
  }

  "calling getSuperMarioCharactersPowerFromCsvLine on a valid csv speed line" should {
    "return the SuperMarioCharactersPower entry" in {
      val character =
        SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(
          List("cat", "cat-game", "23.3")
        )
      character shouldBe (SuperMarioCharactersPower("cat", "cat-game", 23.3))
    }
  }

  "calling getSuperMarioCharactersSpeedFromCsvLine on a valid csv speed line" should {
    "return the SuperMarioCharactersSpeed entry" in {
      val character =
        SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLine(
          List("cat", "27")
        )
      character shouldBe (SuperMarioCharactersSpeed("cat", 27))
    }
  }

  "calling getSuperMarioCharactersPowerFromCsvLineMap on a valid csv speed line" should {
    "return the SuperMarioCharacterPowerModel entry" in {
      val character =
        SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLineMap(
          List("cat", "cat-game", "27")
        )
      character shouldBe ("cat" -> SuperMarioCharacterPowerModel(
        "cat-game",
        27
      ))
    }
  }

  "calling writeSpeedLine with a valid input" should {
    "will write the entry successfully" in {
      val superMarioCharactersParser = new SuperMarioCharactersParser()

      superMarioCharactersParser.writeSpeedLine(
        testSpeedFilePath,
        SuperMarioCharacter("test-1", "game-1", 23.9, 31.1)
      )
      superMarioCharactersParser.writeSpeedLine(
        testSpeedFilePath,
        SuperMarioCharacter("test-1", "game-1", 23.5, 31.2)
      )
      val readItem = superMarioCharactersParser.readAllItems(testSpeedFilePath)

      readItem shouldBe List(List("test-1", "31.2"))
    }
  }

  "calling writePowerLine with n a valid input" should {
    "will write the entry successfully" in {
      val superMarioCharactersParser = new SuperMarioCharactersParser()

      superMarioCharactersParser.writePowerLine(
        testPowerFilePath,
        SuperMarioCharacter("test-1", "game-1", 23.9, 31.1)
      )
      superMarioCharactersParser.writeSpeedLine(
        testPowerFilePath,
        SuperMarioCharacter("test-1", "game-1", 23.5, 31.2)
      )
      val readItem = superMarioCharactersParser.readAllItems(testPowerFilePath)

      readItem shouldBe List(List("test-1", "31.2"))
    }
  }

  "calling updateSpeedLine with a valid input" should {
    "will format to write the correct entry" in {
      reflect.io
        .File(testUpdateSpeedFilePath)
        .writeAll(
          """CHARACTER|SPEED
                                                          |3 Musty Fears|37.999
                                                          |Admiral Bobbery|65.1533
                                                          |Aerodent|22.9676""".stripMargin
        )

      val superMarioCharactersParser = new SuperMarioCharactersParser()
      val formattedLine = superMarioCharactersParser.updateSpeedLine(
        SuperMarioCharacter("Aerodent", "game-1", 23.9, 31.1),
        testUpdateSpeedFilePath
      )

      formattedLine should include("""|Aerodent|31.1""".stripMargin)
    }
  }

  "calling updatePowerLine with a valid input" should {
    "will format to write the correct entry" in {
      reflect.io
        .File(testUpdatePowerFilePath)
        .writeAll("""CHARACTER|FIRST GAME|POWER
                                                          |3 Musty Fears|Super Mario RPG: Legend of the Seven Stars|4.62114
                                                          |Admiral Bobbery|Paper Mario: The Thousand-Year Door|50.3991
                                                          |Aerodent|Wario Land 4|62.536
                                                          |""".stripMargin)

      val superMarioCharactersParser = new SuperMarioCharactersParser()
      val formattedPowerLine = superMarioCharactersParser.updatePowerLine(
        SuperMarioCharacter("Aerodent", "game-1", 23.9, 31.1),
        testUpdatePowerFilePath
      )

      formattedPowerLine should include(
        """|Aerodent|game-1|7.4329""".stripMargin
      )

      superMarioCharactersParser.updatePowerLine(
        SuperMarioCharacter("Aerodent", "Wario Land 4", 62.536, 22.9676),
        testUpdatePowerFilePath
      )
    }
  }
}
