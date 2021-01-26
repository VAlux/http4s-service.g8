package $package$.endpoints

import $package$.endpoints.application.ApplicationEndpoints
import sttp.tapir.docs.openapi.RichOpenAPIServerEndpoints
import sttp.tapir.openapi.OpenAPI

class OpenApiEndpoints[F[_]](first: ApplicationEndpoints[F], remaining: ApplicationEndpoints[F]*) {
  val endpoints: OpenAPI =
    (first +: remaining).flatMap(_.asSeq()).toOpenAPI("$name$ API", "1.0")
}
