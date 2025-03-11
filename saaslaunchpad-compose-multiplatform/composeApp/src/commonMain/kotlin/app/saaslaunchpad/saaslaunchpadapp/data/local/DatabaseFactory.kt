package app.saaslaunchpad.saaslaunchpadapp.data.local

import app.cash.sqldelight.db.SqlDriver
import app.saaslaunchpad.saaslaunchpadapp.domain.model.Post
import app.saaslaunchpad.saaslaunchpadapp.data.SaasLaunchPadDatabase

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

class LocalDatabase(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = app.saaslaunchpad.saaslaunchpadapp.SaasLaunchPadDatabase(
        databaseDriverFactory.createDriver()
    )
    // TODO-FIXME-CLEANUP-REVERT-AFTER-DEBUG private val query = database.paradigmaticDatabaseQueries
    private val query = database.saasLaunchPadDatabaseQueries

    fun readAllPosts(): List<Post> {
        println("INFO: Read the cached data from the local database")
        return query.readAlllPosts()
            .executeAsList()
            .map {
                Post(
                    userId = it.userId.toInt(),
                    id = it.id.toInt(),
                    title = it.title,
                    body = it.body
                )
            }
    }

    fun insertAllPosts(posts: List<Post>) {
        println("INFO: Caching the data from the network")
        query.transaction {
            posts.forEach { post ->
                query.insertPost(
                    userId = post.userId.toLong(),
                    id = post.id.toLong(),
                    title = post.title,
                    body = post.body
                )
            }
        }

    }

    fun removeAllPosts() {
        query.removeAllPosts()
    }
}