package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

abstract sealed class Json {
    class Boolean(val value: kotlin.Boolean) : Json() {
        override val boolean: Decoded<Boolean>
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
        override val boolean: Decoded<Boolean>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val int: Decoded<Int>
            get() = {
                if (value is Int) {
                    pure(value)
                } else {
                    Decoded.Failure(TypeMismatchException(value, "Int"))
                }
            }
        override val long: Decoded<Long>
            get() = {
                if (value is Long) {
                    pure(value)
                } else {
                    Decoded.Failure(TypeMismatchException(value, "Long"))
                }
            }
        override val double: Decoded<Double>
            get() = {
                if (this.value is Int || value is Long || value is Double) {
                    pure(value.toDouble())
                } else {
                    Decoded.Failure(TypeMismatchException(value, "kotlin.Number"))
                }
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
        override val boolean: Decoded<Boolean>
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
        override val boolean: Decoded<Boolean>
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
    }

    class Object(val value: JSONObject) : Json() {
        override val boolean: Decoded<Boolean>
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
                val map = HashMap<String, Json>()
                for (string in value.keys()) {
                    map.put(string, value[string]))
                }
                pure(map)
            }
        override fun get(key: kotlin.String): Json {
            if (value[key] != null) {
                return pure(value[key])
            } else {
                return Json.Null()
            }
        }
    }

    class Null : Json() {
        override val boolean: Decoded<Boolean>
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

    abstract val boolean: Decoded<Boolean>

    abstract val int: Decoded<Int>

    abstract val long: Decoded<Long>

    abstract val double: Decoded<Double>

    abstract val string: Decoded<kotlin.String>

    abstract val list: Decoded<List<Json>>

    abstract val map: Decoded<Map<kotlin.String, Json>>

    abstract fun get(key: kotlin.String): Json

    fun <T> list(decode: (Json) -> Decoded<T>): Decoded<List<T>> {
        if (this is Json.Array){
            return sequence(value.map(decode))
        } else {
            return Decoded.Failure(TypeMismatchException(this, Json.Array.class))
        }
    }
}