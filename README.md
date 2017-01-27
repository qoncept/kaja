# Kaja

_Kaja_ is a type-safe JSON parser/decoder for Kotlin inspired by [Argo](https://github.com/thoughtbot/Argo) and [SwiftyJSON](https://github.com/SwiftyJSON/SwiftyJSON).

```kotlin
val json: Result<Json, JsonException>
    = Json.parse(jsonString)

val person: Result<Person, JsonException>
    = curry(::Person) mp
        json["firstName"].string ap
        json["middleName"].string.optional ap
        json["lastName"].string ap
        json["age"].int ap
        json["knownFor"].list(Json::string)
```

The code above is one to parse JSONs formatted like the following

```json
{
    "firstName": "Albert",
    "lastName": "Einstein",
    "age": 76,
    "knownFor": [
        "General relativity",
        "Special relativity",
        "Photoelectric effect",
        "Mass-energy equivalence",
        "Theory of Brownian motion"
    ]
}
```

and decode it to a `Person`.

```kotlin
data class Person(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val age: Int,
    val knownFor: List<String>
)
```

## Naming

_Kaja_ is derived from _caja_ (box) in Spanish. _Kaja_'s _K_ and _j_ stand for Kotlin and JSON respectively.

## License

[The MIT License](LICENSE)
