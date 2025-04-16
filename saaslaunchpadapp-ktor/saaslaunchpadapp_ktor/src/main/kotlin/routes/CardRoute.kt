package app.saaslaunchpad.routes


private const val BASE_URL = "http://192.168.86.182:8090"

private val cards = listOf(
    Card("Benjamin", "Easy to teach, paws to beat", "$BASE_URL/dog1.png"),
    Card("Holly", "A real fast learner!", "$BASE_URL/dog2.png"),
    Card("Queen Noor", "Queen of all she surveys", "$BASE_URL/dog3.png"),
    Card("Ron", "He's got a real, driving ambition", "$BASE_URL/dog4.png"),
    Card("Sky", "The ultimate high-flyer", "$BASE_URL/dog5.png"),
)

fun Route.randomCard() {
    get("/randomcard") {

    }
}