package com.marevalo

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.Cache
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.marevalo.routing.Routes
import com.marevalo.services.Logic
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    /**
      * 1. Instantiation of Akka-Http Cache, LfuCache
      * 2. By Default Caching Strategy Provided by Akka-Http is Least Frequently Used
      * 3. For customizing additional setting like time to live etc
      * we can use CachingSettings class and configure it for our requirement
      */
    val cache: Cache[String, Float] = LfuCache[String, Float]
    val computeCart = new Logic(cache)
    val userRoutes: Routes = new Routes(cache, computeCart)

    /**
      * Setting up Akka-Http Server and binding the routes
      */
    val config = ConfigFactory.load("settings.properties")
    val hostname = config.getString("http.host")
    val port = config.getInt("http.port")
    val server = Http().bindAndHandle(userRoutes.routes, hostname, port)
    println(s"Listening on $hostname:$port")
    println("Http server started!")
    StdIn.readLine()

    server.flatMap(_.unbind)
    system.terminate()
    println("Http server terminated!")
  }
}
