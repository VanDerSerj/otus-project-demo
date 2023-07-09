package ru.otus.otuskotlin.mrosystem.common.helpers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosError

fun Throwable.asMrosError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MrosError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun MrosContext.addError(vararg error: MrosError) = errors.addAll(error)
