package ru.otus.otuskotlin.mrosystem.biz.statemachine

import ru.otus.otuskotlin.mrosystem.common.statemachine.SMTaskStates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class SMAdStateResolver {
    fun resolve(signal: SMAdSignal): SMTransition {
        require(signal.duration >= 0.milliseconds) { "Publication duration cannot be negative" }
        require(signal.views >= 0) { "View count cannot be negative" }
        val sig = Sig(
            st = signal.state,
            dur = SMDurs.values().first { signal.duration >= it.min && signal.duration < it.max },
            vws = SMViews.values().first { signal.views >= it.min && signal.views < it.max },
        )

        return TR_MX[sig] ?: TR_ERROR
    }

    companion object {
        private enum class SMDurs(val min: Duration, val max: Duration) {
            D_NEW(0.seconds, 3.days),
            D_ACT(3.days, 14.days),
            D_OLD(14.days, Int.MAX_VALUE.seconds),
        }
        private enum class SMViews(val min: Int, val max: Int) { FEW(0, 30), MODER(30, 100), LARGE(100, Int.MAX_VALUE) }
        private data class Sig(
            val st: SMTaskStates,
            val dur: SMDurs,
            val vws: SMViews,
        )

        private val TR_MX = mapOf(
            Sig(SMTaskStates.NEW, SMDurs.D_NEW, SMViews.FEW) to SMTransition(SMTaskStates.NEW, "Новое без изменений"),
            Sig(SMTaskStates.NEW, SMDurs.D_ACT, SMViews.FEW) to SMTransition(SMTaskStates.ACTUAL, "Вышло время, перевод из нового в актуальное"),
            Sig(SMTaskStates.NEW, SMDurs.D_NEW, SMViews.MODER) to SMTransition(
                SMTaskStates.HIT,
                "Много просмотров, стало хитом"
            ),
            Sig(SMTaskStates.NEW, SMDurs.D_NEW, SMViews.LARGE) to SMTransition(
                SMTaskStates.HIT,
                "Очень много просмотров, стало хитом"
            ),
            Sig(SMTaskStates.HIT, SMDurs.D_NEW, SMViews.MODER) to SMTransition(SMTaskStates.HIT, "Остается хитом"),
            Sig(SMTaskStates.HIT, SMDurs.D_ACT, SMViews.MODER) to SMTransition(
                SMTaskStates.ACTUAL,
                "Время вышло, хит утих, становится актуальным"
            ),
            Sig(SMTaskStates.HIT, SMDurs.D_ACT, SMViews.LARGE) to SMTransition(
                SMTaskStates.ACTUAL,
                "Время вышло, хит становится популярным"
            ),
            Sig(SMTaskStates.NEW, SMDurs.D_OLD, SMViews.FEW) to SMTransition(
                SMTaskStates.OLD,
                "Устарело, просмотров мало, непопулярное и старое объявление"
            ),
        )
        private val TR_ERROR = SMTransition(SMTaskStates.ERROR, "Unprovided transition occurred")
    }
}