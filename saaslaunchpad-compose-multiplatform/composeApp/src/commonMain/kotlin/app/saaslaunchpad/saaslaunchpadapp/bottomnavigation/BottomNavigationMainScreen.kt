package app.saadlaunchpad.saaslaunchpadapp.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.saaslaunchpad.saaslaunchpadapp.bottomnavigation.TabOneScreen
import app.saaslaunchpad.saaslaunchpadapp.bottomnavigation.TabThreeScreen
import app.saaslaunchpad.saaslaunchpadapp.bottomnavigation.TabTwoScreen
import app.saaslaunchpad.saadlaunchpadapp.bottomnavigation.BottomNavigationItem
import app.saaslaunchpad.saaslaunchpad.bottomnavigation.TabFiveScreen
import app.saaslaunchpad.saaslaunchpadapp.bottomnavigation.TabFourScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

class BottomNavigationMainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var selectedIndex by remember { mutableStateOf(2) }
        
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            /* TODO-FIXME-CLEANUP
            topBar = {
                TopAppBar(
                    title = { Text(text = "Paradigmatic") },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle navigation */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle search */ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = { /* Handle more options */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                    }
                )
            },
            */
            bottomBar = {
                NavigationBar {
                    listOf(
                        BottomNavigationItem.TabOneItem,
                        BottomNavigationItem.TabTwoItem,
                        BottomNavigationItem.TabThreeItem,
                        BottomNavigationItem.TabFourItem,
                        BottomNavigationItem.TabFiveItem
                    ).forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.selectedIcon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedIndex) {
                    0 -> TabOneScreen().Content()
                    1 -> Navigator(TabTwoScreen()) { navigator ->
                        SlideTransition(navigator)
                    }
                    2 -> TabThreeScreen().Content()
                    3 -> Navigator(TabFourScreen()) { navigator ->
                       SlideTransition(navigator)
                    }
                    4 -> TabFiveScreen().Content()
                }
            }
        }
    }
}
