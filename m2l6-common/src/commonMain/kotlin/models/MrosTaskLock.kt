package ru.otus.otuskotlin.mrosystem.common.models

class MrosTaskLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MrosTaskLock("")
    }
}