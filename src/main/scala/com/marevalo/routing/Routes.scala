package com.marevalo.routing

import akka.http.caching.scaladsl.Cache
import akka.http.scaladsl.server.Directives.{complete, path, _}
import akka.http.scaladsl.server.Route
import com.marevalo.entities.Player.Player
import com.marevalo.services.Logic
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

/**
  *
  * @param cache
  * @param logic
  */

class Routes(cache: Cache[String, Float], logic: Logic) extends PlayJsonSupport {

  val routes = getTeam ~ getTeamScore ~ getWinner ~ getPlayer ~ updatePlayerById ~ updatePlayerByName

  def getPlayer: Route =
    path("getPlayer"/) {
      parameters('id) { id: String =>
        complete(logic.getPlayer(id.toInt))
      }
    }

  /*
   * Obtiene el equipo con todos sus jugadores dado el nombre
   */
  def getTeam: Route =
      path("getTeam"/) {
        parameters('name) { name: String =>
          complete(logic.getTeam(name))
        }
      }

  /*
   * Obtiene el puntaje del equipo dado el nombre
   */
  def getTeamScore: Route ={
    path("teamScore"/) {
      parameters('name) { name: String =>
        complete(logic.getTeamScore(name))
      }
    }
  }

  /*
   * Obtiene el ganador del partido dado el nombre de los dos equipos
   */
  def getWinner: Route = {
    path("getWinner"/) {
      parameters('team1, 'team2) { (team1: String, team2:String) =>
        complete(logic.getWinner(team1,team2))
      }
    }
  }

  /*
   * Actualiza el equipo y puntaje de un jugador dado su id
   */
  def updatePlayerById: Route =
    pathPrefix(IntNumber) { id =>
      put {
        entity(as[Player]) { playerForUpdate =>
          complete {
            logic.updateById(id, playerForUpdate)
          }
        }
      }
    }

  /*
   * Actualiza el equipo y puntaje de un jugador dado su nombre
   */
  def updatePlayerByName: Route =
    path("updatePlayer") {
      put {
        parameters('name.as[String], 'team.as[String], 'score.as[Int]) { (name,team,score) =>
          complete {
            logic.updateByName(name,team,score)
          }
        }
      }
    }

}