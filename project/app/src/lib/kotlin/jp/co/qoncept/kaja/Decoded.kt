package jp.co.qoncept.kaja

abstract sealed class Decoded<T> {
    class Success<T>(value: T): Decoded<T>() {
        override val value: T? = value

        override val exception: DecodeException? = null

        override fun <U> map(transform: (T) -> U): Decoded<U> {
            return pure(transform(value!!))
        }

        override fun <U> flatMap(transform: (T) -> Decoded<U>): Decoded<U>{
            return transform(value!!)
        }

        override fun <U> apply(transform: Decoded<(T) -> U>): Decoded<U> {
            return when(transform) {
                is Success -> this.map { transform.value!!(it) }
                is Failure -> Failure(transform.exception!!)
            }
        }

        override fun ifMissingKey(alternative: T): Decoded<T> {
            return this
        }

        override fun or(alternative: Decoded<T>): Decoded<T> {
            return this
        }

        override fun or(alternative: T): T {
            return value!!
        }
    }

    class Failure<T>(exception: DecodeException): Decoded<T>() {
        override val value: T? = null

        override val exception: DecodeException? = exception

        override fun <U> map(transform: (T) -> U): Decoded<U> {
            return Failure(exception!!)
        }

        override fun <U> flatMap(transform: (T) -> Decoded<U>): Decoded<U> {
            return Failure(exception!!)
        }

        override fun <U> apply(transform: Decoded<(T) -> U>): Decoded<U> {
            return when(transform) {
                is Success -> Failure(exception!!)
                is Failure -> Failure(transform.exception!!)
            }
        }

        override fun ifMissingKey(alternative: T): Decoded<T> {
            return when(exception!!) {
                is MissingKeyException -> Success(alternative)
                else -> this
            }
        }

        override fun or(alternative: Decoded<T>): Decoded<T> {
            return alternative
        }

        override fun or(alternative: T): T {
            return alternative
        }
    }
    abstract val value: T?

    abstract val exception: DecodeException?

    abstract fun or(alternative: T): T

    abstract fun or(alternative: Decoded<T>): Decoded<T>

    abstract fun ifMissingKey(alternative: T): Decoded<T>

    abstract fun <U> map(transform: (T) -> U): Decoded<U>

    abstract fun <U> flatMap(transform: (T) -> Decoded<U>): Decoded<U>

    abstract fun <U> apply(transform: Decoded<(T) -> U>): Decoded<U>
}

fun <T> Decoded<Decoded<T>>.flatten(): Decoded<T> {
    return when(this) {
        is Decoded.Success -> value!!
        is Decoded.Failure -> Decoded.Failure(exception!!)
    }
}

fun <T, U> Decoded<(T) -> U>.ap(value: T): Decoded<U> {
    return when(this) {
        is Decoded.Success -> this.map { it(value) }
        is Decoded.Failure -> Decoded.Failure(exception!!)
    }
}

fun <T> pure(value: T): Decoded<T> {
    return Decoded.Success(value)
}
