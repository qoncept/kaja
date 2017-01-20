package jp.co.qoncept.kaja

import java.util.*

fun <T> sequence(xs: List<Result<T>>): Result<List<T>> {
    return pure(xs.fold(ArrayList<T>()) { accum, x ->
        when (x) {
            is Result.Success -> {
                accum.add(x.value!!)
                accum
            }
            else -> return Result.Failure(x.exception!!)
        }
    })
}

fun <Key, Value> sequence(xs: Map<Key, Result<Value>>): Result<Map<Key, Value>> {
    return pure(xs.fold(HashMap<Key, Value>()) { accum, entry ->
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
