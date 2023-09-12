package ru.otus.otuskotlin.marketplace.backend.repo.sql

import RepoTaskCreateTest
import RepoTaskDeleteTest
import RepoTaskReadTest
import RepoTaskSearchTest
import RepoTaskUpdateTest
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository

class RepoTaskSQLCreateTest : RepoTaskCreateTest() {
    override val repo: ITaskRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoTaskSQLDeleteTest : RepoTaskDeleteTest() {
    override val repo: ITaskRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoTaskSQLReadTest : RepoTaskReadTest() {
    override val repo: ITaskRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoTaskSQLSearchTest : RepoTaskSearchTest() {
    override val repo: ITaskRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoTaskSQLUpdateTest : RepoTaskUpdateTest() {
    override val repo: ITaskRepository = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}