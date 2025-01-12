package tg.stayfit.models

import org.jetbrains.exposed.dao.IntIdTable

object Workouts : IntIdTable() {
    val userId = integer("user_id").references(Users.id)
    val type = varchar("type", 50)
    val duration = integer("duration") // in minutes
}

data class Workout(val id: Int, val userId: Int, val type: String, val duration: Int)