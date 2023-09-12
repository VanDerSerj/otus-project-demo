package ru.otus.otuskotlin.mrosystem.biz.general

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.helpers.errorAdministration
import ru.otus.otuskotlin.mrosystem.common.helpers.fail
import ru.otus.otuskotlin.mrosystem.common.models.MrosWorkMode
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker

fun ICorChainDsl<MrosContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        taskRepo = when {
            workMode == MrosWorkMode.TEST -> settings.repoTest
            workMode == MrosWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != MrosWorkMode.STUB && taskRepo == ITaskRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}