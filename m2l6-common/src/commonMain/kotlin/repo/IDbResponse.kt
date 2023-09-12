package ru.otus.otuskotlin.mrosystem.common.repo

import ru.otus.otuskotlin.mrosystem.common.models.MrosError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<MrosError>

}