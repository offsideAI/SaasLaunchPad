package app.saaslaunchpad.saaslaunchpadapp

import androidx.compose.ui.window.ComposeUIViewController
import app.saaslaunchpad.saaslaunchpadapp.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }