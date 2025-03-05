package app.saaslaunchpad.saaslaunchpadapp.di

import app.saaslaunchpad.saaslaunchpadapp.data.local.AndroidDatabaseDriverFactory
import app.saaslaunchpad.saaslaunchpadapp.data.local.DatabaseDriverFactory
import app.saaslaunchpad.saaslaunchpadapp.data.room.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }

    single{ getDatabaseBuilder(context = get()) }
}