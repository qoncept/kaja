package jp.co.qoncept.kaja

import jp.co.qoncept.kotres.Result
import jp.co.qoncept.kotres.flatMap

operator fun Result<Json, JsonException>.get(index: Int): Result<Json, JsonException> {
    return flatMap { it[index] }
}

operator fun Result<Json, JsonException>.get(key: String): Result<Json, JsonException> {
    return flatMap { it[key] }
}

val Result<Json, JsonException>.boolean: Result<Boolean, JsonException>
    get() = flatMap { it.boolean }

val Result<Json, JsonException>.int: Result<Int, JsonException>
    get() = flatMap { it.int }

val Result<Json, JsonException>.long: Result<Long, JsonException>
    get() = flatMap { it.long }

val Result<Json, JsonException>.double: Result<Double, JsonException>
    get() = flatMap { it. double}

val Result<Json, JsonException>.string: Result<String, JsonException>
    get() = flatMap { it. string}

val Result<Json, JsonException>.list: Result<List<Json>, JsonException>
    get() = flatMap { it.list }

val Result<Json, JsonException>.map: Result<Map<String, Json>, JsonException>
    get() = flatMap { it.map }

fun <T> Result<Json, JsonException>.list(decode: (Json) -> Result<T, JsonException>): Result<List<T>, JsonException> {
    return flatMap { it.list(decode) }
}

fun <T> Result<Json, JsonException>.map(decode: (Json) -> Result<T, JsonException>): Result<Map<kotlin.String, T>, JsonException> {
    return flatMap { it.map(decode) }
}

fun <T> Result<T, JsonException>.ifMissingKey(alternative: T): Result<T, JsonException> {
    return when (this) {
        is Result.Success -> this
        is Result.Failure -> when (exception) {
            is MissingKeyException -> Result.Success(alternative)
            else -> this
        }
    }
}

fun <T> Result<T, JsonException>.nullIfMissingKey(): Result<T?, JsonException> {
    return when (this) {
        is Result.Success -> Result.Success(value)
        is Result.Failure -> when (exception) {
            is MissingKeyException -> Result.Success(null)
            else -> Result.Failure(exception)
        }
    }
}
