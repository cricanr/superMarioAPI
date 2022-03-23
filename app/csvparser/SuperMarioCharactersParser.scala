package csvparser

import com.github.tototoshi.csv._
import com.google.inject.Inject
import csvparser.SuperMarioCharactersParser.{csvPowerFilePath, csvSpeedFilePath}
import models.SuperMarioCharactersPower.toCsv
import models.SuperMarioCharactersSpeed.toSeq
import models._

import java.io.File
import scala.io.Source

trait ISuperMarioCharactersParser {
  def readAllItems(csvFilePath: String): List[List[String]]

  def writeCharacter(superMarioCharacter: SuperMarioCharacter): Unit

  def updateCharacter(superMarioCharacter: SuperMarioCharacter): Unit

  def writeSpeedLine(
                      csvFilePath: String,
                      superMarioCharacter: SuperMarioCharacter
                    ): Unit

  def writePowerLine(
                      csvFilePath: String,
                      superMarioCharacter: SuperMarioCharacter
                    ): Unit

  def updateSpeedLine(
                       superMarioCharacter: SuperMarioCharacter,
                       csvSpeedFilePath: String=csvSpeedFilePath,
                     ): String

  def updatePowerLine(
                       superMarioCharacter: SuperMarioCharacter,
                       csvPowerFilePath: String = csvPowerFilePath,
                     ): Seq[String]
}

class SuperMarioCharactersParser @Inject() extends ISuperMarioCharactersParser {
  private val allLinesPower = Source.fromFile(csvPowerFilePath).getLines.toSeq
  private val allLinesSpeed = Source.fromFile(csvSpeedFilePath).getLines.toSeq

  private implicit object MyFormat extends DefaultCSVFormat {
    override val delimiter = '|'
  }

  def readAllItems(csvFilePath: String): List[List[String]] = {
    val reader = getReader(csvFilePath)
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

  def writeSpeedLine(
      csvFilePath: String,
      superMarioCharacter: SuperMarioCharacter
  ): Unit = {
    val speedFile = new File(csvFilePath)
    val writer = CSVWriter.open(speedFile, append = true)

    val superMarioSpeedModel =
      SuperMarioCharactersSpeed.apply(superMarioCharacter)
    writer.writeRow(toSeq(superMarioSpeedModel))
  }

  def writePowerLine(
      csvFilePath: String,
      superMarioCharacter: SuperMarioCharacter
  ): Unit = {
    val powerFile = new File(csvFilePath)
    val writer = CSVWriter.open(powerFile, append = true)

    val superMarioPowerModel =
      SuperMarioCharactersPower.apply(superMarioCharacter)
    writer.writeRow(SuperMarioCharactersPower.toSeq(superMarioPowerModel))
  }

  def updateSpeedLine(
      superMarioCharacter: SuperMarioCharacter,
      csvSpeedFilePath: String=csvSpeedFilePath,
                     ): String = {
    val superMarioSpeedModel =
      SuperMarioCharactersSpeed.apply(superMarioCharacter)
    val linesSpeedWithoutChangedCharacter = allLinesSpeed.filterNot(line =>
      line.startsWith(s"${superMarioCharacter.name}|")
    )
    val updatedLinesSpeed = {
      linesSpeedWithoutChangedCharacter :+ SuperMarioCharactersSpeed.toCsv(
        superMarioSpeedModel
      )
    }
    val formatted_lines = s"${updatedLinesSpeed.mkString("\n")}\n"
    reflect.io
      .File(csvSpeedFilePath)
      .writeAll(s"${updatedLinesSpeed.mkString("\n")}\n")

    formatted_lines
  }

  def updatePowerLine(
      superMarioCharacter: SuperMarioCharacter,
      csvPowerFilePath: String = csvPowerFilePath,
                             ): Seq[String] = {
    val superMarioPowerModel =
      SuperMarioCharactersPower.apply(superMarioCharacter)
    val linesWithoutChangedCharacter = allLinesPower.filterNot(line =>
      line.startsWith(s"${superMarioCharacter.name}|")
    )
    val updatedLinesPower =
      linesWithoutChangedCharacter :+ toCsv(superMarioPowerModel)
    reflect.io
      .File(csvPowerFilePath)
      .writeAll(s"${updatedLinesPower.mkString("\n")}\n")

    updatedLinesPower
  }

  private def getReader(csvFilePath: String): CSVReader = {
    CSVReader.open(new File(csvFilePath))
  }
}

object SuperMarioCharactersParser {
  val csvPowerFilePath =
    "app/resources/super-mario-characters-power.csv"
  val csvSpeedFilePath =
    "app/resources/super-mario-characters-speed.csv"

  def getSuperMarioCharactersPowerFromCsvLine(
      csvLine: List[String]
  ): SuperMarioCharactersPower = {
    SuperMarioCharactersPower(
      csvLine.head,
      csvLine(1),
      csvLine(2).toDouble
    )
  }

  def getSuperMarioCharactersSpeedFromCsvLine(
      csvLine: List[String]
  ): SuperMarioCharactersSpeed = {
    SuperMarioCharactersSpeed(
      csvLine.head,
      csvLine(1).toDouble
    )
  }

  def getSuperMarioCharactersPowerFromCsvLineMap(
      csvLine: List[String]
  ): (String, SuperMarioCharacterPowerModel) = {
    csvLine.head -> SuperMarioCharacterPowerModel(
      csvLine(1),
      csvLine(2).toDouble
    )
  }

  def getSuperMarioCharactersSpeedFromCsvLineMap(
      csvLine: List[String]
  ): (String, SuperMarioCharacterSpeedModel) = {
    csvLine.head -> SuperMarioCharacterSpeedModel(csvLine(1).toDouble)
  }
}
