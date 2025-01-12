package tg.stayfit.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.request.*
import com.fitnesscoach.models.Goal
import com.fitnesscoach.services.GoalService
import org.slf4j.LoggerFactory

fun Route.goalRoutes() {
    val logger = LoggerFactory.getLogger("GoalRoutes")

    route("/goals") {
        post {
            val goal = call.receive<Goal>()
            // Validate the goal object
            if (goal.description.isBlank() || goal.targetDate.isBlank()) {
                logger.warn("Invalid goal data: $goal")
                call.respond(HttpStatusCode.BadRequest, "Description and target date must not be empty.")
                return@post
            }

            GoalService.createGoal(goal)
            call.respond(HttpStatusCode.Created, "Goal created successfully.")
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val goal = GoalService.getGoalById(id)
                if (goal != null) {
                    call.respond(goal)
                } else {
                    logger.warn("Goal not found: id=$id")
                    call.respond(HttpStatusCode.NotFound, "Goal not found.")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid goal ID.")
            }
        }

        // Additional route for updating a goal
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val updatedGoal = call.receive<Goal>()
                if (updatedGoal.description.isBlank() || updatedGoal.targetDate.isBlank()) {
                    logger.warn("Invalid goal data for update: $updatedGoal")
                    call.respond(HttpStatusCode.BadRequest, "Description and target date must not be empty.")
                    return@put
                }

                val success = GoalService.updateGoal(id, updatedGoal)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Goal updated successfully.")
                } else {
                    logger.warn("Goal not found for update: id=$id")
                    call.respond(HttpStatusCode.NotFound, "Goal not found.")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid goal ID.")
            }
        }

        // Additional route for deleting a goal
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val success = GoalService.deleteGoal(id)
                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    logger.warn("Goal not found for deletion: id=$id")
                    call.respond(HttpStatusCode.NotFound, "Goal not found.")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid goal ID.")
            }
        }
    }
}