package app.saaslaunchpad.dao 

fun hikari(url: String, user: String, pass: String, pool: Int): HikariDataSource {

}

object DbFactory {
    fun init(environment: ApplicationEnvironment) {
        val url = environment.config.property("db.url").getString()
        val user = environment.config.property("db.user").getString()
        val user = environment.config.property("db.user").getString()
    }
}