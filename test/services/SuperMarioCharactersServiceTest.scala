package services

import csvparser.ISuperMarioCharactersParser
import models.{
  SearchRequest,
  SuperMarioCharacter,
  SuperMarioCharacterPowerModel,
  SuperMarioCharacterSpeedModel
}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import testutils.TestData.{
  powerLines,
  speedLines,
  validPowerFilePath,
  validSpeedFilePath
}

class SuperMarioCharactersServiceTest
    extends AnyWordSpec
    with Matchers
    with MockitoSugar {

  private val aerodent =
    SuperMarioCharacter("Aerodent", "Wario Land 4", 272.2792107142235, 22.9676)

  "The SuperMarioCharactersService" when {
    "calling getAllNames on valid file path" should {
      "return a list of character names" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath = any[String])
        ).thenReturn(powerLines)

        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        val characterNames = superMarioCharactersService.getAllNames()

        characterNames shouldBe List(
          "3 Musty Fears",
          "Admiral Bobbery",
          "Aerodent"
        )
      }
    }

    "calling getAllNames on valid empty file" should {
      "return an empty list of character names" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath = any[String])
        ).thenReturn(List.empty)
        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        val characterNames = superMarioCharactersService.getAllNames()

        characterNames shouldBe List.empty
      }
    }

    "calling getAllCharactersSorted on valid file with sort order asc" should {
      "return a list of sorted characters based on sort order param" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validPowerFilePath
          )
        ).thenReturn(powerLines)
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validSpeedFilePath
          )
        ).thenReturn(speedLines)
        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        val characters = superMarioCharactersService.getAllCharactersSorted(
          Some("asc"),
          validSpeedFilePath,
          validPowerFilePath
        )

        characters shouldBe List(
          SuperMarioCharacter(
            "3 Musty Fears",
            "Super Mario RPG: Legend of the Seven Stars",
            12.161214768809705,
            37.999
          ),
          SuperMarioCharacter(
            "Admiral Bobbery",
            "Paper Mario: The Thousand-Year Door",
            77.35463898221579,
            65.1533
          ),
          SuperMarioCharacter(
            "Aerodent",
            "Wario Land 4",
            272.2792107142235,
            22.9676
          )
        )
      }
    }

    "calling getAllCharactersSorted on valid file with sort order invalid" should {
      "return a list of sorted characters based on default sort order param" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validPowerFilePath
          )
        ).thenReturn(powerLines)
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validSpeedFilePath
          )
        ).thenReturn(speedLines)
        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        val characters = superMarioCharactersService.getAllCharactersSorted(
          Some("miau"),
          validSpeedFilePath,
          validPowerFilePath
        )

        characters shouldBe List(
          SuperMarioCharacter(
            "3 Musty Fears",
            "Super Mario RPG: Legend of the Seven Stars",
            12.161214768809705,
            37.999
          ),
          SuperMarioCharacter(
            "Admiral Bobbery",
            "Paper Mario: The Thousand-Year Door",
            77.35463898221579,
            65.1533
          ),
          SuperMarioCharacter(
            "Aerodent",
            "Wario Land 4",
            272.2792107142235,
            22.9676
          )
        )
      }
    }

    "calling getAllCharactersSorted on empty valid files" should {
      "return a list of sorted characters based on sort order param" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validPowerFilePath
          )
        ).thenReturn(List.empty)
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validSpeedFilePath
          )
        ).thenReturn(List.empty)
        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        val characters = superMarioCharactersService.getAllCharactersSorted(
          Some("?sortOrder=asc"),
          validSpeedFilePath,
          validPowerFilePath
        )

        characters shouldBe List.empty
      }
    }

    "calling getAllCharactersSorted on valid file" should {
      "return a list of sorted characters based on sort order param desc" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validPowerFilePath
          )
        ).thenReturn(powerLines)
        when(
          superMarioCharactersParserMock.readAllItems(csvFilePath =
            validSpeedFilePath
          )
        ).thenReturn(speedLines)
        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        val characters = superMarioCharactersService.getAllCharactersSorted(
          Some("desc"),
          validSpeedFilePath,
          validPowerFilePath
        )

        characters shouldBe List(
          SuperMarioCharacter(
            "Aerodent",
            "Wario Land 4",
            272.2792107142235,
            22.9676
          ),
          SuperMarioCharacter(
            "Admiral Bobbery",
            "Paper Mario: The Thousand-Year Door",
            77.35463898221579,
            65.1533
          ),
          SuperMarioCharacter(
            "3 Musty Fears",
            "Super Mario RPG: Legend of the Seven Stars",
            12.161214768809705,
            37.999
          )
        )
      }
    }

    "calling writeCharacter" should {
      "call underlying parser writeCharacter method" in {
        val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
        val superMarioCharactersService =
          new SuperMarioCharactersService(superMarioCharactersParserMock)
        superMarioCharactersService.writeCharacter(aerodent)

        verify(superMarioCharactersParserMock).writeCharacter(aerodent)
      }
    }
  }

  "calling updateCharacter" should {
    "call underlying parser updateCharacter method" in {
      val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]

      val superMarioCharactersService =
        new SuperMarioCharactersService(superMarioCharactersParserMock)
      superMarioCharactersService.updateCharacter(aerodent)

      verify(superMarioCharactersParserMock).updateCharacter(aerodent)
    }
  }

  "calling readSpeedItems on valid file path" should {
    "return the correct speed items" in {
      val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
      when(
        superMarioCharactersParserMock.readAllItems(csvFilePath =
          validSpeedFilePath
        )
      ).thenReturn(speedLines)
      val superMarioCharactersService =
        new SuperMarioCharactersService(superMarioCharactersParserMock)

      val speedItems =
        superMarioCharactersService.readSpeedItems(validSpeedFilePath)

      speedItems shouldBe (
        Map(
          "3 Musty Fears" -> SuperMarioCharacterSpeedModel(37.999),
          "Admiral Bobbery" -> SuperMarioCharacterSpeedModel(65.1533),
          "Aerodent" -> SuperMarioCharacterSpeedModel(22.9676)
        )
      )
    }
  }

  "calling readSpeedItems on valid empty file path" should {
    "return no speed items" in {
      val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
      when(
        superMarioCharactersParserMock.readAllItems(csvFilePath =
          validSpeedFilePath
        )
      ).thenReturn(List.empty)
      val superMarioCharactersService =
        new SuperMarioCharactersService(superMarioCharactersParserMock)

      val speedItems =
        superMarioCharactersService.readSpeedItems(validSpeedFilePath)

      speedItems shouldBe (Map.empty)
    }
  }

  "calling readPowerItems on valid file path" should {
    "return the correct power items" in {
      val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
      when(
        superMarioCharactersParserMock.readAllItems(csvFilePath =
          validPowerFilePath
        )
      ).thenReturn(powerLines)
      val superMarioCharactersService =
        new SuperMarioCharactersService(superMarioCharactersParserMock)

      val speedItems =
        superMarioCharactersService.readPowerItems(validPowerFilePath)

      speedItems shouldBe (
        Map(
          "3 Musty Fears" -> SuperMarioCharacterPowerModel(
            "Super Mario RPG: Legend of the Seven Stars",
            4.62114
          ),
          "Admiral Bobbery" -> SuperMarioCharacterPowerModel(
            "Paper Mario: The Thousand-Year Door",
            50.3991
          ),
          "Aerodent" -> SuperMarioCharacterPowerModel("Wario Land 4", 62.536)
        )
      )
    }
  }

  "calling readPowerItems on valid empty file path" should {
    "return no power items" in {
      val superMarioCharactersParserMock = mock[ISuperMarioCharactersParser]
      when(
        superMarioCharactersParserMock.readAllItems(csvFilePath =
          validPowerFilePath
        )
      ).thenReturn(List.empty)
      val superMarioCharactersService =
        new SuperMarioCharactersService(superMarioCharactersParserMock)

      val speedItems =
        superMarioCharactersService.readPowerItems(validPowerFilePath)

      speedItems shouldBe (Map.empty)
    }
  }

  "calling composeCharacterItems on valid speed & power items" should {
    "return the correct SuperMarioCharacters" in {
      val speedItems = Map("test" -> SuperMarioCharacterSpeedModel(232))
      val powerItems =
        Map("test" -> SuperMarioCharacterPowerModel("test-1", 32))
      val composedCharacter = SuperMarioCharactersService.composeCharacterItems(
        powerItems,
        speedItems
      )
      composedCharacter.size shouldBe 1
      val character = composedCharacter.head

      assert(character.power === 13.79 +- 0.01)
      character.name shouldBe "test"
      character.firstGame shouldBe "test-1"
      character.speed shouldBe 232.0
    }
  }

  "calling composeCharacterItems on valid empty speed & power items" should {
    "return no character" in {
      val powerItems: Map[String, SuperMarioCharacterPowerModel] = Map.empty
      val speedItems: Map[String, SuperMarioCharacterSpeedModel] = Map.empty
      val composedCharacter = SuperMarioCharactersService.composeCharacterItems(
        powerItems,
        speedItems
      )
      composedCharacter shouldBe Seq.empty
    }
  }

  "calling filterCharacters on valid speed & power items" should {
    "return the correct SuperMarioCharacters found" in {
      val speedItems = Map(
        "test-1" -> SuperMarioCharacterSpeedModel(232),
        "test-2" -> SuperMarioCharacterSpeedModel(232)
      )
      val powerItems = Map(
        "test-1" -> SuperMarioCharacterPowerModel("test-1", 32),
        "test-2" -> SuperMarioCharacterPowerModel("test-1", 32)
      )
      val composedCharacter = SuperMarioCharactersService.filterCharacters(
        SearchRequest(List("test-2")),
        powerItems,
        speedItems
      )
      composedCharacter.size shouldBe 1
      val character = composedCharacter.head

      assert(character.power === 13.79 +- 0.01)
      character.name shouldBe "test-2"
      character.firstGame shouldBe "test-1"
      character.speed shouldBe 232.0
    }
  }

  "calling filterCharacters on valid speed & power items with not existing filter" should {
    "return no SuperMarioCharacter" in {
      val speedItems = Map(
        "test-1" -> SuperMarioCharacterSpeedModel(232),
        "test-2" -> SuperMarioCharacterSpeedModel(232)
      )
      val powerItems = Map(
        "test-1" -> SuperMarioCharacterPowerModel("test-1", 32),
        "test-2" -> SuperMarioCharacterPowerModel("test-1", 32)
      )
      val composedCharacter = SuperMarioCharactersService.filterCharacters(
        SearchRequest(List("Jocker")),
        powerItems,
        speedItems
      )
      composedCharacter shouldBe Seq.empty
    }
  }

  "calling filterCharacters on valid empty speed & power items" should {
    "return no SuperMarioCharacter" in {
      val powerItems: Map[String, SuperMarioCharacterPowerModel] = Map.empty
      val speedItems: Map[String, SuperMarioCharacterSpeedModel] = Map.empty
      val composedCharacter = SuperMarioCharactersService.filterCharacters(
        SearchRequest(List("Jocker")),
        powerItems,
        speedItems
      )
      composedCharacter shouldBe Seq.empty
    }
  }
}
