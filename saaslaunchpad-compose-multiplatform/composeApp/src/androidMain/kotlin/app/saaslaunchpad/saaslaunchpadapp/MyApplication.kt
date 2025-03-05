package app.saaslaunchpad.saaslaunchpadapp

import android.app.Application
import app.saaslaunchpad.saaslaunchpadapp.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = { androidContext(this@MyApplication )}
        )
    }
}