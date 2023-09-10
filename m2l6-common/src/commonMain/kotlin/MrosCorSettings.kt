package ru.otus.otuskotlin.mrosystem.common

import ru.otus.otuskotlin.mrosystem.logging.common.MpLoggerProvider

data class MrosCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
) {
    companion object {
        val NONE = MrosCorSettings()
    }
}
