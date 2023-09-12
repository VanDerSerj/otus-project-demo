package ru.otus.otuskotlin.mrosystem.common.repo

import ru.otus.otuskotlin.mrosystem.common.models.MrosTask

data class DbTaskRequest(
    val task: MrosTask
)