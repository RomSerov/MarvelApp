package com.example.marvelapp.domain.usecases

import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.repository.StateSortOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchStateOrderUseCase @Inject constructor(private val stateSortOrderRepository: StateSortOrderRepository) {

    fun fetchState(): Flow<SortOrder> {
        return stateSortOrderRepository.fetchState()
    }
}