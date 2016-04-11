package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream

abstract sealed class Json {
    class Boolean(val value: kotlin.Boolean) : Json() {
        // TODO
    }

    class Number(val value: kotlin.Number) : Json() {
        // TODO
    }

    class String(val value: kotlin.String) : Json() {
        // TODO
    }

    class Array(val value: JSONArray) : Json() {
        // TODO
    }

    class Object(val value: JSONObject) : Json() {
        // TODO
    }

    class Null : Json() {
        // TODO
    }

    companion object {
        fun of(value: Boolean): Json {
            // TODO
        }

        fun of(value: Int): Json {
            // TODO
        }

        fun of(value: Long): Json {
            // TODO
        }

        fun of(value: Double): Json {
            // TODO
        }

        fun of(value: String): Json {
            // TODO
        }

        fun of(value: List<Json>): Json {
            // TODO
        }

        fun of(value: Map<String, Json>): Json {
            // TODO
        }

        fun parse(string: String): Json {
            // TODO
        }

        fun parse(byteArray: ByteArray): Json {
            // TODO
        }

        fun parse(inputStream: InputStream): Json {
            // TODO
        }

        fun parse(file: File): Json {
            // TODO
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
        // TODO
    }
}