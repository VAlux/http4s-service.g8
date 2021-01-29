package $package$.endpoints

import $package$.model.User
import $package$.model.authentication.AuthenticationServiceDescriptor
$if(add-oauth-authentication.truthy)$
import $package$.model.authentication.JwtAuthenticationServiceDescriptor
$endif$
$if(add-basic-authentication.truthy)$
import $package$.model.authentication.BasicAuthenticationServiceDescriptor
$endif$
import $package$.model.response._
import $package$.service.authentication.AuthenticationService
import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.PartialServerEndpoint
import sttp.tapir.{endpoint, _}
import sttp.tapir.model.UsernamePassword

object RootEndpoint {

  val rootV1: Endpoint[Unit, ErrorResponse, Unit, Any] =
    endpoint
      .description("Main entry point of the $name$ API")
      .name("$name$ API")
      .in("api" / "v1")
      .errorOut(errorResponseMapping)

  $if(add-basic-authentication.truthy || add-oauth-authentication.truthy)$
  def secureRootV1[F[_]](
    authenticationService: AuthenticationService[F, AuthenticationServiceDescriptor]
  ): PartialServerEndpoint[User, Unit, ErrorResponse, Unit, Any, F] =
    rootV1
    $if(add-oauth-authentication.truthy)$
      .in(auth.bearer[String])
      .serverLogicForCurrent(token => 
        authenticationService.authenticate(JwtAuthenticationServiceDescriptor(token)))
    $elseif(add-basic-authentication.truthy)$
      .in(auth.basic[UsernamePassword])
      .serverLogicForCurrent(usernamePassword => 
        authenticationService.authenticate(BasicAuthenticationServiceDescriptor(usernamePassword)))
    $endif$
  $endif$


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
