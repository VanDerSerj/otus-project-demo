package ru.otus.otuskotlin.mrosystem.biz.statemachine

import ru.otus.otuskotlin.mrosystem.common.statemachine.SMTaskStates
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

class SMAdStateTest {

    @Test
    fun new2actual() {
        val machine = SMAdStateResolver()
        val signal = SMAdSignal(
            state = SMTaskStates.NEW,
            duration = 4.days,
            views = 20,
        )
        val transition = machine.resolve(signal)
        assertEquals(SMTaskStates.ACTUAL, transition.state)
        assertContains(transition.description, "актуальное", ignoreCase = true)
    }

    @Test
    fun new2hit() {
        val machine = SMAdStateResolver()
        val signal = SMAdSignal(
            state = SMTaskStates.NEW,
            duration = 2.days,
            views = 101,
        )
        val transition = machine.resolve(signal)
        assertEquals(SMTaskStates.HIT, transition.state)
        assertContains(transition.description, "Очень", ignoreCase = true)
    }
}