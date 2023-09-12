package ru.otus.otuskotlin.marketplace.repo.inmemory

import RepoTaskDeleteTest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository

class TaskRepoInMemoryDeleteTest : RepoTaskDeleteTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects
    )
}