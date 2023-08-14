package ru.otus.otuskotlin.mrosystem.biz.statemachine

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.NONE
import ru.otus.otuskotlin.mrosystem.common.models.MrosState
import ru.otus.otuskotlin.mrosystem.common.statemachine.SMTaskStates
import ru.otus.otuskotlin.mrosystem.cor.ICorChainDsl
import ru.otus.otuskotlin.mrosystem.cor.worker
import kotlin.reflect.KClass

private val machine = SMAdStateResolver()
private val clazz: KClass<*> = ICorChainDsl<MrosContext>::computeAdState::class

fun ICorChainDsl<MrosContext>.computeAdState(title: String) = worker {
    this.title = title
    this.description = "Вычисление состояния объявления"
    on { state == MrosState.RUNNING }
    handle {
        val log = settings.loggerProvider.logger(clazz)
        val timeNow = Clock.System.now()
        val task = taskValidated
        val prevState = task.taskState
        val timePublished = task.timePublished.takeIf { it != Instant.NONE } ?: timeNow
        val signal = SMAdSignal(
            state = prevState.takeIf { it != SMTaskStates.NONE } ?: SMTaskStates.NEW,
            duration = timeNow - timePublished,
            views = task.views,
        )
        val transition = machine.resolve(signal)
        if (transition.state != prevState) {
            log.info("New ad state transition: ${transition.description}")
        }
        task.taskState = transition.state
    }
}
