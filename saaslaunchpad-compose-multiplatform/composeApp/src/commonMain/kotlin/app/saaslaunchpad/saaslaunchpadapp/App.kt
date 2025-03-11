package app.saaslaunchpad.saaslaunchpadapp


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import app.saadlaunchpad.saaslaunchpadapp.bottomnavigation.BottomNavigationMainScreen
import app.saaslaunchpad.saaslaunchpadapp.presentation.screen.LandingScreen
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.darkScheme
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.lightScheme
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>
) {
    val colors = if (!isSystemInDarkTheme()) {
        lightScheme
    } else {
        darkScheme
    }
    // Check if user is logged in
    val isLoggedIn by prefs
        .data
        .map {
            val isLoggedInKey = booleanPreferencesKey("isLoggedIn")
            it[isLoggedInKey] ?: false
        }
        .collectAsState(false)

    MaterialTheme(colorScheme = colors) {
        Surface {
            Navigator(
                screen = LandingScreen(prefs),
                onBackPressed = { currentScreen ->
                    // If the user is logged in and the current screen is BottomNavigationMainScreen,
                    // intercept the back button press
                    if (isLoggedIn && currentScreen is BottomNavigationMainScreen) {
                        println("You are currently logged in")
                        false // Consume the back press event
                    } else {
                        println("Back was pressed on ${currentScreen}")
                        // Default behavior - allow popping the screen
                        true
                    }
                }
            ) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}