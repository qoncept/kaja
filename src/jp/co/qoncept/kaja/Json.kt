package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import jp.co.qoncept.util.Result

sealed class Json {
    abstract val boolean: Result<kotlin.Boolean, JsonException>

    abstract val int: Result<Int, JsonException>

    abstract val long: Result<Long, JsonException>

    abstract val double: Result<Double, JsonException>

    abstract val string: Result<kotlin.String, JsonException>

    abstract val list: Result<List<Json>, JsonException>

    abstract val map: Result<Map<kotlin.String, Json>, JsonException>

    abstract operator fun get(key: kotlin.String): Result<Json, JsonException>

    fun <T> list(decode: (Json) -> Result<T, JsonException>): Result<List<T>, JsonException> {
        when (list) {
            is Result.Success -> return sequence(list.value!!.map(decode))
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
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "kotlin.String"))
        }
    }

    class Array(val value: JSONArray) : Json() {
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
            get() {
                val list = ArrayList<Json>()
                for (i in 0..value.length()-1) {
                    val element = value[i]
                    list.add(when (element) {
                        is kotlin.Boolean -> Json.Boolean(element)
                        is kotlin.Number -> Json.Number(element)
                        is kotlin.String -> Json.String(element)
                        is JSONArray -> Json.Array(element)
                        is Json -> element
                        else -> Json.Object(element as JSONObject)
                    })
                }
                return Result.Success(list)
            }
        override val map: Result<Map<kotlin.String, Json>, JsonException>
            get() = Result.Failure(TypeMismatchException(this, "Array"))
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            return Result.Failure(TypeMismatchException(this, "Array"))
        }


        fun <T> map(transform: (Json) -> T): ArrayList<T> {
            var accum = ArrayList<T>()
            for (i in 0..value.length() - 1) {
                accum.add(transform(this.value[i] as Json))
            }
            return accum
        }
    }

    class Object(val value: JSONObject) : Json() {
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
            get() {
                val result = HashMap<kotlin.String, Json>()
                for (key in value.keys()) {
                    val element = value[key]
                    result.put(key, when (element) {
                        is kotlin.Boolean -> Json.Boolean(element)
                        is kotlin.Number -> Json.Number(element)
                        is kotlin.String -> Json.String(element)
                        is JSONArray -> Json.Array(element)
                        is Json -> element
                        JSONObject.NULL -> Json.Null
                        else -> Json.Object(element as JSONObject)
                    })
                }
                return Result.Success(result)
            }
        override operator fun get(key: kotlin.String): Result<Json, JsonException> {
            val element = value.opt(key)
            return when (element) {
                is kotlin.Boolean -> Result.Success(Json.Boolean(element))
                is kotlin.Number -> Result.Success(Json.Number(element))
                is kotlin.String -> Result.Success(Json.String(element))
                is JSONArray -> Result.Success(Json.Array(element))
                JSONObject.NULL -> Result.Success(Json.Null)
                is JSONObject -> Result.Success(Json.Object(element))
                else -> Result.Failure(MissingKeyException(this, key))
            }
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
            val jsonArray = JSONArray()
            for (element in value) {
                jsonArray.put(element)
            }
            return Json.Array(jsonArray)
        }

        fun of(value: Map<kotlin.String, Json>): Json {
            val jsonObject = JSONObject()
            for ((key, element) in value) {
                jsonObject.put(key, element)
            }
            return Json.Object(jsonObject)
        }

        fun parse(string: kotlin.String): Result<Json, JsonException> {
            return try {
                Result.Success(Json.Object(JSONObject(string)))
            } catch (e: JSONException) {
                try {
                    Result.Success(Json.Array(JSONArray(string)))
                }
                catch (e2: JSONException) {
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