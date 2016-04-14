package jp.co.qoncept.kaja

public fun <T> sequence(xs: List<Decoded<T>>): Decoded<ArrayList<T>> {
    var accum = ArrayList<Decoded<T>>()

    for (x in xs) {
        when x {
            is Decoded.Success -> accum.put(x.value)
            else -> return x
        }
    }

    return pure(accum)
}

public fun <Key, Value> sequence(xs: Map<Key, Decoded<Value>>): Decoded<HashMap<Key, Value>> {
    var accum = HashMap<Key, Value>

    for (key, x) in xs {
        when x {
            is Decoded.Success -> accum.put(key, value)
            else -> return x
        }
    }

    return pure(accum)
}
