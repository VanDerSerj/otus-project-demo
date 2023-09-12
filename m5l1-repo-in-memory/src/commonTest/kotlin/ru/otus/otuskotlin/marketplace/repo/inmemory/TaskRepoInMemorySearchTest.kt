package ru.otus.otuskotlin.marketplace.repo.inmemory

import RepoTaskSearchTest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository

class TaskRepoInMemorySearchTest : RepoTaskSearchTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects
    )
}