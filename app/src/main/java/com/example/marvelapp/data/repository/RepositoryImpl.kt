package com.example.marvelapp.data.repository

import com.example.marvelapp.data.mapper.MarvelMapper
import com.example.marvelapp.data.remote.api.ApiService
import com.example.marvelapp.domain.common.BaseResult
import com.example.marvelapp.domain.common.Constants
import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.entity.CharacterEntity
import com.example.marvelapp.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: MarvelMapper
) : Repository {

    override fun getAllCharactersBySortOrder(sortOrder: SortOrder): Flow<BaseResult<List<CharacterEntity>>> {
        return flow {
            val response = when (sortOrder) {
                SortOrder.BY_NAME -> apiService.getAllCharactersSortOrder(orderBy = Constants.BY_NAME)
                SortOrder.BY_MODIFIED -> apiService.getAllCharactersSortOrder(orderBy = Constants.BY_MODIFIED)
                SortOrder.BY_NAME_DESCENDING -> apiService.getAllCharactersSortOrder(orderBy = Constants.BY_NAME_DESCENDING)
                SortOrder.BY_MODIFIED_DESCENDING -> apiService.getAllCharactersSortOrder(orderBy = Constants.BY_MODIFIED_DESCENDING)
                else -> apiService.getAllCharacters()
            }
            if (response.isSuccessful) {
                try {
                    emit(BaseResult.Loading())
                    val characterEntity = response.body()?.data?.results?.map {
                        mapper.mapDtoToEntity(dto = it)
                    } ?: emptyList()
                    emit(BaseResult.Success(data = characterEntity))
                } catch (e: Exception) {
                    emit(BaseResult.Error(message = e.message.toString()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getCharacterById(characterId: Int): Flow<BaseResult<CharacterEntity>> {
        return flow {
            emit(BaseResult.Loading())
            val response = apiService.getCharacterById(characterId = characterId)
            if (response.isSuccessful) {
                try {
                    val characterEntity = response.body()?.data?.results?.map {
                        mapper.mapDtoToEntity(dto = it)
                    } ?: emptyList()
                    emit(BaseResult.Success(data = characterEntity.first()))
                } catch (e: Exception) {
                    emit(BaseResult.Error(message = e.message.toString()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}