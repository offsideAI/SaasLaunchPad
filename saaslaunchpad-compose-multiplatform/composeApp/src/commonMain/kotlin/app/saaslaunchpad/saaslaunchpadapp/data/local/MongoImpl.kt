package app.saaslaunchpad.saaslaunchpadapp.data.local

import app.saaslaunchpad.saaslaunchpadapp.domain.MongoRepository
import app.saaslaunchpad.saaslaunchpadapp.domain.model.Currency
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CurrencyApiRequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoImpl: MongoRepository {
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (realm == null || realm!!.isClosed()) {
           val config = RealmConfiguration.Builder(
               schema = setOf(Currency::class)
           )
               .compactOnLaunch()
               .build()
           realm = Realm.open(config)
        }
    }

    override suspend fun insertCurrencyData(currency: Currency) {
        realm?.write { copyToRealm(currency) }
    }

    override fun readCurrencyData(): Flow<CurrencyApiRequestState<List<Currency>>> {
        return realm?.query<Currency>()
            ?.asFlow()
            ?.map { result ->
                CurrencyApiRequestState.Success(data = result.list)
            }
            ?: flow { CurrencyApiRequestState.Error(message = "Realm not configured.")}

    }

    override suspend fun cleanUp() {
        realm?.write {
            val currencyCollection = this.query<Currency>()
            delete(currencyCollection)
        }
    }
}