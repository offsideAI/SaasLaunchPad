package app.saaslaunchpad.saaslaunchpadapp.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.saaslaunchpad.saaslaunchpadapp.SaasLaunchPadDatabase


// SQLDelight & Ktor (Compose Multiplatform)
class AndroidDatabaseDriverFactory (
    private val context: Context
): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            SaasLaunchPadDatabase.Schema,
            context,
            "saaslaunchpad.db"
        )
    }
}