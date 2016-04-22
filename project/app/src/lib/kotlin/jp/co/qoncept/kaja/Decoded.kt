package jp.co.qoncept.kaja

sealed class Decoded<T> {
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

        override fun nullIfMissingKey(): Decoded<T?> {
            return pure(_value)
        }

        override fun or(alternative: Decoded<T>): Decoded<T> {
            return this
        }

        override fun or(alternative: T): T {
            return _value
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Decoded.Success<*>) return false

            return _value == other._value
        }

        override fun hashCode(): Int {
            return _value?.hashCode() ?: 0
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

        override fun nullIfMissingKey(): Decoded<T?> {
            return when(_exception) {
                is MissingKeyException -> Success(null)
                else -> Failure(_exception)
            }
        }

        override fun or(alternative: Decoded<T>): Decoded<T> {
            return alternative
        }

        override fun or(alternative: T): T {
            return alternative
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Decoded.Failure<*>) return false

            return _exception == other._exception
        }

        override fun hashCode(): Int {
            return _exception.hashCode()
        }
    }
    abstract val value: T?

    abstract val exception: DecodeException?

    abstract fun or(alternative: T): T

    abstract fun or(alternative: Decoded<T>): Decoded<T>

    abstract fun ifMissingKey(alternative: T): Decoded<T>

    abstract fun nullIfMissingKey(): Decoded<T?>

    abstract fun <U> map(transform: (T) -> U): Decoded<U>

    abstract fun <U> flatMap(transform: (T) -> Decoded<U>): Decoded<U>

    abstract fun <U> apply(transform: Decoded<(T) -> U>): Decoded<U>
}

fun <T> Decoded<Decoded<T>>.flatten(): Decoded<T> {
    return this.flatMap { it }
}

infix fun <T, U> Decoded<(T) -> U>.ap(value: Decoded<T>): Decoded<U> {
    return value.apply(this)
}

fun <T> pure(value: T): Decoded<T> {
    return Decoded.Success(value)
}

operator fun Decoded<Json>.get(key: String): Decoded<Json> {
    return this.flatMap { it.get(key) }
}

val Decoded<Json>.boolean: Decoded<Boolean>
    get() = this.flatMap { it.boolean }

val Decoded<Json>.int: Decoded<Int>
    get() = this.flatMap { it.int }

val Decoded<Json>.long: Decoded<Long>
    get() = this.flatMap { it.long }

val Decoded<Json>.double: Decoded<Double>
    get() = this.flatMap { it.double }

val Decoded<Json>.string: Decoded<String>
    get() = this.flatMap { it.string }

val Decoded<Json>.list: Decoded<List<Json>>
    get() = this.flatMap { it.list }

val Decoded<Json>.map: Decoded<Map<String, Json>>
    get() = this.flatMap { it.map }

infix fun <T, U> ((T) -> U).mp(value: Decoded<T>): Decoded<U> {
    return value.map(this)
}
