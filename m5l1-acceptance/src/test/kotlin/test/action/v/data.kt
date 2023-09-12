package test.action.v

import ru.otus.api.models.*

val debug = TaskDebug(mode = TaskRequestDebugMode.STUB, stub = TaskRequestDebugStubs.SUCCESS)
val prod = TaskDebug(mode = TaskRequestDebugMode.PROD)

val someCreateTask = TaskCreateObject(
    title = "Требуется заменить болт",
    description = "Требуется замена болта 100x5 с шестигранной шляпкой",
    taskType = PerformSide.ENGINEER,
    visibility = TaskVisibility.PUBLIC
)