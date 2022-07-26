package cn.edu.pku.treehole.di

import cn.edu.pku.treehole.api.HoleApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideHoleApiService():HoleApiService{
        return HoleApiService.create()
    }
}