package jp.co.qoncept.kaja

import org.json.JSONArray
import org.json.JSONObject
import org.testng.Assert.*
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.File
import java.util.*

class JsonTest {
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
    }

    class NumberTest {
        val intValue = Json.Number(1)
        val longValue = Json.Number(2147483648)
        val doubleValue = Json.Number(0.1)
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
            assertEquals(doubleValue.double.value, 0.1)
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
    }

    class StringTest {
        val value = Json.String("string")
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
            assertEquals(value.string.value, "string")
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
    }

    class ArrayTest {
        companion object {
            val value = Json.Array(JSONArray())
            @BeforeClass @JvmStatic
            fun execBeforeClass() {
                value.value.put(true)
                value.value.put(false)
            }
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
    }

    class ObjectTest {
        companion object {
            val value = Json.Object(JSONObject())
            @BeforeClass @JvmStatic
            fun execBeforeClass() {
                value.value.put("boolean", true)
                value.value.put("number", 0)
                value.value.put("string", "string")
                value.value.put("array", JSONArray())
                value.value.put("jsonNull", JSONObject.NULL)
                value.value.put("object", JSONObject())
            }
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
            assertEquals(value.map.value!!["string"]!!.string.value, "string")
            assertTrue(value.map.value!!["array"]!!.list.value is List)
            assertEquals(value.map.value!!["jsonNull"]!!, Json.Null as Json)
            assertTrue(value.map.value!!["object"]!!.map.value is Map)
            assertEquals(value.map.value!!["null"], null)
        }

        @Test
        fun testGet() {
            assertEquals(value["boolean"].value!!.boolean.value, true)
            assertEquals(value["number"].value!!.int.value, 0)
            assertEquals(value["string"].value!!.string.value, "string")
            assertTrue(value["array"].value!!.list.value is List)
            assertEquals(value["jsonNull"].value, Json.Null as Json)
            assertTrue(value["object"].value!!.map.value is Map)
            assertTrue(value["null"].exception is MissingKeyException)
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
            val value = arrayListOf(Json.Boolean(true))
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
            val string = """
            |{"boolean": true}
            """.trimMargin()
            val arrayString = """
            |[1, 2, 3]
            """.trimMargin()
            val file = File("parseTest.java")

            @BeforeClass @JvmStatic
            fun execBeforeClass() {
                file.createNewFile()
                file.appendText(string)
            }

            @AfterClass @JvmStatic
            fun execAfterClass() {
                file.delete()
            }
        }

        @Test
        fun testParseFromFile() {
            assertEquals(Json.parse(file)["boolean"].boolean.value, true)
        }

        @Test
        fun testParseFromInputStream() {
            val inputStream = string.byteInputStream()
            assertEquals(Json.parse(inputStream)["boolean"].boolean.value, true)
        }

        @Test
        fun testParseFromByteArray() {
            val byteArray = string.toByteArray()
            assertEquals(Json.parse(byteArray)["boolean"].boolean.value, true)
        }

        @Test
        fun testParseFromString() {
            run {
                assertEquals(Json.parse(string)["boolean"].boolean.value, true)
            }
            run {
                assertEquals(Json.parse(arrayString).flatMap { it.list { it.int } }.value, listOf(1, 2, 3))
            }
        }
    }

    @Test
    fun testList() {
        val boolean = Json.Boolean(true)
        val number = Json.Number(1)
        val string = Json.String("string")
        val jsonArray = Json.Array(JSONArray())
        jsonArray.value.put(true)
        val jsonObject = Json.Object(JSONObject())
        jsonObject.value.put("boolean", true)
        val jsonNull = Json.Null

        val decode: (Json) -> Result<Boolean> = { pure(false) }

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
            val list0 = arrayListOf(Result.Success(true), Result.Success(false))
            assertEquals(sequence(list0).value!!.size, 2)
            assertEquals(sequence(list0).value!![0], true)
            assertEquals(sequence(list0).value!![1], false)

            val list1: ArrayList<Result<Boolean>> = arrayListOf(Result.Failure(TypeMismatchException(Json.Null, "failure")))
            assertTrue(sequence(list1).exception is TypeMismatchException)
        }

        @Test
        fun testMapSequence() {
            val map0 = hashMapOf(Pair("true", Result.Success(true)), Pair("false", Result.Success(false)))
            assertEquals(sequence(map0).value!!.size, 2)
            assertEquals(sequence(map0).value!!["true"], true)
            assertEquals(sequence(map0).value!!["false"], false)

            val map1: HashMap<String, Result<Boolean>> = hashMapOf(Pair("failure", Result.Failure(TypeMismatchException(Json.Null, "failure"))))
            assertTrue(sequence(map1).exception is TypeMismatchException)
        }
    }
}