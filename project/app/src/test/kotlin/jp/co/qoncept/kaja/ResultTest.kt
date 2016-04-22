package jp.co.qoncept.kaja

import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

/**
 * Created by nisho on 16/04/17.
 */


class ResultTest {

    private fun <A, B, C, D> curry(function: (A, B, C) -> D): (A) -> (B) -> (C) -> D {
        return { a -> { b -> { c -> function(a, b, c) } } }
    }

    private data class Person(val firstName: String, val lastName: String, val age: Int?)

    @Test
    fun testBasic() {
        run {
            val firstName: Result<String> = Result.Success("Qon")
            val lastName: Result<String> = Result.Success("cept")
            val age: Result<Int?> = Result.Success(1)
            val result: Result<Person> =
                    (curry(::Person)
                            mp firstName
                            ap lastName
                            ap age)
            assertThat(result.value?.firstName, `is`("Qon"))
            assertThat(result.value?.lastName, `is`("cept"))
            assertThat(result.value?.age, `is`(1))
        }
        run {
            val firstName: Result<String> = Result.Failure(MissingKeyException(Json.of(""), "firstName", null))
            val lastName: Result<String> = Result.Success("cept")
            val age: Result<Int?> = Result.Success(1)
            val result: Result<Person> =
                    (curry(::Person)
                            mp firstName
                            ap lastName
                            ap age)
            assertThat(result.exception, `is`(firstName.exception))
        }
        run {
            val firstName: Result<String> = Result.Success("Qon")
            val lastName: Result<String> = Result.Failure(MissingKeyException(Json.of(""), "lastName", null))
            val age: Result<Int?> = Result.Success(1)
            val result: Result<Person> =
                    (curry(::Person)
                            mp firstName
                            ap lastName
                            ap age)
            assertThat(result.exception, `is`(lastName.exception))
        }
        run {
            val firstName: Result<String> = Result.Success("Qon")
            val lastName: Result<String> = Result.Success("cept")
            val age: Result<Int?> = Result.Failure(TypeMismatchException(Json.of(""), "Int", null))
            val result: Result<Person> =
                    (curry(::Person)
                            mp firstName
                            ap lastName
                            ap age)
            assertThat(result.exception, `is`(age.exception))
        }
        run {
            val firstName: Result<String> = Result.Failure(MissingKeyException(Json.of(""), "firstName", null))
            val lastName: Result<String> = Result.Failure(MissingKeyException(Json.of(""), "lastName", null))
            val age: Result<Int?> = Result.Failure(TypeMismatchException(Json.of(""), "Int", null))
            val result: Result<Person> =
                    (curry(::Person)
                            mp firstName
                            ap lastName
                            ap age)
            assertThat(result.exception, `is`(firstName.exception))
        }
    }

    @Test
    fun testGetValue() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.value, `is`(1))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.value, nullValue())
        }
    }

    @Test
    fun testGetException() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.exception, nullValue())
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.exception, `is`(value.exception))
        }
    }

    @Test
    fun testOr() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.or(pure(2)), `is`(pure(1)))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.or(pure(2)), `is`(pure(2)))
        }
    }

    @Test
    fun testOr1() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.or(2), `is`(1))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.or(2), `is`(2))
        }
    }

    @Test
    fun testIfMissingKey() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.ifMissingKey(2), `is`(pure(1)))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.ifMissingKey(2), `is`(pure(2)))
        }
        run {
            val value: Result<Int> = Result.Failure(TypeMismatchException(Json.of("a"), "Int", null))
            assertThat(value.ifMissingKey(2).exception, `is`(value.exception))
        }
    }

    @Test
    fun testNullIfMissingKey() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.nullIfMissingKey(), `is`(pure(1).map { it as Int? }))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.nullIfMissingKey(), `is`(pure(null as Int?)))
        }
        run {
            val value: Result<Int> = Result.Failure(TypeMismatchException(Json.of("a"), "Int", null))
            assertThat(value.nullIfMissingKey().exception, `is`(value.exception))
        }
    }

    @Test
    fun testMap() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.map { it.toString() }, `is`(pure("1")))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.map { it.toString() }.exception, `is`(value.exception))
        }
    }

    @Test
    fun testFlatMap() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.flatMap { pure(it.toString()) }, `is`(pure("1")))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.flatMap { pure(it.toString()) }.exception, `is`(value.exception))
        }
    }

    @Test
    fun testApply() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(value.apply(pure({ a: Int -> a.toString() })), `is`(pure("1")))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(value.apply(pure({ a: Int -> a.toString() })).exception, `is`(value.exception))
        }
    }

    @Test
    fun testAp() {
        run {
            val value: Result<Int> = pure(1)
            assertThat((pure({ a: Int -> a.toString() }) ap value), `is`(pure("1")))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat((pure({ a: Int -> a.toString() }) ap value).exception, `is`(value.exception))
        }
    }

    @Test
    fun testMp() {
        run {
            val value: Result<Int> = pure(1)
            assertThat(({ it: Int -> it.toString() } mp value), `is`(pure("1")))
        }
        run {
            val value: Result<Int> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(({ it: Int -> it.toString() } mp value).exception, `is`(value.exception))
        }
    }

    @Test
    fun testFlatten() {
        run {
            val successSuccess: Result<Result<Int>> = pure(pure(1))
            assertThat(successSuccess.flatten(), `is`(pure(1)))
        }
        run {
            val successFailure: Result<Result<Int>> = pure(Result.Failure(MissingKeyException(Json.of("a"), "width", null)))
            assertThat(successFailure.flatten().exception, `is`(successFailure.value?.exception))
        }
        run {
            val failure: Result<Result<Int>> = Result.Failure(MissingKeyException(Json.of("a"), "width", null))
            assertThat(failure.flatten().exception, `is`(failure.exception))
        }
    }
}

