package models

import play.api.libs.json.{Json, OWrites, Reads}

final case class SuperMarioCharactersPower(
    character: String,
    firstGame: String,
    powerfulness: Double
)

object SuperMarioCharactersPower {
  def apply(
      superMarioCharacter: SuperMarioCharacter
  ): SuperMarioCharactersPower = {
    SuperMarioCharactersPower(
      superMarioCharacter.name,
      superMarioCharacter.firstGame,
      superMarioCharacter.speed * superMarioCharacter.power / 100
    )
  }

  def toSeq(superMarioCharactersPower: SuperMarioCharactersPower): Seq[Any] = {
    Seq(
      superMarioCharactersPower.character,
      superMarioCharactersPower.firstGame,
      superMarioCharactersPower.powerfulness
    )
  }

  def toCsv(superMarioCharactersPower: SuperMarioCharactersPower): String = {
    s"${superMarioCharactersPower.character}|${superMarioCharactersPower.firstGame}|${superMarioCharactersPower.powerfulness}"
  }
}
final case class SuperMarioCharactersSpeed(character: String, speed: Double)

object SuperMarioCharactersSpeed {
  def apply(
      superMarioCharacter: SuperMarioCharacter
  ): SuperMarioCharactersSpeed = {
    SuperMarioCharactersSpeed(
      superMarioCharacter.name,
      superMarioCharacter.speed
    )
  }

  def toSeq(superMarioCharactersSpeed: SuperMarioCharactersSpeed): Seq[Any] = {
    Seq(superMarioCharactersSpeed.character, superMarioCharactersSpeed.speed)
  }

  def toCsv(superMarioCharactersSpeed: SuperMarioCharactersSpeed): String = {
    s"${superMarioCharactersSpeed.character}|${superMarioCharactersSpeed.speed}"
  }
}

final case class SuperMarioCharacterPowerModel(
    firstGame: String,
    powerfulness: Double
)

final case class SuperMarioCharacterSpeedModel(speed: Double)

object SuperMarioCharacterSpeedModel

final case class SuperMarioCharacter(
    name: String,
    firstGame: String,
    power: Double,
    speed: Double
)

object SuperMarioCharacter {
  implicit val superMarioCharacterReads: Reads[SuperMarioCharacter] =
    Json.reads[SuperMarioCharacter]
  implicit val superMarioCharacterWrites: OWrites[SuperMarioCharacter] =
    Json.writes[SuperMarioCharacter]
}
