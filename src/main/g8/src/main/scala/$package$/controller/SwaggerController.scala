package $package$.controller

import cats.effect.{Concurrent, ContextShift, Sync}
import cats.syntax.functor._
import $package$.endpoints.OpenApiEndpoints
import $package$.routes.OpenApiRoutes

object SwaggerController {
  def create[F[_]: Concurrent: ContextShift](
    openApiEndpoints: OpenApiEndpoints[F]
  )(implicit F: Sync[F]): F[Controller[F]] =
    OpenApiRoutes.create(openApiEndpoints.endpoints).map(apiRoutes => Controller.fromRoutes(apiRoutes.routes))
}
