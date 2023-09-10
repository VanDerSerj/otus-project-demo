package ru.otus.otuskotlin.mrosystem.common.models

data class MrosError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: Level = Level.ERROR,
    val exception: Throwable? = null,
) {
    enum class Level {
        ERROR, WARN, INFO
    }
}
