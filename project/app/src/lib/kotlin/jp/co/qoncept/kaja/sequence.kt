package jp.co.qoncept.kaja

import java.util.*

fun <T> sequence(xs: List<Decoded<T>>): Decoded<List<T>> {
    return pure(xs.fold(ArrayList<T>()) { accum, x ->
        when (x) {
            is Decoded.Success -> {
                accum.add(x.value!!)
                accum
            }
            else -> return Decoded.Failure(x.exception!!)
        }
    })
}

fun <Key, Value> sequence(xs: Map<Key, Decoded<Value>>): Decoded<Map<Key, Value>> {
    return pure(xs.fold(HashMap<Key, Value>()) { accum, entry ->
        when (entry.value) {
            is Decoded.Success -> {
                accum.put(entry.key, entry.value.value!!)
                accum
            }
            else -> return Decoded.Failure(entry.value.exception!!)
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
