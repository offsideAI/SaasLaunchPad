package app.saaslaunchpad.saaslaunchpadapp


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import app.saaslaunchpad.saaslaunchpadapp.presentation.screen.LandingScreen
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.darkScheme
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.lightScheme
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview

/* TODO-FIXME-CLEANUP
@Composable
@Preview
fun App() {
    val colors = if (!isSystemInDarkTheme()) {
        lightScheme
    } else {
        darkScheme
    }
    MaterialTheme(colorScheme = colors) {
        Surface {
            MainScreen()
        }
    }
}
*/

@Composable
@Preview
fun App() {
    // TODO-FIXME-BRINGBACK initializeKoin()
    val colors = if (!isSystemInDarkTheme()) {
        // TODO-FIXME
        // TODO-FIXME-Commenting out to force darkScheme
        lightScheme
    } else {
        darkScheme
    }
    MaterialTheme(colorScheme = colors) {
        Surface {
            /* TODO-FIXME-CLEANUP
            Navigator(BottomNavigationMainScreen()) { navigator ->
                SlideTransition(navigator)
            }
            */
            Navigator(LandingScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}