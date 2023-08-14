package ru.otus.otuskotlin.mrosystem.biz.validation

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.helpers.errorValidation
import ru.otus.otuskotlin.mrosystem.common.helpers.fail
import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskId
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { taskValidating.id != MrosTaskId.NONE && !taskValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = taskValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}