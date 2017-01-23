# Kaja

_Kaja_ is a JSON decoder for Kotlin inspired by [Argo](https://github.com/thoughtbot/Argo) and [SwiftyJSON](https://github.com/SwiftyJSON/SwiftyJSON).

```kotlin
val json: Result<Json, JsonException>
        = Json.parse(jsonString)

val person: Result<Person, JsonException>
        = curry(::Person) mp
            json["firstName"].string ap
            json["lastName"] .string ap
            json["age"].int
```

The code above is one to parse JSONs formatted like the following

```json
{
  "firstName": "Albert",
  "lastName": "Einstein",
  "age": 28
}
```

and decode it to a `Person`.

```kotlin
data class Person(
        val firstName: String,
        val lastName: String,
        val age: Int
)
```


## Naming

_Kaja_ is derived from _caja_ (box) in Spanish. _Kaja_'s _K_ and _j_ stand for Kotlin and JSON respectively.

## License

[The MIT License](LICENSE)
