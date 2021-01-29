package $package$.model.authentication

sealed trait AuthenticationServiceDescriptor

$if(add-basic-authentication.truthy)$
case class BasicAuthenticationServiceDescriptor(username: String, password: String)
    extends AuthenticationServiceDescriptor
$endif$

$if(add-oauth-authentication.truthy)$
case class JwtAuthenticationServiceDescriptor(token: String) extends AuthenticationServiceDescriptor
$endif$