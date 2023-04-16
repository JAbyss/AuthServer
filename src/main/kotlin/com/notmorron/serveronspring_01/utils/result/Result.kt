package com.notmorron.utils.result

class Result<V>(
    val value: V? = null,
    val status: Status
){
    inline fun onFailure(action: () -> Unit): Result<V> {
        when(status){
            is Error -> {
                action()
            }
        }
        return this
    }
    inline fun onSuccess(action: (V) -> Unit): Result<V> {
        when(status){
            is Success -> {
                action(value!!)
            }
        }
        return this
    }
}

open class Status()

class Success(val value: Any = Unit) : Status()

class Error(
    val description: String = "",
    val code: Int? = null
) : Status()
