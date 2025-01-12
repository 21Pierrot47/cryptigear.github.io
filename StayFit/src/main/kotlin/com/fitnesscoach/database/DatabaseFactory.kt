package tg.stayfit.database

import com.typesafe.config.Config
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.fitnesscoach.models.Users
import com.fitnesscoach.models.Workouts
import com.fitnesscoach.models.Goals

object DatabaseFactory {
    fun init(config: Config) {
        val dbUrl = config.getString("db.url")
        val dbUser  = config.getString("db.user")
        val dbPassword = config.getString("db.password")

        Database.connect(url = dbUrl, driver = "org.h2.Driver", user = dbUser , password = dbPassword)
        transaction {
            SchemaUtils.create(Users, Workouts, Goals)
        }
    }
}