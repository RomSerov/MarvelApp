package com.example.marvelapp.domain.repository

import com.example.marvelapp.domain.common.SortOrder
import kotlinx.coroutines.flow.Flow

interface StateSortOrderRepository {

    suspend fun saveState(value: SortOrder)
    fun fetchState(): Flow<SortOrder>
    suspend fun clearState()
}