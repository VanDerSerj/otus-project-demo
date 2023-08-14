package ru.otus.otuskotlin.mrosystem.biz.validation

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.helpers.errorValidation
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker
import ru.otus.otuskotlin.mrosystem.common.helpers.fail

fun ICorChainDsl<MrosContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { taskValidating.description.isNotEmpty() && !taskValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}