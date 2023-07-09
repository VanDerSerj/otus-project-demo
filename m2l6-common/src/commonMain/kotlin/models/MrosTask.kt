package ru.otus.otuskotlin.mrosystem.common.models

data class MrosTask(
    var id: MrosTaskId = MrosTaskId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MrosUserId = MrosUserId.NONE,
    var taskType: MrosPerformSide = MrosPerformSide.NONE,
    var visibility: MrosVisibility = MrosVisibility.NONE,
    var productId: MrosProductId = MrosProductId.NONE,
    val permissionsClient: MutableSet<MrosTaskPermissionClient> = mutableSetOf()
)
