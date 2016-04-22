package jp.co.qoncept.kaja

open class DecodeException(message: String, cause: Throwable?): Exception(message, cause) {}

class ParseException(cause: Throwable): DecodeException(cause.message ?: "JSON parse error.", cause) {}

class MissingKeyException(val json: Json, val key: String, cause: Throwable? = null): DecodeException("MissingKey($key)", cause) {}

class TypeMismatchException(val json: Json, val typeName: String, cause: Throwable? = null): DecodeException("TypeMismatch(Expected $typeName, got $json)", cause) {}
