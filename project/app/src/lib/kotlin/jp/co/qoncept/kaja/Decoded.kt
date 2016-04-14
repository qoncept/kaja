package jp.co.qoncept.kaja

sealed class Decoded<out T> {
    class Success<T>(value: T): Decoded<T>() {
        private val _value: T = value

        override val value: T?
            get() = _value

        override val exception: DecodeException?
            get() = null

        override fun <U> map(transform: (T) -> U): Decoded<U> {
            return pure(transform(_value))
        }

        override fun <U> flatMap(transform: (T) -> Decoded<U>): Decoded<U>{
            return transform(_value)
        }

        override fun <U> apply(transform: Decoded<(T) -> U>): Decoded<U> {
            return when(transform) {
                is Success -> pure(transform._value(_value))
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
            return _value
        }
    }

    class Failure<T>(exception: DecodeException): Decoded<T>() {
        private val _exception: DecodeException = exception

        override val value: T?
            get() = null

        override val exception: DecodeException?
            get() = _exception

        override fun <U> map(transform: (T) -> U): Decoded<U> {
            return Failure(_exception)
        }

        override fun <U> flatMap(transform: (T) -> Decoded<U>): Decoded<U> {
            return Failure(_exception)
        }

        override fun <U> apply(transform: Decoded<(T) -> U>): Decoded<U> {
            return when(transform) {
                is Success -> Failure(_exception)
                is Failure -> Failure(transform._exception)
            }
        }

        override fun ifMissingKey(alternative: T): Decoded<T> {
            return when(_exception) {
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
    return this.flatMap { it }
}

fun <T, U> Decoded<(T) -> U>.ap(value: Decoded<T>): Decoded<U> {
    return value.apply(this)
}

fun <T> pure(value: T): Decoded<T> {
    return Decoded.Success(value)
}
