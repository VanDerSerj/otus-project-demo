package ru.otus.otuskotlin.mrosystem.common

import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository
import ru.otus.otuskotlin.mrosystem.logging.common.MpLoggerProvider

data class MrosCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val repoStub: ITaskRepository = ITaskRepository.NONE,
    val repoTest: ITaskRepository = ITaskRepository.NONE,
    val repoProd: ITaskRepository = ITaskRepository.NONE,
) {
    companion object {
        val NONE = MrosCorSettings()
    }
}
