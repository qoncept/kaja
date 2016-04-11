package jp.co.qoncept.kaja

import java.io.File
import java.io.InputStream

class Json {
    constructor(value: Boolean) {
        // TODO
    }

    constructor(value: Int) {
        // TODO
    }

    constructor(value: Long) {
        // TODO
    }

    constructor(value: Double) {
        // TODO
    }

    constructor(value: String) {
        // TODO
    }

    constructor(value: List<Json>) {
        // TODO
    }

    constructor(value: Map<String, Json>) {
        // TODO
    }

    companion object {
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

fun <T> Json.list(decode: (Json) -> Decoded<T>): Decoded<List<T>> {
    // TODO
}
