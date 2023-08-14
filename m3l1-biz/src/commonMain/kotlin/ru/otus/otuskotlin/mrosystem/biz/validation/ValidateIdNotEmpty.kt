package ru.otus.otuskotlin.mrosystem.biz.validation

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.helpers.errorValidation
import ru.otus.otuskotlin.mrosystem.common.helpers.fail
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}