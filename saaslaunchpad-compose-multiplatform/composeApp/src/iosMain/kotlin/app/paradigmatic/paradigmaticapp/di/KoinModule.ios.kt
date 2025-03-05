package app.saaslaunchpad.saaslaunchpadapp.di

import app.saaslaunchpad.saaslaunchpadapp.data.local.DatabaseDriverFactory
import org.koin.dsl.module
import app.saaslaunchpad.saaslaunchpadapp.data.local.IOSDatabaseDriverFactory
import app.saaslaunchpad.saaslaunchpadapp.data.room.getDatabaseBuilder

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }

    single{ getDatabaseBuilder() }

}