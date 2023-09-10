import MrosTaskStubBolts.TASK_ENG_BOLT1
import MrosTaskStubBolts.TASK_MECH_BOLT1
import ru.otus.otuskotlin.mrosystem.common.models.*

object MrosTaskStub {

    fun get(): MrosTask = MrosTask(
        id = MrosTaskId("666"),
        title = "Требуется болт",
        description = "Требуется болт 100x5 с шестигранной шляпкой",
        ownerId = MrosUserId("user-1"),
        taskType = MrosPerformSide.MECHANIC,
        visibility = MrosVisibility.VISIBLE_PUBLIC,
        permissionsClient = mutableSetOf(
            MrosTaskPermissionClient.READ,
            MrosTaskPermissionClient.UPDATE,
            MrosTaskPermissionClient.DELETE,
            MrosTaskPermissionClient.MAKE_VISIBLE_PUBLIC,
            MrosTaskPermissionClient.MAKE_VISIBLE_GROUP,
            MrosTaskPermissionClient.MAKE_VISIBLE_OWNER,
        )
    )

    fun prepareResult(block: MrosTask.() -> Unit): MrosTask = get().apply(block)

    fun prepareSearchList(filter: String, type: MrosPerformSide) = listOf(
        mrosTaskMech("s-69-01", filter, type),
        mrosTaskMech("s-69-02", filter, type),
        mrosTaskMech("s-69-03", filter, type),
        mrosTaskMech("s-69-04", filter, type),
        mrosTaskMech("s-69-05", filter, type),
        mrosTaskMech("s-69-06", filter, type),
    )

    fun prepareOffersList(filter: String, type: MrosPerformSide) = listOf(
        mrosTaskEng("s-666-01", filter, type),
        mrosTaskEng("s-666-02", filter, type),
        mrosTaskEng("s-666-03", filter, type),
        mrosTaskEng("s-666-04", filter, type),
        mrosTaskEng("s-666-05", filter, type),
        mrosTaskEng("s-666-06", filter, type),
    )

    private fun mrosTaskEng(id: String, filter: String, type: MrosPerformSide) =
        mrosTask(TASK_ENG_BOLT1, id = id, filter = filter, type = type)

    private fun mrosTaskMech(id: String, filter: String, type: MrosPerformSide) =
        mrosTask(TASK_MECH_BOLT1, id = id, filter = filter, type = type)

    private fun mrosTask(base: MrosTask, id: String, filter: String, type: MrosPerformSide) = base.copy(
        id = MrosTaskId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        taskType = type
    )



}