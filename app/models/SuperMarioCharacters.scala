package models

final case class SuperMarioCharactersPower(character: String, firstGame: String, powerfulness: Double)
final case class SuperMarioCharactersSpeed(character: String, speed: Double)

final case class SuperMarioCharacterPowerModel(firstGame: String, powerfulness: Double)
final case class SuperMarioCharacterSpeedModel(speed: Double)

final case class SuperMarioCharacter(name: String, firstGame: String, power: Double, speed: Double)