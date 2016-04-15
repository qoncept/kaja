package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

abstract sealed class Json {
    class Boolean(val value: kotlin.Boolean) : Json() {
        override val boolean: Decoded<kotlin.Boolean>
            get() = pure(value)
        override val int: Decoded<Int>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val long: Decoded<Long>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val double: Decoded<Double>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val string: Decoded<kotlin.String>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val map: Decoded<Map<kotlin.String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override fun get(key: kotlin.String): Json {
            return Json.Null()
        }
    }

    class Number(val value: kotlin.Number) : Json() {
        override val boolean: Decoded<kotlin.Boolean>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val int: Decoded<Int>
            get() = if (value is Int) {
                pure(value)
            } else {
                Decoded.Failure(TypeMismatchException(this, "Int"))
            }
        override val long: Decoded<Long>
            get() = if (value is Long) {
                pure(value)
            } else {
                Decoded.Failure(TypeMismatchException(this, "Long"))
            }
        override val double: Decoded<Double>
            get() = when (value) {
                is Int, is Long, is Double -> pure(value.toDouble())
                else -> Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
            }
        override val string: Decoded<kotlin.String>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val map: Decoded<Map<kotlin.String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override fun get(key: kotlin.String): Json {
            return Json.Null()
        }
    }

    class String(val value: kotlin.String) : Json() {
        override val boolean: Decoded<kotlin.Boolean>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override val int: Decoded<Int>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override val long: Decoded<Long>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override val double: Decoded<Double>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override val string: Decoded<kotlin.String>
            get() = pure(value)
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override val map: Decoded<Map<kotlin.String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override fun get(key: kotlin.String): Json {
            return Json.Null()
        }
    }

    class Array(val value: JSONArray) : Json() {
        override val boolean: Decoded<kotlin.Boolean>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override val int: Decoded<Int>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override val long: Decoded<Long>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override val double: Decoded<Double>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override val string: Decoded<kotlin.String>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override val list: Decoded<List<Json>>
            get() = {
                val list = ArrayList<Json>()
                for (element in value) {
                    list.add(element)
                }
                pure(list)
            }
        override val map: Decoded<Map<kotlin.String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override fun get(key: kotlin.String): Json {
            return Json.Null()
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
        override val boolean: Decoded<kotlin.Boolean>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val int: Decoded<Int>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val long: Decoded<Long>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val double: Decoded<Double>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val string: Decoded<kotlin.String>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val map: Decoded<Map<kotlin.String, Json>>
            get() = {
                val result = HashMap<kotlin.String, Json>()
                for (key in value.keys()) {
                    result.put(key, value[key] as Json))
                }
                return result
            }
        override fun get(key: kotlin.String): Json {
            val element = value[key]
            return when (element) {
                is kotlin.Boolean -> Json.Boolean(element)
                is kotlin.Number -> Json.Number(element)
                is kotlin.String -> Json.String(element)
                is JSONArray -> Json.Array(element)
                is JSONObject -> Json.Object(element)
                else -> Json.Null()
            }
        }
    }

    class Null : Json() {
        override val boolean: Decoded<kotlin.Boolean>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override val int: Decoded<Int>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override val long: Decoded<Long>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override val double: Decoded<Double>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override val string: Decoded<kotlin.String>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override val map: Decoded<Map<kotlin.String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "Null"))
        override fun get(key: kotlin.String): Json {
            return Json.Null()
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
            return Json.Array(JSONArray(value))
        }

        fun of(value: Map<String, Json>): Json {
            return Json.Object(JSONObject(value))
        }

        fun parse(string: kotlin.String): Json {
            val jsonObject = try {
                Json.Object(JSONObject(string))
            } catch (e: JSONException) {
                Json.Null()
            }

            return jsonObject
        }

        fun parse(byteArray: ByteArray): Json {
            return parse(byteArray.toString())
        }

        fun parse(inputStream: InputStream): Json {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val string = bufferedReader.lineSequence().toString()
            bufferedReader.close()

            return parse(string)
        }

        fun parse(file: File): Json {
            return parse(FileInputStream(file))
        }
    }

    abstract val boolean: Decoded<kotlin.Boolean>

    abstract val int: Decoded<Int>

    abstract val long: Decoded<Long>

    abstract val double: Decoded<Double>

    abstract val string: Decoded<kotlin.String>

    abstract val list: Decoded<List<Json>>

    abstract val map: Decoded<Map<kotlin.String, Json>>

    abstract fun get(key: kotlin.String): Json

    fun <T> list(decode: (Json) -> Decoded<T>): Decoded<List<T>> {
        when (list) {
            is Decoded.Success -> return sequence(list.value!!.map(decode))
            else -> return Decoded.Failure(TypeMismatchException(this, Json.javaClass.name))
        }
    }
}