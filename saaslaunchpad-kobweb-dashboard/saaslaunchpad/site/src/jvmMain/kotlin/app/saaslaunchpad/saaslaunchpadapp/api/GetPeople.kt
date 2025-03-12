package app.saaslaunchpad.saaslaunchpadapp.api

import app.saaslaunchpad.saaslaunchpadapp.models.ApiResponse
import app.saaslaunchpad.saaslaunchpadapp.models.Person
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val people = listOf(
    Person(name = "Alice", age = 31),
    Person(name = "Bob", age = 32),
    Person(name = "John", age = 33),
    Person(name = "Jane", age = 34),
)

@Api(routeOverride = "/api/getpeople")
suspend fun getPeople(context: ApiContext) {
    try {
        val number = context.req.params.getValue("count").toInt()
        context.res.setBodyText(
            Json.encodeToString<ApiResponse>(
                ApiResponse.Success(data = people.take(number))
            )
        )
    } catch (e: Exception) {
        Json.encodeToString<ApiResponse>(
           ApiResponse.Error(errorMessage = e.message.toString())
        )
    }
}