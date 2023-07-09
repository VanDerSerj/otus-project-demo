package ru.otus.otuskotlin.mrosystem.common.helpers

import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.common.models.MrosCommand


fun MrosContext.isUpdatableCommand() =
    this.command in listOf(MrosCommand.CREATE, MrosCommand.UPDATE, MrosCommand.DELETE)
