package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream

abstract sealed class Json {
    class Boolean(val value: kotlin.Boolean) : Json() {
        override val boolean: Decoded<Boolean>
                get() = Decoded.Success(value)
        override val int: Decoded<Int>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val long: Decoded<Long>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val double: Decoded<Double>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val string: Decoded<String>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val list: Decoded<List<Json>>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override val map: Decoded<Map<String, Json>>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        override fun get(key: String): Json {
            return Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        }
    }

    class Number(val value: kotlin.Number) : Json() {
        override val boolean: Decoded<Boolean>
                get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val int: Decoded<Int>
            get() = {
                if (value is Int) {
                    Decoded.Success(value)
                } else {
                    Decoded.Failure(TypeMismatchException(value, "Int"))
                }
            }
        override val long: Decoded<Long>
            get() = {
                if (value is Long) {
                    Decoded.Success(value)
                } else {
                    Decoded.Failure(TypeMismatchException(value, "Long"))
                }
            }
        override val double: Decoded<Double>
            get() = {
                if (value is Double) {
                    Decoded.Success(value)
                } else {
                    Decoded.Failure(TypeMismatchException(value, "Double"))
                }
            }
        override val string: Decoded<String>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        override val map: Decoded<Map<String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        fun get(key: String): Json {
            return Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
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
        override val string: Decoded<String>
            get() = Decoded.Success(value)
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override val map: Decoded<Map<String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        override fun get(key: String): Json {
            return Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
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
        override val string: Decoded<String>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override val list: Decoded<List<Json>
            get () = {
                val list = List<Json>()
                for (i in this.value.length()) {
                    list.add(this.value[i]))
                }
                return Decoded.success(list)
            }
        override val map: Decoded<Map<String, Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "Array"))
        override fun get(key: String): Json {
            return Decoded.Failure(TypeMismatchException(this, "Array"))
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
        override val string: Decoded<String>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val list: Decoded<List<Json>>
            get() = Decoded.Failure(TypeMismatchException(this, "Object"))
        override val map: Decoded<Map<String, Json>>
            get() = {
                val map = Map<String, Json>()
                for (string, in this.value.keys()) {
                    map.put(string, this.value[string]))
                }
                return map
            }
        override fun get(key: String): Json {
            if (map[key] != nil) {
                return Decoded.Success(map[key])
            } else {
                Decoded.Failure(MissingKeyException(this, key))
            }
        }
    }

    class Null : Json() {
        abstract val boolean: Decoded<Boolean> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract val int: Decoded<Int> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract val long: Decoded<Long> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract val double: Decoded<Double> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract val string: Decoded<String> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract val list: Decoded<List<Json>> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract val map: Decoded<Map<String, Json>> = Decoded.Failure(TypeMismatchException(this, "Null"))
        abstract fun get(key: String): Json = Decoded.Failure(TypeMismatchException(this, "Null"))
    }

    companion object {
        fun of(value: Boolean): Json {
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

        fun of(value: String): Json {
            return Json.String(value)
        }

        fun of(value: List<Json>): Json {
            return Json.Array(JSONArray(value))
        }

        fun of(value: Map<String, Json>): Json {
            return Json.Object(JSONObject(value))
        }

        fun parse(string: String): Json {
            val jsonObject = try {
                JSONObject(string)
            } catch (e: JSONException) {
                Json.Null
            }

            return jsonObject
        }

        fun parse(byteArray: ByteArray): Json {
            return parse(byteArray.toString())
        }

        fun parse(inputStream: InputStream): Json {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var stringBuilder = StringBuilder()

            while((val line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line)
            }

            bufferedReader.close()

            return parse(stringBuilder.toString())
        }

        fun parse(file: File): Json {
            return parse(FileInputStream(file))
        }
    }

    abstract val boolean: Decoded<Boolean>

    abstract val int: Decoded<Int>

    abstract val long: Decoded<Long>

    abstract val double: Decoded<Double>

    abstract val string: Decoded<String>

    abstract val list: Decoded<List<Json>>

    abstract val map: Decoded<Map<String, Json>>

    abstract fun get(key: String): Json

    fun <T> list(decode: (Json) -> Decoded<T>): Decoded<List<T>> {
        when this {
            is Json.Array -> {
                val list = List<T>()
                for (element in this.value) {
                    when decode(element) {
                        is Decoded.Success -> list.add(decode(element).value)
                        else -> return decode(element)
                    }
                }
                return Decoded.success(list)
            }
            else -> return Decoded.Failure(TypeMismatchException(this, Json.Array.class))
        }
    }
}