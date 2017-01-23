package jp.co.qoncept.kaja

import jp.co.qoncept.kotres.Result
import jp.co.qoncept.kotres.flatMap

operator fun Result<Json, JsonException>.get(key: String): Result<Json, JsonException> {
    return flatMap { it[key] }
}

val Result<Json, JsonException>.boolean: Result<Boolean, JsonException>
    get() = flatMap { it.boolean }

val Result<Json, JsonException>.int: Result<Int, JsonException>
    get() = flatMap { it.int }

val Result<Json, JsonException>.long: Result<Long, JsonException>
    get() = flatMap { it.long }

val Result<Json, JsonException>.double: Result<Double, JsonException>
    get() = flatMap { it. double}

val Result<Json, JsonException>.string: Result<String, JsonException>
    get() = flatMap { it. string}

val Result<Json, JsonException>.list: Result<List<Json>, JsonException>
    get() = flatMap { it.list }

val Result<Json, JsonException>.map: Result<Map<String, Json>, JsonException>
    get() = flatMap { it.map }
