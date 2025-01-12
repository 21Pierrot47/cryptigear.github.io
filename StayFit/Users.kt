package com.fitnesscoach.models

import org.jetbrains.exposed.dao.IntIdTable

object Users : IntIdTable() {
    val name = varchar("name", 50)
    val email = varchar("email", 100).uniqueIndex()
}

data class User(val id: Int, val name: String, val email: String)