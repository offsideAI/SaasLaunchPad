package app.saaslaunchpad.saaslaunchpadapp.data.remote.api

import app.saaslaunchpad.saaslaunchpadapp.domain.model.CoinDto
import app.saaslaunchpad.saaslaunchpadapp.domain.model.CoinPaprikaResponse
import app.saaslaunchpad.saaslaunchpadapp.domain.model.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CoinPaprikaApiServiceImpl(
): CoinPaprikaApiService {
    companion object {
        const val ENDPOINT = "https://api.coinpaprika.com/v1/coins"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }

    override suspend fun getCoins(): RequestState<List<CoinDto>> {
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200) {
                val apiResponse = Json.decodeFromString<CoinPaprikaResponse>(response.body())


                RequestState.Success(data = apiResponse)

            } else {
                RequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }

}