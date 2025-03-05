package app.saaslaunchpad.saaslaunchpadapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    var currentRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

    Scaffold(
        bottomBar = {
            BottomNav(
                currentRoute = currentRoute,
                onNavigate = { item ->
                    currentRoute = item.route
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentRoute) {
                BottomNavItem.Markets.route -> MarketsScreen()
                BottomNavItem.Trending.route -> TrendingScreen()
                BottomNavItem.Home.route -> HomeScreen()
                BottomNavItem.Bookmarks.route -> BookmarksScreen()
                BottomNavItem.More.route -> MoreScreen()
            }
        }
    }
}

