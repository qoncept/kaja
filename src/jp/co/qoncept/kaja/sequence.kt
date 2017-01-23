package jp.co.qoncept.kaja

import java.util.*
import jp.co.qoncept.kotres.Result

fun <T> sequence(xs: List<Result<T, JsonException>>): Result<List<T>, JsonException> {
    return Result.Success(xs.fold(ArrayList<T>()) { accum, x ->
        when (x) {
            is Result.Success -> {
                accum.add(x.value!!)
                accum
            }
            else -> return Result.Failure(x.exception!!)
        }
    })
}

fun <Key, Value> sequence(xs: Map<Key, Result<Value, JsonException>>): Result<Map<Key, Value>, JsonException> {
    return Result.Success(xs.fold(HashMap<Key, Value>()) { accum, entry ->
        when (entry.value) {
            is Result.Success -> {
                accum.put(entry.key, entry.value.value!!)
                accum
            }
            else -> return Result.Failure(entry.value.exception!!)
        }
    })
}

inline fun <Key, Value, R> Map<Key, Value>.fold(initial: R, operation: (R, Map.Entry<Key, Value>) -> R): R {
    var result = initial
    for (entry in this) {
        result = operation(initial, entry)
    }
    return result
}
