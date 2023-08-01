package ru.otus.otuskotlin.mrosystem.mappers.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to MrosContext")
