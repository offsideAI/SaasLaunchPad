package app.saaslaunchpad.saaslaunchpadapp.data.remote.api

import app.saaslaunchpad.saaslaunchpadapp.domain.model.Currency
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CurrencyApiRequestState

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): CurrencyApiRequestState<List<Currency>>
}