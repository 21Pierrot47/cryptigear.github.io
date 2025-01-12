package tg.stayfith.models

import org.jetbrains.exposed.dao.IntIdTable

object Goals : IntIdTable() {
    val userId = integer("user_id").references(Users.id)
    val description = varchar("description", 255)
    val targetDate = varchar("target_date", 10) // YYYY-MM-DD format
}

data class Goal(val id: Int, val userId: Int, val description: String, val targetDate: String)