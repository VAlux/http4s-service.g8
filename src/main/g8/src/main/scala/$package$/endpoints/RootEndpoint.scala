package $package$.endpoints

import $package$.model.User
import $package$.model.authentication.JwtAuthenticationServiceDescriptor
import $package$.model.response._
import $package$.service.authentication.JwtAuthenticationService
import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.PartialServerEndpoint
import sttp.tapir.{endpoint, _}

object RootEndpoint {

  val rootV1: Endpoint[Unit, ErrorResponse, Unit, Any] =
    endpoint
      .description("Main entry point of the $name$ API")
      .name("$name$ API")
      .in("api" / "v1")
      .errorOut(errorResponseMapping)

  private lazy val errorResponseMapping: EndpointOutput.OneOf[ErrorResponse, ErrorResponse] =
    oneOf[ErrorResponse](
      statusMapping(
        StatusCode.NotFound,
        jsonBody[NotFoundResponse].description("Not found")
      ),
      statusMapping(
        StatusCode.BadRequest,
        jsonBody[BadRequestResponse].description("Bad request")
      ),
      statusMapping(
        StatusCode.Unauthorized,
        jsonBody[UnauthorizedResponse].description("Unauthorized")
      ),
      statusMapping(
        StatusCode.InternalServerError,
        jsonBody[InternalErrorResponse].description("Internal server error")
      )
    )
}
