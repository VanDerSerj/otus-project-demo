package ru.otus.otuskotlin.mrosystem.common.models

data class MrosTaskFilter(
    var searchString: String = "",
    var ownerId: MrosUserId = MrosUserId.NONE,
    var dealSide: MrosPerformSide = MrosPerformSide.NONE,
)
