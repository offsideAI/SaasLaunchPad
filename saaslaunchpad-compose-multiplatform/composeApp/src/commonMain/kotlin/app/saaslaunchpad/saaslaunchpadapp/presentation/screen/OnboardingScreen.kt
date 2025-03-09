package app.saaslaunchpad.saaslaunchpadapp.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

/**
 *  Data class for our onboarding pages
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: String // This would be a resource ID in actual implementation
)

/**
 * List of onboarding pages
 */
val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to SaaS Launch Pad",
        description = "Your all-in-one platform for building and scaling SaaS applications effortlessly.",
        imageRes = "welcome_illustration"
    ),
    OnboardingPage(
        title = "Mobile & Web",
        description = "Build once, deploy everywhere with our cross-platform solution for web and mobile.",
        imageRes = "mobile_web_illustration"
    ),
    OnboardingPage(
        title = "Secure Backend & Auth",
        description = "Enterprise-grade security and authentication right out of the box.",
        imageRes = "backend_auth_illustration"
    ),
    OnboardingPage(
        title = "Payments & E-commerce",
        description = "Integrated payment processing and e-commerce capabilities to monetize your platform.",
        imageRes = "payments_ecommerce_illustration"
    ),
    OnboardingPage(
        title = "Ready to Launch?",
        description = "You're all set! Get started building your SaaS platform today.",
        imageRes = "get_started_illustration"
    )
)


class OnboardingScreen(): Screen{
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var text by remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()
        val auth = remember { Firebase.auth }
        val djangoAuthService = remember { DjangoAuthService() }
        val firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
        var djangoUser: DjangoUser? by remember { mutableStateOf(null) }
        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }

        /**  Preference key for onboarding */
        // TODO-FIXME-IMPROVE val hasSeenOnboardingKey = booleanPreferencesKey("has_seen_onboarding")

        /* TODO-FIXME-IMPROVE
        // Get from DataStore if user has seen onboarding
        val dataStore = LocalDataStore.current
        val hasSeenOnboarding by remember {
            dataStore.data.map { preferences ->
                preferences[hasSeenOnboardingKey] ?: false
            }
        }.collectAsState(initial = false)
        */

        /** Show onboarding based on user preference */
        // TODO-FIXME-IMPROVE var showOnboarding by remember { mutableStateOf(!hasSeenOnboarding) }

        /** If we decide to show onboarding, set up the pager */
        val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
        val isLastPage = pagerState.currentPage == onboardingPages.size - 1

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
            // Show onboarding if needed
            if (true) {
                OnboardingSection(
                    pages = onboardingPages,
                    pagerState = pagerState,
                    onSkip = {
                        // Save that user has seen onboarding
                        /* TODO-FIXME-IMPROVE
                        scope.launch {
                            dataStore.edit { preferences ->
                                preferences[hasSeenOnboardingKey] = true
                            }
                            showOnboarding = false
                        }
                        */
                    },
                    onFinish = {
                        navigator?.pop()
                        // Save that user has seen onboarding
                        /* TODO-FIXME-IMPROVE
                        scope.launch {
                            dataStore.edit { preferences ->
                                preferences[hasSeenOnboardingKey] = true
                            }
                            showOnboarding = false
                        }
                        */
                    }
                )
            } else {
                // Regular landing screen content from original code
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
                        Text(
                            text = "Login",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp)
                                .clickable {
                                    navigator?.push(LoginScreen())
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
                                    navigator?.push(RegistrationScreen())
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
                                navigator?.push(BottomNavigationMainScreen())
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingSection(
    pages: List<OnboardingPage>,
    pagerState: PagerState,
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1
    val systemInsets = WindowInsets.navigationBars.asPaddingValues()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                // Add bottom padding from navigation bars plus extra space
                bottom = systemInsets.calculateBottomPadding() + 16.dp
            )
    ) {
        // Top section with Skip button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onSkip,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Skip")
            }
        }

        // Middle section with pager content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            OnboardingPage(
                page = pages[page]
            )
        }

        // Bottom section with indicators and next/finish button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Page indicators
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.White.copy(alpha = 0.5f)
                    }
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == iteration) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            // Next or Finish button
            Button(
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(50)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isLastPage) "Get Started" else "Next",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // This would be replaced with your actual SVG rendering in the implementation
        // For now, we'll indicate where the image would go
        Box(
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            // In real implementation, this would be your SVG renderer
            // For now we'll just show the image name
            Text(text = page.imageRes, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

// Helper function to create gradient effect
/* TODO-FIXME-CLEANUP
fun createGradientEffect(colors: List<Color>, isVertical: Boolean): Brush {
    return if (isVertical) {
        Brush.verticalGradient(colors = colors)
    } else {
        Brush.horizontalGradient(colors = colors)
    }
}
*/



