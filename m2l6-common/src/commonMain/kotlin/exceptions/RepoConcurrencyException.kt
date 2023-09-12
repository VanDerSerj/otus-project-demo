package ru.otus.otuskotlin.mrosystem.common.exceptions

import ru.otus.otuskotlin.mrosystem.common.models.MrosTaskLock

class RepoConcurrencyException(expectedLock: MrosTaskLock, actualLock: MrosTaskLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)