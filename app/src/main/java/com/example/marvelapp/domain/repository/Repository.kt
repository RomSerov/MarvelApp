package com.example.marvelapp.domain.repository

import com.example.marvelapp.domain.common.BaseResult
import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllCharactersBySortOrder(sortOrder: SortOrder): Flow<BaseResult<List<CharacterEntity>>>
    fun getCharacterById(characterId: Int): Flow<BaseResult<CharacterEntity>>
}