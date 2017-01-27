package jp.co.qoncept.kaja

import jp.co.qoncept.kotres.Result
import jp.co.qoncept.kotres.ap
import jp.co.qoncept.kotres.flatMap
import jp.co.qoncept.kotres.mp
import org.testng.Assert.*
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.File

class JsonTest {
    class ExampleTest {
        @Test
        fun testExample() {
            data class Person(
                    val firstName: String,
                    val middleName: String?,
                    val lastName: String,
                    val age: Int,
                    val knownFor: List<String>
            )

            val jsonString = """{
                |    "firstName": "Albert",
                |    "lastName": "Einstein",
                |    "age": 76,
                |    "knownFor": [
                |        "General relativity",
                |        "Special relativity",
                |        "Photoelectric effect",
                |        "Mass–energy equivalence",
                |        "Theory of Brownian motion"
                |    ]
                |}""".trimMargin()

            val json: Result<Json, JsonException>
                    = Json.parse(jsonString)

            val person: Result<Person, JsonException>
                    = curry(::Person) mp
                        json["firstName"].string ap
                        json["middleName"].string.optional ap
                        json["lastName"] .string ap
                        json["age"].int ap
                        json["knownFor"].list(Json::string)

            /**/ assertEquals(person.value!!.firstName, "Albert")
            /**/ assertEquals(person.value!!.middleName, null)
            /**/ assertEquals(person.value!!.lastName, "Einstein")
            /**/ assertEquals(person.value!!.age, 76)
            /**/ assertEquals(person.value!!.knownFor, listOf(
                    "General relativity",
                    "Special relativity",
                    "Photoelectric effect",
                    "Mass–energy equivalence",
                    "Theory of Brownian motion"
            ))
        }

        fun <A, B, C, D, E, Z> curry(function: (A, B, C, D, E) -> Z): (A) -> (B) -> (C) -> (D) -> (E) -> Z {
            return { a -> { b -> { c -> { d -> { e -> function(a, b, c, d, e) } } } } }
        }
    }

    class BooleanTest {
        val value = Json.Boolean(true)

        @Test
        fun testBoolean() {
            assertEquals(value.boolean.value, true)
        }

        @Test
        fun testInt() {
            assertTrue(value.int.exception is TypeMismatchException)
        }

        @Test
        fun testLong() {
            assertTrue(value.long.exception is TypeMismatchException)
        }

        @Test
        fun testDouble() {
            assertTrue(value.double.exception is TypeMismatchException)
        }

        @Test
        fun testString() {
            assertTrue(value.string.exception is TypeMismatchException)
        }

        @Test
        fun testList() {
            assertTrue(value.list.exception is TypeMismatchException)
        }

        @Test
        fun testMap() {
            assertTrue(value.map.exception is TypeMismatchException)
        }

        @Test
        fun testGet() {
            assertTrue(value["boolean"].exception is TypeMismatchException)
        }

        @Test
        fun testToString() {
            assertEquals(value.toString(), "true")
        }
    }

    class NumberTest {
        val intValue = Json.Number(1)
        val longValue = Json.Number(2147483648)
        val doubleValue = Json.Number(0.5)
        @Test
        fun testBoolean() {
            assertTrue(intValue.boolean.exception is TypeMismatchException)
            assertTrue(longValue.boolean.exception is TypeMismatchException)
            assertTrue(doubleValue.boolean.exception is TypeMismatchException)
        }

        @Test
        fun testInt() {
            assertEquals(intValue.int.value, 1)
            assertTrue(longValue.int.exception is TypeMismatchException)
            assertTrue(doubleValue.int.exception is TypeMismatchException)
        }

        @Test
        fun testLong() {
            assertTrue(intValue.long.exception is TypeMismatchException)
            assertEquals(longValue.long.value, 2147483648)
            assertTrue(doubleValue.long.exception is TypeMismatchException)
        }

        @Test
        fun testDouble() {
            assertEquals(intValue.double.value, 1.0)
            assertEquals(longValue.double.value, 2147483648.0)
            assertEquals(doubleValue.double.value, 0.5)
        }

        @Test
        fun testString() {
            assertTrue(intValue.string.exception is TypeMismatchException)
            assertTrue(longValue.string.exception is TypeMismatchException)
            assertTrue(doubleValue.string.exception is TypeMismatchException)
        }

        @Test
        fun testList() {
            assertTrue(intValue.list.exception is TypeMismatchException)
            assertTrue(longValue.list.exception is TypeMismatchException)
            assertTrue(doubleValue.list.exception is TypeMismatchException)
        }

        @Test
        fun testMap() {
            assertTrue(intValue.map.exception is TypeMismatchException)
            assertTrue(longValue.map.exception is TypeMismatchException)
            assertTrue(doubleValue.map.exception is TypeMismatchException)
        }

        @Test
        fun testGet() {
            assertTrue(intValue["int"].exception is TypeMismatchException)
            assertTrue(longValue["long"].exception is TypeMismatchException)
            assertTrue(doubleValue["double"].exception is TypeMismatchException)
        }

        @Test
        fun testToString() {
            assertEquals(intValue.toString(), "1")
            assertEquals(longValue.toString(), "2147483648")
            assertEquals(doubleValue.toString(), "0.5")
        }
    }

    class StringTest {
        val value = Json.String("string\\\"abc\"\n")
        @Test
        fun testBoolean() {
            assertTrue(value.boolean.exception is TypeMismatchException)
        }

        @Test
        fun testInt() {
            assertTrue(value.int.exception is TypeMismatchException)
        }

        @Test
        fun testLong() {
            assertTrue(value.long.exception is TypeMismatchException)
        }

        @Test
        fun testDouble() {
            assertTrue(value.double.exception is TypeMismatchException)
        }

        @Test
        fun testString() {
            assertEquals(value.string.value, "string\\\"abc\"\n")
        }

        @Test
        fun testList() {
            assertTrue(value.list.exception is TypeMismatchException)
        }

        @Test
        fun testMap() {
            assertTrue(value.map.exception is TypeMismatchException)
        }

        @Test
        fun testGet() {
            assertTrue(value["string"].exception is TypeMismatchException)
        }

        @Test
        fun testToString() {
            assertEquals(value.toString(), """"string\\\"abc\"\n"""")
        }
    }

    class ArrayTest {
        companion object {
            val value = Json.Array(listOf(Json.Boolean(true), Json.Boolean(false)))
        }

        @Test
        fun testBoolean() {
            assertTrue(value.boolean.exception is TypeMismatchException)
        }

        @Test
        fun testInt() {
            assertTrue(value.int.exception is TypeMismatchException)
        }

        @Test
        fun testLong() {
            assertTrue(value.long.exception is TypeMismatchException)
        }

        @Test
        fun testDouble() {
            assertTrue(value.double.exception is TypeMismatchException)
        }

        @Test
        fun testString() {
            assertTrue(value.string.exception is TypeMismatchException)
        }

        @Test
        fun testList() {
            assertEquals(value.list.value!!.size, 2)
            assertEquals(value.list.value!![0].boolean.value, true)
            assertEquals(value.list.value!![1].boolean.value, false)
        }

        @Test
        fun testMap() {
            assertTrue(value.map.exception is TypeMismatchException)
        }

        @Test
        fun testGet() {
            assertTrue(value["array"].exception is TypeMismatchException)
        }

        @Test
        fun testToString() {
            assertEquals(value.toString(), "[true,false]")
        }
    }

    class ObjectTest {
        companion object {
            val value = Json.Object(mapOf(
                    "boolean" to Json.Boolean(true),
                    "number" to Json.Number(0),
                    "string\\\"key\"\n" to Json.String("string\\\"value\"\n"),
                    "array" to Json.Array(listOf()),
                    "jsonNull" to Json.Null,
                    "object" to Json.Object(mapOf())
            ))
        }

        @Test
        fun testBoolean() {
            assertTrue(value.boolean.exception is TypeMismatchException)
        }

        @Test
        fun testInt() {
            assertTrue(value.int.exception is TypeMismatchException)
        }

        @Test
        fun testLong() {
            assertTrue(value.long.exception is TypeMismatchException)
        }

        @Test
        fun testDouble() {
            assertTrue(value.double.exception is TypeMismatchException)
        }

        @Test
        fun testString() {
            assertTrue(value.string.exception is TypeMismatchException)
        }

        @Test
        fun testList() {
            assertTrue(value.list.exception is TypeMismatchException)
        }

        @Test
        fun testMap() {
            assertEquals(value.map.value!!.size, 6)
            assertEquals(value.map.value!!["boolean"]!!.boolean.value, true)
            assertEquals(value.map.value!!["number"]!!.int.value, 0)
            assertEquals(value.map.value!!["string\\\"key\"\n"]!!.string.value, "string\\\"value\"\n")
            assertTrue(value.map.value!!["array"]!!.list.value is List)
            assertEquals(value.map.value!!["jsonNull"]!!, Json.Null as Json)
            assertTrue(value.map.value!!["object"]!!.map.value is Map)
            assertEquals(value.map.value!!["null"], null)
        }

        @Test
        fun testGet() {
            assertEquals(value["boolean"].value!!.boolean.value, true)
            assertEquals(value["number"].value!!.int.value, 0)
            assertEquals(value["string\\\"key\"\n"].value!!.string.value, "string\\\"value\"\n")
            assertTrue(value["array"].value!!.list.value is List)
            assertEquals(value["jsonNull"].value, Json.Null as Json)
            assertTrue(value["object"].value!!.map.value is Map)
            assertTrue(value["null"].exception is MissingKeyException)
        }

        @Test
        fun testToString() {
            run {
                val jsonString = value.toString()
                assertTrue(jsonString.contains(""""boolean":true"""))
                assertTrue(jsonString.contains(""""number":0"""))
                assertTrue(jsonString.contains(""""string\\\"key\"\n":"string\\\"value\"\n""""))
                assertTrue(jsonString.contains(""""array":[]"""))
                assertTrue(jsonString.contains(""""jsonNull":null"""))
                assertTrue(jsonString.contains(""""object":{}"""))
                assertEquals(jsonString.first(), '{')
                assertEquals(jsonString.last(), '}')
            }
            run {
                val json = Json.of(mapOf(
                        "foo" to Json.of(42)
                ))
                assertEquals(json.toString(), """{"foo":42}""")
            }
        }
    }

    class NullTest {
        val value = Json.Null
        @Test
        fun testBoolean() {
            assertTrue(value.boolean.exception is TypeMismatchException)
        }

        @Test
        fun testInt() {
            assertTrue(value.int.exception is TypeMismatchException)
        }

        @Test
        fun testLong() {
            assertTrue(value.long.exception is TypeMismatchException)
        }

        @Test
        fun testDouble() {
            assertTrue(value.double.exception is TypeMismatchException)
        }

        @Test
        fun testString() {
            assertTrue(value.string.exception is TypeMismatchException)
        }

        @Test
        fun testList() {
            assertTrue(value.list.exception is TypeMismatchException)
        }

        @Test
        fun testMap() {
            assertTrue(value.map.exception is TypeMismatchException)
        }

        @Test
        fun testGet() {
            assertTrue(value["jsonNull"].exception is TypeMismatchException)
        }

        @Test
        fun testToString() {
            assertEquals(value.toString(), "null")
        }
    }

    class OfTest {
        @Test
        fun testOfBoolean() {
            assertEquals(Json.of(true).boolean.value, true)
        }

        @Test
        fun testOfInt() {
            assertEquals(Json.of(1).int.value, 1)
        }

        @Test
        fun testOfLong() {
            assertEquals(Json.of(2147483648).long.value, 2147483648)
        }

        @Test
        fun testOfDouble() {
            assertEquals(Json.of(1.0).double.value, 1.0)
        }

        @Test
        fun testOfString() {
            assertEquals(Json.of("string").string.value, "string")
        }

        @Test
        fun testOfList() {
            val value = listOf(Json.Boolean(true))
            assertEquals(Json.of(value).list.value!![0].boolean.value, true)
        }

        @Test
        fun testOfMap() {
            val value = hashMapOf(Pair("true", Json.Boolean(true)))
            assertTrue(Json.of(value) is Json)
        }
    }

    class ParseTest {
        companion object {
            val jsonObjectString = """{
                |  "boolean": true,
                |  "array": [2, 3, 5]
                |}""".trimMargin()
            val jsonArrayString = """[2, {"b": 3, "c": 5}]"""
            val file = File("ParseTest.java")

            @BeforeClass @JvmStatic
            fun execBeforeClass() {
                file.createNewFile()
                file.appendText(jsonObjectString)
            }

            @AfterClass @JvmStatic
            fun execAfterClass() {
                file.delete()
            }
        }

        @Test
        fun testParseFromFile() {
            val json = Json.parse(file)

            assertEquals(json["boolean"].boolean.value, true)
            assertEquals(json["array"].list { it.int }.value, listOf(2, 3, 5))
        }

        @Test
        fun testParseFromInputStream() {
            val json = Json.parse(jsonObjectString.byteInputStream())

            assertEquals(json["boolean"].boolean.value, true)
            assertEquals(json["array"].list { it.int }.value, listOf(2, 3, 5))
        }

        @Test
        fun testParseFromByteArray() {
            val json = Json.parse(jsonObjectString.toByteArray())

            assertEquals(json["boolean"].boolean.value, true)
            assertEquals(json["array"].list { it.int }.value, listOf(2, 3, 5))
        }

        @Test
        fun testParseFromString() {
            run {
                val json = Json.parse(jsonObjectString)

                assertEquals(json["boolean"].boolean.value, true)
                assertEquals(json["array"].list { it.int }.value, listOf(2, 3, 5))
            }
            run {
                val json = Json.parse(jsonArrayString)

                assertEquals(json[0].int.value, 2)
                assertEquals(json[1].map { it.int }.value, mapOf("b" to 3, "c" to 5))
            }
        }
    }

    @Test
    fun testList() {
        val boolean = Json.Boolean(true)
        val number = Json.Number(1)
        val string = Json.String("string")
        val jsonArray = Json.Array(listOf(Json.Boolean(true)))
        val jsonObject = Json.Object(mapOf("boolean" to Json.Boolean(true)))
        val jsonNull = Json.Null

        val decode: (Json) -> Result<Boolean, JsonException> = { Result.Success(false) }

        assertTrue(boolean.list(decode).exception is TypeMismatchException)
        assertTrue(number.list(decode).exception is TypeMismatchException)
        assertTrue(string.list(decode).exception is TypeMismatchException)
        assertEquals(jsonArray.list(decode).value!![0], false)
        assertTrue(jsonObject.list(decode).exception is TypeMismatchException)
        assertTrue(jsonNull.list(decode).exception is TypeMismatchException)
    }

    class SequenceTest {
        @Test
        fun testArraySequence() {
            val list0 = listOf<Result<Boolean, JsonException>>(Result.Success(true), Result.Success(false))
            assertEquals(sequence(list0).value!!.size, 2)
            assertEquals(sequence(list0).value!![0], true)
            assertEquals(sequence(list0).value!![1], false)

            val list1 = listOf<Result<Boolean, JsonException>>(Result.Failure(TypeMismatchException(Json.Null, "failure")))
            assertTrue(sequence(list1).exception is TypeMismatchException)
        }

        @Test
        fun testMapSequence() {
            val map0 = hashMapOf<String, Result<Boolean, JsonException>>("true" to Result.Success(true), "false" to Result.Success(false))
            assertEquals(sequence(map0).value!!.size, 2)
            assertEquals(sequence(map0).value!!["true"], true)
            assertEquals(sequence(map0).value!!["false"], false)

            val map1 = mapOf<String, Result<Boolean, JsonException>>(Pair("failure", Result.Failure(TypeMismatchException(Json.Null, "failure"))))
            assertTrue(sequence(map1).exception is TypeMismatchException)
        }
    }
}