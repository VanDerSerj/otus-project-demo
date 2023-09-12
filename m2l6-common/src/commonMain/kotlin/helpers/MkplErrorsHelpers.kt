package ru.otus.otuskotlin.mrosystem.common.helpers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.mrosystem.common.models.MrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskLock

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

fun MrosContext.fail(error: MrosError) {
    addError(error)
    state = MrosState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: MrosError.Level = MrosError.Level.ERROR,
) = MrosError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: MrosError.Level = MrosError.Level.ERROR,
) = MrosError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)

fun errorRepoConcurrency(
    expectedLock: MrosTaskLock,
    actualLock: MrosTaskLock?,
    exception: Exception? = null,
) = MrosError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

val errorNotFound = MrosError(
    field = "id",
    message = "Not Found",
    code = "not-found"
)

val errorEmptyId = MrosError(
    field = "id",
    message = "Id must not be null or blank"
)
