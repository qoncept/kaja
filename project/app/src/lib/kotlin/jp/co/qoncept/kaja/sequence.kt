package jp.co.qoncept.kaja

public fun <T> sequence(xs: List<Decoded<T>>): Decoded<List<T>> {
    var accum = List<Decoded<T>>()

    for (x in xs) {
        when x {
            is Decoded.Success -> accum.put(x.value)
            else -> return x
        }
    }

    return pure(accum)
}

public fun <Key, Value> sequence(xs: Map<Key, Decoded<Value>>): Decoded<Map, Value> {
    var accum = Map<Key, Value>

    for (key, x) in xs {
        when x {
            is Decoded.Success -> accum.put(key, value)
            else -> return x
        }
    }

    pure(accum)
}
