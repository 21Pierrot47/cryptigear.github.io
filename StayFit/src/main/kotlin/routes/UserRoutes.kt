package tg.stayfit.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.request.*
import tg.stayfit.models.User
import tg.stayfit.services.UserService

fun Route.userRoutes() {
    route("/users") {
        post {
            val user = call.receive<User>()
            UserService.createUser (user)
            call.respond(HttpStatusCode.Created)
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val user = UserService.getUser ById(id)
                if (user != null) {
                    call.respond(user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}