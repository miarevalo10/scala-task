package com.marevalo.services

import akka.http.caching.scaladsl.Cache
import akka.stream.Materializer
import com.marevalo.entities.Player.Player
import com.marevalo.models.PlayerModel
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}




class Logic (cache: Cache[String, Float])(implicit val executionContext: ExecutionContext, implicit val materializer: Materializer) {


  val SCORE_VALUE: String = "scoreValue"


  //val db = Database.forConfig("postgres")
  val db =Database.forURL("jdbc:postgresql://localhost:5432/scalaTask", "postgres", "123456")

  val players= TableQuery[PlayerModel]


  def listAll: Future[Seq[Player]] = {
    db.run(players.sortBy(_.score.desc).take(5).result)
  }

  def getPlayer(id: Int): Future[Option[Player]] = {
    db.run(players
      .filter(_.id === id)
      .result
      .headOption)
  }

  def getTeam(name: String): Future[Seq[Player]]={
    db.run(players
      .filter(_.team === name)
      .result
       )
  }

  def getTeamScore(name: String): Future[Option[Int]]={
    db.run(players.filter(_.team === name).map(_.score).avg.result)
  }

  def getWinner(team1: String, team2: String): Future[Option[String]]={

    val s1 = Await.result(getTeamScore(team1), Duration.Inf)
    val s2 = Await.result(getTeamScore(team2), Duration.Inf)

    s1 match {
      case Some(i) =>
        s2 match {
          case Some(j) => if (i > j) Future.successful(Some (s"The winner is $team1 with $i points")) else if(i==j) Future.successful(Some ("It's a tie")) else Future.successful(Some (s"The winner is $team2 with $j points "))
          case None => Future.failed(throw new Exception("Team 2 not found") )
        }
      case None =>Future.failed(throw new Exception("Team 1 not found") )
    }
  }

  def updateById(id: Int, playerForUpdate: Player): Future[Option[Player]]={
    val q = for { p <- players if p.id === id} yield (p.team, p.score)
    val updateAction = q.update(playerForUpdate.team,playerForUpdate.score)

    db.run(updateAction)

    db.run(players.filter(_.id===id).result.headOption)
  }

  def updateByName(name: String, team:String, score: Int): Future[Option[Player]]={

    val q = for { p <- players if p.name === name} yield (p.team, p.score)
    val updateAction = q.update(team, score)

    db.run(updateAction)

    db.run(players.filter(_.name === name).result.headOption)
  }


}
