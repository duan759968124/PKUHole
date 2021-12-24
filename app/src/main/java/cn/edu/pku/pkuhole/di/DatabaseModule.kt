package cn.edu.pku.pkuhole.di

import android.content.Context
import cn.edu.pku.pkuhole.data.hole.AppDatabase
import cn.edu.pku.pkuhole.data.hole.HoleAllListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/24
 * @Desc:
 * @Version:        1.0
 */
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideHoleAllListDao(appDatabase: AppDatabase): HoleAllListDao {
        return appDatabase.holeAllListDao
    }

}