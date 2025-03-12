package app.saaslaunchpad.saaslaunchpadapp.api

import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext

@InitApi
fun initApi(ctx: InitApiContext) {
    // Register any API-wide middleware or configurations here
    println("API initialized successfully!")
}