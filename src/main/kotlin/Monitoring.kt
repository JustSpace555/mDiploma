import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import org.slf4j.LoggerFactory

fun Application.configureMonitoring() {

    (LoggerFactory.getILoggerFactory() as LoggerContext)
        .getLogger(Logger.ROOT_LOGGER_NAME)
        .level = Level.ALL

    install(CallLogging)
}
