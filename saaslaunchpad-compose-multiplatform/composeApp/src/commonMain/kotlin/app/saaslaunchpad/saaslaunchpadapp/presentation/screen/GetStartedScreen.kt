package app.saaslaunchpad.saaslaunchpadapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import app.saadlaunchpad.saaslaunchpadapp.bottomnavigation.BottomNavigationMainScreen
import app.saaslaunchpad.saaslaunchpadapp.auth.DjangoAuthService
import app.saaslaunchpad.saaslaunchpadapp.auth.DjangoUser
import app.saaslaunchpad.saaslaunchpadapp.config.FeatureConfiguration
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.ThemeUtils
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.surfaceContainerDark
import app.saaslaunchpad.saaslaunchpadapp.util.createGradientEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import saaslaunchpad.composeapp.generated.resources.Res
import saaslaunchpad.composeapp.generated.resources.app_icon_splash_vector

class GetStartedScreen(
    private val prefs: DataStore<Preferences>
): Screen{
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
        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

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

        BindEffect(controller)

        LaunchedEffect(Unit) {
            if (FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.DJANGO) {
                // djangoUser = djangoAuthService.getCurrentUser()
            } else {
                firebaseUser = auth.currentUser
            }
        }
        if ((FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.FIREBASE && firebaseUser!= null && isLoggedIn) ||
            (FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.DJANGO && isLoggedInDjango)) {
            println("We are logged in it seems - navigate to BottomNavigationMainScreen")
            navigator?.push(BottomNavigationMainScreen(prefs = prefs))
        } else {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {

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
                    if ((FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.FIREBASE && firebaseUser == null) ||
                        (FeatureConfiguration.AuthBackend.ACTIVE == FeatureConfiguration.AuthBackend.DJANGO && djangoUser == null)) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SaaS Launch Pad",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Icon(
                                painter = painterResource(Res.drawable.app_icon_splash_vector),
                                contentDescription = "Profile",
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    checkPermissions(
                                        permission = Permission.REMOTE_NOTIFICATION,
                                        controller = controller,
                                        snackbarHostState = snackbarHostState
                                    )
                                }
                            }
                            ) {
                                Text("Allow notifications")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    checkPermissions(
                                        permission = Permission.LOCATION,
                                        controller = controller,
                                        snackbarHostState = snackbarHostState
                                    )
                                }
                            }
                            ) {
                                Text("Allow location permission")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Login",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)
                                    .clickable {
                                        navigator?.push(LoginScreen(prefs))
                                    }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Register",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)
                                    .clickable {
                                        navigator?.push(RegistrationScreen(prefs = prefs))
                                    }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Onboarding",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)
                                    .clickable {
                                        navigator?.push(OnboardingScreen(prefs))
                                    }
                            )
                        }
                    } else {
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
                                    navigator?.push(BottomNavigationMainScreen(prefs = prefs))
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
        }
    }
}

suspend fun checkPermissions(
    permission: Permission,
    controller: PermissionsController,
    snackbarHostState: SnackbarHostState
) {
    val granted = controller.isPermissionGranted(permission)
    if (!granted) {
        try {
            controller.providePermission(permission)
        } catch (e: DeniedException) {
            val result = snackbarHostState.showSnackbar(
                message = "Denied",
                actionLabel = "Open Settings",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                controller.openAppSettings()
            }
        } catch (e: DeniedAlwaysException) {
            val result = snackbarHostState.showSnackbar(
                message = "Permanently Denied",
                actionLabel = "Open Settings",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                controller.openAppSettings()
            }
        } catch (e: RequestCanceledException) {
            snackbarHostState.showSnackbar(
                message = "Request canceled."
            )
        }
    } else {
        snackbarHostState.showSnackbar(
            message = "Permission already granted"
        )
    }
}
