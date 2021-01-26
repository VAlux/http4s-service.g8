package $package$.service.authentication

import $package$.model.User
import $package$.model.authentication.AuthenticationServiceDescriptor
import $package$.model.response.ErrorResponse

trait AuthenticationService[F[_], Descriptor <: AuthenticationServiceDescriptor] {
  def authenticate(descriptor: Descriptor): F[Either[ErrorResponse, User]]
}
