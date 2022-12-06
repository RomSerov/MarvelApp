package com.example.marvelapp.di

import com.example.marvelapp.domain.repository.StateSortOrderRepository
import com.example.marvelapp.data.repository.PreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindStateFilterRepository(preferencesImpl: PreferencesImpl): StateSortOrderRepository

}