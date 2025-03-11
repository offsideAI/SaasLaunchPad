package app.saaslaunchpad.saaslaunchpadapp.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.saaslaunchpad.saaslaunchpadapp.data.remote.api.CoinPaprikaApiService
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CoinDto
import app.saaslaunchpad.saaslaunchpadapp.domain.model.Post
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.CoinViewModel
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.ThemeUtils
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.primaryColor
import app.saaslaunchpad.saaslaunchpadapp.util.createGradientEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TabOneScreen : Screen, KoinComponent {
    // TODO-FIXME-CLEANUP private val currencyApiService: CurrencyApiService by inject()
    private val coinPaprikaApiService: CoinPaprikaApiService by inject()

    @Composable
    override fun Content() {
        // TODO-FIXME-CLEANUP val viewModel = getScreenModel<PostViewModel>()
        // TODO-FIXME-CLEANUP val allPosts by viewModel.allPosts
        val viewModel = getScreenModel<CoinViewModel>()
        val allCoins by viewModel.allCoins

        LaunchedEffect(Unit) {
            println("TabOneScreen")
            // TODO-FIXME-CLEANUP currencyApiService.getLatestExchangeRates()
            coinPaprikaApiService.getCoins()
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
            if (allCoins.isSuccess()) {
                val data = remember { allCoins.getSuccessData() }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = data,
                        key = { it.id }
                    ) { coin ->
                        CoinCard(coin = coin)
                    }
                }
            } else if (allCoins.isError()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = allCoins.getErrorMessage(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


@Composable
fun CoinCard(coin: CoinDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${coin.name} (${coin.symbol})",
                    style = MaterialTheme.typography.headlineSmall.copy(color = primaryColor),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "#${coin.rank}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Type: ${coin.type}",
                    style = MaterialTheme.typography.bodySmall
                )

                val statusColor = if(coin.isActive) Color.Green else Color.Red
                Text(
                    text = if(coin.isActive) "Active" else "Inactive",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
            }

            if (coin.isNew) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "NEW",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "(${post.id} - ${post.title})",
                style = MaterialTheme.typography.headlineSmall.copy(color = primaryColor),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}