package jp.co.qoncept.kaja

sealed class Result<T> {
    class Success<T>(value: T): Result<T>() {
        private val _value: T = value

        override val value: T?
            get() = _value

        override val exception: JsonException?
            get() = null

        override fun <U> map(transform: (T) -> U): Result<U> {
            return pure(transform(_value))
        }

        override fun <U> flatMap(transform: (T) -> Result<U>): Result<U>{
            return transform(_value)
        }

        override fun <U> apply(transform: Result<(T) -> U>): Result<U> {
            return when(transform) {
                is Success -> pure(transform._value(_value))
                is Failure -> Failure(transform.exception!!)
            }
        }

        override fun ifMissingKey(alternative: T): Result<T> {
            return this
        }

        override fun nullIfMissingKey(): Result<T?> {
            return pure(_value)
        }

        override fun or(alternative: Result<T>): Result<T> {
            return this
        }

        override fun or(alternative: T): T {
            return _value
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Result.Success<*>) return false

            return _value == other._value
        }

        override fun hashCode(): Int {
            return _value?.hashCode() ?: 0
        }
    }

    class Failure<T>(exception: JsonException): Result<T>() {
        private val _exception: JsonException = exception

        override val value: T?
            get() = null

        override val exception: JsonException?
            get() = _exception

        override fun <U> map(transform: (T) -> U): Result<U> {
            return Failure(_exception)
        }

        override fun <U> flatMap(transform: (T) -> Result<U>): Result<U> {
            return Failure(_exception)
        }

        override fun <U> apply(transform: Result<(T) -> U>): Result<U> {
            return when(transform) {
                is Success -> Failure(_exception)
                is Failure -> Failure(transform._exception)
            }
        }

        override fun ifMissingKey(alternative: T): Result<T> {
            return when(_exception) {
                is MissingKeyException -> Success(alternative)
                else -> this
            }
        }

        override fun nullIfMissingKey(): Result<T?> {
            return when(_exception) {
                is MissingKeyException -> Success(null)
                else -> Failure(_exception)
            }
        }

        override fun or(alternative: Result<T>): Result<T> {
            return alternative
        }

        override fun or(alternative: T): T {
            return alternative
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Result.Failure<*>) return false

            return _exception == other._exception
        }

        override fun hashCode(): Int {
            return _exception.hashCode()
        }
    }
    abstract val value: T?

    abstract val exception: JsonException?

    abstract fun or(alternative: T): T

    abstract fun or(alternative: Result<T>): Result<T>

    abstract fun ifMissingKey(alternative: T): Result<T>

    abstract fun nullIfMissingKey(): Result<T?>

    abstract fun <U> map(transform: (T) -> U): Result<U>

    abstract fun <U> flatMap(transform: (T) -> Result<U>): Result<U>

    abstract fun <U> apply(transform: Result<(T) -> U>): Result<U>
}

fun <T> Result<Result<T>>.flatten(): Result<T> {
    return this.flatMap { it }
}

infix fun <T, U> Result<(T) -> U>.ap(value: Result<T>): Result<U> {
    return value.apply(this)
}

fun <T> pure(value: T): Result<T> {
    return Result.Success(value)
}

operator fun Result<Json>.get(key: String): Result<Json> {
    return this.flatMap { it.get(key) }
}

val Result<Json>.boolean: Result<Boolean>
    get() = this.flatMap { it.boolean }

val Result<Json>.int: Result<Int>
    get() = this.flatMap { it.int }

val Result<Json>.long: Result<Long>
    get() = this.flatMap { it.long }

val Result<Json>.double: Result<Double>
    get() = this.flatMap { it.double }

val Result<Json>.string: Result<String>
    get() = this.flatMap { it.string }

val Result<Json>.list: Result<List<Json>>
    get() = this.flatMap { it.list }

val Result<Json>.map: Result<Map<String, Json>>
    get() = this.flatMap { it.map }

infix fun <T, U> ((T) -> U).mp(value: Result<T>): Result<U> {
    return value.map(this)
}
