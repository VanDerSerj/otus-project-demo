package ru.otus.otuskotlin.mrosystem.common.repo

import ru.otus.otuskotlin.mrosystem.common.models.MrosPerformSide
import ru.otus.otuskotlin.mrosystem.common.models.MrosUserId

data class DbTaskFilterRequest(
    val titleFilter: String = "",
    val ownerId: MrosUserId = MrosUserId.NONE,
    val performSide: MrosPerformSide = MrosPerformSide.NONE,
)