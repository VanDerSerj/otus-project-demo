package ru.otus.otuskotlin.marketplace.repo.inmemory

import RepoTaskCreateTest

class TaskRepoInMemoryCreateTest : RepoTaskCreateTest() {
    override val repo = TaskRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}