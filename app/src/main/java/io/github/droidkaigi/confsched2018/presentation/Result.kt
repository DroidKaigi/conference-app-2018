package io.github.droidkaigi.confsched2018.presentation

sealed class Result<T>(val inProgress: Boolean) {

    class InProgress<T> : Result<T>(true)
    class Success<T>(var data: T) : Result<T>(false)
    class Failure<T>(val errorMessage: String?, val e: Throwable) : Result<T>(false)
    companion object {
        fun <T> inProgress(): Result<T> {
            return InProgress<T>()
        }

        fun <T> success(data: T): Result<T> {
            return Success<T>(data)
        }

        fun <T> failure(errorMessage: String, e: Throwable): Result<T> {
            return Failure<T>(errorMessage, e)
        }
    }

}