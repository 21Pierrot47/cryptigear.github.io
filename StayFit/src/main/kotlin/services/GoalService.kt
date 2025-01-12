package tg.stayfit.services

import com.fitnesscoach.models.Goal
import com.fitnesscoach.models.Goals
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.ResultRow
import org.slf4j.LoggerFactory

object GoalService {
    private val logger = LoggerFactory.getLogger("Goal Service")

    fun createGoal(goal: Goal): Int {
        return transaction {
            // Validate goal data
            if (goal.userId <= 0 || goal.description.isBlank() || goal.targetDate.isBlank()) {
                logger.warn("Attempted to create goal with invalid data: $goal")
                throw IllegalArgumentException("User  ID, description, and target date must be valid.")
            }

            val id = Goals.insert {
                it[userId] = goal.userId
                it[description] = goal.description
                it[targetDate] = goal.targetDate
            } get Goals.id // Return the ID of the created goal

            logger.info("Goal created successfully with ID: $id for user ID: ${goal.userId}")
            id
        }
    }

    fun getGoalById(id: Int): Goal? {
        return transaction {
            Goals.select { Goals.id eq id }
                .mapNotNull { toGoal(it) }
                .singleOrNull()
                ?.also { logger.info("Retrieved goal: $it") }
        }
    }

    private fun toGoal(row: ResultRow): Goal {
        return Goal(row[Goals.id].value, row[Goals.userId], row[Goals.description], row[Goals.targetDate])
    }

    fun updateGoal(id: Int, updatedGoal: Goal): Boolean {
        return transaction {
            val updatedRows = Goals.update({ Goals.id eq id }) {
                it[userId] = updatedGoal.userId
                it[description] = updatedGoal.description
                it[targetDate] = updatedGoal.targetDate
            }
            logger.info("Updated goal with ID: $id")
            updatedRows > 0
        }
    }

    fun deleteGoal(id: Int): Boolean {
        return transaction {
            val deletedRows = Goals.deleteWhere { Goals.id eq id }
            logger.info("Deleted goal with ID: $id")
            deletedRows > 0
        }
    }
}