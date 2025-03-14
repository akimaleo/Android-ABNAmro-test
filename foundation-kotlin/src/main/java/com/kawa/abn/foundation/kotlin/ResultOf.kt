package com.kawa.abn.foundation.kotlin

/**
 * Wrapper that allows for two different types to be returned.
 * The ResultOf contains either a Success (with a value of type R) or an Error (with a value of type E)
 * This wrapper offers extra flexibility than Kotlin's [Result] and allows for custom error typing, rather than relying on [Throwable]
 */
sealed class ResultOf<out R, out E> {
    data class Success<R>(val value: R) : ResultOf<R, Nothing>()
    data class Error<E>(val error: E) : ResultOf<Nothing, E>()
}

fun <R, E> ResultOf<R, E>.isSuccess(): Boolean = this is ResultOf.Success
fun <R, E> ResultOf<R, E>.isError(): Boolean = this is ResultOf.Error

fun <R, E> ResultOf<R, E>.getSuccessValueOrNull() = (this as? ResultOf.Success)?.value
fun <R, E> ResultOf<R, E>.getErrorValueOrNull() = (this as? ResultOf.Error)?.error

fun <R> R.success() = ResultOf.Success(this)
fun <L> L.error() = ResultOf.Error(this)

fun <R, E, R1> ResultOf<R, E>.map(f: (R) -> R1): ResultOf<R1, E> = when (this) {
    is ResultOf.Success -> f(value).success()
    is ResultOf.Error -> error.error()
}

fun <W, T, F> ResultOf<List<W>, F>.mapList(map: (W) -> T): ResultOf<List<T>, F> = when (this) {
    is ResultOf.Success -> value.map { map(it) }.success()
    is ResultOf.Error -> error.error()
}

suspend fun <T, R, E> ResultOf<T, E>.mapSuspend(transform: suspend (T) -> R): ResultOf<R, E> {
    return when (this) {
        is ResultOf.Success -> transform(value).success()
        is ResultOf.Error -> error.error()
    }
}

fun <R, E, E1> ResultOf<R, E>.mapError(f: (E) -> E1): ResultOf<R, E1> = when (this) {
    is ResultOf.Success -> value.success()
    is ResultOf.Error -> f(error).error()
}

inline fun <R, E, R1> ResultOf<R, E>.flatMap(f: (R) -> ResultOf<R1, E>): ResultOf<R1, E> =
    when (this) {
        is ResultOf.Success -> f(value)
        is ResultOf.Error -> error.error()
    }

fun <R, E> ResultOf<R, E>.successOrElse(block: () -> R): R {
    return if (this is ResultOf.Success) {
        value
    } else block()
}

inline fun <R, E> ResultOf<R, E>.onSuccess(action: (R) -> Unit): ResultOf<R, E> {
    return apply {
        if (this is ResultOf.Success) action(value)
    }
}

inline fun <R, E> ResultOf<R, E>.onError(action: (E) -> Unit): ResultOf<R, E> {
    return apply { if (this is ResultOf.Error) action(this.error) }
}

inline fun <R, E, R1> ResultOf<R, E>.fold(onSuccess: (R) -> R1, onError: (E) -> R1): R1 =
    when (this) {
        is ResultOf.Success -> onSuccess(value)
        is ResultOf.Error -> onError(error)
    }

inline fun <R> catching(f: () -> R): ResultOf<R, Throwable> = try {
    f().success()
} catch (error: Throwable) {
    error.error()
}

fun <T> Result<T>.toResultOf(): ResultOf<T, Throwable> {
    return getOrNull()?.success() ?: exceptionOrNull()?.error() ?: run {
        IllegalStateException("Response body is null").error()
    }
}
