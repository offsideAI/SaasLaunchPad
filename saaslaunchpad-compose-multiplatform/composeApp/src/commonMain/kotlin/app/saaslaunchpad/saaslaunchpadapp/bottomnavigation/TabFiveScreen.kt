package app.saaslaunchpad.saaslaunchpad.bottomnavigation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.saaslaunchpad.saaslaunchpadapp.auth.DjangoAuthService
import app.saaslaunchpad.saaslaunchpadapp.auth.DjangoUser
import app.saaslaunchpad.saaslaunchpadapp.config.FeatureConfiguration
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent



class TabFiveScreen: Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var text by remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()
        val auth = remember { Firebase.auth }
        val djangoAuthService = remember { DjangoAuthService() }
        var firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
        var djangoUser: DjangoUser? by remember { mutableStateOf(null) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if ((FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.FIREBASE && firebaseUser != null) ||
                (FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.DJANGO && djangoUser != null)) {
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
                                firebaseUser = auth.currentUser
                            }
                        }
                    ) {
                        Text(text = "Sign Out")
                    }

                }
            } else {

            }
        }
    }

}
