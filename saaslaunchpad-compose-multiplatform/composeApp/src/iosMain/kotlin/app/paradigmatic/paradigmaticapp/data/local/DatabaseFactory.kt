package app.saaslaunchpad.saaslaunchpadapp.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.saaslaunchpad.saaslaunchpadapp.data.local.DatabaseDriverFactory
import app.saaslaunchpad.saaslaunchpadapp.SaasLaunchPadDatabase
import co.touchlab.sqliter.native.NativeStatement

// SQLDelight & Ktor (Compose Multiplatform)
class IOSDatabaseDriverFactory(): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            SaasLaunchPadDatabase.Schema,
            "saaslaunchpad.db"
        )
    }
}
