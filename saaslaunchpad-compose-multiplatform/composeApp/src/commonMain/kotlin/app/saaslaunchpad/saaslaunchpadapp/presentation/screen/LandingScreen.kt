package app.saaslaunchpad.saaslaunchpadapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.saadlaunchpad.saaslaunchpadapp.bottomnavigation.BottomNavigationMainScreen
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.surfaceContainerDark
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth


class LandingScreen(): Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var text by remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()
        val auth = remember { Firebase.auth }
        val firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceContainerDark),

            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Text(
                    text = "SaaS Launch Pad",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                /* TODO-FIXME-CLEANUP
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                */
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fullstack Saas Platform Starter Kit",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        navigator?.push(BottomNavigationMainScreen())
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .background(surfaceContainerDark)
                ) {
                    Text(text = "Get Started")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}
