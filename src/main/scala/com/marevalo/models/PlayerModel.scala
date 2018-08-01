package com.marevalo.models

import com.marevalo.entities.Player.Player
import slick.jdbc.PostgresProfile.api._


class PlayerModel(tag: Tag) extends Table[Player](tag, "jugadores") {
  def id = column[Int]("id",O.PrimaryKey)
  def name = column[String]("name")

  def team = column[String]("team")
  def score = column[Int]("score", O.Default(0))

  def * = (id, name, team, score) <> (Player.tupled, Player.unapply _)
}