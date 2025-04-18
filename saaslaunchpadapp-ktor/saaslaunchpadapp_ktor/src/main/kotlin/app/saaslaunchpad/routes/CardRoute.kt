package app.saaslaunchpad.routes

import app.saaslaunchpad.data.model.Card
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val BASE_URL = "http://192.168.86.182:8090"

private val cards = listOf(
    Card("Benjamin", "Easy to teach, paws to beat", "$BASE_URL/dog_1.png"),
    Card("Holly", "A real fast learner!", "$BASE_URL/dog_2.png"),
    Card("Queen Noor", "Queen of all she surveys", "$BASE_URL/dog_3.png"),
    Card("Ron", "He's got a real, driving ambition", "$BASE_URL/dog_4.png"),
    Card("Sky", "The ultimate high-flyer", "$BASE_URL/dog_5.png"),
)

fun Route.randomCard() {
    get("/randomcard") {
        call.respond(
            HttpStatusCode.OK,
            cards.random()
        )
    }
}