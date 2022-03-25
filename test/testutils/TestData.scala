package testutils

object TestData {
  val validPowerFilePath =
    "test/resources/valid-super-mario-characters-power.csv"
  val validSpeedFilePath =
    "test/resources/valid-super-mario-characters-speed.csv"

  val powerLines = List(
    List(
      "3 Musty Fears",
      "Super Mario RPG: Legend of the Seven Stars",
      "4.62114"
    ),
    List(
      "Admiral Bobbery",
      "Paper Mario: The Thousand-Year Door",
      "50.3991"
    ),
    List("Aerodent", "Wario Land 4", "62.536")
  )

  val speedLines = List(
    List("3 Musty Fears", "37.999"),
    List("Admiral Bobbery", "65.1533"),
    List("Aerodent", "22.9676")
  )
}
