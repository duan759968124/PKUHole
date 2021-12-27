package cn.edu.pku.pkuhole.di

import cn.edu.pku.pkuhole.api.HoleApiService
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