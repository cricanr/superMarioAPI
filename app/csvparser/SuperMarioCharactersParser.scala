package csvparser

import com.github.tototoshi.csv._
import com.google.inject.Inject
import models.SuperMarioCharactersPower.toCsv
import models.SuperMarioCharactersSpeed.toSeq
import models._

import java.io.File
import scala.io.Source

object CSVFilePath extends Enumeration {
  type CSVFilePath = Value
  val SpeedCSVFilePath, PowernessCSVFilePath = Value
}
import csvparser.CSVFilePath._


trait ISuperMarioCharactersParser {
  def readAllItems(
                    csvFilePath: CSVFilePath
                  ): List[List[String]]
}

class SuperMarioCharactersParser @Inject() extends ISuperMarioCharactersParser {
  private val csvPowerFilePath = "app/resources/super-mario-characters-power.csv"
  private val csvSpeedFilePath = "app/resources/super-mario-characters-speed.csv"

  private val allLinesPower = Source.fromFile(csvPowerFilePath).getLines.toSeq
  private val allLinesSpeed = Source.fromFile(csvSpeedFilePath).getLines.toSeq


  implicit object MyFormat extends DefaultCSVFormat {
    override val delimiter = '|'
  }

  private def writeSpeedLine(csvFilePath: String, superMarioCharacter: SuperMarioCharacter): Unit = {
    val speedFile = new File(csvFilePath)
    val writer = CSVWriter.open(speedFile, append = true)

    val superMarioSpeedModel = SuperMarioCharactersSpeed.apply(superMarioCharacter)
    writer.writeRow(toSeq(superMarioSpeedModel))
  }

  private def writePowerLine(csvFilePath: String, superMarioCharacter: SuperMarioCharacter): Unit = {
    val powerFile = new File(csvFilePath)
    val writer = CSVWriter.open(powerFile, append = true)

    val superMarioPowerModel = SuperMarioCharactersPower.apply(superMarioCharacter)
    writer.writeRow(SuperMarioCharactersPower.toSeq(superMarioPowerModel))
  }

  def getReader(csvFilePath: String): CSVReader = {
    CSVReader.open(new File(csvFilePath))
  }

  def readAllItems(
                    csvFilePath: CSVFilePath
                  ): List[List[String]] = {
    val reader = if (csvFilePath == SpeedCSVFilePath) getReader(csvSpeedFilePath) else getReader(csvPowerFilePath)
    reader.all().drop(1)
  }

  def writeCharacter(superMarioCharacter: SuperMarioCharacter): Unit = {
    writeSpeedLine(csvSpeedFilePath, superMarioCharacter)
    writePowerLine(csvPowerFilePath, superMarioCharacter)
  }

  def updateCharacter(superMarioCharacter: SuperMarioCharacter): Unit = {
    updatePowerLine(superMarioCharacter)
    updateSpeedLine(superMarioCharacter)
  }

  private def updateSpeedLine(superMarioCharacter: SuperMarioCharacter): Unit = {
    val superMarioSpeedModel = SuperMarioCharactersSpeed.apply(superMarioCharacter)
    val linesSpeedWithoutChangedCharacter = allLinesSpeed.filterNot(line => line.startsWith(s"${superMarioCharacter.name}|"))
    val updatedLinesSpeed = linesSpeedWithoutChangedCharacter :+ SuperMarioCharactersSpeed.toCsv(superMarioSpeedModel)
    reflect.io.File(csvSpeedFilePath).writeAll(s"${updatedLinesSpeed.mkString("\n")}\n")
  }

  private def updatePowerLine(superMarioCharacter: SuperMarioCharacter): Unit = {
    val superMarioPowerModel = SuperMarioCharactersPower.apply(superMarioCharacter)
    val linesWithoutChangedCharacter = allLinesPower.filterNot(line => line.startsWith(s"${superMarioCharacter.name}|"))
    val updatedLinesPower = linesWithoutChangedCharacter :+ toCsv(superMarioPowerModel)
    reflect.io.File(csvPowerFilePath).writeAll(s"${updatedLinesPower.mkString("\n")}\n")
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
