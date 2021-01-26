package $package$.shared

import java.util.UUID

import cats.effect.Sync

trait UuidGenerator[F[_]] {
  def generate: F[UUID]
}

object UuidGenerator {
  def apply[F[_]](implicit F: Sync[F]): F[UuidGenerator[F]] = F.delay {
    new UuidGenerator[F] {
      override def generate: F[UUID] = F.delay(UUID.randomUUID())
    }
  }
}
