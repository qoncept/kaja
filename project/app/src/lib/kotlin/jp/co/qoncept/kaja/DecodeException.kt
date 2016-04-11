package jp.co.qoncept.kaja

open class DecodeException(message: String, cause: Throwable?): Exception(message, cause) {}

class MissingKeyException(val json: Json, val key: String, cause: Throwable? = null): DecodeException(/* TODO */ "", cause) {}

class TypeMismatchException(val json: Json, val typeName: String, cause: Throwable? = null): DecodeException(/* TODO */ "", cause) {}
