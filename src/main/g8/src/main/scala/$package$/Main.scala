package $package$

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Timer}
import cats.syntax.flatMap._
import $package$.application.HttpApplication
import $package$.config.{Configuration, ConfigurationReader}
import fs2.Stream
import org.http4s.HttpApp
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import pureconfig.ConfigSource

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  type FStream[F[_]] = Stream[F, ExitCode]

  def run(args: List[String]): IO[ExitCode] =
    for {
      config <- ConfigurationReader.create[IO].flatMap(_.loadConfiguration(ConfigSource.default))
      app <- createApplication[IO](config)
      server <- stream[IO](config, app).compile.drain.as(ExitCode.Success)
    } yield server

  private def stream[F[_]: ConcurrentEffect: Timer: ContextShift](conf: Configuration, app: HttpApp[F]): FStream[F] =
    for {
      finalHttpApp <- Stream(Logger.httpApp(logHeaders = conf.log.logHeaders, logBody = conf.log.logBody)(app))
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(conf.http.port, conf.http.host)
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode

  private def createApplication[F[_]: ConcurrentEffect: ContextShift: Timer](config: Configuration): F[HttpApp[F]] = ???

  private def createEntrypoint[F[_]: ConcurrentEffect: ContextShift: Timer](
    config: Configuration,
    applicationEffect: F[HttpApplication[F]]
  ): F[HttpApp[F]] = applicationEffect.flatMap(_.createEntrypoint(config))
}
