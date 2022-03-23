package services

import csvparser.CSVFilePath.{PowernessCSVFilePath, SpeedCSVFilePath}
import csvparser.SuperMarioCharactersParser
import models.{SearchRequest, SuperMarioCharacter, SuperMarioCharacterPowerModel, SuperMarioCharacterSpeedModel}

trait ISuperMarioCharactersService {
  def getAllNames: List[String]
}

class SuperMarioCharactersService extends ISuperMarioCharactersService {
  private val superMarioCharactersParser = new SuperMarioCharactersParser()

  def getAllNames: List[String] = {
    val allPowerItems = superMarioCharactersParser.readAllItems(PowernessCSVFilePath)
    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(powerItemRaw)
    )
    val allCharacters = powerItems.map(powerItem => powerItem.character)
    allCharacters
  }

  def getAllCharactersSorted(maybeSortOrderParam: Option[String]): List[SuperMarioCharacter] = {
    val powerItems = readPowerItems()
    val speedItems = readSpeedItems()

    val allCharacters = powerItems.keys.map(key =>
      SuperMarioCharacter(
        name = key,
        firstGame = powerItems(key).firstGame,
        power = calculatePower(speedItems(key),
          powerItems(key)),
        speed = speedItems(key).speed)
    ).toList

    val sortedCharacters = maybeSortOrderParam match {
      case Some("asc") => allCharacters.sortBy(characters => characters.power)
      case Some("desc") => allCharacters.sortBy(characters => characters.power)(Ordering[Double].reverse)
      case None => allCharacters
    }

    sortedCharacters
  }

  def calculatePower(speedItem: SuperMarioCharacterSpeedModel, powerItem: SuperMarioCharacterPowerModel): Double = {
    powerItem.powerfulness * 100 / speedItem.speed
  }

  def readSpeedItems(): Map[String, SuperMarioCharacterSpeedModel] = {
    val allSpeedItems = superMarioCharactersParser.readAllItems(SpeedCSVFilePath)
    val speedItems = allSpeedItems.map(speedItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLineMap(speedItemRaw)
    ).toMap
    speedItems
  }

  def readPowerItems(): Map[String, SuperMarioCharacterPowerModel] = {
    val allPowerItems = superMarioCharactersParser.readAllItems(PowernessCSVFilePath)
    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLineMap(powerItemRaw)
    ).toMap
    powerItems
  }


  def composeCharacterItems(powerItems: Map[String, SuperMarioCharacterPowerModel], speedItems: Map[String, SuperMarioCharacterSpeedModel]): Seq[SuperMarioCharacter] = {
    powerItems.keys.map(characterName =>
      SuperMarioCharacter(
        name = characterName,
        firstGame = powerItems(characterName).firstGame,
        power = calculatePower(speedItems(characterName),
          powerItems(characterName)),
        speed = speedItems(characterName).speed
      )).toList
  }

  def filterCharacters(searchRequest: SearchRequest, powerItems: Map[String, SuperMarioCharacterPowerModel], speedItems: Map[String, SuperMarioCharacterSpeedModel]): Seq[SuperMarioCharacter] = {
    val foundCharacterKeys = powerItems.keys.toList.filter(a => searchRequest.names.contains(a))
    val foundCharacters = foundCharacterKeys.map(characterName => SuperMarioCharacter(name = characterName, firstGame = powerItems(characterName).firstGame, power = calculatePower(speedItems(characterName), powerItems(characterName)), speed = speedItems(characterName).speed)).toList
    foundCharacters
  }
}
