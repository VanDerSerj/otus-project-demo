import ru.otus.otuskotlin.mrosystem.common.models.*

object MrosTaskStubBolts {

    val TASK_ENG_BOLT1: MrosTask
        get() = MrosTask(
            id = MrosTaskId("69"),
            title = "Замена болтов",
            description = "Выполнить замену болтов",
            ownerId = MrosUserId("user-1"),
            taskType = MrosPerformSide.ENGINEER,
            visibility = MrosVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                MrosTaskPermissionClient.READ,
                MrosTaskPermissionClient.UPDATE,
                MrosTaskPermissionClient.DELETE,
                MrosTaskPermissionClient.MAKE_VISIBLE_PUBLIC,
                MrosTaskPermissionClient.MAKE_VISIBLE_GROUP,
                MrosTaskPermissionClient.MAKE_VISIBLE_OWNER
            )
        )
    val TASK_MECH_BOLT1: MrosTask = TASK_ENG_BOLT1.copy(taskType = MrosPerformSide.MECHANIC)


}