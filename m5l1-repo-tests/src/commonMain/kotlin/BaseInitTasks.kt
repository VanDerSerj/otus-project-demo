import ru.otus.otuskotlin.mrosystem.common.models.*

abstract class BaseInitTasks(val op: String): IInitObjects<MrosTask> {

    open val lockOld: MrosTaskLock = MrosTaskLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MrosTaskLock = MrosTaskLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: MrosUserId = MrosUserId("owner-123"),
        taskType: MrosPerformSide = MrosPerformSide.ENGINEER,
        lock: MrosTaskLock = lockOld,
    ) = MrosTask(
        id = MrosTaskId("task-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = MrosVisibility.VISIBLE_TO_ENGINEER,
        taskType = taskType,
        lock = lock,
    )

}