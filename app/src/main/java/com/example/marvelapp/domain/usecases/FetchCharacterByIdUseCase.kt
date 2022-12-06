package com.example.marvelapp.domain.usecases

import com.example.marvelapp.domain.common.BaseResult
import com.example.marvelapp.domain.entity.CharacterEntity
import com.example.marvelapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCharacterByIdUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(characterId: Int): Flow<BaseResult<CharacterEntity>> {
        return repository.getCharacterById(characterId = characterId)
    }
}