package jp.co.qoncept.kaja

import java.io.File
import java.io.InputStream

open class Json {
    constructor(string: String) {
        // TODO
    }

    constructor(byteArray: ByteArray) {
        // TODO
    }

    constructor(inputStream: InputStream) {
        // TODO
    }

    constructor(file: File) {
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
    }
}

val Json.boolean: Decoded<Boolean>
    get() {
        // TODO
    }

val Json.int: Decoded<Int>
    get() {
        // TODO
    }

val Json.long: Decoded<Long>
    get() {
        // TODO
    }

val Json.double: Decoded<Double>
    get() {
        // TODO
    }

val Json.string: Decoded<String>
    get() {
        // TODO
    }

val Json.list: Decoded<List<Json>>
    get() {
        // TODO
    }

val Json.map: Decoded<Map<String, Json>>
    get() {
        // TODO
    }

fun Json.get(key: String): Json {
    // TODO
}

