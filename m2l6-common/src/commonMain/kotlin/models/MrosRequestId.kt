package ru.otus.otuskotlin.mrosystem.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MrosRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MrosRequestId("")
    }
}
