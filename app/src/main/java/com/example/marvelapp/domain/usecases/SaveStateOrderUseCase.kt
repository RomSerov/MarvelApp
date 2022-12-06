package com.example.marvelapp.domain.usecases

import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.repository.StateSortOrderRepository
import javax.inject.Inject

class SaveStateOrderUseCase @Inject constructor(private val stateSortOrderRepository: StateSortOrderRepository) {

    suspend fun saveState(value: SortOrder) {
        stateSortOrderRepository.saveState(value = value)
    }
}