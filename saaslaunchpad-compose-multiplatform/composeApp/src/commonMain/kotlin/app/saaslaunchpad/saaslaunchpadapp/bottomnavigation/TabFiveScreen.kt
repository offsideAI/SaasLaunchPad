package app.saaslaunchpad.saaslaunchpad.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import app.saaslaunchpad.saaslaunchpadapp.auth.DjangoAuthService
import app.saaslaunchpad.saaslaunchpadapp.auth.DjangoUser
import app.saaslaunchpad.saaslaunchpadapp.config.FeatureConfiguration
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.ThemeUtils
import app.saaslaunchpad.saaslaunchpadapp.util.createGradientEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.edit


class TabFiveScreen(
    private val prefs: DataStore<Preferences>
): Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var text by remember {
            mutableStateOf("")
        }
        val auth = remember { Firebase.auth }
        val djangoAuthService = remember { DjangoAuthService() }
        var firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
        var djangoUser: DjangoUser? by remember { mutableStateOf(null) }

        val isLoggedIn by prefs
            .data
            .map {
                val isLoggedInKey = booleanPreferencesKey("isLoggedIn")
                it[isLoggedInKey] ?: false
            }
            .collectAsState(false)

        val isLoggedInDjango by prefs
            .data
            .map {
                val isLoggedInKeyDjango = booleanPreferencesKey("isLoggedInDjango")
                it[isLoggedInKeyDjango] ?: false
            }
            .collectAsState(false)

        val scope = rememberCoroutineScope()

        // Load Django user if that's your active backend
        LaunchedEffect(Unit) {
            if (FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.DJANGO) {
                // djangoUser = djangoAuthService.getCurrentUser()
            } else {
                firebaseUser = auth.currentUser
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = createGradientEffect(
                        colors = ThemeUtils.GradientColors,
                        isVertical = true
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if ((FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.FIREBASE && firebaseUser!= null && isLoggedIn) ||
                (FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.DJANGO && isLoggedInDjango)) {
                    println("We are logged in it seems")
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Profile",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = firebaseUser?.uid ?: "Unknown ID")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    auth.signOut()
                                    prefs.edit { dataStore ->
                                        val isLoggedInKey = booleanPreferencesKey("isLoggedIn")
                                        dataStore[isLoggedInKey] = true
                                    }
                                    navigator?.pop()
                                }
                            }
                        ) {
                            Text(text = "Sign Out")
                        }
                    }
            } else {
                println("We are not logged In")

            }
        }
    }

}
