package com.example.marvelapp.domain.usecases

import com.example.marvelapp.domain.repository.StateSortOrderRepository
import javax.inject.Inject

class ClearStateOrderUseCase @Inject constructor(private val stateSortOrderRepository: StateSortOrderRepository) {

    suspend fun clearState() {
        stateSortOrderRepository.clearState()
    }
}