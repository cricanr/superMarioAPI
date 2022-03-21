package csvparser

import com.google.inject.Inject

import java.io.File
import com.github.tototoshi.csv._
import models.{SuperMarioCharacterPowerModel, SuperMarioCharacterSpeedModel, SuperMarioCharactersPower, SuperMarioCharactersSpeed}

trait ISuperMarioCharactersParser {
  def readAllItems(
                       csvFilePath: String
                     ): List[List[String]]
}

class SuperMarioCharactersParser @Inject() extends ISuperMarioCharactersParser {
  implicit object MyFormat extends DefaultCSVFormat {
    override val delimiter = '|'
  }

  def getReader(csvFilePath: String): CSVReader = {
    CSVReader.open(new File(csvFilePath))
  }

  def readAllItems(
                       csvFilePath: String
                     ): List[List[String]] = {
    val reader = getReader(csvFilePath)
    reader.all().drop(1)
  }
}

object SuperMarioCharactersParser {
  def getSuperMarioCharactersPowerFromCsvLine(
                                               csvLine: List[String]
                                             ): SuperMarioCharactersPower = {
    SuperMarioCharactersPower(
      csvLine.head,
      csvLine(1),
      csvLine(2).toDouble,
    )
  }

  def getSuperMarioCharactersSpeedFromCsvLine(
                                               csvLine: List[String]
                                             ): SuperMarioCharactersSpeed = {
    SuperMarioCharactersSpeed(
      csvLine.head,
      csvLine(1).toDouble,
    )
  }

  def getSuperMarioCharactersPowerFromCsvLineMap(
                                                  csvLine: List[String]
                                                ): (String, SuperMarioCharacterPowerModel) = {
    csvLine.head -> SuperMarioCharacterPowerModel(csvLine(1), csvLine(2).toDouble)
  }

  def getSuperMarioCharactersSpeedFromCsvLineMap(
                                                  csvLine: List[String]
                                                ): (String, SuperMarioCharacterSpeedModel) = {
    csvLine.head -> SuperMarioCharacterSpeedModel(csvLine(1).toDouble)
  }
}
