package jp.co.qoncept.kaja

abstract sealed class Decoded<T> {
    class Success<T>(value: T): Decoded<T>() {
        // TODO
    }

    class Failure<T>(exception: DecodeException): Decoded<T>() {
        // TODO
    }

    abstract val value: T?
    abstract val exception: DecodeException?

    abstract fun <T> or(alternative: T): T

    abstract fun <T> or(alternative: Decoded<T>): Decoded<T>

    abstract fun <T> ifMissingKey(alternative: T): Decoded<T>

    abstract fun <T, U> map(transform: (T) -> U): Decoded<U>

    abstract fun <T, U> flatMap(transform: (T) -> Decoded<U>): Decoded<U>

    abstract fun <T, U> apply(transform: Decoded<(T) -> U>): Decoded<U>
}

fun <T> Decoded<Decoded<T>>.flatten(): Decoded<T> {
    // TODO
}

fun <T, U> Decoded<(T) -> U>.ap(value: T): Decoded<U> {
    // TODO
}

fun <T> pure(value: T): Decoded<T> {
    return Decoded.success(value)
}
