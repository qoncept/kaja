package jp.co.qoncept.kaja

import org.hamcrest.CoreMatchers.*
import org.json.JSONArray
import org.json.JSONObject
import org.junit.AfterClass
import org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.io.File
import java.util.*

@RunWith(Enclosed::class)
class JsonTest {
    class BooleanTest {
        val value = Json.Boolean(true)

        @Test
        fun testBoolean() {
            assertThat(value.boolean.value, `is`(true))
        }

        @Test
        fun testInt() {
            assertThat(value.int.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testLong() {
            assertThat(value.long.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testDouble() {
            assertThat(value.double.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testString() {
            assertThat(value.string.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testList() {
            assertThat(value.list.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testMap() {
            assertThat(value.map.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testGet() {
            assertThat(value.get("boolean").exception, instanceOf(TypeMismatchException::class.java))
        }
    }

    class NumberTest {
        val intValue = Json.Number(1)
        val longValue = Json.Number(2147483648)
        val doubleValue = Json.Number(0.1)
        @Test
        fun testBoolean() {
            assertThat(intValue.boolean.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(longValue.boolean.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(doubleValue.boolean.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testInt() {
            assertThat(intValue.int.value, `is`(1))
            assertThat(longValue.int.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(doubleValue.int.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testLong() {
            assertThat(intValue.long.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(longValue.long.value, `is`(2147483648))
            assertThat(doubleValue.long.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testDouble() {
            assertThat(intValue.double.value, `is`(1.0))
            assertThat(longValue.double.value, `is`(2147483648.0))
            assertThat(doubleValue.double.value, `is`(0.1))
        }

        @Test
        fun testString() {
            assertThat(intValue.string.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(longValue.string.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(doubleValue.string.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testList() {
            assertThat(intValue.list.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(longValue.list.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(doubleValue.list.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testMap() {
            assertThat(intValue.map.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(longValue.map.exception, instanceOf(TypeMismatchException::class.java))
            assertThat(doubleValue.map.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testGet() {
            assertThat(intValue.get("int").exception, instanceOf(TypeMismatchException::class.java))
            assertThat(longValue.get("long").exception, instanceOf(TypeMismatchException::class.java))
            assertThat(doubleValue.get("double").exception, instanceOf(TypeMismatchException::class.java))
        }
    }

    class StringTest {
        val value = Json.String("string")
        @Test
        fun testBoolean() {
            assertThat(value.boolean.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testInt() {
            assertThat(value.int.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testLong() {
            assertThat(value.long.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testDouble() {
            assertThat(value.double.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testString() {
            assertThat(value.string.value, `is`("string"))
        }

        @Test
        fun testList() {
            assertThat(value.list.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testMap() {
            assertThat(value.map.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testGet() {
            assertThat(value.get("string").exception, instanceOf(TypeMismatchException::class.java))
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
            assertThat(value.boolean.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testInt() {
            assertThat(value.int.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testLong() {
            assertThat(value.long.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testDouble() {
            assertThat(value.double.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testString() {
            assertThat(value.string.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testList() {
            assertThat(value.list.value!!.size, `is`(2))
            assertThat(value.list.value!![0].boolean.value, `is`(true))
            assertThat(value.list.value!![1].boolean.value, `is`(false))
        }

        @Test
        fun testMap() {
            assertThat(value.map.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testGet() {
            assertThat(value.get("array").exception, instanceOf(TypeMismatchException::class.java))
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
            assertThat(value.boolean.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testInt() {
            assertThat(value.int.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testLong() {
            assertThat(value.long.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testDouble() {
            assertThat(value.double.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testString() {
            assertThat(value.string.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testList() {
            assertThat(value.list.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testMap() {
            assertThat(value.map.value!!.size, `is`(6))
            assertThat(value.map.value!!["boolean"]!!.boolean.value, `is`(true))
            assertThat(value.map.value!!["number"]!!.int.value, `is`(0))
            assertThat(value.map.value!!["string"]!!.string.value, `is`("string"))
            assertThat(value.map.value!!["array"]!!.list.value, instanceOf(List::class.java))
            assertThat(value.map.value!!["jsonNull"]!!, `is`(Json.Null as Json))
            assertThat(value.map.value!!["object"]!!.map.value, instanceOf(Map::class.java))
            assertThat(value.map.value!!["null"], nullValue())
        }

        @Test
        fun testGet() {
            assertThat(value.get("boolean").value!!.boolean.value, `is`(true))
            assertThat(value.get("number").value!!.int.value, `is`(0))
            assertThat(value.get("string").value!!.string.value, `is`("string"))
            assertThat(value.get("array").value!!.list.value, instanceOf(List::class.java))
            assertThat(value.get("jsonNull").value, `is`(Json.Null as Json))
            assertThat(value.get("object").value!!.map.value, instanceOf(Map::class.java))
            assertThat(value.get("null").exception, instanceOf(MissingKeyException::class.java))
        }
    }

    class NullTest {
        val value = Json.Null
        @Test
        fun testBoolean() {
            assertThat(value.boolean.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testInt() {
            assertThat(value.int.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testLong() {
            assertThat(value.long.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testDouble() {
            assertThat(value.double.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testString() {
            assertThat(value.string.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testList() {
            assertThat(value.list.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testMap() {
            assertThat(value.map.exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testGet() {
            assertThat(value.get("jsonNull").exception, instanceOf(TypeMismatchException::class.java))
        }
    }

    class OfTest {
        @Test
        fun testOfBoolean() {
            assertThat(Json.of(true).boolean.value, `is`(true))
        }

        @Test
        fun testOfInt() {
            assertThat(Json.of(1).int.value, `is`(1))
        }

        @Test
        fun testOfLong() {
            assertThat(Json.of(2147483648).long.value, `is`(2147483648))
        }

        @Test
        fun testOfDouble() {
            assertThat(Json.of(1.0).double.value, `is`(1.0))
        }

        @Test
        fun testOfString() {
            assertThat(Json.of("string").string.value, `is`("string"))
        }

        @Test
        fun testOfList() {
            val value = arrayListOf(Json.Boolean(true))
            assertThat(Json.of(value).list.value!![0].boolean.value, `is`(true))
        }

        @Test
        fun testOfMap() {
            val value = hashMapOf(Pair("true", Json.Boolean(true)))
            assertThat(Json.of(value), instanceOf(Json::class.java))
        }
    }

    class ParseTest {
        companion object {
            val string = """
            |{"boolean": true}
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
            assertThat(Json.parse(file).get("boolean").boolean.value, `is`(true))
        }

        @Test
        fun testParseFromInputStream() {
            val inputStream = string.byteInputStream()
            assertThat(Json.parse(inputStream).get("boolean").boolean.value, `is`(true))
        }

        @Test
        fun testParseFromByteArray() {
            val byteArray = string.toByteArray()
            assertThat(Json.parse(byteArray).get("boolean").boolean.value, `is`(true))
        }

        @Test
        fun testParseFromString() {
            assertThat(Json.parse(string).get("boolean").boolean.value, `is`(true))
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

        val decode: (Json) -> Decoded<Boolean> = { pure(false) }

        assertThat(boolean.list(decode).exception, instanceOf(TypeMismatchException::class.java))
        assertThat(number.list(decode).exception, instanceOf(TypeMismatchException::class.java))
        assertThat(string.list(decode).exception, instanceOf(TypeMismatchException::class.java))
        assertThat(jsonArray.list(decode).value!![0], `is`(false))
        assertThat(jsonObject.list(decode).exception, instanceOf(TypeMismatchException::class.java))
        assertThat(jsonNull.list(decode).exception, instanceOf(TypeMismatchException::class.java))
    }

    class SequenceTest {
        @Test
        fun testArraySequence() {
            val list0 = arrayListOf(Decoded.Success(true), Decoded.Success(false))
            assertThat(sequence(list0).value!!.size, `is`(2))
            assertThat(sequence(list0).value!![0], `is`(true))
            assertThat(sequence(list0).value!![1], `is`(false))

            val list1: ArrayList<Decoded<Boolean>> = arrayListOf(Decoded.Failure(TypeMismatchException(Json.Null, "failure")))
            assertThat(sequence(list1).exception, instanceOf(TypeMismatchException::class.java))
        }

        @Test
        fun testMapSequence() {
            val map0 = hashMapOf(Pair("true", Decoded.Success(true)), Pair("false", Decoded.Success(false)))
            assertThat(sequence(map0).value!!.size, `is`(2))
            assertThat(sequence(map0).value!!["true"], `is`(true))
            assertThat(sequence(map0).value!!["false"], `is`(false))

            val map1: HashMap<String, Decoded<Boolean>> = hashMapOf(Pair("failure", Decoded.Failure(TypeMismatchException(Json.Null, "failure"))))
            assertThat(sequence(map1).exception, instanceOf(TypeMismatchException::class.java))
        }
    }
}