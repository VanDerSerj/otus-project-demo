package ru.otus.otuskotlin.marketplace.repo.inmemory

import RepoTaskReadTest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository

class TaskRepoInMemoryReadTest: RepoTaskReadTest() {
    override val repo: ITaskRepository = TaskRepoInMemory(
        initObjects = initObjects
    )
}