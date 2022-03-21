package controllers

import javax.inject._
import akka.actor.ActorSystem
import csvparser.SuperMarioCharactersParser
import models.{SuperMarioCharacter, SuperMarioCharacterPowerModel, SuperMarioCharacterSpeedModel}
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  /**
   * Creates an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message = Action.async {
    getFutureMessage(1.second).map { msg =>
      val superMarioCharactersParser = new SuperMarioCharactersParser()
      val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
      val allPowerItems = superMarioCharactersParser.readAllItems(csvPowerFilePath)

      val powerItems = allPowerItems.map(powerItemRaw =>
          SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(powerItemRaw)
      )

      val csvSpeedFilePath = "app/resources/super-mario-characters-speed.csv"
      val allSpeedItems = superMarioCharactersParser.readAllItems(csvSpeedFilePath)
      val speedItems = allSpeedItems.map(speedItemRaw =>
          SuperMarioCharactersParser.getSuperMarioCharactersSpeedFromCsvLine(speedItemRaw)
        )



      Ok(s"POWERItems: ${powerItems.toString} ; SpeedItems: ${speedItems}")

    }
  }

    def getAllCharacters = Action.async {
      getFutureMessage(1.second).map { msg =>
        val superMarioCharactersParser = new SuperMarioCharactersParser()
        val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
        val allPowerItems = superMarioCharactersParser.readAllItems(csvPowerFilePath)

        val powerItems = allPowerItems.map(powerItemRaw =>
          SuperMarioCharactersParser.getSuperMarioCharactersPowerFromCsvLine(powerItemRaw)
        )

        val allCharacters = powerItems.map(powerItem => powerItem.character)

        Ok(allCharacters.toString())
      }
    }

  def getAllCharactersSorted(sortOrder: String) = Action.async {
    getFutureMessage(1.second).map { msg =>
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

      val allCharacters = powerItems.keys.map(key => SuperMarioCharacter(character = key, firstGame = powerItems(key).firstGame, power = calculatePower(speedItems(key), powerItems(key)), speed = speedItems(key).speed)).toList

      val sortedCharacters = sortOrder match {
        case "asc" => allCharacters.sortBy(characters => characters.power)
        case "desc" => allCharacters.sortBy(characters => characters.power)(Ordering[Double].reverse)
      }

      Ok(sortedCharacters.toString() + "miauuu")
    }
  }

  def queryCharacters(characterNames: List[String]) = Action.async {
    getFutureMessage(1.second).map { msg =>
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

      val foundCharacterKeys = powerItems.keys.toList.filter(a => characterNames.contains(a))

      val foundCharacters = foundCharacterKeys.map(characterName => SuperMarioCharacter(character = characterName, firstGame = powerItems(characterName).firstGame, power = calculatePower(speedItems(characterName), powerItems(characterName)), speed = speedItems(characterName).speed)).toList

      Ok(foundCharacters.toString())
    }
  }

  //  def getAllCharacters2 = Action.async {
//    getFutureMessage(1.second).map { msg =>
//      val superMarioCharactersParser = new SuperMarioCharactersParser()
//
//      val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
//      val allPowerItems = superMarioCharactersParser.readAllItems(csvPowerFilePath)
//      val powerItems = allPowerItems.map(powerItemsRaw =>
//        powerItemsRaw.map(speedItem =>
//          speedItem.head -> SuperMarioCharacterPowerModel(speedItem(1), speedItem(2).toDouble)
//        ).toMap
//      )
//
//      val csvSpeedFilePath = "app/resources/super-mario-characters-speed.csv"
//      val allSpeedItems = superMarioCharactersParser.readAllItems(csvSpeedFilePath)
//      val speedItems = allSpeedItems.map(speedItemsRaw =>
//        speedItemsRaw.map(speedItem =>
//          speedItem.head -> SuperMarioCharacterSpeedModel(speedItem(1).toDouble)
//        ).toMap
//      )
//
//      //      Ok(s"POWERItems: ${powerItems.toString} ; SpeedItems: ${speedItems} \n " +
//      //        s"""speedItems[Sherry]: ${speedItems.getOrElse("Sherry", "")} ; powerItems[Sherry]: ${powerItems.getOrElse("Sherry", "")}""")
//
//      val superMarioCharacter = for {
//        speedItem <- (speedItems.right.map( b => b))
//        powerItem <- powerItems.map(a => a)
//      } yield speedItem.keys.map(c=>speedItem.get(c).get.speed) SuperMarioCharacter(character = "Sherry", firstGame = powerItem.get.firstGame, power = calculatePower(speedItem.get, powerItem.get), speed = speedItem.get.speed)
//
//      Ok(
//        s"""speedItems[Sherry]: ${speedItems.map(a => a.get("Sherry"))} ; powerItems[Sherry]: ${powerItems.map(a => a.get("Sherry"))} \n
//           |superMarioCharacter: $superMarioCharacter
//           |""".stripMargin)
//    }
//  }

  private def calculatePower(speedItem: SuperMarioCharacterSpeedModel, powerItem: SuperMarioCharacterPowerModel) = {
    powerItem.powerfulness * 100 / speedItem.speed
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

}
