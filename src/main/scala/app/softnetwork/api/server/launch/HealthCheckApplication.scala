package app.softnetwork.api.server.launch

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import app.softnetwork.api.server.ApiRoutes

trait HealthCheckApplication extends Application with ApiRoutes {
  override def apiRoutes(system: ActorSystem[_]): Route = complete(StatusCodes.NotFound)
}
