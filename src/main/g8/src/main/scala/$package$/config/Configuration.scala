package $package$.config

case class Configuration(http: HttpBaseConfig, log: LoggingBaseConfig)

final case class HttpBaseConfig(host: String, port: Int)
final case class LoggingBaseConfig(logBody: Boolean, logHeaders: Boolean)

object Configuration {
  val default: Configuration =
    Configuration(
      HttpBaseConfig("0.0.0.0", 8080),
      LoggingBaseConfig(logBody = true, logHeaders = true),
    )
}
