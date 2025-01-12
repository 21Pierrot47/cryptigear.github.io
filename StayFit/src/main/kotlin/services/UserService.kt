package tg.stayfit.services

import tg.stayfit.models.User
import tg.stayfit.models.Users
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.ResultRow
import org.slf4j.LoggerFactory

object UserService {
    private val logger = LoggerFactory.getLogger("User Service")

    fun createUser (user: User): Int {
        return transaction {
            // Validate user data
            if (user.name.isBlank() || user.email.isBlank()) {
                logger.warn("Attempted to create user with invalid data: $user")
                throw IllegalArgumentException("User  name and email must not be empty.")
            }

            Users.insert {
                it[name] = user.name
                it[email] = user.email
            } get Users.id // Return the ID of the created user
        }
    }

    fun getUser ById(id: Int): User? {
        return transaction {
            Users.select { Users.id eq id }
                .mapNotNull { toUser (it) }
                .singleOrNull()
        }
    }

    private fun toUser (row: ResultRow): User {
        return User(row[Users.id].value, row[Users.name], row[Users.email])
    }
}