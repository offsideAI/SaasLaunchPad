package app.saaslaunchpad.saaslaunchpadapp.data.remote.api

import app.saaslaunchpad.saaslaunchpadapp.domain.model.CoinDto
import app.saaslaunchpad.saaslaunchpadapp.domain.model.RequestState

interface CoinPaprikaApiService {
    suspend fun getCoins() : RequestState<List<CoinDto>>
}