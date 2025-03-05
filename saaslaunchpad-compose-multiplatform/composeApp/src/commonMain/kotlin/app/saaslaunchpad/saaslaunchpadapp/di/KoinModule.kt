package app.saaslaunchpad.saaslaunchpadapp.di

import app.saaslaunchpad.saaslaunchpadapp.data.ParadigmaticDatabase
import app.saaslaunchpad.saaslaunchpadapp.data.local.LocalDatabase
import app.saaslaunchpad.saaslaunchpadapp.data.local.MongoImpl
import app.saaslaunchpad.saaslaunchpadapp.data.local.PreferencesImpl
import app.saaslaunchpad.saaslaunchpadapp.data.remote.api.CurrencyApiServiceImpl
import app.saaslaunchpad.saaslaunchpadapp.data.remote.api.PostApi
import app.saaslaunchpad.saaslaunchpadapp.data.room.getRoomDatabase
import app.saaslaunchpad.saaslaunchpadapp.domain.CurrencyApiService
import app.saaslaunchpad.saaslaunchpadapp.domain.MongoRepository
import app.saaslaunchpad.saaslaunchpadapp.domain.PreferencesRepository
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.HomeViewModel
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.MemeViewModel
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.PostViewModel
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.ManageViewModel
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.DetailViewModel
import org.koin.dsl.module
import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

expect val targetModule: Module

val sharedModule = module {
    single { Settings() }
    single<MongoRepository> { MongoImpl() }
    single<PreferencesRepository> { PreferencesImpl(settings = get()) }
    single<CurrencyApiService> { CurrencyApiServiceImpl(preferences = get() )}

    factory {
        HomeViewModel(
            preferences = get(),
            mongoDb = get(),
            api = get()
        )
    }

    single<PostApi> { PostApi() }
    single<LocalDatabase> { LocalDatabase(
        databaseDriverFactory = get()
    ) }
    single<Settings> { Settings() }

    single<ParadigmaticDatabase> {
        ParadigmaticDatabase(
            api = get(),
            database = get(),
            settings = get()
        )
    }
    factory { PostViewModel(
            database = get()
        )
    }

    single { getRoomDatabase(
            builder = get()
        )
    }

    factory {
        MemeViewModel(
            database = get()
        )
    }

    factory { parameters ->
        ManageViewModel(
            database = get(),
            selectedMemeId = parameters.get()
        )
    }

    factory { parameters ->
        DetailViewModel(
            database = get(),
            selectedMemeId = parameters.get()
        )
    }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}