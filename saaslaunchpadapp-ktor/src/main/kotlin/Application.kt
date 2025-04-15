package app.saaslaunchpad

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureRouting()
    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(StatusPages) {
        exception<Throwable> { call, cause -> 
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    DbFactory.init(environment)
    val taskDao = TaskDao()

    install(Routing) {
        task(taskDao)
    }


}
