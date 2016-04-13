package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream

abstract sealed class Json {
    class Boolean(val value: kotlin.Boolean) : Json() {
        val boolean: Decoded<Boolean> = Decoded.Success(value)
        val int: Decoded<Int> = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        val long: Decoded<Long> = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        val double: Decoded<Double> = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        val string: Decoded<String> = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        val list: Decoded<List<Json>> = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        val map: Decoded<Map<String, Json>> = Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
        fun get(key: String): Json= Decoded.Failure(TypeMismatchException(this, "kotlin.Boolean"))
    }

    class Number(val value: kotlin.Number) : Json() {
        val boolean: Decoded<Boolean> = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        val int: Decoded<Int> = {
            if (value is Int) {
                Decoded.Success(value)
            } else {
                Decoded.Failure(TypeMismatchException(value, "Int"))
            }
        }
        val long: Decoded<Long> = {
            if (value is Long) {
                Decoded.Success(value)
            } else {
                Decoded.Failure(TypeMismatchException(value, "Long"))
            }
        }
        val double: Decoded<Double> = {
            if (value is Double) {
                Decoded.Success(value)
            } else {
                Decoded.Failure(TypeMismatchException(value, "Double"))
            }
        }
        val string: Decoded<String> = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        val list: Decoded<List<Json>> = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        val map: Decoded<Map<String, Json>> = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
        fun get(key: String): Json = Decoded.Failure(TypeMismatchException(this, "kotlin.Number"))
    }

    class String(val value: kotlin.String) : Json() {
        val boolean: Decoded<Boolean> = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        val int: Decoded<Int> = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        val long: Decoded<Long> = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        val double: Decoded<Double> = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        val string: Decoded<String> = Decoded.Success(value)
        val list: Decoded<List<Json>> = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        val map: Decoded<Map<String, Json>> = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
        fun get(key: String): Json = Decoded.Failure(TypeMismatchException(this, "kotlin.String"))
    }

    class Array(val value: JSONArray) : Json() {
        val boolean: Decoded<Boolean> = Decoded.Failure(TypeMismatchException(this, "Array"))
        val int: Decoded<Int> = Decoded.Failure(TypeMismatchException(this, "Array"))
        val long: Decoded<Long> = Decoded.Failure(TypeMismatchException(this, "Array"))
        val double: Decoded<Double> = Decoded.Failure(TypeMismatchException(this, "Array"))
        val string: Decoded<String> = Decoded.Failure(TypeMismatchException(this, "Array"))
        val list: Decoded<List<Json> = {
            val list = List<Json>()
            for (i in this.value.length()) {
                list.add(this.value.get(i))
            }
            return Decoded.success(list)
        }
        val map: Decoded<Map<String, Json>> = Decoded.Failure(TypeMismatchException(this, "Array"))
        fun get(key: String): Json = Decoded.Failure(TypeMismatchException(this, "Array"))
    }

    class Object(val value: JSONObject) : Json() {
        val boolean: Decoded<Boolean> = Decoded.Failure(TypeMismatchException(this, "Object"))
        val int: Decoded<Int> = Decoded.Failure(TypeMismatchException(this, "Object"))
        val long: Decoded<Long> = Decoded.Failure(TypeMismatchException(this, "Object"))
        val double: Decoded<Double> = Decoded.Failure(TypeMismatchException(this, "Object"))
        val string: Decoded<String> = Decoded.Failure(TypeMismatchException(this, "Object"))
        val list: Decoded<List<Json>> = Decoded.Failure(TypeMismatchException(this, "Object"))
        val map: Decoded<Map<String, Json>> = {
            val map = Map<String, Json>()
            for (string, in this.value.keys()) {
                map.put(string, this.value.get(string))
            }
            return map
        }
        fun get(key: String): Json = map[key] != nil ? Decoded.Success(map[key]) : Decoded.Failure(MissingKeyException(this, key))
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
                for (i in this.value.length()) {
                    if !(decode(this.value.get(i)).value != null) {
                        return Decoded.Failure(TypeMismatchException(this.value.get(i), T.class))
                    }
                    list.add((T) this.value.get(i))
                }
                return Decoded.success(list)
            }
            else -> return Decoded.Failure(TypeMismatchException(this, Json.Array.class))
        }
    }
}