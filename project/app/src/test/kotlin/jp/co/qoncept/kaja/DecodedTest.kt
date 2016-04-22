package jp.co.qoncept.kaja

import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

/**
 * Created by nisho on 16/04/17.
 */

@RunWith(Enclosed::class)
class DecodedTest {

    class BasicTest {
        private fun <A, B, C, D> curry(function: (A, B, C) -> D): (A) -> (B) -> (C) -> D {
            return { a -> { b -> { c -> function(a, b, c) } } }
        }

        private data class Person(val firstName: String, val lastName: String, val age: Int?)

        @Test
        fun testBasic() {
            run {
                val firstName: Decoded<String> = Decoded.Success("Qon")
                val lastName: Decoded<String> = Decoded.Success("cept")
                val age: Decoded<Int?> = Decoded.Success(1)
                val result: Decoded<Person> =
                        (curry(::Person)
                                mp firstName
                                ap lastName
                                ap age)
                assertThat(result.value?.firstName, `is`("Qon"))
                assertThat(result.value?.lastName, `is`("cept"))
                assertThat(result.value?.age, `is`(1))
            }
            run {
                val firstName: Decoded<String> = Decoded.Failure(MissingKeyException(Json.of(""), "firstName", null))
                val lastName: Decoded<String> = Decoded.Success("cept")
                val age: Decoded<Int?> = Decoded.Success(1)
                val result: Decoded<Person> =
                        (curry(::Person)
                                mp firstName
                                ap lastName
                                ap age)
                assertThat(result.exception, `is`(firstName.exception))
            }
            run {
                val firstName: Decoded<String> = Decoded.Success("Qon")
                val lastName: Decoded<String> = Decoded.Failure(MissingKeyException(Json.of(""), "lastName", null))
                val age: Decoded<Int?> = Decoded.Success(1)
                val result: Decoded<Person> =
                        (curry(::Person)
                                mp firstName
                                ap lastName
                                ap age)
                assertThat(result.exception, `is`(lastName.exception))
            }
            run {
                val firstName: Decoded<String> = Decoded.Success("Qon")
                val lastName: Decoded<String> = Decoded.Success("cept")
                val age: Decoded<Int?> = Decoded.Failure(TypeMismatchException(Json.of(""), "Int", null))
                val result: Decoded<Person> =
                        (curry(::Person)
                                mp firstName
                                ap lastName
                                ap age)
                assertThat(result.exception, `is`(age.exception))
            }
            run {
                val firstName: Decoded<String> = Decoded.Failure(MissingKeyException(Json.of(""), "firstName", null))
                val lastName: Decoded<String> = Decoded.Failure(MissingKeyException(Json.of(""), "lastName", null))
                val age: Decoded<Int?> = Decoded.Failure(TypeMismatchException(Json.of(""), "Int", null))
                val result: Decoded<Person> =
                        (curry(::Person)
                                mp firstName
                                ap lastName
                                ap age)
                assertThat(result.exception, `is`(firstName.exception))
            }
        }
    }


    class SuccessTest {
        val value: Decoded<Int> = pure(1)

        @Test
        fun testGetValue() {
            assertThat(value.value, `is`(1))
        }

        @Test
        fun testGetException() {
            assertThat(value.exception, nullValue())
        }

        @Test
        fun testOr() {
            assertThat(value.or(pure(2)), `is`(pure(1)))
        }

        @Test
        fun testOr1() {
            assertThat(value.or(2), `is`(1))
        }

        @Test
        fun testIfMissingKey() {
            assertThat(value.ifMissingKey(2), `is`(pure(1)))
        }

        @Test
        fun testNullIfMissingKey() {
            assertThat(value.nullIfMissingKey(), `is`(pure(1).map { it as Int? }))
        }

        @Test
        fun testMap() {
            assertThat(value.map { it.toString() }, `is`(pure("1")))
        }

        @Test
        fun testFlatMap() {
            assertThat(value.flatMap { pure(it.toString()) }, `is`(pure("1")))
        }

        @Test
        fun testApply() {
            assertThat(value.apply(pure({ a: Int -> a.toString() })), `is`(pure("1")))
        }

        @Test
        fun testAp() {
            assertThat((pure({ a: Int -> a.toString() }) ap value), `is`(pure("1")))
        }

        @Test
        fun testMp() {
            assertThat(({ it: Int -> it.toString() } mp value), `is`(pure("1")))
        }
    }


    class MissingKeyFailureTest {
        val value: Decoded<Int> = Decoded.Failure(MissingKeyException(Json.of("a"), "width", null))

        @Test
        fun testGetValue() {
            assertThat(value.value, nullValue())
        }

        @Test
        fun testGetException() {
            assertThat(value.exception, `is`(value.exception))
        }

        @Test
        fun testOr() {
            assertThat(value.or(pure(2)), `is`(pure(2)))
        }

        @Test
        fun testOr1() {
            assertThat(value.or(2), `is`(2))
        }

        @Test
        fun testIfMissingKey() {
            assertThat(value.ifMissingKey(2), `is`(pure(2)))
        }

        @Test
        fun testNullIfMissingKey() {
            assertThat(value.nullIfMissingKey(), `is`(pure(null as Int?)))
        }

        @Test
        fun testMap() {
            assertThat(value.map { it.toString() }.exception, `is`(value.exception))
        }

        @Test
        fun testFlatMap() {
            assertThat(value.flatMap { pure(it.toString()) }.exception, `is`(value.exception))
        }

        @Test
        fun testApply() {
            assertThat(value.apply(pure({ a: Int -> a.toString() })).exception, `is`(value.exception))
        }

        @Test
        fun testAp() {
            assertThat((pure({ a: Int -> a.toString() }) ap value).exception, `is`(value.exception))
        }

        @Test
        fun testMp() {
            assertThat(({ it: Int -> it.toString() } mp value).exception, `is`(value.exception))
        }
    }

    class TypeMismatchFailureTest {
        val value: Decoded<Int> = Decoded.Failure(TypeMismatchException(Json.of("a"), "Int", null))

        @Test
        fun testGetValue() {
            assertThat(value.value, nullValue())
        }

        @Test
        fun testGetException() {
            assertThat(value.exception, `is`(value.exception))
        }

        @Test
        fun testOr() {
            assertThat(value.or(pure(2)), `is`(pure(2)))
        }

        @Test
        fun testOr1() {
            assertThat(value.or(2), `is`(2))
        }

        @Test
        fun testIfMissingKey() {
            assertThat(value.ifMissingKey(2).exception, `is`(value.exception))
        }

        @Test
        fun testNullIfMissingKey() {
            assertThat(value.nullIfMissingKey().exception, `is`(value.exception))
        }

        @Test
        fun testMap() {
            assertThat(value.map { it.toString() }.exception, `is`(value.exception))
        }

        @Test
        fun testFlatMap() {
            assertThat(value.flatMap { pure(it.toString()) }.exception, `is`(value.exception))
        }

        @Test
        fun testApply() {
            assertThat(value.apply(pure({ a: Int -> a.toString() })).exception, `is`(value.exception))
        }

        @Test
        fun testAp() {
            assertThat((pure({ a: Int -> a.toString() }) ap value).exception, `is`(value.exception))
        }

        @Test
        fun testMp() {
            assertThat(({ it: Int -> it.toString() } mp value).exception, `is`(value.exception))
        }
    }

    class FlattenTest {
        val successSuccess: Decoded<Decoded<Int>> = pure(pure(1))
        val successFailure: Decoded<Decoded<Int>> = pure(Decoded.Failure(MissingKeyException(Json.of("a"), "width", null)))
        val failure: Decoded<Decoded<Int>> = Decoded.Failure(MissingKeyException(Json.of("a"), "width", null))
        val message = "MissingKey(width)"
        @Test
        fun testFlatten() {
            assertThat(successSuccess.flatten(), `is`(pure(1)))
            assertThat(successFailure.flatten().exception, `is`(successFailure.value?.exception))
            assertThat(failure.flatten().exception, `is`(failure.exception))
        }
    }
}

