package cn.edu.pku.treehole.di

import android.content.Context
import cn.edu.pku.treehole.data.hole.AppDatabase
import cn.edu.pku.treehole.data.hole.CommentDao
import cn.edu.pku.treehole.data.hole.HoleListDao
import cn.edu.pku.treehole.data.hole.TagDao
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
    // 全局复用一个实例，如果没有@Singleton表示每次都是新的实例
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    // 每次都是新的实例
    @Provides
    fun provideTagDao(appDatabase: AppDatabase): TagDao {
        return appDatabase.tagDao()
    }

    // 每次都是新的实例
    @Provides
    fun provideHoleListDao(appDatabase: AppDatabase): HoleListDao {
        return appDatabase.holeListDao()
    }

    // 每次都是新的实例
    @Provides
    fun provideCommentDao(appDatabase: AppDatabase): CommentDao {
        return appDatabase.commentDao()
    }

}