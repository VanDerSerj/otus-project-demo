package ru.otus.otuskotlin.mrosystem.common.helpers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosError
import ru.otus.otuskotlin.mrosystem.common.models.MrosState

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
