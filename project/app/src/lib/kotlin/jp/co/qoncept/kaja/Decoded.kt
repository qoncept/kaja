package jp.co.qoncept.kaja

class Decoded<T> {
    val value: T?
    val exception: DecodeException?

    companion object {
        fun <T> success(value: T): Decoded<T> {
            return Decoded(value, null)
        }

        fun <T> failure(exception: DecodeException): Decoded<T> {
            return Decoded(null, exception)
        }
    }

    private constructor(value: T?, exception: DecodeException?) {
        this.value = value
        this.exception = exception
    }
}

fun <T> Decoded<T>.or(alternative: T): T {
    // TODO
}

fun <T> Decoded<T>.or(alternative: Decoded<T>): Decoded<T> {
    // TODO
}

fun <T> Decoded<T>.ifMissingKey(alternative: T): Decoded<T> {
    // TODO
}

fun <T, U> Decoded<T>.map(transform: (T) -> U): Decoded<U> {
    // TODO
}

fun <T, U> Decoded<T>.flatMap(transform: (T) -> Decoded<U>): Decoded<U> {
    // TODO
}

fun <T, U> Decoded<T>.apply(transform: Decoded<(T) -> U>): Decoded<U> {
    // TODO
}

fun <T> Decoded<Decoded<T>>.flatten(): Decoded<T> {
    // TODO
}

fun <T> pure(value: T): Decoded<T> {
    return Decoded.success(value)
}

fun <T, U> Decoded<(T) -> U>.ap(value: T): Decoded<U> {
    // TODO
}
