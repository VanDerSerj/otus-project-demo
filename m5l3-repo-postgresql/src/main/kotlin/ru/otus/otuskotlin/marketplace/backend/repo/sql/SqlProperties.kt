package ru.otus.otuskotlin.marketplace.backend.repo.sql

open class SqlProperties (
    val url: String = "jdbc:postgresql://localhost:5432/marketplace",
    val user: String = "postgres",
    val password: String = "mrosystem-pass",
    val schema: String = "mrosystem",
    // Delete tables at startup - needed for testing
    val dropDatabase: Boolean = false,
)