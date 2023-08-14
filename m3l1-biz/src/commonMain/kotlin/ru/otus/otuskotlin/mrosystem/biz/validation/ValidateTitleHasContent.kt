package ru.otus.otuskotlin.mrosystem.biz.validation

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.helpers.errorValidation
import ru.otus.otuskotlin.mrosystem.common.helpers.fail
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { taskValidating.title.isNotEmpty() && !taskValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain leters"
            )
        )
    }
}