package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import jp.co.qoncept.kotres.Result

sealed class Json {
    abstract val boolean: Result<kotlin.Boolean, JsonException>

    abstract val int: Result<Int, JsonException>

    abstract val long: Result<Long, JsonException>

    abstract val double: Result<Double, JsonException>

    abstract val string: Result<kotlin.String, JsonException>

    abstract val list: Result<List<Json>, JsonException>

    abstract val map: Result<Map<kotlin.String, Json>, JsonException>

    abstract operator fun get(index: Int): Result<Json, JsonException>

    abstract operator fun get(key: kotlin.String): Result<Json, JsonException>

    fun <T> list(decode: (Json) -> Result<T, JsonException>): Result<List<T>, JsonException> {
        val list = this.list
        when (list) {
            is Result.Success -> return sequence(list.value.map(decode))
            else -> return Result.Failure(TypeMismatchException(this, Json::class.java.name))
        }
    }

    fun <T> map(decode: (Json) -> Result<T, JsonException>): Result<Map<kotlin.String, T>, JsonException> {
        val map = this.map
        when (map) {
            is Result.Success -> return sequence(map.value.mapValues{ decode(it.value) })
            else -> return Result.Failure(TypeMismatchException(this, Json::class.java.name))
        }
    }

    class Boolean(val value: kotlin.Boolean) : Json() {
        override val boolean: Result<kotlin.Boolean, JsonException>
            get() = Result.Success(value)
        override val int: Result<Int, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val long: Result<Long, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val double: Result<Double, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val string: Result<kotlin.String, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val list: Result<List<Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override operator fun get(index: Int): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        }
    }

    class Number(val value: kotlin.Number) : Json() {
        override val boolean: Result<kotlin.Boolean, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val int: Result<Int, JsonException>
            get() = if (value is Int) {
                Result.Success(value)
            } else {
                Result.Failure(TypeMismatchException(this, "Int"))
            }
        override val long: Result<Long, JsonException>
            get() = if (value is Long) {
                Result.Success(value)
            } else {
                Result.Failure(TypeMismatchException(this, "Long"))
            }
        override val double: Result<Double, JsonException>
            get() = when (value) {
                is Int, is Long, is Double -> Result.Success(value.toDouble())
                else -> Result.Failure(TypeMismatchException(this, "kotlin.Number"))
            }
        override val string: Result<kotlin.String, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val list: Result<List<Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.Number"))
        override operator fun get(index: Int): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.Number"))
        }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.Number"))
        }
    }

    class String(val value: kotlin.String) : Json() {
        override val boolean: Result<kotlin.Boolean, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.String"))
        override val int: Result<Int, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.String"))
        override val long: Result<Long, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.String"))
        override val double: Result<Double, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.String"))
        override val string: Result<kotlin.String, JsonException>
            get() = Result.Success(value)
        override val list: Result<List<Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.String"))
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "kotlin.String"))
        override operator fun get(index: Int): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.String"))
        }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.String"))
        }
    }

    class Array(val value: List<Json>) : Json() {
        override val boolean: Result<kotlin.Boolean, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override val int: Result<Int, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override val long: Result<Long, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override val double: Result<Double, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override val string: Result<kotlin.String, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override val list: Result<List<Json>, JsonException>
            get() = Result.Success(value)
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override operator fun get(index: Int): Result<Json, JsonException> {
            return Result.Success(createJson(value[index]))
        }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "Array"))
        }
    }

    class Object(val value: Map<kotlin.String, Json>) : Json() {
        override val boolean: Result<kotlin.Boolean, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Object"))
        override val int: Result<Int, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Object"))
        override val long: Result<Long, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Object"))
        override val double: Result<Double, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Object"))
        override val string: Result<kotlin.String, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Object"))
        override val list: Result<List<Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Object"))
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Success(value)
        override operator fun get(index: Int): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "Object"))
        }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return this.value[key]?.let { Result.Success<Json, JsonException>(it) } ?: Result.Failure(MissingKeyException(this, key))
        }
    }

    object Null : Json() {
        override val boolean: Result<kotlin.Boolean, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override val int: Result<Int, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override val long: Result<Long, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override val double: Result<Double, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override val string: Result<kotlin.String, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override val list: Result<List<Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Null"))
        override operator fun get(index: Int): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "Null"))
        }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "Null"))
        }
    }

    companion object {
        fun of(value: kotlin.Boolean): Json {
            return Json.Boolean(value)
        }

        fun of(value: Int): Json {
            return Json.Number(value)
        }

        fun of(value: Long): Json {
            return Json.Number(value)
        }

        fun of(value: Double): Json {
            return Json.Number(value)
        }

        fun of(value: kotlin.String): Json {
            return Json.String(value)
        }

        fun of(value: List<Json>): Json {
            return Json.Array(value)
        }

        fun of(value: Map<kotlin.String, Json>): Json {
            return Json.Object(value)
        }

        fun parse(string: kotlin.String): Result<Json, JsonException> {
            return try {
                Result.Success(createJson(JSONObject(string)))
            } catch (e: JSONException) {
                try {
                    Result.Success(createJson(JSONArray(string)))
                }
                catch (e2: JSONException) {
                    println(string)
                    Result.Failure(ParseException(e))
                }
            }
        }

        fun parse(byteArray: ByteArray): Result<Json, JsonException> {
            return parse(String(byteArray))
        }

        fun parse(inputStream: InputStream): Result<Json, JsonException> {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val string = bufferedReader.lineSequence().joinToString("")
            bufferedReader.close()

            return parse(string)
        }

        fun parse(file: File): Result<Json, JsonException> {
            return parse(FileInputStream(file))
        }
    }
}

private fun createJson(value: JSONObject): Json {
    if (value == JSONObject.NULL) { return Json.Null }
    val entries = value.keySet().map { key -> key to createJson(value.get(key)) }.toTypedArray()
    val map = mapOf(*entries)
    return Json.Object(map)
}

private fun createJson(value: JSONArray): Json {
    val list = (0 until value.length()).map { index -> createJson(value.get(index)) }
    return Json.Array(list)
}

private fun createJson(value: Any): Json {
    return when (value) {
        is Json -> value
        is Boolean -> Json.Boolean(value)
        is Number -> Json.Number(value)
        is String -> Json.String(value)
        is JSONArray -> createJson(value)
        is JSONObject -> createJson(value)
        else -> throw IllegalArgumentException(value.javaClass.name)
    }
}
