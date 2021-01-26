package $package$.service.authentication

import cats.effect.Sync
import $package$.model.User
import $package$.model.authentication.JwtAuthenticationServiceDescriptor
import $package$.model.response.{ErrorResponse, UnauthorizedResponse}

trait JwtAuthenticationService[F[_]] extends AuthenticationService[F, JwtAuthenticationServiceDescriptor]

object JwtAuthenticationService {
  def create[F[_]](implicit F: Sync[F]): F[JwtAuthenticationService[F]] =
    F.delay((descriptor: JwtAuthenticationServiceDescriptor) => dummySecurity(descriptor.token))

  private def dummySecurity[F[_]](token: String)(implicit F: Sync[F]): F[Either[ErrorResponse, User]] =
    F.delay {
      if (token == "secret") Right(User("email@email.com", "admin"))
      else Left(UnauthorizedResponse())
    }
}
