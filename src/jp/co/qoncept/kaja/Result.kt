package jp.co.qoncept.kaja

import jp.co.qoncept.util.Result
import jp.co.qoncept.util.flatMap

operator fun Result<Json, JsonException>.get(key: String): Result<Json, JsonException> {
    return flatMap { it[key] }
}

val Result<Json, JsonException>.boolean: Result<Boolean, JsonException>
    get() = flatMap { it.boolean }
