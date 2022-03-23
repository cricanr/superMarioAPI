package controllers

import csvparser.CSVFilePath.{PowernessCSVFilePath, SpeedCSVFilePath}
import csvparser.SuperMarioCharactersParser
import models.{SearchRequest, SuperMarioCharacter, SuperMarioCharacterPowerModel, SuperMarioCharacterSpeedModel}
import play.api.libs.json._
import play.api.mvc._
import query.QueryParameters

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class SuperMarioCharactersAsyncController @Inject()(cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  private val superMarioCharactersParser = new SuperMarioCharactersParser()

  def getAllNames: Action[AnyContent] = Action {
    val allPowerItems = superMarioCharactersParser.readAllItems(PowernessCSVFilePath)
    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(powerItemRaw)
    )
    val allCharacters = powerItems.map(powerItem => powerItem.character)

    Ok(Json.toJson(allCharacters).toString())
  }

  def getAllCharactersSorted: Action[AnyContent] = Action { request =>
    val maybeSortOrderParam = QueryParameters(request.queryString)

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

    Ok(Json.toJson(sortedCharacters).toString())
  }

  def search: Action[AnyContent] = Action { request =>
    val maybeJson = request.body.asJson
    val maybeSearchRequest = maybeJson.map(json => Json.fromJson[SearchRequest](json))

    val powerItems = readPowerItems()
    val speedItems = readSpeedItems()

    maybeSearchRequest.map {
      case JsSuccess(searchRequest, _) =>
        val foundCharacterKeys = powerItems.keys.toList.filter(a => searchRequest.names.contains(a))
        val foundCharacters = foundCharacterKeys.map(characterName => SuperMarioCharacter(name = characterName, firstGame = powerItems(characterName).firstGame, power = calculatePower(speedItems(characterName), powerItems(characterName)), speed = speedItems(characterName).speed)).toList
        Ok(Json.toJson(foundCharacters).toString())
      case JsError(errors) => InternalServerError(errors.toString())
    }.getOrElse {
      val foundCharacters = powerItems.keys.map(characterName =>
        SuperMarioCharacter(
          name = characterName,
          firstGame = powerItems(characterName).firstGame,
          power = calculatePower(speedItems(characterName),
            powerItems(characterName)),
          speed = speedItems(characterName).speed
        )).toList
      Ok(Json.toJson(foundCharacters).toString())
    }

  }

  def create: Action[AnyContent] = Action { request =>
    val maybeJson = request.body.asJson
    val maybeSuperMarioCharacter = maybeJson.map(json => Json.fromJson[SuperMarioCharacter](json))

    maybeSuperMarioCharacter.map {
      case JsSuccess(superMarioCharacter, _) =>
        superMarioCharactersParser.writeCharacter(superMarioCharacter)
        Ok(Json.toJson(superMarioCharacter).toString)
      case JsError(errors) => InternalServerError(errors.toString())
    }.getOrElse {
      BadRequest("Invalid input given in body")
    }
  }

  def update: Action[AnyContent] = Action { request =>
    implicit val superMarioCharacterReads: Reads[SuperMarioCharacter] = Json.reads[SuperMarioCharacter]

    val maybeJson = request.body.asJson
    val maybeSuperMarioCharacter = maybeJson.map(json => Json.fromJson[SuperMarioCharacter](json))

    maybeSuperMarioCharacter.map {
      case JsSuccess(superMarioCharacter, _) =>
        superMarioCharactersParser.updateCharacter(superMarioCharacter)
        Ok(Json.toJson(superMarioCharacter).toString)
      case JsError(errors) => InternalServerError(errors.toString())
    }.getOrElse {
      BadRequest("Invalid input given in body")
    }
  }

  private def calculatePower(speedItem: SuperMarioCharacterSpeedModel, powerItem: SuperMarioCharacterPowerModel): Double = {
    powerItem.powerfulness * 100 / speedItem.speed
  }

  private def readSpeedItems(): Map[String, SuperMarioCharacterSpeedModel] = {
    val allSpeedItems = superMarioCharactersParser.readAllItems(SpeedCSVFilePath)
    val speedItems = allSpeedItems.map(speedItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLineMap(speedItemRaw)
    ).toMap
    speedItems
  }

  private def readPowerItems(): Map[String, SuperMarioCharacterPowerModel] = {
    val allPowerItems = superMarioCharactersParser.readAllItems(PowernessCSVFilePath)
    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLineMap(powerItemRaw)
    ).toMap
    powerItems
  }
}
