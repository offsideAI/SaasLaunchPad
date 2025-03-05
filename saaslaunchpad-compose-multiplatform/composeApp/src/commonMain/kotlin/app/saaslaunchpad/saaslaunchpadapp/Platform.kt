package app.saaslaunchpad.saaslaunchpadapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform