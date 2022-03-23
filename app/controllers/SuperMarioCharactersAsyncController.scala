package controllers

import csvparser.SuperMarioCharactersParser
import models.{SearchRequest, SuperMarioCharacter}
import play.api.libs.json._
import play.api.mvc._
import query.QueryParameters
import services.SuperMarioCharactersService

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class SuperMarioCharactersAsyncController @Inject() (cc: ControllerComponents)(
    implicit exec: ExecutionContext
) extends AbstractController(cc) {
  private val superMarioCharactersParser = new SuperMarioCharactersParser()
  private val superMarioCharactersService = new SuperMarioCharactersService()

  import superMarioCharactersService._

  def getAllNames: Action[AnyContent] = Action {
    val allCharacters = superMarioCharactersService.getAllNames
    Ok(Json.toJson(allCharacters).toString())
  }

  def getAllCharactersSorted: Action[AnyContent] = Action { request =>
    val maybeSortOrderParam = QueryParameters(request.queryString)
    val sortedCharacters =
      superMarioCharactersService.getAllCharactersSorted(maybeSortOrderParam)
    Ok(Json.toJson(sortedCharacters).toString())
  }

  def search: Action[AnyContent] = Action { request =>
    val maybeJson = request.body.asJson
    val maybeSearchRequest =
      maybeJson.map(json => Json.fromJson[SearchRequest](json))

    val powerItems = readPowerItems()
    val speedItems = readSpeedItems()

    maybeSearchRequest
      .map {
        case JsSuccess(searchRequest, _) =>
          val foundCharacters =
            filterCharacters(searchRequest, powerItems, speedItems)
          Ok(Json.toJson(foundCharacters).toString())
        case JsError(errors) => InternalServerError(errors.toString())
      }
      .getOrElse {
        val foundCharacters = composeCharacterItems(powerItems, speedItems)
        Ok(Json.toJson(foundCharacters).toString())
      }
  }

  def create: Action[AnyContent] = Action { request =>
    val maybeJson = request.body.asJson
    val maybeSuperMarioCharacter =
      maybeJson.map(json => Json.fromJson[SuperMarioCharacter](json))

    maybeSuperMarioCharacter
      .map {
        case JsSuccess(superMarioCharacter, _) =>
          superMarioCharactersParser.writeCharacter(superMarioCharacter)
          Ok(Json.toJson(superMarioCharacter).toString)
        case JsError(errors) => InternalServerError(errors.toString())
      }
      .getOrElse {
        BadRequest("Invalid input given in body")
      }
  }

  def update: Action[AnyContent] = Action { request =>
    implicit val superMarioCharacterReads: Reads[SuperMarioCharacter] =
      Json.reads[SuperMarioCharacter]

    val maybeJson = request.body.asJson
    val maybeSuperMarioCharacter =
      maybeJson.map(json => Json.fromJson[SuperMarioCharacter](json))

    maybeSuperMarioCharacter
      .map {
        case JsSuccess(superMarioCharacter, _) =>
          superMarioCharactersParser.updateCharacter(superMarioCharacter)
          Ok(Json.toJson(superMarioCharacter).toString)
        case JsError(errors) => InternalServerError(errors.toString())
      }
      .getOrElse {
        BadRequest("Invalid input given in body")
      }
  }
}
