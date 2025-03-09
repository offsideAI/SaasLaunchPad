package app.saaslaunchpad.saaslaunchpadapp.data.datastorepreferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Create a DataStore extension property for Context:
private val Context.dataStore by preferencesDataStore(name = "auth_preferences")

object AuthPreferencesManager {
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    // Function to update the auth state:
    suspend fun setLoggedIn(context: Context, loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    // Function to observe the auth state as a Flow
    fun isLoggedIn(context: Context): Flow<Boolean> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[IS_LOGGED_IN] ?: false
            }
}
