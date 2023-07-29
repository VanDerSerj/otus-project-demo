package ru.otus.otuskotlin.mrosystem.mappers.exceptions

import ru.otus.otuskotlin.mrosystem.common.models.MrosCommand

class UnknownMrosCommand(command: MrosCommand) : Throwable("Wrong command $command at mapping toTransport stage")
