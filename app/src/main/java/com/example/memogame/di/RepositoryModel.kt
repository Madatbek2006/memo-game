package com.example.memogame.di

import com.example.memogame.domain.AppRepository
import com.example.memogame.domain.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModel {
    @Binds
    fun getAppRepository(impl:AppRepositoryImpl):AppRepository
}