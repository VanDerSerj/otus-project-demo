package ru.otus.otuskotlin.mrosystem.biz.general

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.models.MrosWorkMode
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != MrosWorkMode.STUB }
    handle {
        taskResponse = taskRepoDone
        tasksResponse = tasksRepoDone
        state = when (val st = state) {
            MrosState.RUNNING -> MrosState.FINISHING
            else -> st
        }
    }
}