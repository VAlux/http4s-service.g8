package $package$.application

import $package$.config.Configuration
import org.http4s.HttpApp

trait HttpApplication[F[_]] {
  def createEntrypoint(config: Configuration): F[HttpApp[F]]
}
