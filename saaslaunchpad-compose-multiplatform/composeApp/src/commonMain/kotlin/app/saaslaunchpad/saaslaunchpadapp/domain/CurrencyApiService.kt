package app.saaslaunchpad.saaslaunchpadapp.domain

import app.saaslaunchpad.saaslaunchpadapp.domain.model.Currency
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CurrencyApiRequestState

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): CurrencyApiRequestState<List<Currency>>
}