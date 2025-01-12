package tg.stayfit.services

import com.fitnesscoach.models.Workout
import com.fitnesscoach.models.Workouts
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.ResultRow
import org.slf4j.LoggerFactory

object WorkoutService {
    private val logger = LoggerFactory.getLogger("Workout Service")

    fun createWorkout(workout: Workout): Int {
        return transaction {
            // Validate workout data
            if (workout.userId <= 0 || workout.type.isBlank() || workout.duration <= 0) {
                logger.warn("Attempted to create workout with invalid data: $workout")
                throw IllegalArgumentException("User  ID, type, and duration must be valid.")
            }

            val id = Workouts.insert {
                it[userId] = workout.userId
                it[type] = workout.type
                it[duration] = workout.duration
            } get Workouts.id // Return the ID of the created workout

            logger.info("Workout created successfully with ID: $id for user ID: ${workout.userId}")
            id
        }
    }

    fun getWorkoutById(id: Int): Workout? {
        return transaction {
            Workouts.select { Workouts.id eq id }
                .mapNotNull { toWorkout(it) }
                .singleOrNull()
                ?.also { logger.info("Retrieved workout: $it") }
        }
    }

    private fun toWorkout(row: ResultRow): Workout {
        return Workout(row[Workouts.id].value, row[Workouts.userId], row[Workouts.type], row[Workouts.duration])
    }

    fun updateWorkout(id: Int, updatedWorkout: Workout): Boolean {
        return transaction {
            val updatedRows = Workouts.update({ Workouts.id eq id }) {
                it[userId] = updatedWorkout.userId
                it[type] = updatedWorkout.type
                it[duration] = updatedWorkout.duration
            }
            logger.info("Updated workout with ID: $id")
            updatedRows > 0
        }
    }

    fun deleteWorkout(id: Int): Boolean {
        return transaction {
            val deletedRows = Workouts.deleteWhere { Workouts.id eq id }
            logger.info("Deleted workout with ID: $id")
            deletedRows > 0
        }
    }
}