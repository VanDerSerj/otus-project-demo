package ru.otus.otuskotlin.marketplace.repo.inmemory

import RepoTaskUpdateTest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository

class TaskRepoInMemoryUpdateTest : RepoTaskUpdateTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}