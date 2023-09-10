package ru.otus.otuskotlin.mrosystem.common.models

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.mrosystem.common.statemachine.SMTaskStates
import ru.otus.otuskotlin.mrosystem.common.NONE

data class MrosTask(
    var id: MrosTaskId = MrosTaskId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MrosUserId = MrosUserId.NONE,
    var taskType: MrosPerformSide = MrosPerformSide.NONE,
    var visibility: MrosVisibility = MrosVisibility.NONE,
    var productId: MrosProductId = MrosProductId.NONE,
    var taskState: SMTaskStates = SMTaskStates.NONE,
    var views: Int = 0,
    var timePublished: Instant = Instant.NONE,
    var timeUpdated: Instant = Instant.NONE,
    val permissionsClient: MutableSet<MrosTaskPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): MrosTask = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )
}
