package jp.co.qoncept.kaja

import jp.co.qoncept.kotres.ap
import jp.co.qoncept.kotres.mp
import org.testng.Assert.*
import org.testng.annotations.Test

class ResultTest {
    @Test
    fun testOptionalWithDefaultValue() {
        data class Person(
                val firstName: String,
                val middleName: String,
                val lastName: String,
                val age: Int
        )
        val json = Json.parse("""{
        |  "firstName": "John",
        |  "lastName": "Neumann",
        |  "age": 53
        }""".trimMargin())

        run {
            val person = curry(::Person) mp
                    json["firstName"].string ap
                    json["middleName"].string.optional("von") ap
                    json["lastName"].string ap
                    json["age"].int

            assertEquals(person.value!!.firstName, "John")
            assertEquals(person.value!!.middleName, "von")
            assertEquals(person.value!!.lastName, "Neumann")
            assertEquals(person.value!!.age, 53)
        }

        run {
            val person = curry(::Person) mp
                    json["firstName"].string ap
                    json.string.optional("von") ap
                    json["lastName"].string ap
                    json["age"].int

            assertTrue(person.exception!! is TypeMismatchException)
        }
    }

    @Test
    fun testOptional() {
        data class Person(
                val firstName: String,
                val middleName: String?,
                val lastName: String,
                val age: Int
        )
        val json = Json.parse("""{
        |  "firstName": "Albert",
        |  "lastName": "Einstein",
        |  "age": 28
        }""".trimMargin())

        run {
            val person = curry(::Person) mp
                    json["firstName"].string ap
                    json["middleName"].string.optional ap
                    json["lastName"].string ap
                    json["age"].int

            assertEquals(person.value!!.firstName, "Albert")
            assertEquals(person.value!!.middleName, null)
            assertEquals(person.value!!.lastName, "Einstein")
            assertEquals(person.value!!.age, 28)
        }

        run {
            val person = curry(::Person) mp
                    json["firstName"].string ap
                    json.string.optional ap
                    json["lastName"].string ap
                    json["age"].int

            assertTrue(person.exception!! is TypeMismatchException)
        }
    }
}

private fun <A, B, C, D, E> curry(function: (A, B, C, D) -> E): (A) -> (B) -> (C) -> (D) -> E {
    return { a -> { b -> { c -> { d -> function(a, b, c, d) } } } }
}
