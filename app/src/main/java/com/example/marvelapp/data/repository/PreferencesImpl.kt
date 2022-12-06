package com.example.marvelapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.marvelapp.data.repository.PreferencesImpl.PrefKey.PREF_ORDER_KEY
import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.common.SortOrder.*
import com.example.marvelapp.domain.repository.StateSortOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : StateSortOrderRepository {

    override suspend fun saveState(value: SortOrder) {
        dataStore.edit {
            it[PREF_ORDER_KEY] = value.name
        }
    }

    override fun fetchState(): Flow<SortOrder> {
        return dataStore.data.catch { exception ->
            throw exception
        }.map { pref ->
            when (pref[PREF_ORDER_KEY]) {
                BY_NAME.name -> BY_NAME
                BY_MODIFIED.name -> BY_MODIFIED
                BY_NAME_DESCENDING.name -> BY_NAME_DESCENDING
                BY_MODIFIED_DESCENDING.name -> BY_MODIFIED_DESCENDING
                else -> NON
            }
        }
    }

    override suspend fun clearState() {
        dataStore.edit {
            if (it.contains(PREF_ORDER_KEY)) {
                it.remove(PREF_ORDER_KEY)
            }
        }
    }

    private object PrefKey {
        val PREF_ORDER_KEY = stringPreferencesKey("order_key")
    }
}