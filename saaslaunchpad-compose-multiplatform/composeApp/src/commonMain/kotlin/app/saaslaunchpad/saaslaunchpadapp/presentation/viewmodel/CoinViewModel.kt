package app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import app.saaslaunchpad.saaslaunchpadapp.data.remote.api.CoinPaprikaApiService
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CoinDto
import app.saaslaunchpad.saaslaunchpadapp.domain.model.PostApiRequestState
import app.saaslaunchpad.saaslaunchpadapp.domain.model.RequestState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch


class CoinViewModel(
    private val apiService: CoinPaprikaApiService
) : ScreenModel {

    private val _allCoins: MutableState<RequestState<List<CoinDto>>> = mutableStateOf(RequestState.Idle)
    val allCoins: State<RequestState<List<CoinDto>>> = _allCoins

    init {
        screenModelScope.launch {
            _allCoins.value = RequestState.Loading
            try {
                _allCoins.value = apiService.getCoins()
            } catch (e: Exception) {
                _allCoins.value = RequestState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}