package com.example.marvelapp.data.remote.api

import com.example.marvelapp.domain.common.Constants
import com.example.marvelapp.data.model.charactersdto.Characters
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/public/characters")
    suspend fun getAllCharacters(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") timeStamp: String = Constants.timeStamp,
        @Query("hash") md5Hash: String = Constants.toMd5Hash(),
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Response<Characters>

    @GET("/v1/public/characters")
    suspend fun getAllCharactersSortOrder(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") timeStamp: String = Constants.timeStamp,
        @Query("hash") md5Hash: String = Constants.toMd5Hash(),
        @Query("offset") offset: Int = 0,
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int = 30
    ): Response<Characters>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") characterId: Int,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") timeStamp: String = Constants.timeStamp,
        @Query("hash") md5Hash: String = Constants.toMd5Hash()
    ): Response<Characters>
}