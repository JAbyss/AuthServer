package com.notmorron.utils.result

import kotlin.Error
import kotlin.Result

//suspend fun <V> mTransaction(action: suspend () -> V): Result<V> {
//    return try {
//        if (action() != null)
//            Result(value = action(), Success())
//        else
//            Result(value = null, Error(description = "Null"))
//    } catch (e: Exception) {
//        Result(value = null, Error(description = e.message ?: ""))
//    }
//}

suspend fun <V> mTransaction(action: suspend () -> V): Result<V> = runCatching {
    return if (action() != null)
        Result.success(action())
    else
        Result.failure(Error())
}