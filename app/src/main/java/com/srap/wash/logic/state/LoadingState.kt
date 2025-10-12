package com.srap.wash.logic.state

sealed class LoadingState<out T> : State() {
    data class Success<out T>(val response: T) : LoadingState<T>()
    data class Error(val errMsg: String) : LoadingState<Nothing>()
}
