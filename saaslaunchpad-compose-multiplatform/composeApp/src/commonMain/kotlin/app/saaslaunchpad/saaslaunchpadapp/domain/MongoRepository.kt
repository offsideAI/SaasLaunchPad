package app.saaslaunchpad.saaslaunchpadapp.domain

import app.saaslaunchpad.saaslaunchpadapp.domain.model.Currency
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CurrencyApiRequestState
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun configureTheRealm()
    suspend fun insertCurrencyData(currency: Currency)
    fun readCurrencyData(): Flow<CurrencyApiRequestState<List<Currency>>>
    suspend fun cleanUp()
}