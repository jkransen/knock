package services

import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import akka.stream.FlowShape
import akka.stream.stage.GraphStage
import com.opencsv.CSVParser

object CsvParsingMap {
  def create(): GraphStage[FlowShape[String, Map[String, String]]] = {
    val csvParser = new CSVParser()
    def parseLine(line: String): Array[String] = {
      csvParser.parseLine(line)
    }
    def combiner(header: Array[String], record: Array[String]) = header.zip(record).toMap
    new HeadTailMap(parseLine)(combiner)
  }
}

class HeadTailMap[A, C, B](convertLine: A => C)(combine: (C, C) => B) extends GraphStage[FlowShape[A, B]] {

  val in = Inlet[A]("Map.in")
  val out = Outlet[B]("Map.out")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      var header: Option[C] = None
      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val line = convertLine(grab(in))
          header match {
            case Some(hdr) =>
              push(out, combine(hdr, line))
            case None =>
              header = Some(line)
              pull(in)
          }
        }
      })
      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}