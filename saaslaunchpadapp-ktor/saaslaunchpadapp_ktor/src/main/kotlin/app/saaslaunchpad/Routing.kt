package app.saaslaunchpad

import app.saaslaunchpad.routes.randomCard
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
import org.slf4j.event.*

fun Application.configureRouting() {
    routing {
        // Root path handler for the test
        get("/") {
            call.respondText("Welcome to SaasLaunchPad API", contentType = ContentType.Text.Plain)
        }
        
        // Register CardRoute properly by applying the extension function directly to the routing block
        randomCard()
        
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
