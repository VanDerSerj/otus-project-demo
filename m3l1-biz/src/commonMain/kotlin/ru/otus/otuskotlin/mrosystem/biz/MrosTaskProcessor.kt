package ru.otus.otuskotlin.mrosystem.biz

import ru.otus.otuskotlin.mrosystem.common.MrosContext

class MrosTaskProcessor {
    suspend fun exec(ctx: MrosContext) {
        ctx.taskResponse = MrosTaskStub.get()
    }
}