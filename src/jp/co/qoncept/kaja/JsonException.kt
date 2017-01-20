package jp.co.qoncept.kaja

open class JsonException(message: String, cause: Throwable?): Exception(message, cause) {}

class ParseException(cause: Throwable): JsonException(cause.message ?: "JSON parse error.", cause) {}

class MissingKeyException(val json: Json, val key: String, cause: Throwable? = null): JsonException("MissingKey($key)", cause) {}

class TypeMismatchException(val json: Json, val typeName: String, cause: Throwable? = null): JsonException("TypeMismatch(Expected $typeName, got $json)", cause) {}
