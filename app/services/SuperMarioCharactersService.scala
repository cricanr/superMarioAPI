package services

import com.google.inject.Inject
import csvparser.SuperMarioCharactersParser.{csvPowerFilePath, csvSpeedFilePath}
import csvparser.{ISuperMarioCharactersParser, SuperMarioCharactersParser}
import models.{
  SearchRequest,
  SuperMarioCharacter,
  SuperMarioCharacterPowerModel,
  SuperMarioCharacterSpeedModel
}

trait ISuperMarioCharactersService {
  def getAllNames(csvFilePath: String = csvPowerFilePath): List[String]
  def getAllCharactersSorted(
      maybeSortOrderParam: Option[String],
      csvSpeedFilePath: String = csvSpeedFilePath,
      csvPowerFilePath: String = csvPowerFilePath
  ): List[SuperMarioCharacter]
  def writeCharacter(superMarioCharacter: SuperMarioCharacter): Unit
  def updateCharacter(superMarioCharacter: SuperMarioCharacter): Unit
  def readSpeedItems(
      csvSpeedFilePath: String = csvSpeedFilePath
  ): Map[String, SuperMarioCharacterSpeedModel]
  def readPowerItems(
      csvPowerFilePath: String = csvPowerFilePath
  ): Map[String, SuperMarioCharacterPowerModel]
}

class SuperMarioCharactersService @Inject() (
    superMarioCharactersParser: ISuperMarioCharactersParser
) extends ISuperMarioCharactersService {
  def getAllNames(csvFilePath: String = csvPowerFilePath): List[String] = {
    val allPowerItems =
      superMarioCharactersParser.readAllItems(csvPowerFilePath)
    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(
        powerItemRaw
      )
    )
    val allCharacters = powerItems.map(powerItem => powerItem.character)
    allCharacters
  }

  def getAllCharactersSorted(
      maybeSortOrderParam: Option[String],
      csvSpeedFilePath: String = csvSpeedFilePath,
      csvPowerFilePath: String = csvPowerFilePath
  ): List[SuperMarioCharacter] = {
    val speedItems = readSpeedItems(csvSpeedFilePath)
    val powerItems = readPowerItems(csvPowerFilePath)

    val allCharacters = powerItems.keys
      .map(key =>
        SuperMarioCharacter(
          name = key,
          firstGame = powerItems(key).firstGame,
          power = SuperMarioCharactersService
            .calculatePower(speedItems(key), powerItems(key)),
          speed = speedItems(key).speed
        )
      )
      .toList

    val sortedCharacters = maybeSortOrderParam match {
      case Some("asc") => allCharacters.sortBy(characters => characters.power)
      case Some("desc") =>
        allCharacters.sortBy(characters => characters.power)(
          Ordering[Double].reverse
        )
      case _ => allCharacters
    }

    sortedCharacters
  }

  def writeCharacter(superMarioCharacter: SuperMarioCharacter): Unit = {
    superMarioCharactersParser.writeCharacter(superMarioCharacter)
  }

  def updateCharacter(superMarioCharacter: SuperMarioCharacter): Unit = {
    superMarioCharactersParser.updateCharacter(superMarioCharacter)
  }

  def readSpeedItems(
      csvSpeedFilePath: String = csvSpeedFilePath
  ): Map[String, SuperMarioCharacterSpeedModel] = {
    val allSpeedItems =
      superMarioCharactersParser.readAllItems(csvSpeedFilePath)
    val speedItems = allSpeedItems
      .map(speedItemRaw =>
        SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLineMap(
          speedItemRaw
        )
      )
      .toMap
    speedItems
  }

  def readPowerItems(
      csvPowerFilePath: String = csvPowerFilePath
  ): Map[String, SuperMarioCharacterPowerModel] = {
    val allPowerItems =
      superMarioCharactersParser.readAllItems(csvPowerFilePath)
    val powerItems = allPowerItems
      .map(powerItemRaw =>
        SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLineMap(
          powerItemRaw
        )
      )
      .toMap
    powerItems
  }
}

object SuperMarioCharactersService {
  def calculatePower(
      speedItem: SuperMarioCharacterSpeedModel,
      powerItem: SuperMarioCharacterPowerModel
  ): Double = {
    powerItem.powerfulness * 100 / speedItem.speed
  }

  def composeCharacterItems(
      powerItems: Map[String, SuperMarioCharacterPowerModel],
      speedItems: Map[String, SuperMarioCharacterSpeedModel]
  ): Seq[SuperMarioCharacter] = {
    powerItems.keys
      .map(characterName =>
        SuperMarioCharacter(
          name = characterName,
          firstGame = powerItems(characterName).firstGame,
          power = calculatePower(
            speedItems(characterName),
            powerItems(characterName)
          ),
          speed = speedItems(characterName).speed
        )
      )
      .toList
  }

  def filterCharacters(
      searchRequest: SearchRequest,
      powerItems: Map[String, SuperMarioCharacterPowerModel],
      speedItems: Map[String, SuperMarioCharacterSpeedModel]
  ): Seq[SuperMarioCharacter] = {
    val foundCharacterKeys =
      powerItems.keys.toList.filter(a => searchRequest.names.contains(a))
    val foundCharacters = foundCharacterKeys
      .map(characterName =>
        SuperMarioCharacter(
          name = characterName,
          firstGame = powerItems(characterName).firstGame,
          power = calculatePower(
            speedItems(characterName),
            powerItems(characterName)
          ),
          speed = speedItems(characterName).speed
        )
      )
    foundCharacters
  }
}
