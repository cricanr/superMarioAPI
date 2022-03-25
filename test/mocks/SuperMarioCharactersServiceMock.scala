package mocks

import models.{
  SuperMarioCharacter,
  SuperMarioCharacterPowerModel,
  SuperMarioCharacterSpeedModel
}
import services.ISuperMarioCharactersService

class SuperMarioCharactersServiceMock extends ISuperMarioCharactersService {
  override def getAllNames(csvFilePath: String): List[String] = {
    List(
      "3 Musty Fears",
      "Admiral Bobbery",
      "Aerodent"
    )
  }

  override def getAllCharactersSorted(
      maybeSortOrderParam: Option[String],
      csvSpeedFilePath: String,
      csvPowerFilePath: String
  ): List[SuperMarioCharacter] = {
    List(
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

  override def writeCharacter(superMarioCharacter: SuperMarioCharacter): Unit =
    ???

  override def updateCharacter(superMarioCharacter: SuperMarioCharacter): Unit =
    ???

  override def readSpeedItems(
      csvSpeedFilePath: String
  ): Map[String, SuperMarioCharacterSpeedModel] = ???

  override def readPowerItems(
      csvPowerFilePath: String
  ): Map[String, SuperMarioCharacterPowerModel] = ???
}
