package tg.stayfit

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory
import com.fitnesscoach.database.DatabaseFactory
import com.fitnesscoach.routes.userRoutes
import com.fitnesscoach.routes.workoutRoutes
import com.fitnesscoach.routes.goalRoutes
import com.fitnesscoach.config.Config

fun main() {
    embeddedServer(Netty, port = getPort(), module = Application::module).start(wait = true)
}

fun Application.module() {
    // Load configuration
    val config = Config.load()

    // Initialize the database
    DatabaseFactory.init(config)

    // Install features
    install(ContentNegotiation) {
        gson { }
    }

    // Install StatusPages for error handling
    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
            logger.error("Unhandled exception: ${cause.localizedMessage}", cause)
        }
    }

    // Define routing
    routing {
        healthCheckRoute() // Health check endpoint
        userRoutes()
        workoutRoutes()
        goalRoutes()
    }
}

// Function to get the port from environment variables or default to 8080
private fun getPort(): Int {
    return System.getenv("PORT")?.toInt() ?: 8080
}

// Health check route
fun Route.healthCheckRoute() {
    get("/health") {
        call.respond(HttpStatusCode.OK, "Server is running")
    }
}

// Optional: Add a logger for better debugging
private val logger = LoggerFactory.getLogger("Application")