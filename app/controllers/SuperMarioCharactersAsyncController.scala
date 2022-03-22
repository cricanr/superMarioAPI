package controllers

import akka.actor.ActorSystem
import csvparser.SuperMarioCharactersParser
import models.{SearchRequest, SuperMarioCharacter, SuperMarioCharacterPowerModel, SuperMarioCharacterSpeedModel}
import play.api.libs.json._
import play.api.mvc._
import query.QueryParameters

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class SuperMarioCharactersAsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  def getAllNames: Action[AnyContent] = Action {
    val superMarioCharactersParser = new SuperMarioCharactersParser()
    val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
    val allPowerItems = superMarioCharactersParser.readAllItems(csvPowerFilePath)

    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(powerItemRaw)
    )

    val allCharacters = powerItems.map(powerItem => powerItem.character)

    Ok(allCharacters.toString())
  }

  def getAllCharactersSorted: Action[AnyContent] = Action { request =>
    val maybeSortOrderParam = QueryParameters(request.queryString)
    val superMarioCharactersParser = new SuperMarioCharactersParser()
    val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
    val allPowerItems = superMarioCharactersParser.readAllItems(csvPowerFilePath)

    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLineMap(powerItemRaw)
    ).toMap

    val csvSpeedFilePath = "app/resources/super-mario-characters-speed.csv"
    val allSpeedItems = superMarioCharactersParser.readAllItems(csvSpeedFilePath)
    val speedItems = allSpeedItems.map(speedItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLineMap(speedItemRaw)
    ).toMap

    val allCharacters = powerItems.keys.map(key => SuperMarioCharacter(name = key, firstGame = powerItems(key).firstGame, power = calculatePower(speedItems(key), powerItems(key)), speed = speedItems(key).speed)).toList

    val sortedCharacters = maybeSortOrderParam match {
      case Some("asc") => allCharacters.sortBy(characters => characters.power)
      case Some("desc") => allCharacters.sortBy(characters => characters.power)(Ordering[Double].reverse)
      case None => allCharacters
    }

    Ok(sortedCharacters.toString())
  }

  def search: Action[AnyContent] = Action { request =>
    implicit val searchRequestReads: Reads[SearchRequest] = Json.reads[SearchRequest]

    val maybeJson = request.body.asJson
    val maybeSearchRequest = maybeJson.map(json => Json.fromJson[SearchRequest](json))

    val superMarioCharactersParser = new SuperMarioCharactersParser()
    val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
    val allPowerItems = superMarioCharactersParser.readAllItems(csvPowerFilePath)

    val powerItems = allPowerItems.map(powerItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLineMap(powerItemRaw)
    ).toMap

    val csvSpeedFilePath = "app/resources/super-mario-characters-speed.csv"
    val allSpeedItems = superMarioCharactersParser.readAllItems(csvSpeedFilePath)
    val speedItems = allSpeedItems.map(speedItemRaw =>
      SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLineMap(speedItemRaw)
    ).toMap

    maybeSearchRequest.map {
      case JsSuccess(searchRequest, _) =>
        val foundCharacterKeys = powerItems.keys.toList.filter(a => searchRequest.names.contains(a))
        val foundCharacters = foundCharacterKeys.map(characterName => SuperMarioCharacter(name = characterName, firstGame = powerItems(characterName).firstGame, power = calculatePower(speedItems(characterName), powerItems(characterName)), speed = speedItems(characterName).speed)).toList
        Ok(foundCharacters.toString())
      case JsError(errors) => InternalServerError(errors.toString())
    }.getOrElse {
      val foundCharacters = powerItems.keys.map(characterName => SuperMarioCharacter(name = characterName, firstGame = powerItems(characterName).firstGame, power = calculatePower(speedItems(characterName), powerItems(characterName)), speed = speedItems(characterName).speed)).toList
      Ok(foundCharacters.toString())
    }

  }

  def create: Action[AnyContent] = Action { request =>
    implicit val superMarioCharacterReads: Reads[SuperMarioCharacter] = Json.reads[SuperMarioCharacter]
    val superMarioCharactersParser = new SuperMarioCharactersParser()

    val maybeJson = request.body.asJson
    val maybeSuperMarioCharacter = maybeJson.map(json => Json.fromJson[SuperMarioCharacter](json))

    maybeSuperMarioCharacter.map {
      case JsSuccess(superMarioCharacter, _) =>
        superMarioCharactersParser.writeCharacter(superMarioCharacter)
        Ok(superMarioCharacter.toString)
      case JsError(errors) => InternalServerError(errors.toString())
    }.getOrElse {
      BadRequest("Invalid input given in body")
    }


    Ok(s"maybeSuperMarioCharacter: ${maybeSuperMarioCharacter.toString}")
  }

  def update: Action[AnyContent] = Action { request =>
    implicit val superMarioCharacterReads: Reads[SuperMarioCharacter] = Json.reads[SuperMarioCharacter]
    val superMarioCharactersParser = new SuperMarioCharactersParser()

    val maybeJson = request.body.asJson
    val maybeSuperMarioCharacter = maybeJson.map(json => Json.fromJson[SuperMarioCharacter](json))

    maybeSuperMarioCharacter.map {
      case JsSuccess(superMarioCharacter, _) =>
        superMarioCharactersParser.updateCharacter(superMarioCharacter)
        Ok(superMarioCharacter.toString)
      case JsError(errors) => InternalServerError(errors.toString())
    }.getOrElse {
      BadRequest("Invalid input given in body")
    }
  }

  private def calculatePower(speedItem: SuperMarioCharacterSpeedModel, powerItem: SuperMarioCharacterPowerModel): Double = {
    powerItem.powerfulness * 100 / speedItem.speed
  }
}
