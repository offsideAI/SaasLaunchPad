package app.saaslaunchpad.saaslaunchpadapp.config

import app.saaslaunchpad.saaslaunchpadapp.BuildConfig

actual object ApiConfig {

    init {
        println("ApiConfig initialized")
        println("BuildConfig.CURRENCY_API_KEY length: ${BuildConfig.CURRENCY_API_KEY.length}")
    }

    actual val CURRENCY_API_KEY: String
        get() {
            println("Getting CURRENCY_API_KEY from BuildConfig: ${BuildConfig.CURRENCY_API_KEY}")
            return BuildConfig.CURRENCY_API_KEY
        }

    /* TODO-FIXME
    actual val CURRENCY_API_KEY: String
        get() = BuildConfig.CURRENCY_API_KEY
    */
}

