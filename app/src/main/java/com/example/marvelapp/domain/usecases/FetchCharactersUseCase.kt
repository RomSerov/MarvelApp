package com.example.marvelapp.domain.usecases

import com.example.marvelapp.domain.common.BaseResult
import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.entity.CharacterEntity
import com.example.marvelapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCharactersUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(sortOrder: SortOrder): Flow<BaseResult<List<CharacterEntity>>> {
        return repository.getAllCharactersBySortOrder(sortOrder = sortOrder)
    }
}