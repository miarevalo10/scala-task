package com.marevalo.entities

import play.api.libs.json.Json


object Player {

  case class Player(id: Int, name: String, team: String, score: Int)

  implicit val userWrites = Json.writes[Player]
  implicit val userReads = Json.reads[Player]

}
