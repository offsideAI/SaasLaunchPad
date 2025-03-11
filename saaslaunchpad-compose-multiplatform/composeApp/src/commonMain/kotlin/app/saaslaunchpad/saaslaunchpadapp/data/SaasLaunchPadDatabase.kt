package app.saaslaunchpad.saaslaunchpadapp.data

import app.saaslaunchpad.saaslaunchpadapp.data.local.LocalDatabase
import app.saaslaunchpad.saaslaunchpadapp.data.remote.api.PostApi
import app.saaslaunchpad.saaslaunchpadapp.domain.model.PostApiRequestState
import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import app.saaslaunchpad.saaslaunchpadapp.domain.model.Post

const val FRESH_DATA_KEY = "freshDataTimestamp"

class SaasLaunchPadDatabase (
    private val api: PostApi,
    private val database: LocalDatabase,
    private val settings: Settings
    ) {
    @Throws(Exception::class)
    suspend fun getAllPosts(): PostApiRequestState<List<Post>> {

        /* TODO-FIXME-CLEAN-REVERT-AFTER-DEBUG
        return try {
            val cachedPosts = database.readAllPosts()
            if (cachedPosts.isEmpty()) {
                settings.putLong(
                    FRESH_DATA_KEY,
                    Clock.System.now().toEpochMilliseconds()
                )
                PostApiRequestState.Success(
                    api.fetchAllPosts().also {
                        database.removeAllPosts()
                        database.insertAllPosts(it)
                    }
                )
            } else {
                if (isDataStale()) {
                    settings.putLong(
                        FRESH_DATA_KEY,
                        Clock.System.now().toEpochMilliseconds()
                    )
                    PostApiRequestState.Success(
                        api.fetchAllPosts().also {
                            database.removeAllPosts()
                            database.insertAllPosts(it)
                        }
                    )
                } else PostApiRequestState.Success(cachedPosts)
            }
        } catch (e: Exception) {
            PostApiRequestState.Error(e.message.toString())
        }
        */

        return try {
                PostApiRequestState.Success(
                    api.fetchAllPosts()
                )
        } catch(e: Exception) {
            PostApiRequestState.Error(e.message.toString())
        }
    }



    private fun isDataStale(): Boolean {
        val savedTimestamp = Instant.fromEpochMilliseconds(
            settings.getLong(FRESH_DATA_KEY, defaultValue = 0L)
        )

        val currentTimestamp = Clock.System.now()
        val difference =
            if (savedTimestamp > currentTimestamp) savedTimestamp - currentTimestamp
            else currentTimestamp - savedTimestamp

        return difference >= 24.hours

    }
}