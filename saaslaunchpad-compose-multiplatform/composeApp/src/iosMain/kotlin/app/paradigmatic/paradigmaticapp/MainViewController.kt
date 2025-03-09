package app.saaslaunchpad.saaslaunchpadapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import app.saaslaunchpad.saaslaunchpadapp.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App(
    prefs = remember {
        createDataStore {

        }
    }
) }