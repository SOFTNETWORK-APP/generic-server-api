package app.softnetwork.api.server.scalatest

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorSystem
import app.softnetwork.concurrent.Completion
import app.softnetwork.persistence.typed._
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}

trait MockServer extends Completion with StrictLogging {

  def name: String

  implicit def system: ActorSystem[_]

  implicit lazy val ec: ExecutionContext = system.executionContext

  protected def start(): Boolean

  protected def stop(): Future[Done]

  final def initMockServer(): Boolean = {
    val started = start()
    if (started) {
      logger.info(s"Mock Server $name started")
      implicit val classicSystem: _root_.akka.actor.ActorSystem = system
      val shutdown = CoordinatedShutdown(classicSystem)
      shutdown.addTask(
        CoordinatedShutdown.PhaseServiceRequestsDone,
        s"$name-graceful-terminate"
      ) { () =>
        logger.info(s"Stopping Mock Server $name ...")
        stop()
      }
    }
    started
  }

}
